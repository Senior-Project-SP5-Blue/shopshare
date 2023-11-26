package com.sp5blue.shopshare.controllers.user;

import com.sp5blue.shopshare.dtos.user.ChangePasswordRequest;
import com.sp5blue.shopshare.models.user.User;
import com.sp5blue.shopshare.security.accessannotations.AdminPermission;
import com.sp5blue.shopshare.security.accessannotations.UserPermission;
import com.sp5blue.shopshare.services.user.IUserService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api-prefix}/users")
public class UserController {

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

  @PatchMapping("/{user_id}/password")
  @UserPermission
  public ResponseEntity<?> changePassword(
      @RequestBody @Valid ChangePasswordRequest request, Principal connectedUser) {
    userService.changePassword(request, connectedUser);
    return ResponseEntity.ok().build();
  }
}
