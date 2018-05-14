package com.ttt.chat_module.presenters.user_profile;

import com.google.firebase.firestore.FirebaseFirestore;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.models.User;
import com.ttt.chat_module.presenters.splash.OnGetUserCompleteListener;

public class UserProfileInteractorImpl implements UserProfileInteractor {
    @Override
    public void onViewDestroy() {

    }

    @Override
    public void getUser(String userID, OnGetUserCompleteListener listener) {
        FirebaseFirestore.getInstance().collection(Constants.USERS_COLLECTION)
                .document(userID)
                .get()
                .addOnSuccessListener(documentSnapshot -> listener.onGetUserSuccess(documentSnapshot.toObject(User.class)))
                .addOnFailureListener(e -> listener.onRequestError(e.getMessage()));
    }
}
