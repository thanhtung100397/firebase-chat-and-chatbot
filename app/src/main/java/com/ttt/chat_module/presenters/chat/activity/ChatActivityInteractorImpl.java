package com.ttt.chat_module.presenters.chat.activity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.models.ChatRoomInfo;
import com.ttt.chat_module.models.TypingState;
import com.ttt.chat_module.models.UserInfo;

import java.util.List;

/**
 * Created by TranThanhTung on 12/03/2018.
 */

public class ChatActivityInteractorImpl implements ChatActivityInteractor {
    @Override
    public void onViewDestroy() {

    }

    @Override
    public void getChatRoomInfo(String chatRoomID, OnGetChatRoomInfoCompleteListener listener) {
        FirebaseFirestore.getInstance().collection(Constants.CHAT_ROOMS_COLLECTION)
                .document(chatRoomID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    listener.onGetChatRoomInfoSuccess(documentSnapshot.toObject(ChatRoomInfo.class));
                })
                .addOnFailureListener(e -> listener.onRequestError(e.getMessage()));
    }

    @Override
    public void findChatRoom(List<UserInfo> usersInfo, OnGetChatRoomInfoCompleteListener listener) {
        Query chatRoomQuery = FirebaseFirestore.getInstance()
                .collection(Constants.CHAT_ROOMS_COLLECTION);
        for (UserInfo userInfo : usersInfo) {
            String userID = userInfo.getId();
            chatRoomQuery = chatRoomQuery.whereEqualTo(ChatRoomInfo.USERS_INFO + "." + userID + "." + UserInfo.ID, userID);
        }
        chatRoomQuery.get()
                .addOnSuccessListener(documentSnapshots -> {
                    List<DocumentSnapshot> documentSnapshotList = documentSnapshots.getDocuments();
                    if (documentSnapshotList.size() > 0) {
                        DocumentSnapshot documentSnapshot = documentSnapshotList.get(0);
                        listener.onGetChatRoomInfoSuccess(documentSnapshot.toObject(ChatRoomInfo.class));
                    } else {
                        listener.onGetChatRoomInfoSuccess(null);
                    }
                })
                .addOnFailureListener(e -> listener.onRequestError(e.getMessage()));
    }

    @Override
    public void createChatRoom(List<UserInfo> usersInfo, OnGetChatRoomInfoCompleteListener listener) {
        DocumentReference newChatRoomDocRef = FirebaseFirestore.getInstance()
                .collection(Constants.CHAT_ROOMS_COLLECTION)
                .document();
        ChatRoomInfo chatRoomInfo = new ChatRoomInfo(newChatRoomDocRef.getId(), usersInfo);
        FirebaseFirestore.getInstance().runTransaction(transaction -> {
            transaction.set(newChatRoomDocRef, chatRoomInfo);

            CollectionReference typingStatesCollectionRef = newChatRoomDocRef.collection(ChatRoomInfo.TYPING_STATES);
            for (UserInfo userInfo : usersInfo) {
                transaction.set(typingStatesCollectionRef.document(userInfo.getId()), new TypingState());
            }
            return chatRoomInfo;
        }).addOnSuccessListener(aVoid -> listener.onGetChatRoomInfoSuccess(chatRoomInfo))
                .addOnFailureListener(e -> listener.onRequestError(e.getMessage()));
    }
}
