package com.sp5blue.shopshare.exceptions.shoppinglist;

public class ListItemNotFoundException extends RuntimeException {
    public ListItemNotFoundException(String message) {
        super(message);
    }

    public ListItemNotFoundException() {
    }
}
