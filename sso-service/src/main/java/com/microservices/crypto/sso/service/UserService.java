package com.microservices.crypto.sso.service;


import com.microservices.crypto.sso.dto.model.AppUserDto;
import com.microservices.crypto.sso.dto.request.RegisterRequest;
import com.microservices.crypto.sso.dto.request.UpdateProfileRequest;
import com.microservices.crypto.sso.dto.response.page.PageData;
import com.microservices.crypto.sso.model.AppUser;

import java.util.UUID;

public interface UserService {
    PageData<AppUserDto> findUsers(
            int page,
            int pageSize,
            AppUser currentUser
    );

    AppUserDto getUserProfile(UUID id, AppUser currentUser);

    AppUserDto save(UpdateProfileRequest profile, AppUser currentUser);

    AppUserDto signUp(RegisterRequest registerRequest);
}

