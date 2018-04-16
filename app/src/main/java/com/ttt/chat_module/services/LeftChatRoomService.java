package com.ttt.chat_module.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.models.ChatRoomInfo;
import com.ttt.chat_module.models.VisitState;

public class LeftChatRoomService extends Service {
    public static final String TAG = "ChangeInCRStateService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String roomID = intent.getStringExtra(Constants.KEY_ROOM_ID);
        String userID = intent.getStringExtra(Constants.KEY_USER_ID);
        String inRoomState = intent.getStringExtra(Constants.KEY_IN_ROOM_STATE);
        FirebaseFirestore.getInstance().collection(Constants.CHAT_ROOMS_COLLECTION)
                .document(roomID)
                .collection(ChatRoomInfo.VISIT_STATES)
                .document(userID)
                .set(new VisitState(inRoomState))
                .addOnSuccessListener(aVoid -> {})
                .addOnFailureListener(e -> {
                    Log.i(TAG, "onStartCommand: " + e.getMessage());
                });
        return START_NOT_STICKY;
    }
}
