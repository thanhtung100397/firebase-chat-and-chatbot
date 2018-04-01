package com.ttt.chat_module.presenters.chat.fragment;

import com.ttt.chat_module.models.ImageItem;
import com.ttt.chat_module.models.message_models.ImageMessage;
import com.ttt.chat_module.presenters.BaseRequestListener;

import java.util.Map;

public interface UploadImageListener extends BaseRequestListener {
    void onEachImageUploadSuccess(String key, ImageMessage imageMessage);
    void onImageUploadFailure(String key, ImageMessage imageMessage);
    void onAllImageUploadSuccess(Map<String, ImageItem> imageMessage);
}
