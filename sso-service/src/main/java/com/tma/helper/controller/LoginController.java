package com.tma.helper.controller;

import com.tma.helper.dto.request.LoginRequest;
import com.tma.helper.dto.request.RefreshTokenRequest;
import com.tma.helper.dto.response.LoginResponse;
import com.tma.helper.exception.InvalidUsernameOrPassword;
import com.tma.helper.repository.AppUserRepository;
import com.tma.helper.security.exception.TokenRefreshException;
import com.tma.helper.security.model.RefreshToken;
import com.tma.helper.security.model.SecurityUser;
import com.tma.helper.security.model.token.JwtGenerator;
import com.tma.helper.security.service.CustomUserDetailsService;
import com.tma.helper.security.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@Slf4j
@RestController
@RequestMapping("api/auth")
public class LoginController extends BaseController {

    private final AuthenticationManager authenticationManager;
    private final JwtGenerator jwtGenerator;
    private final AppUserRepository appUserRepository;
    private final RefreshTokenService refreshTokenService;
    private final CustomUserDetailsService securityUserService;
    @Value(value = "${jwt.exp}")
    private Long jwtExp;
    @Value(value = "${jwt.refreshExp}")
    private Long jwtRefreshExp;

    @Autowired
    public LoginController(AuthenticationManager authenticationManager, JwtGenerator jwtGenerator, AppUserRepository appUserRepository, RefreshTokenService refreshTokenService, CustomUserDetailsService securityUserService) {
        this.authenticationManager = authenticationManager;
        this.jwtGenerator = jwtGenerator;
        this.appUserRepository = appUserRepository;
        this.refreshTokenService = refreshTokenService;
        this.securityUserService = securityUserService;
    }

    @PostMapping("login")
    @Operation(summary = "Login method to get user JWT token data (loginEndpoint)")
    public ResponseEntity<LoginResponse> loginEndpoint(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();

            String token = jwtGenerator.generateToken(securityUser, jwtExp);
            String refreshToken = jwtGenerator.generateToken(securityUser, jwtRefreshExp);

            refreshTokenService.createRefreshToken(securityUser.getUser(), refreshToken);
            return ResponseEntity.ok(
                    new LoginResponse(token, refreshToken, jwtExp)
            );
        } catch (AuthenticationException e) {
            throw new InvalidUsernameOrPassword("Incorrect email or password");
        }
    }

    @PostMapping("token")
    @Operation(summary = "Refresh Token (refreshToken)")
    public ResponseEntity<LoginResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return refreshTokenService.findByToken(request.getRefreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    SecurityUser securityUser = securityUserService.loadUserByUsername(user.getUsername());
                    String token = jwtGenerator.generateToken(securityUser, jwtExp);
                    String refreshToken = jwtGenerator.generateToken(securityUser, jwtRefreshExp);
                    refreshTokenService.createRefreshToken(securityUser.getUser(), refreshToken);
                    return ResponseEntity.ok(new LoginResponse(token, refreshToken, jwtExp));
                })
                .orElseThrow(() -> {
                    return new TokenRefreshException(
                            request.getRefreshToken(),
                            "Refresh token is not found!"
                    );
                });
    }
}
