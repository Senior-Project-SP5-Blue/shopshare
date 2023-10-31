package com.sp5blue.shopshare.controllers.shoppergroup;

import com.sp5blue.shopshare.dtos.shoppergroup.ShopperGroupDto;
import com.sp5blue.shopshare.dtos.user.UserDto;
import com.sp5blue.shopshare.services.shoppergroup.IInvitationService;
import com.sp5blue.shopshare.services.shoppergroup.IShopperGroupService;
import com.sp5blue.shopshare.services.user.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api-prefix}/users/{user_id}/groups")
public class ShopperGroupController implements ShopperGroupControllerBase {
    private final Logger logger = LoggerFactory.getLogger(ShopperGroupController.class);

    private final IShopperGroupService shopperGroupService;

    private final IInvitationService invitationService;

    @Autowired
    public ShopperGroupController(IShopperGroupService shopperGroupService, IUserService userService, IInvitationService invitationService) {
        this.shopperGroupService = shopperGroupService;
        this.invitationService = invitationService;
    }

    @Override
    @GetMapping
    public ResponseEntity<List<ShopperGroupDto>> getShopperGroups(@PathVariable("user_id") UUID userId) {
        return ResponseEntity.ok(shopperGroupService.getShopperGroups(userId).join());
    }

    @Override
    @PostMapping
    public ResponseEntity<ShopperGroupDto> addShopperGroup(@PathVariable("user_id") UUID userId, @RequestBody String name) {
        return ResponseEntity.ok().body(shopperGroupService.addShopperGroup(userId, name).join());
    }

    @Override
    @GetMapping("/{group_id}")
    public ResponseEntity<ShopperGroupDto> getShopperGroup(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId) {
        return ResponseEntity.ok(shopperGroupService.getShopperGroupById(userId, groupId).join());
    }

    @Override
    @DeleteMapping("/{group_id}")
    public ResponseEntity<?> deleteShopperGroup(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId) {
        shopperGroupService.deleteShopperGroup(userId, groupId);
        return ResponseEntity.status(200).body("Shopper group deleted successfully");
    }

    @Override
    @GetMapping("/{group_id}/members")
    public ResponseEntity<List<UserDto>> getShopperGroupMembers(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId) {
        return ResponseEntity.ok().body(shopperGroupService.getShopperGroupUsers(userId, groupId).join());
    }

    @Override
    @GetMapping("/{group_id}/members/{member_id}")
    public ResponseEntity<UserDto> getShopperGroupMember(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId, @PathVariable("member_id") UUID memberId) {
        return ResponseEntity.ok().body(shopperGroupService.getShopperGroupUser(userId, groupId, memberId).join());
    }

    @Override
    @PostMapping("/{group_id}/invitations/{member_id}")
    public ResponseEntity<Boolean> inviteShopperToGroup(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId, @PathVariable("member_id") UUID invitedShopperId) {
        return ResponseEntity.ok().body(invitationService.invite(groupId, invitedShopperId).join());
    }

    @Override
    @DeleteMapping("/{group_id}/members/{member_id}")
    public ResponseEntity<?> removeShopperFromGroup(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId, @PathVariable("member_id") UUID memberId) {
        boolean shopperRemoved = shopperGroupService.removeUserFromShopperGroup(userId, groupId, memberId).join();
        if (!shopperRemoved) return ResponseEntity.status(404).body("Shopper is not in group");
        return ResponseEntity.ok().body("Shopper removed successfully");
    }
}
