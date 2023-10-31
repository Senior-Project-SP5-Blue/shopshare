package com.sp5blue.shopshare.services.shoppergroup;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;

//@SpringBootTest
//@AutoConfigureTestDatabase
//@TestPropertySource(locations = "classpath:application-test.properties")
@ExtendWith(MockitoExtension.class)
class InvitationServiceTest {

    @Mock
    EntityManager entityManager = mock(EntityManager.class, RETURNS_DEEP_STUBS);

    @InjectMocks
    InvitationService invitationService;


    @Test
    void invite_AlreadyInvited_ReturnTrue() {
    }

    @Test
    void invite_NotAlreadyInvited_InvitesUser_ReturnTrue() {
    }

    @Test
    void acceptInvite_NotInvited_ReturnsFalse() {
    }

    @Test
    void acceptInvite_ValidInvite_AddsUserToGroup_ReturnsTrue() {

    }


}