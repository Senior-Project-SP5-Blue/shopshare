package com.sp5blue.shopshare.models;

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

    @ManyToOne
    @JoinColumn(name = "shopper_id")
    private Shopper shopper;

    public Token(String token, Shopper shopper) {
        this.token = token;
        this.shopper = shopper;
        this.type = TokenType.ACCESS;
        isRevoked = false;
        isExpired = false;
    }

    public Token(String token, TokenType type, boolean isExpired, boolean isRevoked, Shopper shopper) {
        this.token = token;
        this.type = type;
        this.isExpired = isExpired;
        this.isRevoked = isRevoked;
        this.shopper = shopper;
    }

    public Token(String token, Shopper shopper, TokenType type) {
        this.token = token;
        this.type = type;
        this.shopper = shopper;
    }

    public Token() {
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

    public Shopper getShopper() {
        return shopper;
    }

    public void setShopper(Shopper shopper) {
        this.shopper = shopper;
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }
}
