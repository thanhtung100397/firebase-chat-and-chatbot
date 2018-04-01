package com.ttt.chat_module.models.message_models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.Map;

public abstract class BaseMessage {
    public static final String CREATED_DATE = "createdDate";

    public static final String TEXT_MESSAGE = "text";
    public static final String IMAGE_MESSAGE = "image";

    private String type;
    private String ownerID;
    @ServerTimestamp
    private Date createdDate;
    private Map<String, Boolean> seenBy;

    public BaseMessage(String ownerID, String type) {
        this.ownerID = ownerID;
        this.type = type;
    }

    public BaseMessage() {
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Map<String, Boolean> getSeenBy() {
        return seenBy;
    }

    public void setSeenBy(Map<String, Boolean> seenBy) {
        this.seenBy = seenBy;
    }
}
