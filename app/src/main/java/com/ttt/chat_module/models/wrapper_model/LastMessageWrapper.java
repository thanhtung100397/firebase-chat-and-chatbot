package com.ttt.chat_module.models.wrapper_model;

import com.ttt.chat_module.models.message_models.BaseMessage;

public class LastMessageWrapper {
    private BaseMessage lastMessage;

    public LastMessageWrapper(BaseMessage lastMessage) {
        this.lastMessage = lastMessage;
    }

    public LastMessageWrapper() {
    }

    public BaseMessage getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(BaseMessage lastMessage) {
        this.lastMessage = lastMessage;
    }
}
