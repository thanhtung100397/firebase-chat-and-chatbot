package com.ttt.chat_module.models;

public class Path {
    public static final String TYPE = "type";

    public static final String CHAT_ROOM_TYPE = "chat_room";

    private String type;
    private String path;

    public Path(String type, String path) {
        this.type = type;
        this.path = path;
    }

    public Path() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
