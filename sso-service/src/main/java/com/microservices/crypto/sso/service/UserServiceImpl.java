package com.microservices.crypto.sso.service;


import com.microservices.crypto.sso.exception.BadRequestException;
import com.microservices.crypto.sso.exception.UnAuthorizedException;
import com.microservices.crypto.sso.repository.AppUserRepository;
import com.microservices.crypto.sso.dto.mapper.AppUserMapper;
import com.microservices.crypto.sso.dto.model.AppUserDto;
import com.microservices.crypto.sso.dto.request.RegisterRequest;
import com.microservices.crypto.sso.dto.request.UpdateProfileRequest;
import com.microservices.crypto.sso.dto.response.page.PageData;
import com.microservices.crypto.sso.model.AppUser;
import com.microservices.crypto.sso.model.enums.RoleType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final AppUserRepository userRepository;
    private final UserCredentialService userCredentialService;
    private final AppUserMapper mapper;

    @Autowired
    public UserServiceImpl(AppUserRepository userRepository, UserCredentialService userCredentialService, AppUserMapper mapper) {
        this.userRepository = userRepository;
        this.userCredentialService = userCredentialService;
        this.mapper = mapper;
    }

    @Override
    public PageData<AppUserDto> findUsers(int page, int pageSize, AppUser currentUser) {
        if (!checkIfAdmin(currentUser)) {
            throw new UnAuthorizedException("You do not have permission to do this action");
        }
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<AppUserDto> userDtoPage = userRepository.findAllByRole(RoleType.USER, pageable)
                .map(mapper::toDto);
        return new PageData<>(userDtoPage);
    }

    @Override
    public AppUserDto getUserProfile(UUID id, AppUser currentUser) {
        if (id == currentUser.getId() || currentUser.getRole() == RoleType.ADMIN) {
            return mapper.toDto(currentUser);
        } else {
            throw new UnAuthorizedException("You do not have permission to do this action");
        }
    }

    @Override
    public AppUserDto save(UpdateProfileRequest profile, AppUser currentUser) {
        currentUser.setDisplayName(profile.getDisplayName());
        AppUser savedUser = userRepository.save(currentUser);
        return mapper.toDto(savedUser);
    }

    @Override
    public AppUserDto signUp(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new BadRequestException("Username is already taken!");
        }

        AppUser newUser = userRepository.save(
                AppUser.builder()
                        .username(registerRequest.getUsername())
                        .role(RoleType.USER)
                        .build()
        );

        userCredentialService.setPassword(newUser.getId(), registerRequest.getPassword());
        return mapper.toDto(newUser);
    }

    private boolean checkIfAdmin(AppUser currentUser) {
        return currentUser.getRole() == RoleType.ADMIN;
    }
}


