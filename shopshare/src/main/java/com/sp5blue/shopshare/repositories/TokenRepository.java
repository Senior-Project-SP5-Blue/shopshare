package com.sp5blue.shopshare.repositories;

import com.sp5blue.shopshare.models.user.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query(nativeQuery = true, name = """
            SELECT token FROM tokens t INNER JOIN users u ON t.user_id = u.id
            WHERE u.id = :user_id
            AND (t.is_expired = false AND t.is_revoked = false)""")
    List<Token> findAllValidTokensByUser_Id(@Param("user_id") UUID user_id);

    List<Token> findAllByUser_Id(UUID user_id);
    @Query(nativeQuery = true, name = """
            SELECT token FROM tokens t INNER JOIN users u ON t.user_id = u.id
            WHERE u.id = :user_id
            AND t.type = 'ACCESS'
            AND (t.is_expired = false AND t.is_revoked = false)""")
    List<Token> findAllValidAccessTokensByUser_Id(@Param("user_id") UUID user_id);

    @Query(nativeQuery = true, name = """
            SELECT token FROM tokens t INNER JOIN users u ON t.user_id = u.id
            WHERE u.id = :user_id
            AND t.type = 'ACCESS'""")
    List<Token> findAllAccessTokensByUser_Id(@Param("user_id") UUID user_id);

    @Query(nativeQuery = true, name = """
            SELECT token FROM tokens t INNER JOIN users u ON t.user_id = u.id
            WHERE u.id = :user_id
            AND t.type = 'REFRESH'
            AND t.is_expired = false
            AND t.is_revoked = false""")
    Optional<Token> findRefreshTokenByUser_Id(@Param("user_id") UUID user_id);

    Optional<Token> findByToken(String token);

    @Query(nativeQuery = true, value = """
SELECT * FROM tokens t WHERE t.type = 'CONFIRMATION'
""")
    Optional<Token> findByConfirmationToken(String token);
    @Query(nativeQuery = true, value = """
    SELECT * FROM tokens t WHERE t.type = 'INVITATION'
    """)
    Optional<Token> findByInvitationToken(String token);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Token t set t.isRevoked = TRUE, t.isExpired = TRUE WHERE t.user.id = :user_id")
    void revokeTokensByUser(@Param("user_id") UUID userId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM Token t WHERE t.isExpired  OR t.isRevoked")
    void deleteExpiredTokens();
}
