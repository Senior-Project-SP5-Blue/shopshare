package com.sp5blue.shopshare.models.user;

import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.*;
import java.util.Objects;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

  @Id
  @Column(name = "id")
  private String id;

  @Column(name = "type", columnDefinition = "role_type")
  @Enumerated(EnumType.STRING)
  @Type(PostgreSQLEnumType.class)
  private RoleType type;

  public Role() {}

  public Role(String id, RoleType type) {
    this.id = id;
    this.type = type;
  }

  @Override
  public String getAuthority() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Role role)) return false;
    return Objects.equals(id, role.id) && type == role.type;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, type);
  }
}
