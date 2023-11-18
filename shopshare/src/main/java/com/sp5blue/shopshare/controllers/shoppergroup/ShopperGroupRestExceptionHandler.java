package com.sp5blue.shopshare.controllers.shoppergroup;


import com.sp5blue.shopshare.exceptions.shoppergroup.GroupNotFoundException;
import com.sp5blue.shopshare.exceptions.shoppergroup.UserAlreadyInGroupException;
import com.sp5blue.shopshare.exceptions.shoppergroup.UserNotInvitedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ShopperGroupRestExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ShopperGroupErrorResponse> handleException(GroupNotFoundException exception) {
        ShopperGroupErrorResponse error = new ShopperGroupErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ShopperGroupErrorResponse> handleException(UserNotInvitedException exception) {
        ShopperGroupErrorResponse error = new ShopperGroupErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ShopperGroupErrorResponse> handleException(UserAlreadyInGroupException exception) {
        ShopperGroupErrorResponse error = new ShopperGroupErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
