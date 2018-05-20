package com.ttt.chat_module.presenters.chat.fragment;

import com.ttt.chat_module.models.UserSettings;
import com.ttt.chat_module.presenters.BaseRequestListener;

public interface OnUserSettingsChangeListener extends BaseRequestListener {
    void onUserSettingsChange(String userID, UserSettings userSettings);
}
