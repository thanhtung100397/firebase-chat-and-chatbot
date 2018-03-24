package com.ttt.chat_module.models;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.Set;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public class Message {
    public static final String CREATED_DATE = "createdDate";

    private String ownerEmail;
    @ServerTimestamp
    private String message;
    private Date createdDate;
    private Set<String> seenBy;

    private boolean isExpanded;

    public Message(String ownerEmail, String message) {
        this.ownerEmail = ownerEmail;
        this.message = message;
    }

    public Message() {
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
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

    @Exclude
    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}
