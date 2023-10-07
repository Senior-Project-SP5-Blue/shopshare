package com.sp5blue.shopshare.services.shopper;

import com.sp5blue.shopshare.exceptions.authentication.UserAlreadyExistsException;
import com.sp5blue.shopshare.exceptions.authentication.UserNotFoundException;
import com.sp5blue.shopshare.models.Shopper;

import java.util.List;
import java.util.UUID;

public interface IShopperService {
    Shopper create(Shopper shopper) throws UserAlreadyExistsException;

    Shopper create(String firstName, String lastName, String username, String email, String password) throws UserAlreadyExistsException;

    Shopper readById(String id);

    Shopper readById(UUID id) throws UserNotFoundException;

    Shopper readByEmail(String email) throws UserNotFoundException;

    List<Shopper> read();

    boolean shopperExists(UUID id);
}