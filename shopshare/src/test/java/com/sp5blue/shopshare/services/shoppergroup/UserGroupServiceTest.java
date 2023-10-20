package com.sp5blue.shopshare.services.shoppergroup;

import com.sp5blue.shopshare.exceptions.shoppergroup.GroupNotFoundException;
import com.sp5blue.shopshare.exceptions.shoppergroup.InvalidUserPermissionsException;
import com.sp5blue.shopshare.exceptions.shoppergroup.RemoveGroupAdminException;
import com.sp5blue.shopshare.models.user.User;
import com.sp5blue.shopshare.models.shoppergroup.ShopperGroup;
import com.sp5blue.shopshare.repositories.ShopperGroupRepository;
import com.sp5blue.shopshare.services.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserGroupServiceTest {

    @Mock
    UserService userService;

    @Mock
    ShopperGroupRepository shopperGroupRepository;

    @InjectMocks
    ShopperGroupService shopperGroupService1;

    @Test
    void createShopperGroup_CreatesShopperGroup() {
        User user = new User();
        ArgumentCaptor<ShopperGroup> groupCaptor = ArgumentCaptor.forClass(ShopperGroup.class);
        when(userService.getUserById(user.getId())).thenReturn(user);
        when(shopperGroupRepository.save(any(ShopperGroup.class))).thenAnswer(i -> i.getArguments()[0]);

        var result = shopperGroupService1.createShopperGroup(user.getId(), "Group 1");

        verify(shopperGroupRepository).save(groupCaptor.capture());
        assertEquals(groupCaptor.getValue(), result);
        assertTrue(user.getRoles().stream().anyMatch(g -> g.getAuthority().equals("ROLE_GROUP_ADMIN-" + groupCaptor.getValue().getId())));
    }

    @Test
    void getShopperGroups_NoMatches_ReturnsEmptyList() {
        UUID shopperId = UUID.randomUUID();
        var results = shopperGroupService1.getShopperGroups(shopperId);
        assertTrue(results.isEmpty());
    }

    @Test
    void getShopperGroups_Matches_ReturnsMatches() {
        User admin = new User();
        ShopperGroup shopperGroup1 = new ShopperGroup("Group1", admin);
        ShopperGroup shopperGroup2 = new ShopperGroup("Group2", admin);
        User user1 = new User();
        shopperGroup1.addUser(user1);
        shopperGroup2.addUser(user1);

        when(shopperGroupRepository.findAllByUserId(user1.getId())).thenReturn(Arrays.asList(shopperGroup1, shopperGroup2));

        var results = shopperGroupService1.getShopperGroups(user1.getId());
        assertEquals(2, results.size());
        assertAll(
                () -> assertEquals(shopperGroup1, results.get(0)),
                () -> assertEquals(shopperGroup2, results.get(1))
        );
    }

    @Test
    void getShopperGroupById_NotMember_ThrowsGroupNotFoundException() {
        User admin = new User();
        ShopperGroup shopperGroup1 = new ShopperGroup("Group1", admin);
        User user1 = new User();

        var exception = assertThrows(GroupNotFoundException.class, () -> shopperGroupService1.getShopperGroupById(user1.getId(), shopperGroup1.getId()));
        assertEquals("Shopper group does not exist - " + shopperGroup1.getId(), exception.getMessage());
    }

    @Test
    void getShopperGroupById_Member_ReturnsShopperGroup() {
        User admin = new User();
        ShopperGroup shopperGroup1 = new ShopperGroup("Group1", admin);
        User user1 = new User();
        shopperGroup1.addUser(user1);
        when(shopperGroupRepository.findByUserIdAndId(user1.getId(), shopperGroup1.getId())).thenReturn(Optional.of(shopperGroup1));

        var result = shopperGroupService1.getShopperGroupById(user1.getId(), shopperGroup1.getId());
        assertEquals(shopperGroup1, result);
    }

    @Test
    void deleteShopperGroup_NotMember_ThrowsGroupNotFoundException() {
        User user1 = new User();
        UUID groupId = UUID.randomUUID();

        var exception = assertThrows(GroupNotFoundException.class, () -> shopperGroupService1.deleteShopperGroup(user1.getId(), groupId));
        assertEquals("Shopper group does not exist - " + groupId, exception.getMessage());
    }

    @Test
    void deleteShopperGroup_Member_RemovesShopperFromShopperGroup() {
        User admin = new User();
        ShopperGroup shopperGroup1 = spy(new ShopperGroup("Group1", admin));

        ArgumentCaptor<User> removedShopper = ArgumentCaptor.forClass(User.class);
        User user1 = new User();
        shopperGroup1.addUser(user1);
        when(shopperGroupRepository.findByUserIdAndId(user1.getId(), shopperGroup1.getId())).thenReturn(Optional.of(shopperGroup1));
        when(userService.userExistsAsAdminByGroup(user1.getId(), shopperGroup1.getId())).thenReturn(false);
        when(userService.getUserById(user1.getId())).thenReturn(user1);

        shopperGroupService1.deleteShopperGroup(user1.getId(), shopperGroup1.getId());
        verify(shopperGroup1).removeUser(removedShopper.capture());
        assertEquals(user1.getId(), removedShopper.getValue().getId());
    }

    @Test
    void deleteShopperGroup_Admin_DeletesShopperGroup() {
        User admin = new User();
        ShopperGroup shopperGroup1 = new ShopperGroup("Group1", admin);
        ArgumentCaptor<ShopperGroup> deletedGroup = ArgumentCaptor.forClass(ShopperGroup.class);
        when(shopperGroupRepository.findByUserIdAndId(admin.getId(), shopperGroup1.getId())).thenReturn(Optional.of(shopperGroup1));
        when(userService.userExistsAsAdminByGroup(admin.getId(), shopperGroup1.getId())).thenReturn(true);

        shopperGroupService1.deleteShopperGroup(admin.getId(), shopperGroup1.getId());
        verify(shopperGroupRepository).delete(deletedGroup.capture());
        assertEquals(shopperGroup1, deletedGroup.getValue());
    }


    @Test
    void changeShopperGroupName_InvalidId_ThrowsInvalidUserPermissionsException() {
        UUID shopperId = UUID.randomUUID();
        UUID groupId = UUID.randomUUID();

        var exception = assertThrows(GroupNotFoundException.class, () -> shopperGroupService1.changeShopperGroupName(shopperId, groupId, "New Name"));
        assertEquals("Shopper group does not exist - " + groupId, exception.getMessage());
    }

    @Test
    void changeShopperGroupName_NotAdmin_ThrowsInvalidUserPermissionsException() {
        User admin = new User();
        ShopperGroup shopperGroup1 = spy(new ShopperGroup("Group1", admin));
        User user1 = new User();
        shopperGroup1.addUser(user1);
        when(shopperGroupRepository.findById(shopperGroup1.getId())).thenReturn(Optional.of(shopperGroup1));
        when(userService.userExistsAsAdminByGroup(user1.getId(), shopperGroup1.getId())).thenReturn(false);

        var exception = assertThrows(InvalidUserPermissionsException.class, () -> shopperGroupService1.changeShopperGroupName(user1.getId(), shopperGroup1.getId(), "New Name"));
        assertEquals("User - " + user1.getId() + " does not have permission to modify group", exception.getMessage());
    }

    @Test
    void changeShopperGroupName_Admin_ChangesGroupName() {
        User admin = new User();
        ShopperGroup shopperGroup1 = spy(new ShopperGroup("Group1", admin));
        ArgumentCaptor<String> newName = ArgumentCaptor.forClass(String.class);

        when(shopperGroupRepository.findById(shopperGroup1.getId())).thenReturn(Optional.of(shopperGroup1));
        when(userService.userExistsAsAdminByGroup(admin.getId(), shopperGroup1.getId())).thenReturn(true);

        shopperGroupService1.changeShopperGroupName(admin.getId(), shopperGroup1.getId(), "New Name");
        verify(shopperGroup1).setName(newName.capture());
        assertEquals("New Name", shopperGroup1.getName());
        assertEquals("New Name", newName.getValue());
    }

    @Test
    void addShopperToShopperGroup_InvalidId_ThrowsGroupNotFoundException() {
        UUID shopperId = UUID.randomUUID();
        UUID groupId = UUID.randomUUID();

        var exception = assertThrows(GroupNotFoundException.class, () -> shopperGroupService1.addUserToShopperGroup(groupId, shopperId));
        assertEquals("Shopper group does not exist - " + groupId, exception.getMessage());
    }

    @Test
    void addShopperToShopperGroup_AddsShopperToGroup() {
        User admin = new User();
        ShopperGroup shopperGroup1 = spy(new ShopperGroup("Group1", admin));
        User user = new User();
        ArgumentCaptor<User> addedShopper = ArgumentCaptor.forClass(User.class);
        when(shopperGroupRepository.findById(shopperGroup1.getId())).thenReturn(Optional.of(shopperGroup1));
        when(userService.getUserById(user.getId())).thenReturn(user);

        shopperGroupService1.addUserToShopperGroup(shopperGroup1.getId(), user.getId());

        verify(shopperGroup1).addUser(addedShopper.capture());
        assertEquals(user, addedShopper.getValue());
    }

    @Test
    void removeShopperFromShopperGroup_RemoveAdmin_ThrowsRemoveGroupAdminException() {
        User admin = new User();
        ShopperGroup shopperGroup1 = spy(new ShopperGroup("Group1", admin));
        when(userService.userExistsAsAdminByGroup(admin.getId(), shopperGroup1.getId())).thenReturn(true);

        var exception = assertThrows(RemoveGroupAdminException.class, () -> shopperGroupService1.removeUserFromShopperGroup(admin.getId(), shopperGroup1.getId(), admin.getId()));
        assertEquals("Cannot remove group admin.", exception.getMessage());
    }

    @Test
    void removeShopperFromShopperGroup_RemoveSelf_InvalidId_ThrowsGroupNotFoundException() {
        UUID shopperId = UUID.randomUUID();
        UUID groupId = UUID.randomUUID();
        when(userService.userExistsAsAdminByGroup(shopperId, groupId)).thenReturn(false);

        var exception = assertThrows(GroupNotFoundException.class, () -> shopperGroupService1.removeUserFromShopperGroup(shopperId, groupId, shopperId));
        assertEquals("Shopper group does not exist - " + groupId, exception.getMessage());
    }
    @Test
    void removeShopperFromShopperGroup_RemoveSelf_RemovesSelfFromGroup() {
        User admin = new User();
        User user = new User();
        ShopperGroup shopperGroup1 = spy(new ShopperGroup("Group1", admin));
        shopperGroup1.addUser(user);
        when(userService.userExistsAsAdminByGroup(user.getId(), shopperGroup1.getId())).thenReturn(false);
        when(shopperGroupRepository.findById(shopperGroup1.getId())).thenReturn(Optional.of(shopperGroup1));

        var result = shopperGroupService1.removeUserFromShopperGroup(user.getId(), shopperGroup1.getId(), user.getId());
        verify(shopperGroup1).removeUser(user.getId());
        assertTrue(result);
    }

    @Test
    void removeShopperFromShopperGroup_RemoveOther_NotAdmin_ThrowsInvalidUserPermissionsException() {
        User admin = new User();
        User user = new User();
        UUID removeShopperId = UUID.randomUUID();
        ShopperGroup shopperGroup1 = spy(new ShopperGroup("Group1", admin));
        shopperGroup1.addUser(user);
        when(userService.userExistsAsAdminByGroup(removeShopperId, shopperGroup1.getId())).thenReturn(false);
        when(shopperGroupRepository.findById(shopperGroup1.getId())).thenReturn(Optional.of(shopperGroup1));

        var exception = assertThrows(InvalidUserPermissionsException.class, () -> shopperGroupService1.removeUserFromShopperGroup(user.getId(), shopperGroup1.getId(), removeShopperId));
        assertEquals("User - " + user.getId() + " does not have permission to remove from group", exception.getMessage());
    }

    @Test
    void removeShopperFromShopperGroup_RemoveOther_Admin_RemovesOtherFromGroup() {
        User admin = new User();
        User user = new User();
        User removeUser = new User();
        ShopperGroup shopperGroup1 = spy(new ShopperGroup("Group1", admin));
        shopperGroup1.addUser(user);
        shopperGroup1.addUser(removeUser);
        when(userService.userExistsAsAdminByGroup(removeUser.getId(), shopperGroup1.getId())).thenReturn(false);
        when(shopperGroupRepository.findById(shopperGroup1.getId())).thenReturn(Optional.of(shopperGroup1));

        var result = shopperGroupService1.removeUserFromShopperGroup(admin.getId(), shopperGroup1.getId(), removeUser.getId());
        assertTrue(result);
    }
}