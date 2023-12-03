package com.sp5blue.shopshare.controllers.shoppinglist;

import com.sp5blue.shopshare.exceptions.shoppinglist.ListNotFoundException;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ShoppingListRestExceptionHandler {
  private final Logger logger = LoggerFactory.getLogger(ShoppingListRestExceptionHandler.class);

  @ExceptionHandler
  public ResponseEntity<ShoppingListErrorResponse> handleException(
      ListNotFoundException exception) {
    ShoppingListErrorResponse error =
        new ShoppingListErrorResponse(
            HttpStatus.NOT_FOUND.value(), exception.getMessage(), LocalDateTime.now());
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }
}
