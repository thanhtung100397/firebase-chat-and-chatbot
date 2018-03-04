package com.ttt.chat_module.presenters.chat;

import android.content.Context;
import android.util.Log;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.SetOptions;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.common.adapter.recycler_view_adapter.Message;
import com.ttt.chat_module.common.utils.UserAuth;
import com.ttt.chat_module.presenters.OnRequestCompleteListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public class ChatInteractorImpl implements ChatInteractor {
    private Context context;
    private String roomID;
    private ListenerRegistration messageChangeListenerRegistration;

    public ChatInteractorImpl(Context context, String roomID) {
        this.context = context;
        this.roomID = roomID;
    }

    @Override
    public void onViewDestroy() {
        context = null;
    }

    @Override
    public void sendMessage(String message, OnRequestCompleteListener listener) {
        Message objectMessage = new Message(UserAuth.getUserID(context), message);
        Map<String, Message> messageMap = new HashMap<>();
        messageMap.put(Constants.MESSAGES_COLLECTIONS, objectMessage);
        FirebaseFirestore.getInstance().collection(Constants.COUPLE_ROOMS_COLLECTION)
                .document(roomID)
                .set(messageMap, SetOptions.merge())
                .addOnSuccessListener(success -> listener.onRequestSuccess())
                .addOnFailureListener(error -> listener.onRequestError(error.getMessage()));
    }

    @Override
    public void registerOnMessageChangedListener(OnMessageChangedListener listener) {
        messageChangeListenerRegistration = FirebaseFirestore.getInstance().collection(Constants.COUPLE_ROOMS_COLLECTION)
                .document(roomID)
                .collection(Constants.MESSAGES_COLLECTIONS)
                .addSnapshotListener((documentSnapshots, e) -> {
                    if (e != null) {
                        return;
                    }
                    for (DocumentChange documentChange : documentSnapshots.getDocumentChanges()) {
                        switch (documentChange.getType()) {
                            case ADDED:{
                                listener.onMessageAdded(documentChange.getDocument().toObject(Message.class));
                            }
                            break;

                            case MODIFIED:{
                                listener.onMessageModified(documentChange.getDocument().toObject(Message.class));
                            }
                            break;

                            default:{
                                break;
                            }
                        }
                    }
                });
    }

    public void unregisterOnMessageChangedListener() {
        messageChangeListenerRegistration.remove();
    }
}
