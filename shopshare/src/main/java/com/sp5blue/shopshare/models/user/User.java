package com.sp5blue.shopshare.models.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
public class User implements UserDetails {

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

  @JsonIgnore
  @Column(name = "email")
  private String email;

  @JsonIgnore
  @Column(name = "password")
  private String password;

  @Column(name = "number")
  private String number;

  @ManyToMany(
      cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH},
      fetch = FetchType.EAGER)
  @JoinTable(
      name = "users_roles",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();

  @JsonIgnore
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Token> tokens = new ArrayList<>();

  @JsonIgnore
  @Column(name = "active")
  private boolean active = true;

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

  public User() {}

  public User(
      String firstName,
      String lastName,
      String username,
      String email,
      String password,
      Set<Role> roles) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.username = username;
    this.email = email;
    this.password = password;
    this.roles = roles;
  }

  public User(
      String firstName,
      String lastName,
      String username,
      String email,
      String number,
      String password) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.username = username;
    this.email = email;
    this.password = password;
    this.number = number;
  }

  public User(String firstName, String lastName, String username, String email, String password) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.username = username;
    this.email = email;
    this.password = password;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  @Override
  public String toString() {
    return "User{"
        + "id="
        + id
        + ", firstName='"
        + firstName
        + '\''
        + ", lastName='"
        + lastName
        + '\''
        + ", username='"
        + username
        + '\''
        + ", email='"
        + email
        + '\''
        + ", number='"
        + number
        + '\''
        + ", active="
        + active
        + '}';
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

  @JsonIgnore
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @JsonIgnore
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @JsonIgnore
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

  public Set<Role> getRoles() {
    return roles;
  }

  public void addRole(Role role) {
    roles.add(role);
  }

  public void removeRole(Role role) {
    roles.remove(role);
  }

  public void removeRole(String roleName) {
    roles.removeIf(r -> r.getAuthority().equals(roleName));
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof User user)) return false;
    return Objects.equals(getId(), user.getId())
        && Objects.equals(getUsername(), user.getUsername())
        && Objects.equals(getEmail(), user.getEmail());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getUsername(), getEmail());
  }
}
