package com.sp5blue.shopshare.models.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "tokens")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "token")
    private String token;

    @Column(name = "type", columnDefinition = "token_type")
    @Enumerated(EnumType.STRING)
    @Type(PostgreSQLEnumType.class)
    private TokenType type;

    @Column(name = "is_expired")
    private boolean isExpired;

    @Column(name = "is_revoked")
    private boolean isRevoked;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Token(String token, User user) {
        this.token = token;
        this.user = user;
        this.type = TokenType.ACCESS;
        isRevoked = false;
        isExpired = false;
    }

    public Token(String token, TokenType type, boolean isExpired, boolean isRevoked, User user) {
        this.token = token;
        this.type = type;
        this.isExpired = isExpired;
        this.isRevoked = isRevoked;
        this.user = user;
    }

    public Token(String token, User user, TokenType type) {
        this.token = token;
        this.type = type;
        this.user = user;
    }

    public Token() {
    }

    public Integer getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    public boolean isRevoked() {
        return isRevoked;
    }

    public void setRevoked(boolean revoked) {
        isRevoked = revoked;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }
}
