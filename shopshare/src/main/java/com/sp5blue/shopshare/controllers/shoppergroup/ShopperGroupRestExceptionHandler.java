package com.sp5blue.shopshare.controllers.shoppergroup;


import com.sp5blue.shopshare.controllers.shopper.ShopperErrorResponse;
import com.sp5blue.shopshare.exceptions.authentication.UserAlreadyExistsException;
import com.sp5blue.shopshare.exceptions.shoppergroup.GroupNotFoundException;
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
}
