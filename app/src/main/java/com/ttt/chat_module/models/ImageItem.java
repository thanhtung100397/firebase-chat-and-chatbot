package com.ttt.chat_module.models;

import android.net.Uri;

import com.google.firebase.firestore.Exclude;

public class ImageItem {
    private String url;
    private Uri uri;

    public ImageItem(Uri uri) {
        this.uri = uri;
    }

    public ImageItem(String url) {
        this.url = url;
    }

    public ImageItem() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Exclude
    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
