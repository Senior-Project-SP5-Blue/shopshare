package com.sp5blue.shopshare.services.user;

import com.sp5blue.shopshare.exceptions.authentication.UserAlreadyExistsException;
import com.sp5blue.shopshare.exceptions.authentication.UserNotFoundException;
import com.sp5blue.shopshare.models.user.User;
import com.sp5blue.shopshare.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@Primary
public class UserService implements UserDetailsService, IUserService {

    private final UserRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    @Async
    public CompletableFuture<User> createUser(User user) throws UserAlreadyExistsException {
        if (userRepository.existsByEmail(user.getEmail())) throw new UserAlreadyExistsException("Shopper with email already exists - " + user.getEmail());
        if (userRepository.existsByUsername(user.getUsername())) throw new UserAlreadyExistsException("Shopper with username already exists - " + user.getUsername());
        return CompletableFuture.completedFuture(userRepository.save(user));
    }

    @Override
    @Transactional
    @Async
    public CompletableFuture<User> createUser(String firstName, String lastName, String username, String email, String password) throws UserAlreadyExistsException {
        if (userRepository.existsByEmail(email)) throw new UserAlreadyExistsException("Shopper with email already exists - " + email);
        if (userRepository.existsByEmail(username)) throw new UserAlreadyExistsException("Shopper with username already exists - " + username);
        User user = new User(firstName, lastName, username, email, password);
        return CompletableFuture.completedFuture(userRepository.save(user));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User does not exist - " + username));
    }

    @Override
    public User getUserById(String id){
        UUID _id = UUID.fromString(id);
        return getUserById(_id).join();
    }

    @Override
    @Async
    public CompletableFuture<User> getUserById(UUID id) throws UserNotFoundException {
        return CompletableFuture.completedFuture(userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User does not exist - " + id)));
    }

    @Override
    @Async
    public CompletableFuture<User> getUserByEmail(String email) throws UserNotFoundException {
        logger.warn("Right before getting by user");
        return CompletableFuture.completedFuture(userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User does not exist - " + email)));
    }

    @Override
    @Async
    public CompletableFuture<User> getUserByUsername(String username) throws UserNotFoundException {
        return CompletableFuture.completedFuture(userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User does not exist - " + username)));
    }

    @Override
    public CompletableFuture<List<User>> getUsersByShopperGroup(UUID groupId) {
        return CompletableFuture.completedFuture(userRepository.findAllByShopperGroup(groupId));
    }

    @Override
    public CompletableFuture<User> getUserByShopperGroup(UUID groupId, UUID userId) {
        return userRepository.findByShopperGroup(groupId, userId).orElseThrow(() -> new UserNotFoundException("User does not exist - " + userId));
    }

    @Override
    @Async
    public CompletableFuture<List<User>> getUsers() {
        return CompletableFuture.completedFuture(userRepository.findAll());
    }

    @Override
    @Async
    public CompletableFuture<Boolean> userExists(UUID id) {
        return CompletableFuture.completedFuture(userRepository.existsById(id));
    }

    @Override
    @Async
    public CompletableFuture<Boolean> userExistsByGroup(UUID shopperId, UUID groupId) {
        return CompletableFuture.completedFuture(userRepository.existsByGroup(shopperId, groupId));
    }


    @Override
    @Async
    public CompletableFuture<Boolean> userExistsByEmail(String email) {
        return CompletableFuture.completedFuture(userRepository.existsByEmail(email));
    }

    @Override
    @Async
    public CompletableFuture<Boolean> userExistsByUsername(String username) {
        return CompletableFuture.completedFuture(userRepository.existsByUsername(username));
    }

    @Override
    @Async
    public CompletableFuture<Boolean> userExistsAsAdminByGroup(UUID shopperId, UUID groupId) {
        return CompletableFuture.completedFuture(userRepository.existsAsAdminByGroup(shopperId, groupId));
    }
}
