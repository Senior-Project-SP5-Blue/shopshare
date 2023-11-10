package com.sp5blue.shopshare.services.shoppergroup;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface IInvitationService {
    CompletableFuture<Void> invite(UUID groupId, UUID shopperId);
    CompletableFuture<Void> acceptInvite(UUID groupId, UUID shopperId);

    CompletableFuture<Void> acceptInvite(String inviteToken);
}
