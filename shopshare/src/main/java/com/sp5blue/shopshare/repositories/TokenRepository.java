package com.sp5blue.shopshare.repositories;

import com.sp5blue.shopshare.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query(nativeQuery = true, name = """
            SELECT token FROM tokens t INNER JOIN shoppers s ON t.shopper_id = s.id
            WHERE s.id = :shopper_id
            AND (t.is_expired = false AND t.is_revoked = false)""")
    List<Token> findAllValidTokensByShopper_Id(@Param("shopper_id") UUID shopperId);

    List<Token> findAllByShopper_Id(UUID shopperId);
    @Query(nativeQuery = true, name = """
            SELECT token FROM tokens t INNER JOIN shoppers s ON t.shopper_id = s.id
            WHERE s.id = :shopper_id
            AND t.type = 'ACCESS'
            AND (t.is_expired = false AND t.is_revoked = false)""")
    List<Token> findAllValidAccessTokensByShopper_Id(@Param("shopper_id") UUID shopperId);

    @Query(nativeQuery = true, name = """
            SELECT token FROM tokens t INNER JOIN shoppers s ON t.shopper_id = s.id
            WHERE s.id = :shopper_id
            AND t.type = 'ACCESS'""")
    List<Token> findAllAccessTokensByShopper_Id(@Param("shopper_id") UUID shopperId);

    @Query(nativeQuery = true, name = """
            SELECT token FROM tokens t INNER JOIN shoppers s ON t.shopper_id = s.id
            WHERE s.id = :shopper_id
            AND t.type = 'REFRESH'
            AND t.is_expired = false
            AND t.is_revoked = false""")
    Optional<Token> findRefreshTokenByShopper_Id(@Param("shopper_id") UUID shopperId);

    Optional<Token> findByToken(String token);
}
