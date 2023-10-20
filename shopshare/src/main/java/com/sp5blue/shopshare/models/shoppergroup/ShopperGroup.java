package com.sp5blue.shopshare.models.shoppergroup;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sp5blue.shopshare.models.user.Role;
import com.sp5blue.shopshare.models.user.RoleType;
import com.sp5blue.shopshare.models.user.User;
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
        Role role = new Role("ROLE_GROUP_ADMIN-"+ getId(), RoleType.ROLE_GROUP_ADMIN);
        createdBy.addRole(role);
        this.admin = createdBy;
        this.users = new ArrayList<>();
        this.users.add(createdBy);
        this.lists = new ArrayList<>();
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
        Role role = new Role("ROLE_GROUP_MEMBER-" + getId(), RoleType.ROLE_GROUP_MEMBER);
        user.addRole(role);
        return users.add(user);
    }

    public boolean removeUser(User user) {
        user.removeRole("ROLE_GROUP_MEMBER-" + getId());
        return users.remove(user);
    }

    public boolean removeUser(UUID userId) {
        User user = users.stream().filter(s -> s.getId().equals(userId)).findFirst().orElse(null);
        if (user == null) return false;
        user.removeRole("ROLE_GROUP_MEMBER-" + getId());
        return users.removeIf(x -> x.getId().equals(userId));
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
