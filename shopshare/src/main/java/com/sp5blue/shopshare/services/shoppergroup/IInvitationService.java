package com.sp5blue.shopshare.services.shoppergroup;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface IInvitationService {
    CompletableFuture<Boolean> invite(UUID groupId, UUID shopperId);
    CompletableFuture<Boolean> acceptInvite(UUID groupId, UUID shopperId);
}
