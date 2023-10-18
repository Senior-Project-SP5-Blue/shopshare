package com.sp5blue.shopshare.repositories;


import com.sp5blue.shopshare.models.shoppergroup.ShopperGroup;
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
public interface ShopperGroupRepository extends JpaRepository<ShopperGroup, UUID> {
    Page<ShopperGroup> findAllByName (String name, Pageable pageable);
    List<ShopperGroup> findAllByName (String name);

    @Query(nativeQuery = true, value = """
SELECT sg.* FROM shopper_groups sg
JOIN shoppers_shopepr_groups ssg
ON sg.id = ssg.shopper_group_id
AND ssg.shopper_id = :shopper_id
WHERE sg.name =:name
""")
    Page<ShopperGroup> findAllByShopperIdAndName(@Param("shopper_id") UUID shopperId, @Param("name") String name, Pageable pageable);

    @Query(nativeQuery = true, value = """
SELECT sg.* FROM shopper_groups sg
JOIN shoppers_shopper_groups ssg
ON sg.id = ssg.shopper_group_id
AND ssg.shopper_id = :shopper_id
WHERE sg.name =:name
""")
    List<ShopperGroup> findAllByShopperIdAndName(@Param("shopper_id") UUID shopperId, @Param("name") String name);

    Page<ShopperGroup> findAllByAdmin_Id(UUID adminId, Pageable pageable);

//    @Query(nativeQuery = true, value = """
//SELECT sg.* FROM shopper_groups sg JOIN shoppers_shopper_groups ssg ON sg.id = ssg.shopper_group_id WHERE ssg.shopper_id = :shopper_id
//""")
    List<ShopperGroup> findAllByAdmin_Id(@Param("shopper_id") UUID shopperId);

    @Query(nativeQuery = true, value = """
SELECT sg.* FROM shopper_groups sg
JOIN shoppers_shopper_groups ssg ON sg.id = ssg.shopper_group_id
JOIN shoppers s ON s.id = ssg.shopper_id
WHERE s.id = :shopper_id""")
    List<ShopperGroup> findAllByShopperId(@Param("shopper_id") UUID shopperId);

    @Query(nativeQuery = true, value = """
SELECT sg.* FROM shopper_groups sg
JOIN shoppers_shopper_groups ssg ON sg.id = ssg.shopper_group_id
JOIN shoppers s ON s.id = ssg.shopper_id
WHERE s.id = :shopper_id
AND sg.id = :group_id""")
    Optional<ShopperGroup> findByShopperIdAndId(@Param("shopper_id") UUID shopperId, @Param("group_id") UUID groupId);


    @Query(nativeQuery = true, value = """
SELECT COUNT(*) > 1
FROM shopper_shopper_groups
WHERE shopper_id = :shopper_id
AND shopper_group_id = :group_id""")
    boolean existsByShopper_Id(@Param("shopper_id") UUID shopperId, @Param("group_id") UUID groupId);
}
