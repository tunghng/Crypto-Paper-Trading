package com.tma.helper.service;


import com.tma.helper.dto.request.ChangePasswordRequest;
import com.tma.helper.model.AppUser;

import java.util.UUID;

public interface UserCredentialService {
    void changePassword(AppUser currentUser, ChangePasswordRequest passwordRequest);

    void setPassword(UUID userId, String password);

    void setPassword(UUID userId);
}
