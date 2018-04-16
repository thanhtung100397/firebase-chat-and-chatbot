package com.ttt.chat_module.presenters.main.home;

import android.content.Context;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.models.ChatRoom;
import com.ttt.chat_module.models.ChatRoomInfo;
import com.ttt.chat_module.models.UserInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeInteractorImpl implements HomeInteractor {
    private Context context;

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
            Map<String, Integer> roomPositionMap = new HashMap<>(size);
            ChatRoomInfo chatRoomInfo = null;
            for (DocumentSnapshot chatRoomSnapshot : chatRoomSnapshots) {
                chatRoomInfo = chatRoomSnapshot.toObject(ChatRoomInfo.class);
                Map<String, Object> lastMessageMap = (Map<String, Object>) chatRoomSnapshot.get(ChatRoomInfo.LAST_MESSAGE);
                if (lastMessageMap == null) {
                    continue;
                }
                roomPositionMap.put(chatRoomSnapshot.getId(), chatRooms.size());
                chatRooms.add(new ChatRoom(context, chatRoomInfo, lastMessageMap));
            }
            listener.onGetChatRoomsSuccess(roomPositionMap, chatRooms);
            listener.onLastElementFetched(chatRoomInfo, size < pageSize);
        }).addOnFailureListener(e -> {
            listener.onRequestError(e.getMessage());
        });
    }
}
