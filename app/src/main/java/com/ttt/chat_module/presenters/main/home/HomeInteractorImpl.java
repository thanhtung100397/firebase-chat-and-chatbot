package com.ttt.chat_module.presenters.main.home;

import android.content.Context;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.models.ChatRoom;
import com.ttt.chat_module.models.ChatRoomInfo;
import com.ttt.chat_module.models.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class HomeInteractorImpl implements HomeInteractor {
    private Context context;
    private ListenerRegistration chatRoomInfoListenerRegistration;

    public HomeInteractorImpl(Context context) {
        this.context = context;
    }

    @Override
    public void onViewDestroy() {

    }

    @Override
    public void getChatRooms(ChatRoomInfo startAt, int pageSize,
                             String userID,
                             OnGetChatRoomsCompleteListener listener) {
        Query chatRoomQuery = FirebaseFirestore.getInstance()
                .collection(Constants.CHAT_ROOMS_COLLECTION)
                .whereEqualTo(ChatRoomInfo.USERS_INFO + "." + userID + "." + UserInfo.ID, userID)
                .limit(pageSize);
        if (startAt != null) {
            chatRoomQuery = chatRoomQuery.startAfter(startAt);
        }
        chatRoomQuery.get().addOnSuccessListener(documentSnapshots -> {
            List<DocumentSnapshot> chatRoomSnapshots = documentSnapshots.getDocuments();
            int size = chatRoomSnapshots.size();
            List<ChatRoom> chatRooms = new ArrayList<>(size);
            ChatRoomInfo chatRoomInfo = null;
            for (DocumentSnapshot chatRoomSnapshot : chatRoomSnapshots) {
                chatRoomInfo = chatRoomSnapshot.toObject(ChatRoomInfo.class);
                if (chatRoomInfo.getLastMessage() == null) {
                    continue;
                }
                chatRooms.add(new ChatRoom(context, chatRoomInfo, chatRoomInfo.getLastMessage()));
            }
            listener.onGetChatRoomsSuccess(chatRooms);
            listener.onLastElementFetched(chatRoomInfo, size < pageSize);
        }).addOnFailureListener(e -> {
            listener.onRequestError(e.getMessage());
        });
    }

    @Override
    public void registerChatRoomsChangeListener(String userID, int limit, OnChatRoomChangeListener listener) {
        chatRoomInfoListenerRegistration = FirebaseFirestore.getInstance()
                .collection(Constants.CHAT_ROOMS_COLLECTION)
                .whereEqualTo(ChatRoomInfo.USERS_INFO + "." + userID + "." + UserInfo.ID, userID)
                .limit(limit)
                .addSnapshotListener((documentSnapshots, e) -> {
                    if(e != null) {
                        listener.onRequestError(e.getMessage());
                        return;
                    }
                    List<DocumentChange> documentChanges = documentSnapshots.getDocumentChanges();
                    for (DocumentChange documentChange : documentChanges) {
                        switch (documentChange.getType()){
                            case MODIFIED:{
                                listener.onChatRoomChanged(documentChange.getDocument().toObject(ChatRoomInfo.class));
                            }
                            break;

                            default:{
                                break;
                            }
                        }
                    }
                });
    }

    @Override
    public void unregisterChatRoomsChangeListener() {
        if (chatRoomInfoListenerRegistration != null) {
            chatRoomInfoListenerRegistration.remove();
        }
    }
}
