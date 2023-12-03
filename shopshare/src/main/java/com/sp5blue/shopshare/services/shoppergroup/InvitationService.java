package com.sp5blue.shopshare.services.shoppergroup;

import com.sp5blue.shopshare.dtos.shoppergroup.InvitationDto;
import com.sp5blue.shopshare.events.OnInvitationCompleteEvent;
import com.sp5blue.shopshare.exceptions.shoppergroup.UserAlreadyInGroupException;
import com.sp5blue.shopshare.exceptions.shoppergroup.UserNotInvitedException;
import com.sp5blue.shopshare.models.shoppergroup.Invitation;
import com.sp5blue.shopshare.models.shoppergroup.InvitationId;
import com.sp5blue.shopshare.models.shoppergroup.ShopperGroup;
import com.sp5blue.shopshare.models.user.Token;
import com.sp5blue.shopshare.models.user.User;
import com.sp5blue.shopshare.repositories.InvitationRepository;
import com.sp5blue.shopshare.services.security.JwtService;
import com.sp5blue.shopshare.services.token.ITokenService;
import com.sp5blue.shopshare.services.user.IUserService;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InvitationService implements IInvitationService {
  private final Logger logger = LoggerFactory.getLogger(InvitationService.class);

  private final IShopperGroupService shopperGroupService;

  private final IUserService userService;

  private final ApplicationEventPublisher eventPublisher;

  private final ITokenService tokenService;

  private final JwtService jwtService;

  private final InvitationRepository invitationRepository;

  @Autowired
  public InvitationService(
      IShopperGroupService shopperGroupService,
      IUserService userService,
      ApplicationEventPublisher eventPublisher,
      ITokenService tokenService,
      JwtService jwtService,
      InvitationRepository invitationRepository) {
    this.shopperGroupService = shopperGroupService;
    this.userService = userService;
    this.eventPublisher = eventPublisher;
    this.tokenService = tokenService;
    this.jwtService = jwtService;
    this.invitationRepository = invitationRepository;
  }

  @Override
  @Async
  @Transactional
  public CompletableFuture<List<InvitationDto>> getInvitations(UUID userId) {
    var _invitations = invitationRepository.findAllByUserId(userId);
    var invitations = _invitations.stream().map(InvitationDto::new).toList();
    return CompletableFuture.completedFuture(invitations);
  }

  @Override
  @Async
  @Transactional(rollbackFor = Exception.class)
  public CompletableFuture<Void> invite(UUID groupId, UUID userId)
      throws UserAlreadyInGroupException {
    boolean userInGroup = shopperGroupService.userExistsInGroup(userId, groupId).join();

    CompletableFuture<User> getUser = userService.getUserById(userId);
    CompletableFuture<ShopperGroup> getGroup = shopperGroupService.findShopperGroupById(groupId);

    CompletableFuture.allOf(getUser, getGroup).join();

    User user = getUser.join();
    ShopperGroup group = getGroup.join();

    if (userInGroup) throw new UserAlreadyInGroupException("User is already a member of group");

    Invitation invitation = new Invitation(group, user);
    invitationRepository.save(invitation);

    eventPublisher.publishEvent(new OnInvitationCompleteEvent(this, user, group));
    return null;
  }

  @Override
  @Async
  @Transactional(rollbackFor = Exception.class)
  public CompletableFuture<Void> invite(UUID groupId, String username)
      throws UserAlreadyInGroupException {
    boolean userInGroup = shopperGroupService.userExistsInGroup(username, groupId).join();

    CompletableFuture<User> getUser = userService.getUserByUsername(username);
    CompletableFuture<ShopperGroup> getGroup = shopperGroupService.findShopperGroupById(groupId);

    CompletableFuture.allOf(getUser, getGroup).join();

    User user = getUser.join();
    ShopperGroup group = getGroup.join();

    if (userInGroup) throw new UserAlreadyInGroupException("User is already a member of group");

    Invitation invitation = new Invitation(group, user);
    invitationRepository.save(invitation);

    eventPublisher.publishEvent(new OnInvitationCompleteEvent(this, user, group));
    return null;
  }

  @Override
  @Transactional
  @Async
  public CompletableFuture<Void> acceptInvite(UUID groupId, UUID userId) {
    InvitationId inviteId = new InvitationId(groupId, userId);
    boolean hasInvite = invitationRepository.existsById(inviteId);

    if (!hasInvite) throw new UserNotInvitedException("Invalid Invitation");

    shopperGroupService.addUserToShopperGroup(groupId, userId).join();
    invitationRepository.deleteById(inviteId);
    invitationRepository.flush();
    return null;
  }

  @Override
  @Transactional
  @Async
  public CompletableFuture<Void> declineInvite(UUID groupId, UUID userId) {
    InvitationId inviteId = new InvitationId(groupId, userId);
    boolean hasInvite = invitationRepository.existsById(inviteId);

    if (!hasInvite) throw new UserNotInvitedException("Invalid Invitation");

    invitationRepository.deleteById(inviteId);
    invitationRepository.flush();
    return null;
  }

  @Override
  @Transactional
  @Async
  public CompletableFuture<Void> acceptInvite(String inviteToken) {
    Token invitationToken = tokenService.readByInvitationToken(inviteToken).join();
    UUID userId = UUID.fromString(jwtService.extractSubject(inviteToken));
    UUID groupId = UUID.fromString(jwtService.extractSubject("group"));

    InvitationId inviteId = new InvitationId(groupId, userId);

    boolean hasInvite = invitationRepository.existsById(inviteId);

    if (!hasInvite || invitationToken.isExpired())
      throw new UserNotInvitedException("Invalid Invitation");

    shopperGroupService.addUserToShopperGroup(groupId, userId).join();

    invitationRepository.deleteById(inviteId);
    tokenService.createOrSave(invitationToken);
    return null;
  }
}
