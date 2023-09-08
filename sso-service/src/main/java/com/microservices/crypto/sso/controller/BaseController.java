package com.microservices.crypto.sso.controller;


import com.microservices.crypto.sso.repository.AppUserRepository;
import com.microservices.crypto.sso.exception.BadRequestException;
import com.microservices.crypto.sso.model.AppUser;
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
