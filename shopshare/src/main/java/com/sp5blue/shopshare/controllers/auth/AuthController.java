package com.sp5blue.shopshare.controllers.auth;


import com.sp5blue.shopshare.security.request.SignInRequest;
import com.sp5blue.shopshare.security.request.SignUpRequest;
import com.sp5blue.shopshare.services.security.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api-prefix}/auth")
public class AuthController {
    private final AuthenticationService authenticationService;

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody SignInRequest authRequest) {
        return ResponseEntity.ok(authenticationService.signIn(authRequest));
    }
    @PostMapping("/signup")
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody SignUpRequest authRequest) {
        return ResponseEntity.ok(authenticationService.signUp(authRequest));
    }

    @PostMapping("/refresh-signin")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        authenticationService.refreshToken(request, response);
    }

}
