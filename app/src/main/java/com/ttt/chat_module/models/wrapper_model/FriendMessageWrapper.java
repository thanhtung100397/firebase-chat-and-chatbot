package com.ttt.chat_module.models.wrapper_model;

import com.ttt.chat_module.models.message_models.BaseMessage;

/**
 * Created by TranThanhTung on 27/03/2018.
 */

public class FriendMessageWrapper {
    private BaseMessage message;
    private boolean isExpanded = false;

    public FriendMessageWrapper(BaseMessage message) {
        this.message = message;
    }

    public BaseMessage getMessage() {
        return message;
    }

    public void setMessage(BaseMessage message) {
        this.message = message;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}
