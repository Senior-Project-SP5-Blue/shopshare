package com.sp5blue.shopshare.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "list_items")
public class ListItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private final UUID id = UUID.randomUUID();

    @Column(name = "name")
    private String name;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ItemStatus status;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @ManyToOne(optional = false)
    @JoinColumn(name = "shopper_id")
    private Shopper createdBy;

    @Column(name = "locked")
    private boolean locked;

    public ListItem() {
    }

    public ListItem(String name, Shopper createdBy) {
        this.name = name;
        this.createdBy = createdBy;
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
