package com.sp5blue.shopshare.services.user;

import com.sp5blue.shopshare.exceptions.authentication.UserAlreadyExistsException;
import com.sp5blue.shopshare.exceptions.authentication.UserNotFoundException;
import com.sp5blue.shopshare.models.user.User;
import com.sp5blue.shopshare.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

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
    public User createUser(User user) throws UserAlreadyExistsException {
        if (userRepository.existsByEmail(user.getEmail())) throw new UserAlreadyExistsException("Shopper with email already exists - " + user.getEmail());
        if (userRepository.existsByUsername(user.getUsername())) throw new UserAlreadyExistsException("Shopper with username already exists - " + user.getUsername());
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User createUser(String firstName, String lastName, String username, String email, String password) throws UserAlreadyExistsException {
        if (userRepository.existsByEmail(email)) throw new UserAlreadyExistsException("Shopper with email already exists - " + email);
        if (userRepository.existsByEmail(username)) throw new UserAlreadyExistsException("Shopper with username already exists - " + username);
        User user = new User(firstName, lastName, username, email, password);
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User does not exist - " + username));
    }

    @Override
    public User getUserById(String id){
        UUID _id = UUID.fromString(id);
        return getUserById(_id);
    }

    @Override
    public User getUserById(UUID id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User does not exist - " + id));
    }

    @Override
    public User getUserByEmail(String email) throws UserNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User does not exist - " + email));
    }

    @Override
    public User getUserByUsername(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User does not exist - " + username));
    }

    @Override
    public List<User> getUsersByShopperGroup(UUID groupId) {
        return userRepository.findAllByShopperGroup(groupId);
    }

    @Override
    public User getUserByShopperGroup(UUID groupId, UUID userId) {
        return userRepository.findByShopperGroup(groupId, userId).orElseThrow(() -> new UserNotFoundException("User does not exist - " + userId));
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean userExists(UUID id) {
        return userRepository.existsById(id);
    }

    @Override
    public boolean userExistsByGroup(UUID shopperId, UUID groupId) {
        return userRepository.existsByGroup(shopperId, groupId);
    }


    @Override
    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean userExistsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean userExistsAsAdminByGroup(UUID shopperId, UUID groupId) {
        return userRepository.existsAsAdminByGroup(shopperId, groupId);
    }
}
