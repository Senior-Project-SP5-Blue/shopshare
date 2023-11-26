package com.sp5blue.shopshare.controllers;

import com.sp5blue.shopshare.controllers.shoppinglist.ShoppingListErrorResponse;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class MiscExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(MiscExceptionHandler.class);

    @ExceptionHandler
    public ResponseEntity<ShoppingListErrorResponse> handleException(AccessDeniedException exception) {
        ShoppingListErrorResponse error = new ShoppingListErrorResponse(HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ShoppingListErrorResponse> handleException(MethodArgumentTypeMismatchException exception) {
//        exception.printStackTrace();
//        logger.warn(exception.getMessage());
        ShoppingListErrorResponse error = new ShoppingListErrorResponse(HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler
//    public ResponseEntity<ShoppingListErrorResponse> handleException(Exception exception) {
//        exception.printStackTrace();
//        logger.warn(exception.getMessage());
//        ShoppingListErrorResponse error = new ShoppingListErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
//                LocalDateTime.now());
//        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}
