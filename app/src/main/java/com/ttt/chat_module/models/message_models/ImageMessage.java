package com.ttt.chat_module.models.message_models;

import android.net.Uri;

import com.ttt.chat_module.models.ImageItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageMessage extends BaseMessage {
    public static final String IMAGES = "images";

    private Map<String, ImageItem> images;

    public ImageMessage(String owner, List<Uri> uris) {
        super(owner, IMAGE_MESSAGE);
        int size = uris.size();
        this.images = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            images.put(i + "", new ImageItem(uris.get(i)));
        }
    }

    public ImageMessage(String owner, Map<String, String> mapUrls) {
        super(owner, IMAGE_MESSAGE);
        this.images = new HashMap<>(mapUrls.size());
        for (Map.Entry<String, String> entry : mapUrls.entrySet()) {
            images.put(entry.getKey(), new ImageItem(entry.getValue()));
        }
    }

    public ImageMessage() {
    }

    public Map<String, ImageItem> getImages() {
        return images;
    }

    public void setImages(Map<String, ImageItem> images) {
        this.images = images;
    }
}
