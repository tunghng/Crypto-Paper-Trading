package com.microservices.crypto.sso.dto.request;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
}
