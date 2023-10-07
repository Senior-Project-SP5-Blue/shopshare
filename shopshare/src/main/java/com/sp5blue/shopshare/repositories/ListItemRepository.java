package com.sp5blue.shopshare.repositories;

import com.sp5blue.shopshare.models.ListItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ListItemRepository extends JpaRepository<ListItem, UUID> {
    Page<ListItem> findAllByName (String name, Pageable pageable);

    List<ListItem> findAllByName (String name);

    Page<ListItem> findAllByCreatedBy_Id(UUID shopperId, Pageable pageable);

    List<ListItem> findAllByCreatedBy_Id(UUID shopperId);

    long countByCreatedBy_Id(UUID shopperId);

    boolean existsByName(String name);

    boolean existsByCreatedBy_Id(UUID shopperId);
}
