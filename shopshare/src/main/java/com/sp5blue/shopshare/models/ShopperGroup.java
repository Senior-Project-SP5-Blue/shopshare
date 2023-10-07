package com.sp5blue.shopshare.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "shopper_groups")
public class ShopperGroup {

    @Id
    @Column(name = "id")
    private final UUID id = UUID.randomUUID();

    @Column(name = "name")
    private String name;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "shoppers_shopper_groups",
    joinColumns = @JoinColumn(name = "shopper_group_id"),
    inverseJoinColumns = @JoinColumn(name = "shopper_id"))
    private List<Shopper> shoppers;

    @OneToMany(mappedBy = "group")

    private List<ShoppingList> lists;


    @ManyToOne(optional = false)
    @JoinColumn(name = "created_by")
    private Shopper createdBy;

    public ShopperGroup() {
    }

    /**
     *
     */
    public ShopperGroup(String name, Shopper createdBy) {
        this.name = name;
        this.createdBy = createdBy;
        this.shoppers = new ArrayList<>();
        this.shoppers.add(createdBy);
        this.lists = new ArrayList<>();
    }

    public Shopper getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Shopper createdBy) {
        this.createdBy = createdBy;
    }
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Shopper> getShoppers() {
        return shoppers;
    }

    public void setShoppers(List<Shopper> shoppers) {
        this.shoppers = shoppers;
    }

    public List<ShoppingList> getLists() {
        return lists;
    }

    public void setLists(List<ShoppingList> lists) {
        this.lists = lists;
    }

    public boolean addShopper(Shopper shopper) {
        if (shoppers == null) shoppers = new ArrayList<>();

        return shoppers.add(shopper);
    }
    public boolean removeShopper(Shopper shopper) {
        return shoppers.remove(shopper);
    }

    public boolean removeShopper(UUID shopperId) {
        return shoppers.removeIf(x -> x.getId().equals(shopperId));
    }
}
