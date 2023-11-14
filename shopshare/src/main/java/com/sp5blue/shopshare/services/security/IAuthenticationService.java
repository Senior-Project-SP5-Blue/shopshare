package com.sp5blue.shopshare.services.security;

import com.sp5blue.shopshare.dtos.auth.AuthenticationResponse;
import com.sp5blue.shopshare.dtos.auth.SignInRequest;
import com.sp5blue.shopshare.dtos.auth.SignUpRequest;
import com.sp5blue.shopshare.models.user.Token;
import com.sp5blue.shopshare.models.user.TokenType;
import com.sp5blue.shopshare.models.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.concurrent.CompletableFuture;

public interface IAuthenticationService {
    void signUp(SignUpRequest request);

    CompletableFuture<AuthenticationResponse> signIn(SignInRequest request);

    void confirmEmail(String token);

    void saveUserToken(User user, String token, TokenType tokenType);

    void saveUserTokens(Token... tokens);

    void refreshToken(HttpServletRequest request, HttpServletResponse response);
}
