package com.microservices.crypto.sso.dto.request;

import lombok.Data;

@Data
public class MessageRequest {
    private String phone;
    private String message;
}

