package com.sp5blue.shopshare.services.security;

import com.sp5blue.shopshare.models.user.Token;
import com.sp5blue.shopshare.models.user.TokenType;
import com.sp5blue.shopshare.models.user.User;
import com.sp5blue.shopshare.security.request.SignInRequest;
import com.sp5blue.shopshare.security.request.SignUpRequest;
import com.sp5blue.shopshare.security.response.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.concurrent.CompletableFuture;

public interface IAuthenticationService {
    CompletableFuture<AuthenticationResponse> signUp(SignUpRequest request);

    CompletableFuture<AuthenticationResponse> signIn(SignInRequest request);

    void saveUserToken(User user, String token, TokenType tokenType);

    void saveUserTokens(Token... tokens);

    void refreshToken(HttpServletRequest request, HttpServletResponse response);
}
