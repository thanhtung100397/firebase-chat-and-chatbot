package com.ttt.chat_module.presenters.main.friends;

import com.google.firebase.firestore.FirebaseFirestore;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.models.User;

import java.util.List;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public class FriendsInteractorImpl implements FriendsInteractor {
    @Override
    public void onViewDestroy() {

    }

    @Override
    public void getFriends(int pageIndex, int pageSize, OnGetFriendsCompleteListener listener) {
        FirebaseFirestore.getInstance().collection(Constants.USERS_COLLECTION)
                .orderBy(User.FULL_NAME)
                .orderBy(User.EMAIL)
                .startAt(pageIndex * pageSize)
                .limit(pageSize)
                .get()
                .addOnSuccessListener(documentSnapshots -> {
                    List<User> users = documentSnapshots.toObjects(User.class);
                    listener.onGetFriendsSuccess(users);
                })
                .addOnFailureListener(e -> listener.onError(e.getMessage()));
    }
}
