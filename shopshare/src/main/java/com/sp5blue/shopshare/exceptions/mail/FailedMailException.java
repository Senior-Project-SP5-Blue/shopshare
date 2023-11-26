package com.sp5blue.shopshare.exceptions.mail;

public class FailedMailException extends RuntimeException {
  public FailedMailException() {}

  public FailedMailException(String message) {
    super(message);
  }
}
