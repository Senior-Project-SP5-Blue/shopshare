package com.sp5blue.shopshare.exceptions.shoppergroup;

public class GroupNotFoundException extends RuntimeException {
  public GroupNotFoundException(String message) {
    super(message);
  }

  public GroupNotFoundException() {}
}
