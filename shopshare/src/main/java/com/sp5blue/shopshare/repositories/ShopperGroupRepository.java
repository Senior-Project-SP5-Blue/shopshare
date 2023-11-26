package com.sp5blue.shopshare.repositories;

import com.sp5blue.shopshare.models.shoppergroup.ShopperGroup;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShopperGroupRepository extends JpaRepository<ShopperGroup, UUID> {
  Page<ShopperGroup> findAllByName(String name, Pageable pageable);

  List<ShopperGroup> findAllByName(String name);

  @Query(
      nativeQuery = true,
      value =
          """
    SELECT sg.* FROM shopper_groups sg
    JOIN shoppers_shopepr_groups ssg
    ON sg.id = ssg.shopper_group_id
    AND ssg.user_id = :user_id
    WHERE sg.name =:name
    """)
  Page<ShopperGroup> findAllByUserIdAndName(
      @Param("user_id") UUID userId, @Param("name") String name, Pageable pageable);

  @Query(
      nativeQuery = true,
      value =
          """
    SELECT sg.* FROM shopper_groups sg
    JOIN shoppers_shopper_groups ssg
    ON sg.id = ssg.shopper_group_id
    AND ssg.user_id = :user_id
    WHERE sg.name =:name
    """)
  List<ShopperGroup> findAllByUserIdAndName(
      @Param("user_id") UUID userId, @Param("name") String name);

  Page<ShopperGroup> findAllByAdmin_Id(UUID adminId, Pageable pageable);

  List<ShopperGroup> findAllByAdmin_Id(@Param("user_id") UUID userId);

  //    @Query("SELECT sg FROM ShopperGroup sg JOIN sg.users u JOIN FETCH sg.users WHERE u.id =
  // :user_id")
  @Query(
      nativeQuery = true,
      value =
          """
SELECT * FROM shopper_groups sg JOIN users_shopper_groups usg ON sg.id = usg.shopper_group_id WHERE usg.user_id = :user_id
""")
  List<ShopperGroup> findAllByUserId(@Param("user_id") UUID userId);

  @Query(
      nativeQuery = true,
      value =
          """
    SELECT sg.* FROM shopper_groups sg
    JOIN users_shopper_groups usg ON sg.id = usg.shopper_group_id
    JOIN users u ON u.id = usg.user_id
    WHERE u.id = :user_id
    AND sg.id = :group_id""")
  Optional<ShopperGroup> findByUserIdAndId(
      @Param("user_id") UUID userId, @Param("group_id") UUID groupId);

  @Query(
      nativeQuery = true,
      value =
          """
    SELECT COUNT(*) > 1
    FROM users_shopper_groups
    WHERE user_id = :user_id
    AND shopper_group_id = :group_id""")
  boolean existsByUser_Id(@Param("user_id") UUID userId, @Param("group_id") UUID groupId);
}
