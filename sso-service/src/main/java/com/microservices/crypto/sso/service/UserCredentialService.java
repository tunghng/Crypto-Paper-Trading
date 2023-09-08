package com.microservices.crypto.sso.service;


import com.microservices.crypto.sso.dto.request.ChangePasswordRequest;
import com.microservices.crypto.sso.model.AppUser;

import java.util.UUID;

public interface UserCredentialService {
    void changePassword(AppUser currentUser, ChangePasswordRequest passwordRequest);

    void setPassword(UUID userId, String password);

    void setPassword(UUID userId);
}
