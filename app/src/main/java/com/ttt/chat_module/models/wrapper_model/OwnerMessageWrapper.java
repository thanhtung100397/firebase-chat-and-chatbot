package com.ttt.chat_module.models.wrapper_model;

import com.ttt.chat_module.models.message_models.BaseMessage;

/**
 * Created by TranThanhTung on 27/03/2018.
 */

public class OwnerMessageWrapper {
    private BaseMessage message;
    private boolean isSeenExpanded = false;
    private boolean isDateExpanded = false;

    public OwnerMessageWrapper(BaseMessage message) {
        this.message = message;
    }

    public BaseMessage getMessage() {
        return message;
    }

    public void setMessage(BaseMessage message) {
        this.message = message;
    }

    public boolean isSeenExpanded() {
        return isSeenExpanded;
    }

    public void setSeenExpanded(boolean seenExpanded) {
        isSeenExpanded = seenExpanded;
    }

    public boolean isDateExpanded() {
        return isDateExpanded;
    }

    public void setDateExpanded(boolean dateExpanded) {
        isDateExpanded = dateExpanded;
    }
}
