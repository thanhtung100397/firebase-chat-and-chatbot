package com.ttt.chat_module.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class VisitState {
    public static final String IN_ROOM_STATE = "in_room";
    public static final String LEFT_ROOM_STATE = "left_room";

    private String state;
    @ServerTimestamp
    private Date lastVisited;

    public VisitState(String state) {
        this.state = state;
    }

    public VisitState() {
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getLastVisited() {
        return lastVisited;
    }

    public void setLastVisited(Date lastVisited) {
        this.lastVisited = lastVisited;
    }
}
