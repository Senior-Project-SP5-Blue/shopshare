package com.sp5blue.shopshare.exceptions.shoppergroup;

public class UserNotInvitedException extends RuntimeException {
    public UserNotInvitedException(String message) {
        super(message);
    }

    public UserNotInvitedException() {
    }
}
