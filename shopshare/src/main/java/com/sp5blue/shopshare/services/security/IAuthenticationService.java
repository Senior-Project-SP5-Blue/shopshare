package com.sp5blue.shopshare.services.security;

import com.sp5blue.shopshare.security.request.SignInRequest;
import com.sp5blue.shopshare.security.request.SignUpRequest;
import com.sp5blue.shopshare.security.response.AuthenticationResponse;

public interface IAuthenticationService {
    AuthenticationResponse signUp(SignUpRequest request);

    AuthenticationResponse signIn(SignInRequest request);
}
