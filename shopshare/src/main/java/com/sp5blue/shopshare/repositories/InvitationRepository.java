package com.sp5blue.shopshare.repositories;

import com.sp5blue.shopshare.models.shoppergroup.Invitation;
import com.sp5blue.shopshare.models.shoppergroup.InvitationId;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

  @Query(
      value =
          """
   SELECT EXISTS (SELECT * FROM group_invitations gi JOIN users u ON gi.user_id = u.id
   WHERE u.username = :username AND gi.shopper_group_id = :group_id)
   """,
      nativeQuery = true)
  boolean existsByUsernameAndGroup(
      @Param("group_id") UUID groupId, @Param("username") String username);

  //  Optional<Invitation> findByUsernameAndGroup(@Param("group_id") UUID groupId,
  // @Param("username") String username);

  //    @Query("""
  //    SELECT i FROM Invitation i WHERE i.id.userId = :user_id
  //    """)
  //    boolean existByGroupIdAndUserId(UUID groupId, UUID userId);
}
