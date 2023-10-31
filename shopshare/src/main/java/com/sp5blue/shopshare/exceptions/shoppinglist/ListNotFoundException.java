package com.sp5blue.shopshare.exceptions.shoppinglist;

public class ListNotFoundException extends RuntimeException{
    public ListNotFoundException(String message) {
        super(message);
    }

    public ListNotFoundException() {
    }
}
