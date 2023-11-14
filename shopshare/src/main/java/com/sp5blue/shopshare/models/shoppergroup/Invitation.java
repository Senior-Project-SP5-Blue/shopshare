package com.sp5blue.shopshare.models.shoppergroup;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sp5blue.shopshare.models.user.User;
import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "group_invitations")
public class Invitation {
    @EmbeddedId
    private InvitationId id = new InvitationId();

    public Invitation() {
    }

    public Invitation(ShopperGroup group, User user) {
        this.group = group;
        this.user = user;
    }

    public Invitation(UUID groupId, UUID userId) {
        id = new InvitationId(groupId, userId);
    }


    @ManyToOne
    @MapsId("groupId")
    @JoinColumn(name = "shopper_group_id")
    @JsonIgnore
    private ShopperGroup group;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

//    TO-DO: implement invitation token
//    @OneToOne
//    @JoinColumn(name = "")
//    private Token token;


    public InvitationId getId() {
        return id;
    }

    public ShopperGroup getGroup() {
        return group;
    }

    public User getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Invitation that)) return false;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
