package com.sp5blue.shopshare.exceptions.shoppergroup;

public class InvalidUserPermissionsException extends RuntimeException {
    public InvalidUserPermissionsException() {
    }

    public InvalidUserPermissionsException(String message) {
        super(message);
    }
}
