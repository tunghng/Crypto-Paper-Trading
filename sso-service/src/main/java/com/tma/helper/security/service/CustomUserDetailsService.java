package com.tma.helper.security.service;

import com.tma.helper.model.AppUser;
import com.tma.helper.model.UserCredential;
import com.tma.helper.repository.AppUserRepository;
import com.tma.helper.repository.UserCredentialRepository;
import com.tma.helper.security.model.SecurityUser;
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
