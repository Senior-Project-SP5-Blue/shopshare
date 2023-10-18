package com.sp5blue.shopshare.models.listitem;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sp5blue.shopshare.models.shopper.Shopper;
import com.sp5blue.shopshare.models.shoppinglist.ShoppingList;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

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
    private ItemStatus status;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(optional = false)
    @JoinColumn(name = "created_by")
    private Shopper createdBy;

    @ManyToOne
    @JoinColumn(name = "shopping_list_id")
    private ShoppingList list;

    @Column(name = "locked")
    private boolean locked;

    public ListItem() {
    }

    public ListItem(String name, Shopper createdBy) {
        this.name = name;
        this.createdBy = createdBy;
        this.createdOn = LocalDateTime.now();
    }

    public ListItem(String name, ItemStatus status, boolean locked) {
        this.name = name;
        this.status = status;
        this.locked = locked;
    }

    public ListItem(String name, ItemStatus status, Shopper createdBy, boolean locked) {
        this.name = name;
        this.status = status;
        this.createdBy = createdBy;
        this.locked = locked;
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

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public Shopper getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Shopper createdBy) {
        this.createdBy = createdBy;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
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
}
