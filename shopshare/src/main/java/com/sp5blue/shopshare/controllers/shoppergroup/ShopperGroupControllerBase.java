package com.sp5blue.shopshare.controllers.shoppergroup;

import com.sp5blue.shopshare.dtos.shoppergroup.CreateEditShopperGroupRequest;
import com.sp5blue.shopshare.dtos.shoppergroup.ShopperGroupDto;
import com.sp5blue.shopshare.dtos.user.UserDto;
import com.sp5blue.shopshare.security.accessannotations.GroupAdminPermission;
import com.sp5blue.shopshare.security.accessannotations.GroupPermission;
import com.sp5blue.shopshare.security.accessannotations.UserPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@Tag(name = "Shopper Groups", description = """
        Showing group information regarding a user. Also, adding a user to group via invitation. NEED to be apart of group.""")

public interface ShopperGroupControllerBase {
    @UserPermission
    @Operation(
            summary = "Get all groups that the user is a part of"
    )
    ResponseEntity<List<ShopperGroupDto>> getShopperGroups(@PathVariable("user_id") UUID userId);

    @UserPermission
    @Operation(
            summary = "Creates a new group, user will be admin"
    )
    ResponseEntity<ShopperGroupDto> addShopperGroup(@PathVariable("user_id") UUID userId, @RequestBody CreateEditShopperGroupRequest request);

    @GroupPermission
    @Operation(
            summary = "Get a specific group that a user is a part of. Returns group"
    )
    ResponseEntity<ShopperGroupDto> getShopperGroup(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId);

    @UserPermission
    @Operation(
            summary = "Accepts invitation to a group. User will now be apart of group."
    )
    ResponseEntity<ShopperGroupDto> acceptShopperGroupInvitation(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId);

    @GroupAdminPermission
    @Operation(
            summary = "Deletes a group. This also deletes all lists and items of that group."
    )
    ResponseEntity<?> deleteShopperGroup(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId);

    @GroupAdminPermission
    @Operation(
            summary = "Changes a group's name"
    )
    ResponseEntity<?> modifyShopperGroup(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId, @RequestBody CreateEditShopperGroupRequest request);

    @GroupPermission
    @Operation(
            summary = "Get a list of all the users in the group"
    )
    ResponseEntity<List<UserDto>> getShopperGroupMembers(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId);

    @GroupPermission
    @Operation(
            summary = "Get a specific user in the group"
    )
    ResponseEntity<UserDto> getShopperGroupMember(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId, @PathVariable("member_id") UUID memberId);

    @GroupAdminPermission
    @Operation(
            summary = "Used to invite a user to group"
    )
    ResponseEntity<?> inviteShopperToGroup(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId, @PathVariable("new_member_id") UUID invitedShopperId);

    @GroupAdminPermission
    @Operation(
            summary = "Remove a user from a group - MUST be admin of group to do so"
    )
    ResponseEntity<?> removeShopperFromGroup(@PathVariable("user_id") UUID userId, @PathVariable("group_id") UUID groupId, @PathVariable("member_id") UUID memberId);
}
