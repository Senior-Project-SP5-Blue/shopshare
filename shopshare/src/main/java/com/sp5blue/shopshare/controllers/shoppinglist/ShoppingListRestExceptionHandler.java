package com.sp5blue.shopshare.controllers.shoppinglist;


import com.sp5blue.shopshare.exceptions.shoppinglist.ListNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ShoppingListRestExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ShoppingListErrorResponse> handleException(ListNotFoundException exception) {
        ShoppingListErrorResponse error = new ShoppingListErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
