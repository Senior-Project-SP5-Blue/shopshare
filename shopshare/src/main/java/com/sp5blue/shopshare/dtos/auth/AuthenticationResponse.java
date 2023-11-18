package com.sp5blue.shopshare.dtos.auth;

import com.sp5blue.shopshare.dtos.user.UserDto;

public record AuthenticationResponse (String accessToken, String refreshToken, UserDto userContext) {
}
