package com.ttt.chat_module.services;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.common.utils.FirebaseUploadImageHelper;
import com.ttt.chat_module.models.message_models.ImageMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bus_event.AllImageItemsUploadCompleteEvent;
import bus_event.ImageItemStartUploadingEvent;
import bus_event.ImageItemUploadFailureEvent;
import bus_event.ImageItemUploadSuccessEvent;
import bus_event.SendImageMessageFailureEvent;

public class SendImageMessageService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String folderName = intent.getStringExtra(Constants.KEY_IMAGE_FOLDER);
        String ownerID = intent.getStringExtra(Constants.KEY_OWNER_ID);
        List<String> urisString = intent.getStringArrayListExtra(Constants.KEY_IMAGE_URIS);
        int size = urisString.size();
        if (size != 0) {
            Map<String, Uri> mapUris = new HashMap<>(urisString.size());
            for (int i = 0; i < size; i++) {
                mapUris.put(i + "", Uri.parse(urisString.get(i)));
            }
            FirebaseUploadImageHelper.uploadImagesToStorage(folderName, mapUris,
                    (roomID, successKey, url) -> {
                        EventBus.getDefault().post(new ImageItemUploadSuccessEvent(roomID, Integer.parseInt(successKey), url));
                    },
                    (roomID, nextKey) -> {
                        EventBus.getDefault().post(new ImageItemStartUploadingEvent(roomID, Integer.parseInt(nextKey)));
                    },
                    (roomID, failureKey, e) -> {
                        EventBus.getDefault().post(new ImageItemUploadFailureEvent(roomID, Integer.parseInt(failureKey)));
                    },
                    (roomID, mapUrls) -> {
                        EventBus.getDefault().post(new AllImageItemsUploadCompleteEvent(roomID, mapUrls));
                    }, null);

        } else {
            stopSelf();
        }
        return START_NOT_STICKY;
    }

    private void sendImageMessage(String roomID, String ownerID, Map<String, String> imageUrls) {
        ImageMessage imageMessage = new ImageMessage(ownerID, imageUrls);
        FirebaseFirestore.getInstance().collection(Constants.CHAT_ROOMS_COLLECTION)
                .document(roomID)
                .collection(Constants.MESSAGES_COLLECTIONS)
                .add(imageMessage)
                .addOnFailureListener(e -> {
                    EventBus.getDefault().post(new SendImageMessageFailureEvent(imageMessage));
                });
    }
}
