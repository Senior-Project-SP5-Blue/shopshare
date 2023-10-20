package com.sp5blue.shopshare.services.user;

import com.sp5blue.shopshare.exceptions.authentication.UserAlreadyExistsException;
import com.sp5blue.shopshare.exceptions.authentication.UserNotFoundException;
import com.sp5blue.shopshare.models.user.User;
import com.sp5blue.shopshare.repositories.UserRepository;
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
class UserServiceTest {

    @Mock
    UserRepository mockShopperRepo;

    @InjectMocks
    UserService userService;


    @Test
    void create_DuplicateEmail_ThrowsUserAlreadyExistsException() {
        when(mockShopperRepo.existsByEmail("tom@email.com")).thenReturn(true);
        User user = new User("Tom", "Banks", "tomUserName", "tom@email.com", "tomPass");

        var exception = assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(user));

        assertEquals("Shopper with email already exists - tom@email.com", exception.getMessage());
    }


    @Test
    void create_DuplicateUsername_ThrowsUserAlreadyExistsException() {
        when(mockShopperRepo.existsByUsername("tomUserName")).thenReturn(true);
        User user = new User("Tom", "Banks", "tomUserName", "tom@email.com", "tomPass");

        var exception = assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(user));

        assertEquals("Shopper with username already exists - tomUserName", exception.getMessage());
    }

    @Test
    void create_Valid_Success() {
        User user = new User("Tom", "Banks", "tomUserName", "tom@email.com", "tomPass");
        when(mockShopperRepo.save(user)).thenReturn(user);

        var result = userService.createUser(user);

        verify(mockShopperRepo).existsByEmail("tom@email.com");
        verify(mockShopperRepo).existsByUsername("tomUserName");
        verify(mockShopperRepo).save(user);
        assertEquals(user, result);
    }

    @Test
    void loadUserByUsername_InvalidUsername_ThrowsUserNotFoundException() {
        String username = "User";

        var exception = assertThrows(UserNotFoundException.class, () -> userService.loadUserByUsername(username));

        assertEquals("User does not exist - User", exception.getMessage());
    }

    @Test
    void loadUserByUsername_Valid_ReturnsShopper() {
        User user = new User("Tom", "Banks", "tomUserName", "tom@email.com", "tomPass");
        when(mockShopperRepo.findByEmail("tom@email.com")).thenReturn(Optional.of(user));

        var result = userService.loadUserByUsername("tom@email.com");
        assertEquals(user, result);
    }

    @Test
    void readById_InvalidId_ThrowsUserNotFoundException() {
        UUID shopperId = UUID.randomUUID();

        var exception = assertThrows(UserNotFoundException.class, () -> userService.getUserById(shopperId));

        assertEquals("User does not exist - " + shopperId, exception.getMessage());
    }

    @Test
    void readById_Valid_ReturnsShopper() {
        User user = new User("Tom", "Banks", "tomUserName", "tom@email.com", "tomPass");
        UUID shopperId = user.getId();
        when(mockShopperRepo.findById(shopperId)).thenReturn(Optional.of(user));

        var result = userService.getUserById(shopperId);

        assertEquals(user, result);
    }

    @Test
    void readByEmail_InvalidEmail_ThrowsUserNotFoundException() {
        String shopperEmail = "tom@email.com";

        var exception = assertThrows(UserNotFoundException.class, () -> userService.getUserByEmail(shopperEmail));

        assertEquals("User does not exist - tom@email.com", exception.getMessage());
    }

    @Test
    void readByEmail_Valid_ReturnsShopper() {
        User user = new User("Tom", "Banks", "tomUserName", "tom@email.com", "tomPass");
        when(mockShopperRepo.findByEmail("tom@email.com")).thenReturn(Optional.of(user));

        var result = userService.getUserByEmail("tom@email.com");

        assertEquals(user, result);
    }

    @Test
    void read_NoShoppers_ReturnsEmptyList() {
        var result = userService.getUsers();
        assertEquals(0, result.size());
    }

    @Test
    void read_OneShopper_ReturnsShopper() {
        User user = new User("Tom", "Banks", "tomUserName", "tom@email.com", "tomPass");
        when(mockShopperRepo.findAll()).thenReturn(List.of(user));

        var results = userService.getUsers();

        assertEquals(1, results.size());
        assertEquals(user, results.get(0));
    }
    @Test
    void read_MultipleShoppers_ReturnsShoppers() {
        User user = new User("Tom", "Banks", "tomUserName", "tom@email.com", "tomPass");
        User user1 = new User("Betty", "Crocker", "bettyC", "betty@email.com", "bettyPass");
        User user2 = new User("Steve", "Henry", "steveH", "steve@email.com", "steveass");
        when(mockShopperRepo.findAll()).thenReturn(Arrays.asList(user, user1, user2));

        var results = userService.getUsers();

        assertEquals(3, results.size());
        assertAll(
                () -> assertEquals(user, results.get(0)),
                () -> assertEquals(user1, results.get(1)),
                () -> assertEquals(user2, results.get(2))
        );
    }

    @Test
    void shopperExists_InvalidId_ReturnsFalse() {
        UUID shopperId = UUID.randomUUID();
        when(mockShopperRepo.existsById(shopperId)).thenReturn(false);

        var result = userService.userExists(shopperId);
        assertFalse(result);
    }
    @Test
    void shopperExists_Valid_ReturnsTrue() {
        UUID shopperId = UUID.randomUUID();
        when(mockShopperRepo.existsById(shopperId)).thenReturn(true);

        var result = userService.userExists(shopperId);
        assertTrue(result);
    }
}