package com.sp5blue.shopshare.services.shoppergroup;

import com.sp5blue.shopshare.dtos.shoppergroup.InvitationDto;
import com.sp5blue.shopshare.exceptions.shoppergroup.UserAlreadyInGroupException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface IInvitationService {
  CompletableFuture<List<InvitationDto>> getInvitations(UUID userId);

  CompletableFuture<Void> invite(UUID groupId, UUID shopperId);

  CompletableFuture<Void> invite(UUID groupId, String username) throws UserAlreadyInGroupException;

  CompletableFuture<Void> acceptInvite(UUID groupId, UUID shopperId);

    CompletableFuture<Void> declineInvite(UUID groupId, UUID userId);

    CompletableFuture<Void> acceptInvite(String inviteToken);
}
