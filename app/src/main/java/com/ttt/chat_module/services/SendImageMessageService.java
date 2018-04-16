package com.ttt.chat_module.services;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.common.utils.FirebaseUploadImageHelper;
import com.ttt.chat_module.models.ChatRoomInfo;
import com.ttt.chat_module.models.message_models.ImageMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ttt.chat_module.bus_event.ImageItemStartUploadingEvent;
import com.ttt.chat_module.bus_event.ImageItemUploadFailureEvent;
import com.ttt.chat_module.bus_event.ImageItemUploadSuccessEvent;
import com.ttt.chat_module.bus_event.SendImageMessageFailureEvent;
import com.ttt.chat_module.bus_event.SendImageMessageSuccessEvent;
import com.ttt.chat_module.models.message_models.TextMessage;
import com.ttt.chat_module.models.wrapper_model.LastMessageWrapper;

public class SendImageMessageService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String roomID = intent.getStringExtra(Constants.KEY_IMAGE_FOLDER);
        String folderName = roomID + "_" + (new Date().getTime());
        String ownerID = intent.getStringExtra(Constants.KEY_OWNER_ID);
        List<String> urisString = intent.getStringArrayListExtra(Constants.KEY_IMAGE_URIS);
        int size = urisString.size();
        if (size != 0) {
            Map<String, Uri> mapUris = new HashMap<>(urisString.size());
            for (int i = 0; i < size; i++) {
                mapUris.put(i + "", Uri.parse(urisString.get(i)));
            }
            FirebaseUploadImageHelper.uploadImagesToStorage(folderName, mapUris,
                    (successKey, url) -> {
                        EventBus.getDefault().post(new ImageItemUploadSuccessEvent(roomID, Integer.parseInt(successKey), url));
                    },
                    (nextKey) -> {
                        EventBus.getDefault().post(new ImageItemStartUploadingEvent(roomID, Integer.parseInt(nextKey)));
                    },
                    (failureKey, e) -> {
                        EventBus.getDefault().post(new ImageItemUploadFailureEvent(roomID, Integer.parseInt(failureKey)));
                    },
                    (mapUrls) -> {
//                        EventBus.getDefault().post(new AllImageItemsUploadCompleteEvent(roomID, mapUrls));
                        sendImageMessage(roomID, ownerID, mapUrls);
                    }, null);

        } else {
            stopSelf();
        }
        return START_NOT_STICKY;
    }

    private void sendImageMessage(String roomID, String ownerID, Map<String, String> imageUrls) {
        ImageMessage imageMessage = new ImageMessage(ownerID, imageUrls);
        DocumentReference chatRoomRef = FirebaseFirestore.getInstance().collection(Constants.CHAT_ROOMS_COLLECTION)
                .document(roomID);

        WriteBatch writeBatch = FirebaseFirestore.getInstance().batch();
        writeBatch.set(chatRoomRef.collection(ChatRoomInfo.MESSAGES).document(), imageMessage);
        writeBatch.set(chatRoomRef, new LastMessageWrapper(imageMessage), SetOptions.merge());
        writeBatch.commit()
                .addOnSuccessListener(documentReference -> EventBus.getDefault().post(new SendImageMessageSuccessEvent(roomID)))
                .addOnFailureListener(e -> {
                    EventBus.getDefault().post(new SendImageMessageFailureEvent(roomID, imageMessage));
                });
    }
}
