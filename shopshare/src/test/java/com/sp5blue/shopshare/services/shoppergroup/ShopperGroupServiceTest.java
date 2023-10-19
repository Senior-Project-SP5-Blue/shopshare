package com.sp5blue.shopshare.services.shoppergroup;

import com.sp5blue.shopshare.exceptions.shoppergroup.GroupNotFoundException;
import com.sp5blue.shopshare.exceptions.shoppergroup.InvalidUserPermissionsException;
import com.sp5blue.shopshare.exceptions.shoppergroup.RemoveGroupAdminException;
import com.sp5blue.shopshare.models.shopper.Shopper;
import com.sp5blue.shopshare.models.shoppergroup.ShopperGroup;
import com.sp5blue.shopshare.repositories.ShopperGroupRepository;
import com.sp5blue.shopshare.services.shopper.ShopperService;
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
class ShopperGroupServiceTest {

    @Mock
    ShopperService shopperService1;

    @Mock
    ShopperGroupRepository shopperGroupRepository;

    @InjectMocks
    ShopperGroupService shopperGroupService1;

    @Test
    void createShopperGroup_CreatesShopperGroup() {
        Shopper shopper = new Shopper();
        ArgumentCaptor<ShopperGroup> groupCaptor = ArgumentCaptor.forClass(ShopperGroup.class);
        when(shopperService1.getShopperById(shopper.getId())).thenReturn(shopper);
        when(shopperGroupRepository.save(any(ShopperGroup.class))).thenAnswer(i -> i.getArguments()[0]);

        var result = shopperGroupService1.createShopperGroup(shopper.getId(), "Group 1");

        verify(shopperGroupRepository).save(groupCaptor.capture());
        assertEquals(groupCaptor.getValue(), result);
        assertTrue(shopper.getRoles().stream().anyMatch(g -> g.getAuthority().equals("ROLE_GROUP_ADMIN-" + groupCaptor.getValue().getId())));
    }

    @Test
    void getShopperGroups_NoMatches_ReturnsEmptyList() {
        UUID shopperId = UUID.randomUUID();
        var results = shopperGroupService1.getShopperGroups(shopperId);
        assertTrue(results.isEmpty());
    }

    @Test
    void getShopperGroups_Matches_ReturnsMatches() {
        Shopper admin = new Shopper();
        ShopperGroup shopperGroup1 = new ShopperGroup("Group1", admin);
        ShopperGroup shopperGroup2 = new ShopperGroup("Group2", admin);
        Shopper shopper1 = new Shopper();
        shopperGroup1.addShopper(shopper1);
        shopperGroup2.addShopper(shopper1);

        when(shopperGroupRepository.findAllByShopperId(shopper1.getId())).thenReturn(Arrays.asList(shopperGroup1, shopperGroup2));

        var results = shopperGroupService1.getShopperGroups(shopper1.getId());
        assertEquals(2, results.size());
        assertAll(
                () -> assertEquals(shopperGroup1, results.get(0)),
                () -> assertEquals(shopperGroup2, results.get(1))
        );
    }

    @Test
    void getShopperGroupById_NotMember_ThrowsGroupNotFoundException() {
        Shopper admin = new Shopper();
        ShopperGroup shopperGroup1 = new ShopperGroup("Group1", admin);
        Shopper shopper1 = new Shopper();

        var exception = assertThrows(GroupNotFoundException.class, () -> shopperGroupService1.getShopperGroupById(shopper1.getId(), shopperGroup1.getId()));
        assertEquals("Shopper group does not exist - " + shopperGroup1.getId(), exception.getMessage());
    }

    @Test
    void getShopperGroupById_Member_ReturnsShopperGroup() {
        Shopper admin = new Shopper();
        ShopperGroup shopperGroup1 = new ShopperGroup("Group1", admin);
        Shopper shopper1 = new Shopper();
        shopperGroup1.addShopper(shopper1);
        when(shopperGroupRepository.findByShopperIdAndId(shopper1.getId(), shopperGroup1.getId())).thenReturn(Optional.of(shopperGroup1));

        var result = shopperGroupService1.getShopperGroupById(shopper1.getId(), shopperGroup1.getId());
        assertEquals(shopperGroup1, result);
    }

    @Test
    void deleteShopperGroup_NotMember_ThrowsGroupNotFoundException() {
        Shopper shopper1 = new Shopper();
        UUID groupId = UUID.randomUUID();

        var exception = assertThrows(GroupNotFoundException.class, () -> shopperGroupService1.deleteShopperGroup(shopper1.getId(), groupId));
        assertEquals("Shopper group does not exist - " + groupId, exception.getMessage());
    }

    @Test
    void deleteShopperGroup_Member_RemovesShopperFromShopperGroup() {
        Shopper admin = new Shopper();
        ShopperGroup shopperGroup1 = spy(new ShopperGroup("Group1", admin));

        ArgumentCaptor<Shopper> removedShopper = ArgumentCaptor.forClass(Shopper.class);
        Shopper shopper1 = new Shopper();
        shopperGroup1.addShopper(shopper1);
        when(shopperGroupRepository.findByShopperIdAndId(shopper1.getId(), shopperGroup1.getId())).thenReturn(Optional.of(shopperGroup1));
        when(shopperService1.shopperExistsAsAdminByGroup(shopper1.getId(), shopperGroup1.getId())).thenReturn(false);
        when(shopperService1.getShopperById(shopper1.getId())).thenReturn(shopper1);

        shopperGroupService1.deleteShopperGroup(shopper1.getId(), shopperGroup1.getId());
        verify(shopperGroup1).removeShopper(removedShopper.capture());
        assertEquals(shopper1.getId(), removedShopper.getValue().getId());
    }

    @Test
    void deleteShopperGroup_Admin_DeletesShopperGroup() {
        Shopper admin = new Shopper();
        ShopperGroup shopperGroup1 = new ShopperGroup("Group1", admin);
        ArgumentCaptor<ShopperGroup> deletedGroup = ArgumentCaptor.forClass(ShopperGroup.class);
        when(shopperGroupRepository.findByShopperIdAndId(admin.getId(), shopperGroup1.getId())).thenReturn(Optional.of(shopperGroup1));
        when(shopperService1.shopperExistsAsAdminByGroup(admin.getId(), shopperGroup1.getId())).thenReturn(true);

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
        Shopper admin = new Shopper();
        ShopperGroup shopperGroup1 = spy(new ShopperGroup("Group1", admin));
        Shopper shopper1 = new Shopper();
        shopperGroup1.addShopper(shopper1);
        when(shopperGroupRepository.findById(shopperGroup1.getId())).thenReturn(Optional.of(shopperGroup1));
        when(shopperService1.shopperExistsAsAdminByGroup(shopper1.getId(), shopperGroup1.getId())).thenReturn(false);

        var exception = assertThrows(InvalidUserPermissionsException.class, () -> shopperGroupService1.changeShopperGroupName(shopper1.getId(), shopperGroup1.getId(), "New Name"));
        assertEquals("User - " + shopper1.getId() + " does not have permission to modify group", exception.getMessage());
    }

    @Test
    void changeShopperGroupName_Admin_ChangesGroupName() {
        Shopper admin = new Shopper();
        ShopperGroup shopperGroup1 = spy(new ShopperGroup("Group1", admin));
        ArgumentCaptor<String> newName = ArgumentCaptor.forClass(String.class);

        when(shopperGroupRepository.findById(shopperGroup1.getId())).thenReturn(Optional.of(shopperGroup1));
        when(shopperService1.shopperExistsAsAdminByGroup(admin.getId(), shopperGroup1.getId())).thenReturn(true);

        shopperGroupService1.changeShopperGroupName(admin.getId(), shopperGroup1.getId(), "New Name");
        verify(shopperGroup1).setName(newName.capture());
        assertEquals("New Name", shopperGroup1.getName());
        assertEquals("New Name", newName.getValue());
    }

    @Test
    void addShopperToShopperGroup_InvalidId_ThrowsGroupNotFoundException() {
        UUID shopperId = UUID.randomUUID();
        UUID groupId = UUID.randomUUID();

        var exception = assertThrows(GroupNotFoundException.class, () -> shopperGroupService1.addShopperToShopperGroup(groupId, shopperId));
        assertEquals("Shopper group does not exist - " + groupId, exception.getMessage());
    }

    @Test
    void addShopperToShopperGroup_AddsShopperToGroup() {
        Shopper admin = new Shopper();
        ShopperGroup shopperGroup1 = spy(new ShopperGroup("Group1", admin));
        Shopper shopper = new Shopper();
        ArgumentCaptor<Shopper> addedShopper = ArgumentCaptor.forClass(Shopper.class);
        when(shopperGroupRepository.findById(shopperGroup1.getId())).thenReturn(Optional.of(shopperGroup1));
        when(shopperService1.getShopperById(shopper.getId())).thenReturn(shopper);

        shopperGroupService1.addShopperToShopperGroup(shopperGroup1.getId(), shopper.getId());

        verify(shopperGroup1).addShopper(addedShopper.capture());
        assertEquals(shopper, addedShopper.getValue());
    }

    @Test
    void removeShopperFromShopperGroup_RemoveAdmin_ThrowsRemoveGroupAdminException() {
        Shopper admin = new Shopper();
        ShopperGroup shopperGroup1 = spy(new ShopperGroup("Group1", admin));
        when(shopperService1.shopperExistsAsAdminByGroup(admin.getId(), shopperGroup1.getId())).thenReturn(true);

        var exception = assertThrows(RemoveGroupAdminException.class, () -> shopperGroupService1.removeShopperFromShopperGroup(admin.getId(), shopperGroup1.getId(), admin.getId()));
        assertEquals("Cannot remove group admin.", exception.getMessage());
    }

    @Test
    void removeShopperFromShopperGroup_RemoveSelf_InvalidId_ThrowsGroupNotFoundException() {
        UUID shopperId = UUID.randomUUID();
        UUID groupId = UUID.randomUUID();
        when(shopperService1.shopperExistsAsAdminByGroup(shopperId, groupId)).thenReturn(false);

        var exception = assertThrows(GroupNotFoundException.class, () -> shopperGroupService1.removeShopperFromShopperGroup(shopperId, groupId, shopperId));
        assertEquals("Shopper group does not exist - " + groupId, exception.getMessage());
    }
    @Test
    void removeShopperFromShopperGroup_RemoveSelf_RemovesSelfFromGroup() {
        Shopper admin = new Shopper();
        Shopper shopper = new Shopper();
        ShopperGroup shopperGroup1 = spy(new ShopperGroup("Group1", admin));
        shopperGroup1.addShopper(shopper);
        when(shopperService1.shopperExistsAsAdminByGroup(shopper.getId(), shopperGroup1.getId())).thenReturn(false);
        when(shopperGroupRepository.findById(shopperGroup1.getId())).thenReturn(Optional.of(shopperGroup1));

        var result = shopperGroupService1.removeShopperFromShopperGroup(shopper.getId(), shopperGroup1.getId(), shopper.getId());
        verify(shopperGroup1).removeShopper(shopper.getId());
        assertTrue(result);
    }

    @Test
    void removeShopperFromShopperGroup_RemoveOther_NotAdmin_ThrowsInvalidUserPermissionsException() {
        Shopper admin = new Shopper();
        Shopper shopper = new Shopper();
        UUID removeShopperId = UUID.randomUUID();
        ShopperGroup shopperGroup1 = spy(new ShopperGroup("Group1", admin));
        shopperGroup1.addShopper(shopper);
        when(shopperService1.shopperExistsAsAdminByGroup(removeShopperId, shopperGroup1.getId())).thenReturn(false);
        when(shopperGroupRepository.findById(shopperGroup1.getId())).thenReturn(Optional.of(shopperGroup1));

        var exception = assertThrows(InvalidUserPermissionsException.class, () -> shopperGroupService1.removeShopperFromShopperGroup(shopper.getId(), shopperGroup1.getId(), removeShopperId));
        assertEquals("User - " + shopper.getId() + " does not have permission to remove from group", exception.getMessage());
    }

    @Test
    void removeShopperFromShopperGroup_RemoveOther_Admin_RemovesOtherFromGroup() {
        Shopper admin = new Shopper();
        Shopper shopper = new Shopper();
        Shopper removeShopper = new Shopper();
        ShopperGroup shopperGroup1 = spy(new ShopperGroup("Group1", admin));
        shopperGroup1.addShopper(shopper);
        shopperGroup1.addShopper(removeShopper);
        when(shopperService1.shopperExistsAsAdminByGroup(removeShopper.getId(), shopperGroup1.getId())).thenReturn(false);
        when(shopperGroupRepository.findById(shopperGroup1.getId())).thenReturn(Optional.of(shopperGroup1));

        var result = shopperGroupService1.removeShopperFromShopperGroup(admin.getId(), shopperGroup1.getId(), removeShopper.getId());
        assertTrue(result);
    }
}