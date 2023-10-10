package com.sp5blue.shopshare.security.response;

public record AuthenticationResponse (String accessToken, String refreshToken) {
}
