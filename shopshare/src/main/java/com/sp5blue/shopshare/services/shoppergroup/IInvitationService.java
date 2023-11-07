package com.sp5blue.shopshare.services.shoppergroup;

import java.util.UUID;

public interface IInvitationService {
    void invite(UUID groupId, UUID shopperId);
    void acceptInvite(UUID groupId, UUID shopperId);

    void acceptInvite(String inviteToken);
}
