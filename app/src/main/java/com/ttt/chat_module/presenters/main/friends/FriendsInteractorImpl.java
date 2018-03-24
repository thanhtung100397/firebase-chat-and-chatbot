package com.ttt.chat_module.presenters.main.friends;

import android.content.Context;

import com.google.firebase.firestore.FirebaseFirestore;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.common.utils.UserAuth;
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
                .orderBy(User.FIRST_NAME)
                .orderBy(User.EMAIL)
                .startAt(pageIndex * pageSize)
                .limit(pageSize)
                .get()
                .addOnSuccessListener(documentSnapshots -> {
                    List<User> users = documentSnapshots.toObjects(User.class);
                    String userID = UserAuth.getUserID();
                    for (int i = users.size() - 1; i >= 0; i--) {
                        User user = users.get(i);
                        if(user.getEmail().equals(userID)) {
                            users.remove(i);
                            break;
                        }
                    }
                    listener.onGetFriendsSuccess(users);
                })
                .addOnFailureListener(e -> listener.onError(e.getMessage()));
    }
}
