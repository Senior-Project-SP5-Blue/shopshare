package com.sp5blue.shopshare.models.shoppergroup;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sp5blue.shopshare.models.shopper.Role;
import com.sp5blue.shopshare.models.shopper.RoleType;
import com.sp5blue.shopshare.models.shopper.Shopper;
import com.sp5blue.shopshare.models.shoppinglist.ShoppingList;
import com.sp5blue.shopshare.serializers.ShopperGroupSerializer;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@JsonSerialize(using = ShopperGroupSerializer.class)
@Entity
@Table(name = "shopper_groups")
public class ShopperGroup {

    @Id
    @Column(name = "id")
    private final UUID id = UUID.randomUUID();

    @Column(name = "name")
    private String name;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(name = "shoppers_shopper_groups",
    joinColumns = @JoinColumn(name = "shopper_group_id"),
    inverseJoinColumns = @JoinColumn(name = "shopper_id"))
    private List<Shopper> shoppers = new ArrayList<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<ShoppingList> lists = new ArrayList<>();

    @ManyToOne(optional = false)
    @JoinColumn(name = "admin_id")
    private Shopper admin;

    public ShopperGroup() {
    }

    public ShopperGroup(String name, Shopper createdBy) {
        this.name = name;
        Role role = new Role("ROLE_GROUP_ADMIN-"+ getId(), RoleType.ROLE_GROUP_ADMIN);
        createdBy.addRole(role);
        this.admin = createdBy;
        this.shoppers = new ArrayList<>();
        this.shoppers.add(createdBy);
        this.lists = new ArrayList<>();
    }

    public Shopper getAdmin() {
        return admin;
    }

    public void setAdmin(Shopper createdBy) {
        this.admin = createdBy;
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
        Role role = new Role("ROLE_GROUP_MEMBER-" + getId(), RoleType.ROLE_GROUP_MEMBER);
        shopper.addRole(role);
        return shoppers.add(shopper);
    }

    public boolean removeShopper(Shopper shopper) {
        shopper.removeRole("ROLE_GROUP_MEMBER-" + getId());
        return shoppers.remove(shopper);
    }

    public boolean removeShopper(UUID shopperId) {
        Shopper shopper = shoppers.stream().filter(s -> s.getId().equals(shopperId)).findFirst().orElse(null);
        if (shopper == null) return false;
        shopper.removeRole("ROLE_GROUP_MEMBER-" + getId());
        return shoppers.removeIf(x -> x.getId().equals(shopperId));
    }

    public boolean addList(ShoppingList shoppingList) {
        return lists.add(shoppingList);
    }

    public boolean removeList(ShoppingList shoppingList) {
        return lists.remove(shoppingList);
    }
    public boolean removeList(UUID listId) {
        return lists.removeIf(l -> l.getId().equals(listId));
    }
}
