package com.ttt.chat_module.models.notification;

public class TopicNotification<T> {
    private String to;
    private T data;

    public TopicNotification(String topicName, T data) {
        this.to = "/topics/" + topicName;
        this.data = data;
    }

    public TopicNotification() {
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
