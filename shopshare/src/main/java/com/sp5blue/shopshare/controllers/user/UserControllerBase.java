package com.sp5blue.shopshare.controllers.user;

import com.sp5blue.shopshare.dtos.user.ChangePasswordRequest;
import com.sp5blue.shopshare.security.accessannotations.UserPermission;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.UUID;

public interface UserControllerBase {
    @UserPermission
    ResponseEntity<?> changePassword(@PathVariable("user_id") UUID userId,
                                     @RequestBody @Valid ChangePasswordRequest request, Principal connectedUser);
}
