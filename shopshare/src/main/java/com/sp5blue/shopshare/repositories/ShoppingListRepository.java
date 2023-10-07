package com.sp5blue.shopshare.repositories;

import com.sp5blue.shopshare.models.ShoppingList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ShoppingListRepository extends JpaRepository<ShoppingList, UUID> {

    Page<ShoppingList> findAllByName(String name, Pageable pageable);
    List<ShoppingList> findAllByName(String name);

    Page<ShoppingList> findAllByShopper_Id(UUID shopperId, Pageable pageable);

    List<ShoppingList> findAllByShopper_Id(UUID shopperId);

    Page<ShoppingList> findAllByGroup_Id(UUID groupId, Pageable pageable);
    List<ShoppingList> findAllByGroup_Id(UUID groupId);

    long countByShopper_Id(UUID shopperId);

    long countByGroup_Id(UUID shopperId);

    boolean existsByShopper_Id(UUID shopperId);

    boolean existsByGroup_Id(UUID groupId);

}
