package com.sp5blue.shopshare.controllers.shoppergroup;

import com.sp5blue.shopshare.dtos.shoppergroup.CreateEditShopperGroupRequest;
import com.sp5blue.shopshare.dtos.shoppergroup.InvitationDto;
import com.sp5blue.shopshare.dtos.shoppergroup.ShopperGroupDto;
import com.sp5blue.shopshare.dtos.shoppergroup.SlimShopperGroupDto;
import com.sp5blue.shopshare.dtos.user.UserDto;
import com.sp5blue.shopshare.services.shoppergroup.IInvitationService;
import com.sp5blue.shopshare.services.shoppergroup.IShopperGroupService;
import com.sp5blue.shopshare.services.user.IUserService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api-prefix}/users/{user_id}/groups")
public class ShopperGroupController implements ShopperGroupControllerBase {
  private final Logger logger = LoggerFactory.getLogger(ShopperGroupController.class);

  private final IShopperGroupService shopperGroupService;

  private final IInvitationService invitationService;

  @Autowired
  public ShopperGroupController(
      IShopperGroupService shopperGroupService,
      IUserService userService,
      IInvitationService invitationService) {
    this.shopperGroupService = shopperGroupService;
    this.invitationService = invitationService;
  }

  @Override
  @GetMapping
  public ResponseEntity<List<SlimShopperGroupDto>> getShopperGroups(
      @PathVariable("user_id") UUID userId) {
    return ResponseEntity.ok(shopperGroupService.getShopperGroups(userId).join());
  }

  @Override
  @GetMapping("/invitations")
  public ResponseEntity<List<InvitationDto>> getInvitations(@PathVariable("user_id") UUID userId) {
    return ResponseEntity.ok(invitationService.getInvitations(userId).join());
  }

  @Override
  @PostMapping
  public ResponseEntity<SlimShopperGroupDto> addShopperGroup(
      @PathVariable("user_id") UUID userId,
      @RequestBody @Valid CreateEditShopperGroupRequest request) {
    return ResponseEntity.ok()
        .body(shopperGroupService.addShopperGroup(userId, request.name()).join());
  }

  @Override
  @GetMapping("/{group_id}")
  public ResponseEntity<ShopperGroupDto> getShopperGroup(
      @PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId) {
    return ResponseEntity.ok(shopperGroupService.getShopperGroupById(userId, groupId).join());
  }

  @Override
  @PostMapping("/{group_id}")
  public ResponseEntity<SlimShopperGroupDto> acceptShopperGroupInvitation(
      @PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId) {
    invitationService.acceptInvite(groupId, userId);
    return ResponseEntity.noContent().build();
  }

  @Override
  @DeleteMapping("/{group_id}")
  public ResponseEntity<?> deleteShopperGroup(
      @PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId) {
    shopperGroupService.deleteShopperGroup(userId, groupId);
    return ResponseEntity.noContent().build();
  }

  @Override
  @PatchMapping("/{group_id}")
  public ResponseEntity<?> modifyShopperGroup(
      @PathVariable("user_id") UUID userId,
      @PathVariable("group_id") UUID groupId,
      @RequestBody @Valid CreateEditShopperGroupRequest request) {
    return ResponseEntity.ok()
        .body(shopperGroupService.changeShopperGroupName(userId, groupId, request.name()).join());
  }

  @Override
  @GetMapping("/{group_id}/members")
  public ResponseEntity<List<UserDto>> getShopperGroupMembers(
      @PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId) {
    return ResponseEntity.ok()
        .body(shopperGroupService.getShopperGroupUsers(userId, groupId).join());
  }

  @Override
  @GetMapping("/{group_id}/members/{member_id}")
  public ResponseEntity<UserDto> getShopperGroupMember(
      @PathVariable("user_id") UUID userId,
      @PathVariable("group_id") UUID groupId,
      @PathVariable("member_id") UUID memberId) {
    return ResponseEntity.ok()
        .body(shopperGroupService.getShopperGroupUser(userId, groupId, memberId).join());
  }

  @Override
  @PostMapping("/{group_id}/invitations/{new_member_username}")
  public ResponseEntity<?> inviteShopperToGroup(
      @PathVariable("user_id") UUID userId,
      @PathVariable("group_id") UUID groupId,
      @PathVariable("new_member_username") String username) {
    invitationService.invite(groupId, username);
    return ResponseEntity.noContent().build();
  }

  @Override
  @DeleteMapping("/{group_id}/members/{member_id}")
  public ResponseEntity<?> removeShopperFromGroup(
      @PathVariable("user_id") UUID userId,
      @PathVariable("group_id") UUID groupId,
      @PathVariable("member_id") UUID memberId) {
    boolean shopperRemoved =
        shopperGroupService.removeUserFromShopperGroup(userId, groupId, memberId).join();
    if (!shopperRemoved) return ResponseEntity.status(404).body("Shopper is not in group");
    return ResponseEntity.noContent().build();
  }
}
