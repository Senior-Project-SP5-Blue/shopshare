package com.sp5blue.shopshare.repositories;


import com.sp5blue.shopshare.models.ShopperGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ShopperGroupRepository extends JpaRepository<ShopperGroup, UUID> {
    Page<ShopperGroup> findAllByName (String name, Pageable pageable);
    List<ShopperGroup> findAllByName (String name);
    @Query(nativeQuery = true, value = "SELECT * FROM shoppers_shopper_groups WHERE shopper_id = :shopper_id")

    Page<ShopperGroup> findAllByCreatedBy_Id(@Param("shopper_id") UUID shopperId, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM shoppers_shopper_groups WHERE shopper_id = :shopper_id")
    List<ShopperGroup> findAllByCreatedBy_Id(@Param("shopper_id") UUID shopperId);

    @Query(nativeQuery = true, value = "SELECT count(*) from shoppers_shopper_groups WHERE shopper_id = :shopper_id")
    long countByShopper_Id(@Param("shopper_id") UUID shopperId);

//    long countByShopperGroup_Id(UUID groupId);

    boolean existsByName(String name);
}
