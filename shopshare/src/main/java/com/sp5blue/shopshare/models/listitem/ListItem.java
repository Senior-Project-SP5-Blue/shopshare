package com.sp5blue.shopshare.models.listitem;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sp5blue.shopshare.models.shoppinglist.ShoppingList;
import com.sp5blue.shopshare.models.user.User;
import com.sp5blue.shopshare.serializers.ListItemSerializer;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@JsonSerialize(using = ListItemSerializer.class)
@Entity
@Table(name = "list_items")
public class ListItem {

    @Id
    @Column(name = "id")
    private final UUID id = UUID.randomUUID();

    @Column(name = "name")
    private String name;

    @Column(name = "status", columnDefinition = "item_status")
    @Enumerated(EnumType.STRING)
    @Type(PostgreSQLEnumType.class)
    private ItemStatus status = ItemStatus.ACTIVE;

    @Column(name = "created_on")
    private final LocalDateTime createdOn = LocalDateTime.now();

    @ManyToOne(optional = false)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(name = "locked")
    private boolean locked = false;

    @ManyToOne
    @JoinColumn(name = "shopping_list_id")
    private ShoppingList list;

    public ListItem() {
    }

    public ListItem(String name, User createdBy) {
        this.name = name;
        this.createdBy = createdBy;
    }

    public ListItem(String name, ItemStatus status, boolean locked) {
        this.name = name;
        this.status = status;
        this.locked = locked;
    }

    public ListItem(String name, ItemStatus status, User createdBy, boolean locked) {
        this.name = name;
        this.status = status;
        this.createdBy = createdBy;
        this.locked = locked;
    }

    public ListItem(String name, User createdBy, boolean locked) {
        this.name = name;
        this.createdBy = createdBy;
        this.locked = locked;
    }

    public ListItem(String name, User createdBy, boolean locked, ShoppingList list) {
        this.name = name;
        this.createdBy = createdBy;
        this.locked = locked;
        this.list = list;
    }

    public ListItem(String name) {
        this.name = name;
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

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public ShoppingList getList() {
        return list;
    }

    public void setList(ShoppingList list) {
        this.list = list;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListItem listItem)) return false;
        return Objects.equals(getId(), listItem.getId()) && Objects.equals(getName(), listItem.getName()) && Objects.equals(getCreatedOn(), listItem.getCreatedOn());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getCreatedOn());
    }

    @Override
    public String toString() {
        return "ListItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", createdOn=" + createdOn +
                ", locked=" + locked +
                '}';
    }
}
