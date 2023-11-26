package com.sp5blue.shopshare.controllers.auth;

import com.sp5blue.shopshare.dtos.auth.SignInRequest;
import com.sp5blue.shopshare.dtos.auth.SignUpRequest;
import com.sp5blue.shopshare.services.security.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api-prefix}/auth")
public class AuthController implements AuthControllerBase {
  private final AuthenticationService authenticationService;

  private final Logger logger = LoggerFactory.getLogger(AuthController.class);

  @Autowired
  public AuthController(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @Override
  @PostMapping("/signin")
  public ResponseEntity<?> authenticateAndGetToken(@RequestBody @Valid SignInRequest authRequest) {
    return ResponseEntity.ok(authenticationService.signIn(authRequest).join());
  }

  @Override
  @PostMapping("/signup")
  public ResponseEntity<?> authenticateAndGetToken(@RequestBody @Valid SignUpRequest authRequest) {
    authenticationService.signUp(authRequest).join();
    return ResponseEntity.noContent().build();
  }

  @Override
  @RequestMapping(
      value = "/confirm-signup",
      method = {RequestMethod.GET, RequestMethod.POST})
  public ResponseEntity<String> confirmEmail(@RequestParam("token") String token) {
    authenticationService.confirmEmail(token).join();
    String content = """
                <h1>Account Email Verified</h1>""";
    HttpHeaders responseHeader = new HttpHeaders();
    responseHeader.setContentType(MediaType.TEXT_HTML);
    return new ResponseEntity<>(content, responseHeader, HttpStatus.OK);
  }

  @Override
  @PostMapping("/refresh-signin")
  public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
    authenticationService.refreshToken(request, response);
  }
}
