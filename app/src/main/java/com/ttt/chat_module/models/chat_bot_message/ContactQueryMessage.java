package com.ttt.chat_module.models.chat_bot_message;

import com.ttt.chat_module.models.ContactInfo;

import java.util.List;

public class ContactQueryMessage extends SimpleMessage {
    private String action;
    private List<ContactInfo> contactsInfo;

    public ContactQueryMessage(String message, String action, List<ContactInfo> contactsInfo) {
        super(message);
        this.action = action;
        this.contactsInfo = contactsInfo;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<ContactInfo> getContactsInfo() {
        return contactsInfo;
    }

    public void setContactsInfo(List<ContactInfo> contactsInfo) {
        this.contactsInfo = contactsInfo;
    }
}
