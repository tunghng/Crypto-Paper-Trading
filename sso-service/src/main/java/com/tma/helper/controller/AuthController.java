package com.tma.helper.controller;

import com.tma.helper.dto.model.AppUserDto;
import com.tma.helper.dto.request.ChangePasswordRequest;
import com.tma.helper.dto.request.RegisterRequest;
import com.tma.helper.dto.request.UpdateProfileRequest;
import com.tma.helper.dto.response.LoginResponse;
import com.tma.helper.model.AppUser;
import com.tma.helper.security.model.SecurityUser;
import com.tma.helper.security.model.token.JwtGenerator;
import com.tma.helper.security.service.RefreshTokenService;
import com.tma.helper.service.UserCredentialService;
import com.tma.helper.service.UserService;
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
