package com.ttt.chat_module.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.models.ChatRoomInfo;
import com.ttt.chat_module.models.message_models.BaseMessage;

import java.util.List;

public class SeenMessagesService extends Service {
    private IBinder binder = new SeenMessagesServiceBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class SeenMessagesServiceBinder extends Binder {
        public SeenMessagesService getService() {
            return SeenMessagesService.this;
        }
    }

    public void seenAllMessages(String roomID, String userID, List<String> unseenMessageIDs, boolean hasLastMessage) {
        WriteBatch writeBatch = FirebaseFirestore.getInstance().batch();
        DocumentReference chatRoomRef = FirebaseFirestore.getInstance()
                .collection(Constants.CHAT_ROOMS_COLLECTION)
                .document(roomID);
        seenAllMessages(writeBatch, 0, chatRoomRef, userID, unseenMessageIDs, hasLastMessage);
    }

    private void seenAllMessages(WriteBatch writeBatch, int start,
                                 DocumentReference chatRoomRef, String userID,
                                 List<String> unseenMessageIDs,
                                 boolean hasLastMessage) {
        int end;
        if (unseenMessageIDs.size() < 450) {//firestore batch write is only support <= 500 operation per commit, take 450
            end = start + unseenMessageIDs.size();
            if (hasLastMessage) {
                writeBatch.update(chatRoomRef, ChatRoomInfo.LAST_MESSAGE + "." + BaseMessage.SEEN_BY + "." + userID, true);
            }
        } else {
            end = start + 450;
        }
        CollectionReference messagesRef = chatRoomRef.collection(ChatRoomInfo.MESSAGES);
        for (int i = start; i < end; i++) {
            writeBatch.update(messagesRef.document(unseenMessageIDs.get(i)), BaseMessage.SEEN_BY + "." + userID, true);
        }
        writeBatch.commit().addOnSuccessListener(aVoid -> {
            if (end < unseenMessageIDs.size()) {
                seenAllMessages(writeBatch, end, chatRoomRef, userID, unseenMessageIDs, hasLastMessage);
            }
        }).addOnFailureListener(e -> {
        });
    }
}
