package com.ttt.chat_module.models;

import com.ttt.chat_module.common.Constants;

public class EmojiItem {
    private String path;
    private String type;
    private String id;

    public EmojiItem(String type, String id) {
        setType(type);
        setId(id);
        setPath(Constants.ABSOLUTE_EMOJI_ASSETS_FOLDER_PATH + "/" + type + "/" + id);
    }

    public EmojiItem() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
