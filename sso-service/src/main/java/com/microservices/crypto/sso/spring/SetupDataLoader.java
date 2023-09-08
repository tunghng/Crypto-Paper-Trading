package com.microservices.crypto.sso.spring;

import com.microservices.crypto.sso.repository.AppUserRepository;
import com.microservices.crypto.sso.service.UserCredentialServiceImpl;
import com.microservices.crypto.sso.service.UserService;
import com.microservices.crypto.sso.model.AppUser;
import com.microservices.crypto.sso.model.enums.RoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final boolean alreadySetup = false;
    @Autowired
    UserService userService;
    @Autowired
    AppUserRepository userRepository;
    @Autowired
    UserCredentialServiceImpl userCredentialsService;
    @Value("${app.password.default}")
    private String defaultPassword;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        try {
            createUserIfNotFound("admin", RoleType.ADMIN);

            createUserIfNotFound("user", RoleType.USER);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    void createUserIfNotFound(String username, RoleType role) {
        if (!userRepository.existsByUsername(username)) {
            AppUser newUser = userRepository.save(
                    AppUser.builder()
                            .username(username)
                            .role(role)
                            .build()
            );
            userCredentialsService.setPassword(newUser.getId());
        }

    }
}
