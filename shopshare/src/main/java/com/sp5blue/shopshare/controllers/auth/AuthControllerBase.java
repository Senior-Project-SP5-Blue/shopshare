package com.sp5blue.shopshare.controllers.auth;

import com.sp5blue.shopshare.dtos.auth.SignInRequest;
import com.sp5blue.shopshare.dtos.auth.SignUpRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Authorization", description = "Signin, Signup, and refreshing Signin")
public interface AuthControllerBase {

  @Operation(summary = "Logs in users, returns tokens")
  ResponseEntity<?> authenticateAndGetToken(@RequestBody SignInRequest authRequest);

  @Operation(summary = "Creates new user account, returns tokens")
  ResponseEntity<?> authenticateAndGetToken(@RequestBody SignUpRequest authRequest);

  @Operation(summary = "Confirms user's email address")
  ResponseEntity<?> confirmEmail(@RequestParam("token") String token);

  @Operation(summary = "Refreshes login by providing new access token")
  void refreshToken(HttpServletRequest request, HttpServletResponse response);
}
