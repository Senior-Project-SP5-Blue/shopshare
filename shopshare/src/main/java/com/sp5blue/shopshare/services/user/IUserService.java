package com.sp5blue.shopshare.services.user;

import com.sp5blue.shopshare.exceptions.authentication.UserAlreadyExistsException;
import com.sp5blue.shopshare.exceptions.authentication.UserNotFoundException;
import com.sp5blue.shopshare.models.user.User;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface IUserService {
    CompletableFuture<User> createUser(User user) throws UserAlreadyExistsException;

    CompletableFuture<User> createUser(String firstName, String lastName, String username, String email, String password) throws UserAlreadyExistsException;

    User getUserById(String id);

    CompletableFuture<User> getUserById(UUID id) throws UserNotFoundException;

    CompletableFuture<User> getUserByEmail(String email) throws UserNotFoundException;

    CompletableFuture<User> getUserByUsername(String username) throws UserNotFoundException;

    CompletableFuture<List<User>> getUsersByShopperGroup(UUID groupId);

    CompletableFuture<List<User>> getUsers();

    CompletableFuture<Boolean> userExists(UUID id);

    CompletableFuture<Boolean> userExistsByGroup(UUID shopperId, UUID groupId);

    CompletableFuture<Boolean> userExistsByEmail(String email);

    CompletableFuture<Boolean> userExistsByUsername(String username);

    CompletableFuture<Boolean> userExistsAsAdminByGroup(UUID shopperId, UUID groupId);
}