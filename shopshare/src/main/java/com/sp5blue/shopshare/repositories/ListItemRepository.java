package com.sp5blue.shopshare.repositories;

import com.sp5blue.shopshare.models.listitem.ListItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ListItemRepository extends JpaRepository<ListItem, UUID> {
    Page<ListItem> findAllByName (String name, Pageable pageable);

    List<ListItem> findAllByName (String name);

    Page<ListItem> findAllByCreatedBy_Id(UUID listId, Pageable pageable);

    List<ListItem> findAllByCreatedBy_Id(UUID listId);

    @Query(nativeQuery = true, value = """
SELECT li.* FROM list_items li JOIN shopping_lists sl ON sl.id = :list_id WHERE li.id = :item_id""")
    Optional<ListItem> findByList_IdAndId(@Param("list_id") UUID listId, @Param("item_id") UUID itemId);

    long countByCreatedBy_Id(UUID listId);

    boolean existsByName(String name);

    boolean existsByCreatedBy_Id(UUID listId);
}
