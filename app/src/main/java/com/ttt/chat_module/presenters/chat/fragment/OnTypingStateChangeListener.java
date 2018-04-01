package com.ttt.chat_module.presenters.chat.fragment;

public interface OnTypingStateChangeListener {
    void onTypingStateChanged(String userID, boolean state);
}
