package com.microservices.crypto.sso.controller;

import com.microservices.crypto.sso.dto.model.AppUserDto;
import com.microservices.crypto.sso.dto.request.ChangePasswordRequest;
import com.microservices.crypto.sso.dto.request.RegisterRequest;
import com.microservices.crypto.sso.dto.request.UpdateProfileRequest;
import com.microservices.crypto.sso.dto.response.LoginResponse;
import com.microservices.crypto.sso.model.AppUser;
import com.microservices.crypto.sso.security.model.SecurityUser;
import com.microservices.crypto.sso.security.model.token.JwtGenerator;
import com.microservices.crypto.sso.security.service.RefreshTokenService;
import com.microservices.crypto.sso.service.UserCredentialService;
import com.microservices.crypto.sso.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("api/auth")
public class AuthController extends BaseController {
    private final UserCredentialService userCredentialService;
    private final UserService userService;
    private final JwtGenerator jwtGenerator;
    private final RefreshTokenService refreshTokenService;

    @Value(value = "${jwt.exp}")
    private Long jwtExp;

    @Value(value = "${jwt.refreshExp}")
    private Long jwtRefreshExp;

    @Autowired
    public AuthController(UserCredentialService userCredentialService, UserService userService, JwtGenerator jwtGenerator, RefreshTokenService refreshTokenService) {
        this.userCredentialService = userCredentialService;
        this.userService = userService;
        this.jwtGenerator = jwtGenerator;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("signup")
    @Operation(summary = "Sign Up Customer (signUp)")
    public ResponseEntity<AppUserDto> signUp(
            @RequestBody RegisterRequest registerRequest
    ) {
        return ResponseEntity.ok(
                userService.signUp(registerRequest)
        );
    }

    @PostMapping("password/change")
    @Operation(summary = "Change password for current user (changePassword)")
    public ResponseEntity<LoginResponse> changePassword(
            @RequestBody ChangePasswordRequest changePasswordRequest
    ) {
        AppUser currentUser = getCurrentUser();
        userCredentialService.changePassword(currentUser, changePasswordRequest);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();

        String token = jwtGenerator.generateToken(securityUser, jwtExp);
        String refreshToken = jwtGenerator.generateToken(securityUser, jwtRefreshExp);

        refreshTokenService.createRefreshToken(securityUser.getUser(), refreshToken);

        return ResponseEntity.ok(
                new LoginResponse(token, refreshToken, jwtExp)
        );
    }

    @GetMapping("user")
    @Operation(summary = "Get current user (getCurrentUser)")
    public ResponseEntity<AppUserDto> getUserProfile() {
        return ResponseEntity.ok(
                userService.getUserProfile(getCurrentUser().getId(), getCurrentUser())
        );
    }

    @PostMapping
    @Transactional
    @Operation(summary = "Update Account (saveUser)")
    public ResponseEntity<AppUserDto> saveUser(
            @Valid @RequestBody UpdateProfileRequest profile
    ) {
        AppUser currentUser = getCurrentUser();
        return ResponseEntity.ok(
                userService.save(profile, currentUser)
        );
    }
}
