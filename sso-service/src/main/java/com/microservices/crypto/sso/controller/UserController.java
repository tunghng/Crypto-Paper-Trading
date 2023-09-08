package com.microservices.crypto.sso.controller;

import com.microservices.crypto.sso.dto.model.AppUserDto;
import com.microservices.crypto.sso.dto.response.page.PageData;
import com.microservices.crypto.sso.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("api/user")
public class UserController extends BaseController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Get Users (getUsers)")
    public ResponseEntity<PageData<AppUserDto>> getUsers(
            @Parameter(description = "Sequence number of page starting from 0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Maximum amount of entities in a one page")
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return ResponseEntity.ok(
                userService.findUsers(
                        page, pageSize, getCurrentUser()
                )
        );
    }

    @GetMapping("{userId}")
    @Operation(summary = "Get User by id (getUserById)")
    public ResponseEntity<AppUserDto> getUserById(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.getUserProfile(userId, getCurrentUser()));
    }
}
