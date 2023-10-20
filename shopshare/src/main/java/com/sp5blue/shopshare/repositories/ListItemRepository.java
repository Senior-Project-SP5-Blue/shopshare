package com.sp5blue.shopshare.repositories;

import com.sp5blue.shopshare.models.listitem.ListItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ListItemRepository extends JpaRepository<ListItem, UUID> {
    Page<ListItem> findAllByName (String name, Pageable pageable);

    List<ListItem> findAllByName (String name);

    Page<ListItem> findAllByCreatedBy_Id(UUID listId, Pageable pageable);

    List<ListItem> findAllByCreatedBy_Id(UUID listId);
    Page<ListItem> findAllByList_Id(UUID listId, Pageable pageable);

    List<ListItem> findAllByList_Id(UUID listId);

    Optional<ListItem> findByList_IdAndId(UUID listId, UUID itemId);

    long countByCreatedBy_Id(UUID listId);

    boolean existsByName(String name);

    boolean existsByCreatedBy_Id(UUID listId);

    boolean existsByList_Id(UUID listId);
}
