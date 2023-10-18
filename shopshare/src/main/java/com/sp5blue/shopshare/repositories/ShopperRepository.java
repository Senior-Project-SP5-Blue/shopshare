package com.sp5blue.shopshare.repositories;

import com.sp5blue.shopshare.models.shopper.Shopper;
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
public interface ShopperRepository extends JpaRepository<Shopper, UUID> {

    Page<Shopper> findAllByFirstName(String firstName, Pageable pageable);
    List<Shopper> findAllByFirstName(String firstName);

    Optional<Shopper> findByUsername(String username);

    Optional<Shopper> findByEmail(String email);

    @Query(nativeQuery = true, value = """
SELECT s.* FROM shoppers s JOIN shoppers_shopper_groups ssg on s.id = ssg.shopper_id WHERE ssg.shopper_group_id = :group_id
""")
    List<Shopper> findByShopperGroup(@Param("group_id") UUID groupId);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    @Query(nativeQuery = true, value = """
SELECT EXISTS (SELECT * FROM shoppers_shopper_groups ssg WHERE ssg.shopper_group_id = :group_id AND ssg.shopper_id =:shopper_id )
""")
    boolean existsByGroup(@Param("shopper_id") UUID shopperId, @Param("group_id") UUID groupId);

    @Query(nativeQuery = true, value = """
SELECT EXISTS (SELECT * FROM shopper_groups sg JOIN shoppers_shopper_groups ssg ON sg.id = ssg.shopper_group_id WHERE ssg.shopper_group_id =:group_id AND sg.admin =:shopper_id)""")
    boolean existsAsAdminByGroup(@Param("shopper_id") UUID shopperId, @Param("group_id") UUID groupId);
}
