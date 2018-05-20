package com.ttt.chat_module.bus_event;

import com.ttt.chat_module.models.User;

public class UserChangeEvent {
    private User user;

    public UserChangeEvent(User user) {
        this.user = user;
    }

    public UserChangeEvent() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
