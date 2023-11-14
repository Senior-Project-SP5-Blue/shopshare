package com.sp5blue.shopshare.dtos.shoppergroup;

import com.sp5blue.shopshare.models.shoppergroup.Invitation;

import java.util.UUID;

public record InvitationDto(UUID groupId, String groupName) {
    public InvitationDto(Invitation inv) {
        this(
                inv.getId().getGroupId(),
                inv.getGroup().getName()
        );
    }
}
