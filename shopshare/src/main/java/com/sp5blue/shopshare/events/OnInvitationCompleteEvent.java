package com.sp5blue.shopshare.events;

import com.sp5blue.shopshare.models.shoppergroup.ShopperGroup;
import com.sp5blue.shopshare.models.user.User;
import org.springframework.context.ApplicationEvent;

public class OnInvitationCompleteEvent extends ApplicationEvent {

  private final User user;

  private final ShopperGroup group;

  public OnInvitationCompleteEvent(Object source, User user, ShopperGroup group) {
    super(source);
    this.user = user;
    this.group = group;
  }

  public User getUser() {
    return user;
  }

  public ShopperGroup getGroup() {
    return group;
  }
}
