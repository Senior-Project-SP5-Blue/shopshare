package com.sp5blue.shopshare.services.shoppergroup;

import com.sp5blue.shopshare.exceptions.shoppergroup.UserAlreadyInvitedException;
import com.sp5blue.shopshare.exceptions.shoppergroup.UserNotInvitedException;
import com.sp5blue.shopshare.repositories.ShopperGroupRepository;
import com.sp5blue.shopshare.repositories.ShopperRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@TestPropertySource(locations = "classpath:application-test.properties")
@ExtendWith(MockitoExtension.class)
class InvitationServiceTest {

    @Autowired
    InvitationService invitationService;

    @Autowired
    ShopperGroupRepository shopperGroupRepository;

    @Autowired
    ShopperRepository shopperRepository;

    @Test
    void invite_AlreadyInvited_ReturnTrue() {
        var exception = assertThrows(UserAlreadyInvitedException.class,
                () -> invitationService.invite(UUID.fromString("7b80db40-6f0c-4463-b5f0-81853acb3802"), UUID.fromString("00b5086c-9ae5-4915-a8e2-7717ddb9bb25"))
                );
        assertEquals("User - 00b5086c-9ae5-4915-a8e2-7717ddb9bb25 already has active invitation to Group - 7b80db40-6f0c-4463-b5f0-81853acb3802", exception.getMessage());
    }

    @Test
    void invite_NotAlreadyInvited_InvitesUser_ReturnTrue() {
        var result = invitationService.invite(UUID.fromString("7b80db40-6f0c-4463-b5f0-81853acb3802"), UUID.fromString("eb616167-53de-4220-b312-6822aa13adb8"));
        assertTrue(result);
    }

    @Test
    void acceptInvite_NotInvited_ReturnsFalse() {
        var exception = assertThrows(UserNotInvitedException.class, () -> invitationService.acceptInvite(
                UUID.fromString("26147cc0-9499-4ab7-9a8c-d7de7d42d073"),
                UUID.fromString("eb616167-53de-4220-b312-6822aa13adb8")));
        assertEquals("User - eb616167-53de-4220-b312-6822aa13adb8 has not been invited to Group - 26147cc0-9499-4ab7-9a8c-d7de7d42d073", exception.getMessage());
    }

    @Test
    void acceptInvite_ValidInvite_AddsUserToGroup_ReturnsTrue() {
        var result = invitationService.acceptInvite(
                UUID.fromString("7b80db40-6f0c-4463-b5f0-81853acb3802"),
                UUID.fromString("00b5086c-9ae5-4915-a8e2-7717ddb9bb25"));
        assertTrue(result);
    }


}