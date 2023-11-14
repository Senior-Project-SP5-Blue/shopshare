package com.sp5blue.shopshare.security.accessannotations;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasRole('ADMIN') or (authentication.principal.getId() == #userId and hasRole('GROUP_MEMBER-' + #groupId))")
public @interface GroupPermission {
}
