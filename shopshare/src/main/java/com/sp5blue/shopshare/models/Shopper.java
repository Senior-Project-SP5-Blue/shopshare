package com.sp5blue.shopshare.models;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "shoppers")
public class Shopper implements UserDetails {
    @Id
    @Column(name = "id")
    private final UUID id = UUID.randomUUID();

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "username")
    private String username;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @ManyToMany(mappedBy = "shoppers", fetch = FetchType.EAGER)
    private List<ShopperGroup> groups;

    @OneToMany(mappedBy = "shopper", fetch = FetchType.EAGER)
    private List<ShoppingList> personalLists;

    @OneToMany(mappedBy = "shopper", fetch = FetchType.EAGER)
    private List<Role> roles;

    @OneToMany(mappedBy = "shopper", fetch = FetchType.EAGER)
    private List<Token> tokens;

    @Column(name = "active")
    private boolean active;

    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Shopper() {
//        this.id = UUID.randomUUID();
    }

    public Shopper(String firstName, String lastName, String username, String email, String password, List<Role> roles) {
//        this.id = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.active = true;
    }

    public Shopper(String firstName, String lastName, String username, String email, String password) {
//        this.id = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.active = true;
    }

    @Override
    public String toString() {
        return "Shopper{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", groups=" + groups +
                ", personalList=" + personalLists +
                ", roles=" + roles +
                ", active=" + active +
                '}';
    }

    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<ShopperGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<ShopperGroup> groups) {
        this.groups = groups;
    }

    public List<ShoppingList> getPersonalList() {
        return personalLists;
    }

    public void setPersonalList(List<ShoppingList> personalLists) {
        this.personalLists = personalLists;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void addShoppingList(ShoppingList list) {
        if (personalLists == null) personalLists = new ArrayList<>();

        personalLists.add(list);
    }

    public boolean removeShoppingList(UUID listId) {
        if (personalLists == null) return false;

        return personalLists.removeIf(l -> l.getId().equals(listId));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Shopper shopper)) return false;
        return Objects.equals(getId(), shopper.getId()) && Objects.equals(getUsername(), shopper.getUsername()) && Objects.equals(getEmail(), shopper.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUsername(), getEmail());
    }
}
