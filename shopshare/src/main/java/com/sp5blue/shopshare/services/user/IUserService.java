package com.sp5blue.shopshare.services.user;

import com.sp5blue.shopshare.exceptions.authentication.UserAlreadyExistsException;
import com.sp5blue.shopshare.exceptions.authentication.UserNotFoundException;
import com.sp5blue.shopshare.models.user.User;

import java.util.List;
import java.util.UUID;

public interface IUserService {
    User createUser(User user) throws UserAlreadyExistsException;

    User createUser(String firstName, String lastName, String username, String email, String password) throws UserAlreadyExistsException;

    User getUserById(String id);

    User getUserById(UUID id) throws UserNotFoundException;

    User getUserByEmail(String email) throws UserNotFoundException;

    User getUserByUsername(String username) throws UserNotFoundException;

    List<User> getUsersByShopperGroup(UUID groupId);

    User getUserByShopperGroup(UUID groupId, UUID userId);

    List<User> getUsers();

    boolean userExists(UUID id);

    boolean userExistsByGroup(UUID shopperId, UUID groupId);

    boolean userExistsByEmail(String email);

    boolean userExistsByUsername(String username);

    boolean userExistsAsAdminByGroup(UUID shopperId, UUID groupId);
}