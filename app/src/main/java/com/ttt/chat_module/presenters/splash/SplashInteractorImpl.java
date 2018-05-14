package com.ttt.chat_module.presenters.splash;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.models.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by TranThanhTung on 25/01/2018.
 */

public class SplashInteractorImpl implements SplashInteractor {

    @Override
    public void onViewDestroy() {

    }


    @Override
    public void updateUserOnlineStateAndFetchUser(String userID, boolean isOnline, OnGetUserCompleteListener listener) {
        DocumentReference userRef = FirebaseFirestore.getInstance()
                .collection(Constants.USERS_COLLECTION).document(userID);
        FirebaseFirestore.getInstance().runTransaction(transaction -> {
            User user = transaction.get(userRef).toObject(User.class);
            Map<String, Object> onlineStateMap = new HashMap<>(1);
            onlineStateMap.put(User.IS_ONLINE, true);
            transaction.set(userRef, onlineStateMap, SetOptions.merge());
            return user;
        }).addOnSuccessListener(listener::onGetUserSuccess)
                .addOnFailureListener(e -> listener.onRequestError(e.getMessage()));
    }
}
