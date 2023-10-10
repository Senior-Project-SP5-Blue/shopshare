package com.sp5blue.shopshare.services.security;

import com.sp5blue.shopshare.models.Shopper;
import com.sp5blue.shopshare.models.TokenType;
import com.sp5blue.shopshare.security.request.SignInRequest;
import com.sp5blue.shopshare.security.request.SignOutRequest;
import com.sp5blue.shopshare.security.request.SignUpRequest;
import com.sp5blue.shopshare.security.response.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

public interface IAuthenticationService {
    AuthenticationResponse signUp(SignUpRequest request);

    AuthenticationResponse signIn(SignInRequest request);

    @Transactional
    void saveUserToken(Shopper user, String token, TokenType tokenType);

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

    @Transactional
    void _revokeAllTokens(Shopper shopper);
}
