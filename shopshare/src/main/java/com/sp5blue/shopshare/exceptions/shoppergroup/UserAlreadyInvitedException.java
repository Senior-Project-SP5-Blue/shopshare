package com.sp5blue.shopshare.exceptions.shoppergroup;

public class UserAlreadyInvitedException extends RuntimeException{
    public UserAlreadyInvitedException(String message) {
        super(message);
    }

    public UserAlreadyInvitedException() {
    }
}
