package com.sp5blue.shopshare.services.shopper;

import com.sp5blue.shopshare.exceptions.authentication.UserAlreadyExistsException;
import com.sp5blue.shopshare.exceptions.authentication.UserNotFoundException;
import com.sp5blue.shopshare.models.shopper.Shopper;
import com.sp5blue.shopshare.repositories.ShopperRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

//@SpringBootTest
//@ExtendWith(SpringExtension.class)
//@AutoConfigureTestDatabase
//@TestPropertySource(locations = "classpath:application-test.properties")
@ExtendWith(MockitoExtension.class)
class ShopperServiceTest {

    @Mock
    ShopperRepository mockShopperRepo;

    @InjectMocks
    ShopperService shopperService;


    @Test
    void create_DuplicateEmail_ThrowsUserAlreadyExistsException() {
        when(mockShopperRepo.existsByEmail("tom@email.com")).thenReturn(true);
        Shopper shopper = new Shopper("Tom", "Banks", "tomUserName", "tom@email.com", "tomPass");

        var exception = assertThrows(UserAlreadyExistsException.class, () -> shopperService.createShopper(shopper));

        assertEquals("Shopper with email already exists - tom@email.com", exception.getMessage());
    }


    @Test
    void create_DuplicateUsername_ThrowsUserAlreadyExistsException() {
        when(mockShopperRepo.existsByUsername("tomUserName")).thenReturn(true);
        Shopper shopper = new Shopper("Tom", "Banks", "tomUserName", "tom@email.com", "tomPass");

        var exception = assertThrows(UserAlreadyExistsException.class, () -> shopperService.createShopper(shopper));

        assertEquals("Shopper with username already exists - tomUserName", exception.getMessage());
    }

    @Test
    void create_Valid_Success() {
        Shopper shopper = new Shopper("Tom", "Banks", "tomUserName", "tom@email.com", "tomPass");
        when(mockShopperRepo.save(shopper)).thenReturn(shopper);

        var result = shopperService.createShopper(shopper);

        verify(mockShopperRepo).existsByEmail("tom@email.com");
        verify(mockShopperRepo).existsByUsername("tomUserName");
        verify(mockShopperRepo).save(shopper);
        assertEquals(shopper, result);
    }

    @Test
    void loadUserByUsername_InvalidUsername_ThrowsUserNotFoundException() {
        String username = "User";

        var exception = assertThrows(UserNotFoundException.class, () -> shopperService.loadUserByUsername(username));

        assertEquals("User does not exist - User", exception.getMessage());
    }

    @Test
    void loadUserByUsername_Valid_ReturnsShopper() {
        Shopper shopper = new Shopper("Tom", "Banks", "tomUserName", "tom@email.com", "tomPass");
        when(mockShopperRepo.findByEmail("tom@email.com")).thenReturn(Optional.of(shopper));

        var result = shopperService.loadUserByUsername("tom@email.com");
        assertEquals(shopper, result);
    }

    @Test
    void readById_InvalidId_ThrowsUserNotFoundException() {
        UUID shopperId = UUID.randomUUID();

        var exception = assertThrows(UserNotFoundException.class, () -> shopperService.getShopperById(shopperId));

        assertEquals("User does not exist - " + shopperId, exception.getMessage());
    }

    @Test
    void readById_Valid_ReturnsShopper() {
        Shopper shopper = new Shopper("Tom", "Banks", "tomUserName", "tom@email.com", "tomPass");
        UUID shopperId = shopper.getId();
        when(mockShopperRepo.findById(shopperId)).thenReturn(Optional.of(shopper));

        var result = shopperService.getShopperById(shopperId);

        assertEquals(shopper, result);
    }

    @Test
    void readByEmail_InvalidEmail_ThrowsUserNotFoundException() {
        String shopperEmail = "tom@email.com";

        var exception = assertThrows(UserNotFoundException.class, () -> shopperService.getShopperByEmail(shopperEmail));

        assertEquals("User does not exist - tom@email.com", exception.getMessage());
    }

    @Test
    void readByEmail_Valid_ReturnsShopper() {
        Shopper shopper = new Shopper("Tom", "Banks", "tomUserName", "tom@email.com", "tomPass");
        when(mockShopperRepo.findByEmail("tom@email.com")).thenReturn(Optional.of(shopper));

        var result = shopperService.getShopperByEmail("tom@email.com");

        assertEquals(shopper, result);
    }

    @Test
    void read_NoShoppers_ReturnsEmptyList() {
        var result = shopperService.getShoppers();
        assertEquals(0, result.size());
    }

    @Test
    void read_OneShopper_ReturnsShopper() {
        Shopper shopper = new Shopper("Tom", "Banks", "tomUserName", "tom@email.com", "tomPass");
        when(mockShopperRepo.findAll()).thenReturn(List.of(shopper));

        var results = shopperService.getShoppers();

        assertEquals(1, results.size());
        assertEquals(shopper, results.get(0));
    }
    @Test
    void read_MultipleShoppers_ReturnsShoppers() {
        Shopper shopper = new Shopper("Tom", "Banks", "tomUserName", "tom@email.com", "tomPass");
        Shopper shopper1 = new Shopper("Betty", "Crocker", "bettyC", "betty@email.com", "bettyPass");
        Shopper shopper2 = new Shopper("Steve", "Henry", "steveH", "steve@email.com", "steveass");
        when(mockShopperRepo.findAll()).thenReturn(Arrays.asList(shopper, shopper1, shopper2));

        var results = shopperService.getShoppers();

        assertEquals(3, results.size());
        assertAll(
                () -> assertEquals(shopper, results.get(0)),
                () -> assertEquals(shopper1, results.get(1)),
                () -> assertEquals(shopper2, results.get(2))
        );
    }

    @Test
    void shopperExists_InvalidId_ReturnsFalse() {
        UUID shopperId = UUID.randomUUID();
        when(mockShopperRepo.existsById(shopperId)).thenReturn(false);

        var result = shopperService.shopperExists(shopperId);
        assertFalse(result);
    }
    @Test
    void shopperExists_Valid_ReturnsTrue() {
        UUID shopperId = UUID.randomUUID();
        when(mockShopperRepo.existsById(shopperId)).thenReturn(true);

        var result = shopperService.shopperExists(shopperId);
        assertTrue(result);
    }
}