package com.sp5blue.shopshare.repositories;

import com.sp5blue.shopshare.models.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Page<User> findAllByFirstName(String firstName, Pageable pageable);
    List<User> findAllByFirstName(String firstName);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    @Query(nativeQuery = true, value = """
SELECT u.* FROM users u JOIN users_shopper_groups usg on u.id = usg.user_id WHERE usg.shopper_group_id = :group_id
""")
    List<User> findByShopperGroup(@Param("group_id") UUID groupId);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    @Query(nativeQuery = true, value = """
SELECT EXISTS (SELECT * FROM users_shopper_groups usg WHERE usg.shopper_group_id = :group_id AND usg.user_id =:user_id)
""")
    boolean existsByGroup(@Param("user_id") UUID userId, @Param("group_id") UUID groupId);

    @Query(nativeQuery = true, value = """
SELECT EXISTS (SELECT * FROM shopper_groups sg JOIN users_shopper_groups usg ON sg.id = usg.shopper_group_id WHERE usg.shopper_group_id =:group_id AND sg.admin_id =:user_id)""")
    boolean existsAsAdminByGroup(@Param("user_id") UUID userId, @Param("group_id") UUID groupId);
}
