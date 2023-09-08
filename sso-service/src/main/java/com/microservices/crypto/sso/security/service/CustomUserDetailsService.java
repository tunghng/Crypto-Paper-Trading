package com.microservices.crypto.sso.security.service;

import com.microservices.crypto.sso.repository.AppUserRepository;
import com.microservices.crypto.sso.repository.UserCredentialRepository;
import com.microservices.crypto.sso.model.AppUser;
import com.microservices.crypto.sso.model.UserCredential;
import com.microservices.crypto.sso.security.model.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    UserCredentialRepository userCredentialRepository;

    @Override
    @Transactional
    public SecurityUser loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with email [" + username + "] not found"));
        UserCredential userCredential = userCredentialRepository.findByUserId(user.getId()).get();
        return new SecurityUser(user, userCredential);
    }
}
