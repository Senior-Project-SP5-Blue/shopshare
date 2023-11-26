package com.sp5blue.shopshare.controllers.listitem;

import com.sp5blue.shopshare.exceptions.shoppinglist.ListItemNotFoundException;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ListItemRestExceptionHandler {
  @ExceptionHandler
  public ResponseEntity<ListItemErrorResponse> handleException(
      ListItemNotFoundException exception) {
    ListItemErrorResponse error =
        new ListItemErrorResponse(
            HttpStatus.NOT_FOUND.value(), exception.getMessage(), LocalDateTime.now());
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }
}
