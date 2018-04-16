package com.ttt.chat_module.models.notification;

import com.ttt.chat_module.models.UserInfo;
import com.ttt.chat_module.models.message_models.ImageMessage;

public class NewImageMessageNotification extends NewMessageNotification {
    public static final String IMAGE_COUNT = "imageCount";
    private int imageCount;

    public NewImageMessageNotification(String roomID, UserInfo ownerInfo, ImageMessage imageMessage) {
        super(roomID, ownerInfo, imageMessage);
        this.imageCount = imageMessage.getImages().size();
    }



    public int getImageCount() {
        return imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
    }
}
