package com.sp5blue.shopshare.models.shoppinglist;

import com.sp5blue.shopshare.models.listitem.ListItem;
import com.sp5blue.shopshare.models.shoppergroup.ShopperGroup;
import com.sp5blue.shopshare.models.user.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.springframework.lang.NonNull;

@Entity
@Table(name = "shopping_lists")
public class ShoppingList {
  @Id
  @Column(name = "id")
  private final UUID id = UUID.randomUUID();

  @Column(name = "name")
  private String name;

  @NonNull
  @Column(name = "modified_on")
  private LocalDateTime modifiedOn;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "modified_by")
  private User modifiedBy;

  @OneToMany(
      mappedBy = "list",
      cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REMOVE})
  private List<ListItem> items = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "group_id")
  private ShopperGroup group;

  public ShoppingList() {
    modifiedOn = LocalDateTime.now();
  }

  public ShoppingList(String name) {
    modifiedOn = LocalDateTime.now();
    this.name = name;
  }

  public ShoppingList(String name, ShopperGroup group) {
    modifiedOn = LocalDateTime.now();
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

  public User getModifiedBy() {
    return modifiedBy;
  }

  public void setModifiedBy(User modifiedBy) {
    this.modifiedBy = modifiedBy;
  }

  public List<ListItem> getItems() {
    return items;
  }

  public void setItems(List<ListItem> items) {
    this.items = items;
  }

  public ShopperGroup getGroup() {
    return group;
  }

  public void setGroup(ShopperGroup group) {
    this.group = group;
  }

  public void addItem(ListItem item) {
    items.add(item);
  }

  public void removeItem(UUID itemId) {
    items.removeIf(i -> i.getId().equals(itemId));
  }

  public void removeItem(ListItem item) {
    this.items.remove(item);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ShoppingList that)) return false;
    return Objects.equals(getId(), that.getId()) && Objects.equals(getName(), that.getName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getName());
  }
}
