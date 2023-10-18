package com.sp5blue.shopshare.services.shoppergroup;

import com.sp5blue.shopshare.exceptions.shoppergroup.GroupNotFoundException;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShopperGroupServiceTest {

    @Mock
    ShopperGroupRepository mockShopperGroupRepo;

    @Mock
    ShopperService mockShopperService;

    @InjectMocks
    ShopperGroupService shopperGroupService;


    @Test
    void create_CreatesGroup() {
        ArgumentCaptor<ShopperGroup> addedShopperGroup = ArgumentCaptor.forClass(ShopperGroup.class);
        Shopper creator = new Shopper();
        ShopperGroup shopperGroup = new ShopperGroup("group1", creator);

        shopperGroupService.createShopperGroup(shopperGroup);

        verify(mockShopperGroupRepo).save(addedShopperGroup.capture());
        assertEquals(shopperGroup, addedShopperGroup.getValue());
    }

    @Test
    void readById_InvalidId_ThrowsGroupNotFoundException() {
        UUID groupId = UUID.randomUUID();
        when(mockShopperGroupRepo.findById(groupId)).thenReturn(Optional.empty());

        var exception = assertThrows(GroupNotFoundException.class, () -> shopperGroupService.getShopperGroupById(groupId));

        assertEquals("Shopper group does not exist - " + groupId, exception.getMessage());
    }

    @Test
    void readById_Valid_ReturnsGroupId() {
        UUID groupId = UUID.randomUUID();
        Shopper creator = new Shopper();
        ShopperGroup shopperGroup = new ShopperGroup("group1", creator);
        when(mockShopperGroupRepo.findById(groupId)).thenReturn(Optional.of(shopperGroup));

        var result = shopperGroupService.getShopperGroupById(groupId);

        assertEquals(shopperGroup, result);
    }

    @Test
    void readByName_NoShopperGroups_ReturnsEmptyList() {
        String shopperGroupName = "group1";
        var result = shopperGroupService.getShopperGroupsByName(shopperGroupName);
        assertEquals(0, result.size());
    }
    @Test
    void readByName_OneShopperGroup_ReturnsShopperGroup() {
        String shopperGroupName = "group1";
        Shopper creator = new Shopper();
        ShopperGroup shopperGroup = new ShopperGroup(shopperGroupName, creator);
        when(mockShopperGroupRepo.findAllByName(shopperGroupName)).thenReturn(List.of(shopperGroup));

        var result = shopperGroupService.getShopperGroupsByName("group1");
        assertEquals(1, result.size());
        assertEquals(shopperGroup, result.get(0));
    }

    @Test
    void readByName_MultipleShopperGroupS_ReturnsShopperGroups() {
        String shopperGroupName1 = "group1";
        Shopper creator1 = new Shopper();
        ShopperGroup shopperGroup1 = new ShopperGroup(shopperGroupName1, creator1);

        String shopperGroupName2 = "group1";
        Shopper creator2 = new Shopper();
        ShopperGroup shopperGroup2 = new ShopperGroup(shopperGroupName2, creator2);

        when(mockShopperGroupRepo.findAllByName("group1")).thenReturn(Arrays.asList(shopperGroup1, shopperGroup2));

        var results = shopperGroupService.getShopperGroupsByName("group1");
        assertEquals(2, results.size());
        assertAll(
                () -> assertEquals(shopperGroup1, results.get(0)),
                () -> assertEquals(shopperGroup2, results.get(1))
        );
    }

    @Test
    void readByShopperId() {
        String shopperGroupName1 = "group1";
        Shopper creator1 = new Shopper();
        UUID creatorId = creator1.getId();
        ShopperGroup shopperGroup1 = new ShopperGroup(shopperGroupName1, creator1);

        String shopperGroupName2 = "group2";
        ShopperGroup shopperGroup2 = new ShopperGroup(shopperGroupName2, creator1);

        when(mockShopperGroupRepo.findAllByAdmin_Id(creatorId)).thenReturn(Arrays.asList(shopperGroup1, shopperGroup2));

//        var results = shopperGroupService.readShopperGroupsByShopperId(creatorId);
//        assertEquals(2, results.size());
//        assertAll(
//                () -> assertEquals(shopperGroup1, results.get(0)),
//                () -> assertEquals(shopperGroup2, results.get(1))
//        );
    }

    @Test
    void read() {
        String shopperGroupName1 = "group1";
        Shopper creator1 = new Shopper();
        ShopperGroup shopperGroup1 = new ShopperGroup(shopperGroupName1, creator1);

        String shopperGroupName2 = "group1";
        Shopper creator2 = new Shopper();
        ShopperGroup shopperGroup2 = new ShopperGroup(shopperGroupName2, creator2);

        String shopperGroupName3 = "group2";
        Shopper creator3 = new Shopper();
        ShopperGroup shopperGroup3 = new ShopperGroup(shopperGroupName3, creator3);

        when(mockShopperGroupRepo.findAll()).thenReturn(Arrays.asList(shopperGroup1, shopperGroup2, shopperGroup3));

        var results = shopperGroupService.getShopperGroups();
        assertEquals(3, results.size());
        assertAll(
                () -> assertEquals(shopperGroup1, results.get(0)),
                () -> assertEquals(shopperGroup2, results.get(1)),
                () -> assertEquals(shopperGroup3, results.get(2))
        );
    }

    @Test
    void addShopperToGroup_InvalidId_ThrowsGroupNotFoundException() {
        UUID shopperGroupId = UUID.randomUUID();
        UUID shopperId = UUID.randomUUID();

        var exception =assertThrows(GroupNotFoundException.class, () -> shopperGroupService.addShopperToShopperGroup(shopperGroupId, shopperId));
        assertEquals("Shopper group does not exist - " + shopperGroupId, exception.getMessage());
    }

    @Test
    void addShopperToGroup_Valid_AddsShopperToGroup() {
        Shopper shopper1 = new Shopper();
        Shopper shopper2 = new Shopper();
        ShopperGroup shopperGroup = new ShopperGroup("group 1", shopper1);
        when(mockShopperGroupRepo.findById(shopperGroup.getId())).thenReturn(Optional.of(shopperGroup));
        when(mockShopperService.readShopperById(shopper2.getId())).thenReturn(shopper2);

        var result = shopperGroupService.addShopperToShopperGroup(shopperGroup.getId(), shopper2.getId());

        assertTrue(result);
        assertEquals(2, shopperGroup.getShoppers().size());
        assertEquals(shopper2, shopperGroup.getShoppers().get(1));
    }
    @Test
    void removeShopperFromGroup_InvalidId_ThrowsGroupNotFoundException() {
        UUID shopperGroupId = UUID.randomUUID();
        UUID shopperId = UUID.randomUUID();

        var exception = assertThrows(GroupNotFoundException.class, () -> shopperGroupService.removeShopperFromShopperGroup(shopperGroupId, shopperId));
        assertEquals("Shopper group does not exist - " + shopperGroupId, exception.getMessage());
    }

    @Test
    void removeShopperFromGroup_Valid_RemovesShopperFromGroup() {
        Shopper shopper1 = new Shopper();
        Shopper shopper2 = new Shopper();
        ShopperGroup shopperGroup = new ShopperGroup("group 1", shopper1);
        shopperGroup.addShopper(shopper2);
        when(mockShopperGroupRepo.findById(shopperGroup.getId())).thenReturn(Optional.of(shopperGroup));

        var result = shopperGroupService.removeShopperFromShopperGroup(shopperGroup.getId(), shopper2.getId());

        assertTrue(result);
        assertEquals(1, shopperGroup.getShoppers().size());
        assertEquals(shopper1, shopperGroup.getShoppers().get(0));
    }
}