package com.sp5blue.shopshare.models;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne()
    @JoinColumn(name = "shopper_id")
    private Shopper shopper;


    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private RoleType role;

    public Role() {
    }

    public Shopper getShopper() {
        return shopper;
    }

    public void setShopper(Shopper shopper) {
        this.shopper = shopper;
    }

    public RoleType getRole() {
        return role;
    }

    @Override
    public String getAuthority() {
        return role.name();
    }
}
