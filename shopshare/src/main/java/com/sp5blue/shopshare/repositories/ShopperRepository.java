package com.sp5blue.shopshare.repositories;

import com.sp5blue.shopshare.models.Shopper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

}
