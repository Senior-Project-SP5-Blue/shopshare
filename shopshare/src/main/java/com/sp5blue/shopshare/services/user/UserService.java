package com.sp5blue.shopshare.services.user;

import com.sp5blue.shopshare.dtos.user.ChangePasswordRequest;
import com.sp5blue.shopshare.exceptions.authentication.BadCredentialsException;
import com.sp5blue.shopshare.exceptions.authentication.UserAlreadyExistsException;
import com.sp5blue.shopshare.exceptions.authentication.UserNotFoundException;
import com.sp5blue.shopshare.models.user.User;
import com.sp5blue.shopshare.repositories.UserRepository;
import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Primary
public class UserService implements UserDetailsService, IUserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final Logger logger = LoggerFactory.getLogger(UserService.class);

  @Autowired
  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  @Transactional
  @Async
  public CompletableFuture<User> createOrSaveUser(User user) throws UserAlreadyExistsException {
    if (userRepository.existsByEmailIgnoreCase(user.getEmail())
        && !userRepository.existsById(user.getId()))
      throw new UserAlreadyExistsException("Email is unavailable");
    if (userRepository.existsByUsernameIgnoreCase(user.getUsername())
        && !userRepository.existsById(user.getId()))
      throw new UserAlreadyExistsException("Username is unavailable");
    return CompletableFuture.completedFuture(userRepository.save(user));
  }

  @Override
  @Transactional
  @Async
  public CompletableFuture<User> createOrSaveUser(
      String firstName,
      String lastName,
      String username,
      String email,
      String number,
      String password)
      throws UserAlreadyExistsException {
    if (userRepository.existsByEmailIgnoreCase(email))
      throw new UserAlreadyExistsException("Email is unavailable");
    if (userRepository.existsByUsernameIgnoreCase(username))
      throw new UserAlreadyExistsException("Username is unavailable");
    User user = new User(firstName, lastName, username, email, number, password);
    return CompletableFuture.completedFuture(userRepository.save(user));
  }

  @Override
  @Transactional
  public void changePassword(ChangePasswordRequest request, Principal connectedUser) {
    var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

    if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
      throw new BadCredentialsException("Invalid password");
    }
    if (!request.newPassword().equals(request.confirmPassword())) {
      throw new BadCredentialsException("Confirmation password must match");
    }

    user.setPassword(passwordEncoder.encode(request.newPassword()));
    userRepository.save(user);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
    return userRepository
        .findByEmailIgnoreCase(username)
        .orElseThrow(
            () -> new UserNotFoundException("Account with email does not exist - " + username));
  }

  @Override
  public User getUserById(String id) {
    UUID _id = UUID.fromString(id);
    return getUserById(_id).join();
  }

  @Override
  @Async
  public CompletableFuture<User> getUserById(UUID id) throws UserNotFoundException {
    return CompletableFuture.completedFuture(
        userRepository
            .findById(id)
            .orElseThrow(
                () -> new UserNotFoundException("Account with id does not exist - " + id)));
  }

  @Override
  @Async
  public CompletableFuture<User> getUserByEmail(String email) throws UserNotFoundException {
    return CompletableFuture.completedFuture(
        userRepository
            .findByEmailIgnoreCase(email)
            .orElseThrow(
                () -> new UserNotFoundException("Account with email does not exist - " + email)));
  }

  @Override
  @Async
  public CompletableFuture<User> getUserByUsername(String username) throws UserNotFoundException {
    return CompletableFuture.completedFuture(
        userRepository
            .findByUsernameIgnoreCase(username)
            .orElseThrow(
                () ->
                    new UserNotFoundException(
                        "Account with username does not exist - " + username)));
  }

  @Override
  @Async
  public CompletableFuture<List<User>> getUsersByShopperGroup(UUID groupId) {
    return CompletableFuture.completedFuture(userRepository.findAllByShopperGroup(groupId));
  }

  @Override
  @Async
  public CompletableFuture<User> getUserByShopperGroup(UUID groupId, UUID userId) {
    User user =
        userRepository
            .findByShopperGroup(groupId, userId)
            .orElseThrow(
                () -> new UserNotFoundException("Account with id does not exist - " + userId));
    return CompletableFuture.completedFuture(user);
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
  public CompletableFuture<Boolean> userExistsByGroup(String username, UUID groupId) {
    return CompletableFuture.completedFuture(userRepository.existsByGroup(username, groupId));
  }

  @Override
  @Async
  public CompletableFuture<Boolean> userExistsByEmail(String email) {
    return CompletableFuture.completedFuture(userRepository.existsByEmailIgnoreCase(email));
  }

  @Override
  @Async
  public CompletableFuture<Boolean> userExistsByUsername(String username) {
    return CompletableFuture.completedFuture(userRepository.existsByUsernameIgnoreCase(username));
  }

  @Override
  @Async
  public CompletableFuture<Boolean> userExistsAsAdminByGroup(UUID shopperId, UUID groupId) {
    return CompletableFuture.completedFuture(
        userRepository.existsAsAdminByGroup(shopperId, groupId));
  }
}
