package com.sp5blue.shopshare.security.accessannotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.access.prepost.PreAuthorize;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize(
    "hasRole('ADMIN') or (authentication.principal.getId() == #userId and hasRole('GROUP_MEMBER-' + #groupId))")
public @interface GroupPermission {}
