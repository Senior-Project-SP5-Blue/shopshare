package com.sp5blue.shopshare.dtos.user;

public record ChangePasswordRequest(String currentPassword, String newPassword, String confirmPassword) {
}
