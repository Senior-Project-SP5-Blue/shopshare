package com.sp5blue.shopshare.events;

import com.sp5blue.shopshare.models.user.User;
import org.springframework.context.ApplicationEvent;

public class OnSignUpCompleteEvent extends ApplicationEvent {
    private User user;



    public OnSignUpCompleteEvent(Object source, User user) {
        super(source);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
