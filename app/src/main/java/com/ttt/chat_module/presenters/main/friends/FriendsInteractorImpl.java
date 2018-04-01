package com.ttt.chat_module.presenters.main.friends;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.common.utils.UserAuth;
import com.ttt.chat_module.models.User;

import java.util.ArrayList;
import java.util.List;

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

        if(startAt != null) {
            friendQuery = friendQuery.startAfter(startAt);
        }

        friendQuery.get()
                .addOnSuccessListener(documentSnapshots -> {
                    String currentUserID = UserAuth.getUserID();
                    List<DocumentSnapshot> listDocumentSnapshots = documentSnapshots.getDocuments();
                    List<User> users = new ArrayList<>(listDocumentSnapshots.size());
                    for (DocumentSnapshot documentSnapshot : listDocumentSnapshots) {
                        User user = documentSnapshot.toObject(User.class);
                        if (!user.getId().equals(currentUserID)) {
                            users.add(user);
                        }
                    }
                    listener.onGetFriendsSuccess(users);
                    int size = listDocumentSnapshots.size();
                    if(size == 1) {
                        listener.onLastElementFetched(null, true);
                    } else {
                        listener.onLastElementFetched(documentSnapshots.getDocuments().get(size - 1), size < pageSize);
                    }
                })
                .addOnFailureListener(e -> listener.onRequestError(e.getMessage()));
    }
}
