package com.sp5blue.shopshare.services.shoppergroup;

import java.util.UUID;

public interface IInvitationService {

    boolean invite(UUID groupId, UUID shopperId);
    boolean acceptInvite(UUID groupId, UUID shopperId);
}
