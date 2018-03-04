package com.ttt.chat_module.common.adapter.recycler_view_adapter;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.Set;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public class Message {
    private String owner;
    private String message;
    @ServerTimestamp
    private Date createdDate;
    private Set<String> seenBy;

    @Exclude
    private boolean isExpanded;

    public Message(String owner, String message) {
        this.owner = owner;
        this.message = message;
    }

    public Message() {
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Set<String> getSeenBy() {
        return seenBy;
    }

    public void setSeenBy(Set<String> seenBy) {
        this.seenBy = seenBy;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}
