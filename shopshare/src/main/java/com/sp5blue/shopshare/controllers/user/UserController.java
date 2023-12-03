package com.sp5blue.shopshare.controllers.user;

import com.sp5blue.shopshare.dtos.user.ChangePasswordRequest;
import com.sp5blue.shopshare.models.user.User;
import com.sp5blue.shopshare.security.accessannotations.AdminPermission;
import com.sp5blue.shopshare.security.accessannotations.UserPermission;
import com.sp5blue.shopshare.services.user.IUserService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api-prefix}/users")
public class UserController implements UserControllerBase {

  private final IUserService userService;

  @Autowired
  public UserController(IUserService userService) {
    this.userService = userService;
  }

  @GetMapping
  @AdminPermission
  public List<User> getUsers() {
    return userService.getUsers().join();
  }

  @Override
  @PatchMapping("/{user_id}/password")
  public ResponseEntity<?> changePassword(@PathVariable("user_id") UUID userId,
      @RequestBody @Valid ChangePasswordRequest request, Principal connectedUser) {
    userService.changePassword(request, connectedUser);
    return ResponseEntity.ok().build();
  }
}
