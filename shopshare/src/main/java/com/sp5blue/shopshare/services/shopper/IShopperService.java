package com.sp5blue.shopshare.services.shopper;

import com.sp5blue.shopshare.exceptions.authentication.UserAlreadyExistsException;
import com.sp5blue.shopshare.exceptions.authentication.UserNotFoundException;
import com.sp5blue.shopshare.models.shopper.Shopper;

import java.util.List;
import java.util.UUID;

public interface IShopperService {
    Shopper createShopper(Shopper shopper) throws UserAlreadyExistsException;

    Shopper createShopper(String firstName, String lastName, String username, String email, String password) throws UserAlreadyExistsException;

    Shopper getShopperById(String id);

    Shopper getShopperById(UUID id) throws UserNotFoundException;

    Shopper getShopperByEmail(String email) throws UserNotFoundException;

    Shopper getShopperByUsername(String username) throws UserNotFoundException;

    List<Shopper> getShoppersByShopperGroup(UUID groupId);

    List<Shopper> getShoppers();

    boolean shopperExists(UUID id);

    boolean shopperExistsByGroup(UUID shopperId, UUID groupId);

    boolean shopperExistsByEmail(String email);

    boolean shopperExistsByUsername(String username);

    boolean shopperExistsAsAdminByGroup(UUID shopperId, UUID groupId);
}