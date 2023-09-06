package com.tma.helper.service;


import com.tma.helper.dto.model.AppUserDto;
import com.tma.helper.dto.request.RegisterRequest;
import com.tma.helper.dto.request.UpdateProfileRequest;
import com.tma.helper.dto.response.page.PageData;
import com.tma.helper.model.AppUser;

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

