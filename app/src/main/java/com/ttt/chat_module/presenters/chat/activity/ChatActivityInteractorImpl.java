package com.ttt.chat_module.presenters.chat.activity;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.models.ChatRoomInfo;
import com.ttt.chat_module.models.User;
import com.ttt.chat_module.models.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TranThanhTung on 12/03/2018.
 */

public class ChatActivityInteractorImpl implements ChatActivityInteractor {
    @Override
    public void onViewDestroy() {

    }

    @Override
    public void findChatRoom(String[] userIDs, OnGetChatRoomIDCompleteListener listener) {
        CollectionReference chatRoomCollectionRef = FirebaseFirestore.getInstance()
                .collection(Constants.CHAT_ROOMS_COLLECTION);
        for (String userID : userIDs) {
            chatRoomCollectionRef.whereEqualTo(ChatRoomInfo.USER_IDS + "." + userID, true);
        }
        chatRoomCollectionRef.get()
                .addOnSuccessListener(documentSnapshots -> {
                    List<DocumentSnapshot> documentSnapshotList = documentSnapshots.getDocuments();
                    if (documentSnapshotList.size() > 0) {
                        DocumentSnapshot documentSnapshot = documentSnapshotList.get(0);
                        listener.onGetChatRoomIDSuccess(documentSnapshot.getId());
                    } else {
                        listener.onGetChatRoomIDSuccess(null);
                    }
                })
                .addOnFailureListener(e -> listener.onGetChatRoomIDFailure(e.getMessage()));
    }

    @Override
    public void createChatRoom(String[] userIDs, OnGetChatRoomIDCompleteListener listener) {
        CollectionReference chatRoomCollectionRef = FirebaseFirestore.getInstance()
                .collection(Constants.CHAT_ROOMS_COLLECTION);
        DocumentReference newChatRoomDocumentRef = chatRoomCollectionRef.document();
        newChatRoomDocumentRef.set(new ChatRoomInfo(userIDs))
                .addOnSuccessListener(aVoid -> listener.onGetChatRoomIDSuccess(newChatRoomDocumentRef.getId()))
                .addOnFailureListener(e -> listener.onGetChatRoomIDFailure(e.getMessage()));
    }

    @Override
    public void getUsersInfo(String[] userIDs, OnGetUsersInfoCompleteListener listener) {
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection(Constants.USERS_COLLECTION);
        for (String userID : userIDs) {
            collectionReference.whereEqualTo(User.EMAIL, userID);
        }
        collectionReference.get()
                .addOnSuccessListener(documentSnapshots -> {
                    List<DocumentSnapshot> documentSnapshotList = documentSnapshots.getDocuments();
                    List<UserInfo> usersInfo = new ArrayList<>(documentSnapshotList.size());
                    for (DocumentSnapshot documentSnapshot : documentSnapshotList) {
                        usersInfo.add(documentSnapshot.toObject(UserInfo.class));
                    }
                    listener.onGetUsersInfoSuccess(usersInfo);
                })
                .addOnFailureListener(e -> listener.onServerError(e.getMessage()));
    }
}
