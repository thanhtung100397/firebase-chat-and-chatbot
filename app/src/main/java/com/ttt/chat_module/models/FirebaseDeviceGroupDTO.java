package com.ttt.chat_module.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDeviceGroupDTO {
    public static final String CREATE_OPERATION = "create";
    public static final String ADD_OPERATION = "add";
    public static final String REMOVE_OPERATION = "remove";

    @SerializedName("operation")
    @Expose
    private String operation;
    @SerializedName("notification_key_name")
    @Expose
    private String notificationKeyName;
    @SerializedName("notification_key")
    @Expose
    private String notificationKey;
    @SerializedName("registration_ids")
    @Expose
    private List<String> registrationIDs;

    public FirebaseDeviceGroupDTO(String operation, String notificationKeyName, String notificationKey) {
        this.operation = operation;
        this.notificationKeyName = notificationKeyName;
        this.notificationKey = notificationKey;
        this.registrationIDs = new ArrayList<>(1);
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getNotificationKeyName() {
        return notificationKeyName;
    }

    public void setNotificationKeyName(String notificationKeyName) {
        this.notificationKeyName = notificationKeyName;
    }

    public String getNotificationKey() {
        return notificationKey;
    }

    public void setNotificationKey(String notificationKey) {
        this.notificationKey = notificationKey;
    }

    public List<String> getRegistrationIDs() {
        return registrationIDs;
    }

    public void setRegistrationIDs(List<String> registrationIDs) {
        this.registrationIDs = registrationIDs;
    }

    public void addRegistrationID(String id) {
        registrationIDs.add(id);
    }
}
