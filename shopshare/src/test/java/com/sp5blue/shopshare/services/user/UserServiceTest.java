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
import java.util.concurrent.ExecutionException;

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
        when(mockShopperRepo.existsByEmailIgnoreCase("tom@email.com")).thenReturn(true);
        User user = new User("Tom", "Banks", "tomUserName", "tom@email.com", "tomPass");

        var exception = assertThrows(UserAlreadyExistsException.class, () -> userService.createOrSaveUser(user));

        assertEquals("Shopper with email already exists - tom@email.com", exception.getMessage());
    }


    @Test
    void create_DuplicateUsername_ThrowsUserAlreadyExistsException() {
        when(mockShopperRepo.existsByUsernameIgnoreCase("tomUserName")).thenReturn(true);
        User user = new User("Tom", "Banks", "tomUserName", "tom@email.com", "tomPass");

        var exception = assertThrows(UserAlreadyExistsException.class, () -> userService.createOrSaveUser(user));

        assertEquals("Shopper with username already exists - tomUserName", exception.getMessage());
    }

    @Test
    void create_Valid_Success() throws ExecutionException, InterruptedException {
        User user = new User("Tom", "Banks", "tomUserName", "tom@email.com", "tomPass");
        when(mockShopperRepo.save(user)).thenReturn(user);

        var _result = userService.createOrSaveUser(user);
        var result = _result.get();

        verify(mockShopperRepo).existsByEmailIgnoreCase("tom@email.com");
        verify(mockShopperRepo).existsByUsernameIgnoreCase("tomUserName");
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
        when(mockShopperRepo.findByEmailIgnoreCase("tom@email.com")).thenReturn(Optional.of(user));

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
    void readById_Valid_ReturnsShopper() throws ExecutionException, InterruptedException {
        User user = new User("Tom", "Banks", "tomUserName", "tom@email.com", "tomPass");
        UUID shopperId = user.getId();
        when(mockShopperRepo.findById(shopperId)).thenReturn(Optional.of(user));

        var _result = userService.getUserById(shopperId);
        var result = _result.get();

        assertEquals(user, result);
    }

    @Test
    void readByEmail_InvalidEmail_ThrowsUserNotFoundException() {
        String shopperEmail = "tom@email.com";

        var exception = assertThrows(UserNotFoundException.class, () -> userService.getUserByEmail(shopperEmail));

        assertEquals("User does not exist - tom@email.com", exception.getMessage());
    }

    @Test
    void readByEmail_Valid_ReturnsShopper() throws ExecutionException, InterruptedException {
        User user = new User("Tom", "Banks", "tomUserName", "tom@email.com", "tomPass");
        when(mockShopperRepo.findByEmailIgnoreCase("tom@email.com")).thenReturn(Optional.of(user));

        var _result = userService.getUserByEmail("tom@email.com");
        var result = _result.get();

        assertEquals(user, result);
    }

    @Test
    void read_NoShoppers_ReturnsEmptyList() throws ExecutionException, InterruptedException {
        var _result = userService.getUsers();
        var result = _result.get();
        assertEquals(0, result.size());
    }

    @Test
    void read_OneShopper_ReturnsShopper() throws ExecutionException, InterruptedException {
        User user = new User("Tom", "Banks", "tomUserName", "tom@email.com", "tomPass");
        when(mockShopperRepo.findAll()).thenReturn(List.of(user));

        var _results = userService.getUsers();
        var results = _results.get();

        assertEquals(1, results.size());
        assertEquals(user, results.get(0));
    }
    @Test
    void read_MultipleShoppers_ReturnsShoppers() throws ExecutionException, InterruptedException {
        User user = new User("Tom", "Banks", "tomUserName", "tom@email.com", "tomPass");
        User user1 = new User("Betty", "Crocker", "bettyC", "betty@email.com", "bettyPass");
        User user2 = new User("Steve", "Henry", "steveH", "steve@email.com", "steveass");
        when(mockShopperRepo.findAll()).thenReturn(Arrays.asList(user, user1, user2));

        var _results = userService.getUsers();
        var results = _results.get();

        assertEquals(3, results.size());
        assertAll(
                () -> assertEquals(user, results.get(0)),
                () -> assertEquals(user1, results.get(1)),
                () -> assertEquals(user2, results.get(2))
        );
    }

    @Test
    void shopperExists_InvalidId_ReturnsFalse() throws ExecutionException, InterruptedException {
        UUID shopperId = UUID.randomUUID();
        when(mockShopperRepo.existsById(shopperId)).thenReturn(false);

        var _result = userService.userExists(shopperId);
        var result = _result.get();

        assertFalse(result);
    }
    @Test
    void shopperExists_Valid_ReturnsTrue() throws ExecutionException, InterruptedException {
        UUID shopperId = UUID.randomUUID();
        when(mockShopperRepo.existsById(shopperId)).thenReturn(true);

        var _result = userService.userExists(shopperId);
        var result = _result.get();

        assertTrue(result);
    }
}