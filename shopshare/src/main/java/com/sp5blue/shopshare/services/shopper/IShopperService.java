package com.sp5blue.shopshare.services.shopper;

import com.sp5blue.shopshare.exceptions.authentication.UserAlreadyExistsException;
import com.sp5blue.shopshare.exceptions.authentication.UserNotFoundException;
import com.sp5blue.shopshare.models.shopper.Shopper;

import java.util.List;
import java.util.UUID;

public interface IShopperService {
    Shopper createShopper(Shopper shopper) throws UserAlreadyExistsException;

    Shopper createShopper(String firstName, String lastName, String username, String email, String password) throws UserAlreadyExistsException;

    Shopper readShopperById(String id);

    Shopper readShopperById(UUID id) throws UserNotFoundException;

    Shopper readShopperByEmail(String email) throws UserNotFoundException;

    Shopper readShopperByUsername(String username) throws UserNotFoundException;

    List<Shopper> readShoppersByShopperGroup(UUID groupId);

    List<Shopper> readShoppers();

    boolean shopperExists(UUID id);

    boolean shopperExistsByEmail(String email);

    boolean shopperExistsByUsername(String username);
}