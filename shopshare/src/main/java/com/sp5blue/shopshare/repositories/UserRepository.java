package com.sp5blue.shopshare.repositories;

import com.sp5blue.shopshare.models.user.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

public interface UserRepository extends JpaRepository<User, UUID> {
  @NonNull
  @Override
  @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.id = :user_id")
  Optional<User> findById(@NonNull @Param("user_id") UUID uuid);

  Page<User> findAllByFirstName(String firstName, Pageable pageable);

  List<User> findAllByFirstName(String firstName);

  @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE lower(u.username) = lower(:user_username)")
  Optional<User> findByUsernameIgnoreCase(@Param("user_username") String username);

  @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE lower(u.email) = lower(:user_email)")
  Optional<User> findByEmailIgnoreCase(@Param("user_email") String email);

  @Query(
      "SELECT DISTINCT u FROM ShopperGroup shopperGroup JOIN shopperGroup.users u JOIN FETCH u.roles WHERE shopperGroup.id = :group_id ORDER BY u.username")
  List<User> findAllByShopperGroup(@Param("group_id") UUID groupId);

  @Query(
      nativeQuery = true,
      value =
          """
    SELECT u.* FROM users u JOIN users_shopper_groups usg on u.id = usg.user_id WHERE usg.shopper_group_id = :group_id AND u.id = :user_id
    """)
  Optional<User> findByShopperGroup(@Param("group_id") UUID groupId, @Param("user_id") UUID userId);

  boolean existsByEmailIgnoreCase(String email);

  boolean existsByUsernameIgnoreCase(String username);

  @Query(
      nativeQuery = true,
      value =
          """
    SELECT EXISTS (SELECT * FROM users_shopper_groups usg WHERE usg.shopper_group_id = :group_id AND usg.user_id =:user_id)
    """)
  boolean existsByGroup(@Param("user_id") UUID userId, @Param("group_id") UUID groupId);

  @Query(
      nativeQuery = true,
      value =
          """
    SELECT EXISTS (SELECT * FROM shopper_groups sg JOIN users_shopper_groups usg ON sg.id = usg.shopper_group_id WHERE usg.shopper_group_id =:group_id AND sg.admin_id =:user_id)""")
  boolean existsAsAdminByGroup(@Param("user_id") UUID userId, @Param("group_id") UUID groupId);
}
