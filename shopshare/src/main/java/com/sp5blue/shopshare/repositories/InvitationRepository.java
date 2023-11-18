package com.sp5blue.shopshare.repositories;

import com.sp5blue.shopshare.models.shoppergroup.Invitation;
import com.sp5blue.shopshare.models.shoppergroup.InvitationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface InvitationRepository extends JpaRepository<Invitation, InvitationId> {

//    @Modifying
//    @Query(" Invitation")
//    Invitation save(@Param("group_id") UUID groupId, @Param("user_id") UUID userId);

    @Query("""
    SELECT i FROM Invitation i WHERE i.id.groupId = :group_id
    """)
    List<Invitation> findAllByGroupId(@Param("group_id") UUID groupId);

    @Query("""
    SELECT i FROM Invitation i WHERE i.id.userId = :user_id
    """)
    List<Invitation> findAllByUserId(@Param("user_id") UUID userId);


//    @Query("""
//    SELECT i FROM Invitation i WHERE i.id.userId = :user_id
//    """)
//    boolean existByGroupIdAndUserId(UUID groupId, UUID userId);
}
