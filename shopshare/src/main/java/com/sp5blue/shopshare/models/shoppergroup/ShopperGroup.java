package com.sp5blue.shopshare.models.shoppergroup;


import com.sp5blue.shopshare.models.shoppinglist.ShoppingList;
import com.sp5blue.shopshare.models.user.User;
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

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(name = "users_shopper_groups",
    joinColumns = @JoinColumn(name = "shopper_group_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<ShoppingList> lists = new ArrayList<>();

    @ManyToOne(optional = false)
    @JoinColumn(name = "admin_id")
    private User admin;

    public ShopperGroup() {
    }

    public ShopperGroup(String name, User createdBy) {
        this.name = name;
        this.admin = createdBy;
        this.users.add(createdBy);
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User createdBy) {
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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<ShoppingList> getLists() {
        return lists;
    }

    public void setLists(List<ShoppingList> lists) {
        this.lists = lists;
    }

    public boolean addUser(User user) {
        return users.add(user);
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public boolean removeUser(UUID userId) {
        User user = users.stream().filter(s -> s.getId().equals(userId)).findFirst().orElse(null);
        if (user == null) return false;
        return users.removeIf(x -> x.getId().equals(userId));
    }

    public void removeAllUsers() {
        users = new ArrayList<>();
    }

    public void addList(ShoppingList shoppingList) {
        shoppingList.setGroup(this);
        lists.add(shoppingList);
    }

    public void removeList(ShoppingList shoppingList) {
        shoppingList.setGroup(null);
        lists.remove(shoppingList);
    }
    public void removeList(UUID listId) {
        lists.removeIf(l -> l.getId().equals(listId));
    }
}
