package com.sp5blue.shopshare.controllers.shoppergroup;

import com.sp5blue.shopshare.services.shopper.IShopperService;
import com.sp5blue.shopshare.services.shoppergroup.IInvitationService;
import com.sp5blue.shopshare.services.shoppergroup.IShopperGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("${api-prefix}/users/{user_id}/groups")
public class ShopperGroupController {
    private final Logger logger = LoggerFactory.getLogger(ShopperGroupController.class);

    private final IShopperGroupService shopperGroupService;

    private final IShopperService shopperService;

    private final IInvitationService invitationService;

    @Autowired
    public ShopperGroupController(IShopperGroupService shopperGroupService, IShopperService shopperService, IInvitationService invitationService) {
        this.shopperGroupService = shopperGroupService;
        this.shopperService = shopperService;
        this.invitationService = invitationService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.getId() == #userId")
    public ResponseEntity<?> getShopperGroups(@PathVariable("user_id") UUID userId) {
        return ResponseEntity.ok(shopperGroupService.getShopperGroups(userId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.getId() == #userId")
    public ResponseEntity<?> addShopperGroup(@PathVariable("user_id") UUID userId, @RequestBody String name) {
        return ResponseEntity.ok().body(shopperGroupService.createShopperGroup(userId, name));
    }

    @GetMapping("/{group_id}")
    @PreAuthorize("hasRole('ADMIN') or (authentication.principal.getId() == #userId and hasRole('GROUP_MEMBER-' + #groupId))")
    public ResponseEntity<?> getShopperGroup(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId) {
        return ResponseEntity.ok(shopperGroupService.getShopperGroupById(userId, groupId));
    }

    @DeleteMapping("/{group_id}")
    @PreAuthorize("hasRole('ADMIN') or (authentication.principal.getId() == #userId and hasRole('GROUP_ADMIN-' + #groupId))")
    public ResponseEntity<?> deleteShopperGroup(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId) {
        shopperGroupService.deleteShopperGroup(userId, groupId);
        return ResponseEntity.status(200).body("Shopper group deleted successfully");
    }

    @GetMapping("/{group_id}/members")
    @PreAuthorize("hasRole('ADMIN') or (authentication.principal.getId() == #userId and hasRole('GROUP_MEMBER-' + #groupId))")
    public ResponseEntity<?> getShopperGroupMembers(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId) {
        return ResponseEntity.ok().body(shopperService.getShoppersByShopperGroup(groupId));
    }

    @PostMapping("/{group_id}/invitations/{shopper_id}")
    @PreAuthorize("hasRole('ADMIN') or (authentication.principal.getId() == #userId and hasRole('GROUP_ADMIN-' + #groupId))")
    public ResponseEntity<?> inviteShopperToGroup(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId, @PathVariable("shopper_id") UUID invitedShopperId) {
        return ResponseEntity.ok().body(invitationService.invite(groupId, invitedShopperId));
    }

    @DeleteMapping("/{group_id}/members/{shopper_id}")
    @PreAuthorize("hasRole('ADMIN') or (authentication.principal.getId() == #userId and hasRole('GROUP_ADMIN-' + #groupId))")
    public ResponseEntity<?> removeShopperFromGroup(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId, @PathVariable("shopper_id") UUID shopperId) {
        boolean shopperRemoved = shopperGroupService.removeShopperFromShopperGroup(userId, groupId, shopperId);
        if (!shopperRemoved) return ResponseEntity.status(404).body("Shopper is not in group");
        return ResponseEntity.ok().body("Shopper removed successfully");
    }
}
