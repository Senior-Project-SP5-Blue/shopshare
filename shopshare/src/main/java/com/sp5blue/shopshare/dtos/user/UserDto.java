package com.sp5blue.shopshare.dtos.user;

import com.sp5blue.shopshare.models.user.User;
import java.util.UUID;

public record UserDto(UUID id, String firstName, String lastName, String username, String email) {
  public UserDto(User user) {
    this(
        user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail());
  }
}
