package com.tma.helper.security.exception;

import com.tma.helper.dto.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TokenRefreshExceptionHandler {
    @ExceptionHandler(value = TokenRefreshException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Response handleTokenRefreshException(TokenRefreshException ex) {
        return new Response(
                HttpStatus.FORBIDDEN.value(), ex.getMessage());
    }
}
