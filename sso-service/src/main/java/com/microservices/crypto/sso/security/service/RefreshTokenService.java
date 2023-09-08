package com.microservices.crypto.sso.security.service;


import com.microservices.crypto.sso.repository.AppUserRepository;
import com.microservices.crypto.sso.security.exception.TokenRefreshException;
import com.microservices.crypto.sso.security.repository.RefreshTokenRepository;
import com.microservices.crypto.sso.model.AppUser;
import com.microservices.crypto.sso.security.model.RefreshToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@Slf4j
public class RefreshTokenService {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Autowired
    AppUserRepository userRepository;
    @Value("${jwt.refreshExp}")
    private Long refreshTokenDurationMs;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public void createRefreshToken(AppUser user, String token) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(token);

        refreshTokenRepository.saveAndFlush(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) throws TokenRefreshException {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new login request");
        }

        return token;
    }
}
