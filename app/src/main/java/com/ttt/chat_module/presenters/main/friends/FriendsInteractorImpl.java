package com.ttt.chat_module.presenters.main.friends;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.common.utils.UserAuth;
import com.ttt.chat_module.models.User;
import com.ttt.chat_module.models.UserInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public class FriendsInteractorImpl implements FriendsInteractor {

    @Override
    public void onViewDestroy() {

    }

    @Override
    public void getFriends(DocumentSnapshot startAt, int pageSize, OnGetFriendsCompleteListener listener) {
        Query friendQuery = FirebaseFirestore.getInstance().collection(Constants.USERS_COLLECTION)
                .orderBy(User.FIRST_NAME, Query.Direction.ASCENDING)
                .orderBy(User.EMAIL, Query.Direction.ASCENDING)
                .limit(pageSize);

        if (startAt != null) {
            friendQuery = friendQuery.startAfter(startAt);
        }

        friendQuery.get()
                .addOnSuccessListener(documentSnapshots -> {
                    String currentUserID = UserAuth.getUserID();
                    List<DocumentSnapshot> listDocumentSnapshots = documentSnapshots.getDocuments();
                    List<UserInfo> usersInfo = new ArrayList<>(listDocumentSnapshots.size());
                    Map<String, Integer> userPositionMap = new HashMap<>(listDocumentSnapshots.size());
                    for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                        UserInfo userInfo = documentSnapshot.toObject(UserInfo.class);
                        if (!userInfo.getId().equals(currentUserID)) {
                            userPositionMap.put(userInfo.getId(), usersInfo.size());
                            usersInfo.add(userInfo);
                        }
                    }
                    listener.onGetFriendsSuccess(userPositionMap, usersInfo);
                    int size = listDocumentSnapshots.size();
                    if (size == 1) {
                        listener.onLastElementFetched(null, true);
                    } else {
                        listener.onLastElementFetched(documentSnapshots.getDocuments().get(size - 1), size < pageSize);
                    }
                })
                .addOnFailureListener(e -> listener.onRequestError(e.getMessage()));
    }
}
