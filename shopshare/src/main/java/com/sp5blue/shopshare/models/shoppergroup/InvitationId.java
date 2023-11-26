package com.sp5blue.shopshare.models.shoppergroup;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class InvitationId implements Serializable {
  private UUID groupId;
  private UUID userId;

  public InvitationId() {}

  public InvitationId(UUID groupId, UUID userId) {
    this.groupId = groupId;
    this.userId = userId;
  }

  public UUID getGroupId() {
    return groupId;
  }

  public void setGroupId(UUID groupId) {
    this.groupId = groupId;
  }

  public UUID getUserId() {
    return userId;
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof InvitationId that)) return false;
    return Objects.equals(getGroupId(), that.getGroupId())
        && Objects.equals(getUserId(), that.getUserId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getGroupId(), getUserId());
  }
}
