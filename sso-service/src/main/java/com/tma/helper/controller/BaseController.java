package com.tma.helper.controller;


import com.tma.helper.exception.BadRequestException;
import com.tma.helper.model.AppUser;
import com.tma.helper.repository.AppUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;


@Slf4j
public abstract class BaseController {
    @Autowired
    private AppUserRepository appUserRepository;

    protected AppUser getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser currentUser = appUserRepository.findByUsername(username).get();
        if (currentUser == null) {
            throw new BadRequestException("You aren't authorized to perform this operation.");
        }
        return currentUser;
    }

}
