package com.sp5blue.shopshare.exceptions.shoppergroup;

public class InvitationExpiredException extends RuntimeException {
  public InvitationExpiredException() {}

  public InvitationExpiredException(String message) {
    super(message);
  }
}
