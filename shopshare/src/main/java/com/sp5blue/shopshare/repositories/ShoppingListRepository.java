package com.sp5blue.shopshare.repositories;

import com.sp5blue.shopshare.models.shoppinglist.ShoppingList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShoppingListRepository extends JpaRepository<ShoppingList, UUID> {

    Page<ShoppingList> findAllByName(String name, Pageable pageable);
    List<ShoppingList> findAllByName(String name);

    Page<ShoppingList> findAllByGroup_Id(UUID groupId, Pageable pageable);
    List<ShoppingList> findAllByGroup_Id(UUID groupId);

    @Query("SELECT sl FROM ShoppingList sl LEFT JOIN FETCH sl.items WHERE sl.group.id= :group_id AND sl.id= :list_id")
    Optional<ShoppingList> findByGroup_IdAndId(@Param("group_id") UUID groupId, @Param("list_id")UUID listId);

    long countByGroup_Id(UUID shopperId);

    boolean existsByGroup_Id(UUID groupId);
    boolean existsById(@NonNull UUID listId);



}
