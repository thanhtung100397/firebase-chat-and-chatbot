package com.ttt.chat_module.presenters.splash;

import com.google.firebase.firestore.FirebaseFirestore;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.models.User;
import com.ttt.chat_module.presenters.OnRequestCompleteListener;

/**
 * Created by TranThanhTung on 25/01/2018.
 */

public class SplashInteractorImpl implements SplashInteractor {

    @Override
    public void onViewDestroy() {

    }


    @Override
    public void updateUserOnlineState(String userID, boolean isOnline, OnRequestCompleteListener listener) {
        FirebaseFirestore.getInstance().collection(Constants.USERS_COLLECTION)
                .document(userID)
                .update(User.IS_ONLINE, isOnline)
                .addOnSuccessListener(aVoid -> {
                    if (listener != null) {
                        listener.onRequestSuccess();
                    }
                })
                .addOnFailureListener(e -> {
                    if (listener != null) {
                        listener.onRequestError(e.getMessage());
                    }
                });
    }
}
