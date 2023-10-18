package com.sp5blue.shopshare.controllers.shopper;

import com.sp5blue.shopshare.exceptions.authentication.UserAlreadyExistsException;
import com.sp5blue.shopshare.exceptions.authentication.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ShopperRestExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ShopperErrorResponse> handleException(UserAlreadyExistsException exception) {
        ShopperErrorResponse error = new ShopperErrorResponse(HttpStatus.CONFLICT.value(), exception.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<ShopperErrorResponse> handleException(BadCredentialsException exception) {
        ShopperErrorResponse error = new ShopperErrorResponse(HttpStatus.UNAUTHORIZED.value(), exception.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<ShopperErrorResponse> handleException(UserNotFoundException exception) {
        ShopperErrorResponse error = new ShopperErrorResponse(HttpStatus.UNAUTHORIZED.value(), exception.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }
}
