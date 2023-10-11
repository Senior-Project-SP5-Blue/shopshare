package com.sp5blue.shopshare.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "shopping_lists")
public class ShoppingList {
    @Id
    @Column(name = "id")
    private final UUID id = UUID.randomUUID();

    @Column(name = "name")
    private String name;

    @Column(name = "modified_on")
    private LocalDateTime modifiedOn;

    @ManyToOne
    @JoinColumn(name = "modified_by")
    private Shopper modifiedBy;

    @OneToMany
    @JoinColumn(name = "shopping_list_id")
    private List<ListItem> items;

    @ManyToOne
    @JoinColumn(name = "shopper_id")
    private Shopper shopper;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private ShopperGroup group;

    public ShoppingList() {
    }

    /**
     *
     */
    public ShoppingList(String name, Shopper shopper) {
        this.name = name;
        this.shopper = shopper;
    }

    /**
     *
     */
    public ShoppingList(String name, ShopperGroup group) {
        this.name = name;
        this.group = group;
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

    public LocalDateTime getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(LocalDateTime modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Shopper getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Shopper modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public List<ListItem> getItems() {
        return items;
    }

    public void setItems(List<ListItem> items) {
        this.items = items;
    }

    public Shopper getShopper() {
        return shopper;
    }

    public void setShopper(Shopper shopper) {
        this.shopper = shopper;
    }

    public ShopperGroup getGroup() {
        return group;
    }

    public void setGroup(ShopperGroup group) {
        this.group = group;
    }

    public boolean addItem(ListItem item) {
        if (items == null) items = new ArrayList<>();

        return items.add(item);
    }

    public boolean removeItem(UUID itemId) {
        if (items == null) return false;

        return items.removeIf(i -> i.getId().equals(itemId));
    }

    public boolean removeItem(ListItem item) {
        if (items == null) return false;

        return items.remove(item);
    }


}
