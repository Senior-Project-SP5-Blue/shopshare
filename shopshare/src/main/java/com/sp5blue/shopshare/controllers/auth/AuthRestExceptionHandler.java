package com.sp5blue.shopshare.controllers.auth;

import com.sp5blue.shopshare.exceptions.authentication.UserAlreadyExistsException;
import com.sp5blue.shopshare.exceptions.authentication.UserNotFoundException;
import com.sp5blue.shopshare.exceptions.token.InvalidRefreshTokenException;
import com.sp5blue.shopshare.exceptions.token.TokenNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthRestExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<AuthErrorResponse> handleException(TokenNotFoundException exception) {
        AuthErrorResponse error = new AuthErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Access denied. Please log in.");
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<AuthErrorResponse> handleException(BadCredentialsException exception) {
        AuthErrorResponse error = new AuthErrorResponse(HttpStatus.UNAUTHORIZED.value(), exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<AuthErrorResponse> handleException(UserAlreadyExistsException exception) {
        AuthErrorResponse error = new AuthErrorResponse(HttpStatus.CONFLICT.value(), exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
    @ExceptionHandler
    public ResponseEntity<AuthErrorResponse> handleException(InvalidRefreshTokenException exception) {
        AuthErrorResponse error = new AuthErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Invalid permissions. Please log in.");
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<AuthErrorResponse> handleException(UserNotFoundException exception) {
        AuthErrorResponse error = new AuthErrorResponse(HttpStatus.UNAUTHORIZED.value(), exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }
}
