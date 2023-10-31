package com.sp5blue.shopshare.controllers.auth;

import com.sp5blue.shopshare.security.request.SignInRequest;
import com.sp5blue.shopshare.security.request.SignUpRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Authorization", description = "Signin, Signup, and refreshing Signin")
public interface AuthControllerBase {

    @Operation(
            summary = "Logs in users, returns tokens"
    )
    ResponseEntity<?> authenticateAndGetToken(@RequestBody SignInRequest authRequest);

    @Operation(
            summary = "Creates new user account, returns tokens"
    )
    ResponseEntity<?> authenticateAndGetToken(@RequestBody SignUpRequest authRequest);

    @Operation(
            summary = "Refreshes login by providing new access token"
    )
    void refreshToken(HttpServletRequest request, HttpServletResponse response);
}
