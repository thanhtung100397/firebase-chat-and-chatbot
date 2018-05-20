package com.ttt.chat_module.models.message_models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.Map;

public abstract class BaseMessage {
    public static final String TYPE = "type";
    public static final String OWNER_ID = "ownerID";
    public static final String CREATED_DATE = "createdDate";
    public static final String SEEN_BY = "seenBy";

    public static final String TEXT_MESSAGE = "text";
    public static final String IMAGE_MESSAGE = "image";
    public static final String EMOJI_MESSAGE = "emoji";
    public static final String LOCATION_MESSAGE = "location";

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

    public void update(BaseMessage baseMessage) {
        setOwnerID(baseMessage.getOwnerID());
        setCreatedDate(baseMessage.getCreatedDate());
        setSeenBy(baseMessage.getSeenBy());
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
