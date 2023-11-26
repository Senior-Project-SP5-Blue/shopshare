package com.sp5blue.shopshare.services.shoppergroup;

import com.sp5blue.shopshare.dtos.shoppergroup.InvitationDto;
import com.sp5blue.shopshare.exceptions.shoppergroup.UserAlreadyInGroupException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface IInvitationService {
  CompletableFuture<List<InvitationDto>> getInvitations(UUID userId);

  CompletableFuture<Void> invite(UUID groupId, UUID shopperId);

  CompletableFuture<Void> invite(UUID groupId, String username) throws UserAlreadyInGroupException;

  CompletableFuture<Void> acceptInvite(UUID groupId, UUID shopperId);

  CompletableFuture<Void> acceptInvite(String inviteToken);
}
