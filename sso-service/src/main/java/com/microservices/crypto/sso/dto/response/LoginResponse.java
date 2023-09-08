package com.microservices.crypto.sso.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    @JsonProperty(value = "token")
    private String token;

    @JsonProperty(value = "refreshToken")
    private String refreshToken;

    @JsonProperty(value = "expiresIn")
    private long expiresIn;
}
