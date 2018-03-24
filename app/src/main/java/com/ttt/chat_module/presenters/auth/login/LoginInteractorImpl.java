package com.ttt.chat_module.presenters.auth.login;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.models.User;

/**
 * Created by TranThanhTung on 19/02/2018.
 */

public class LoginInteractorImpl implements LoginInteractor {
    @Override
    public void onViewDestroy() {

    }

    @Override
    public void login(String username, String password, OnLoginCompleteListener listener) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(username, password)
                .addOnSuccessListener(authResult -> goToOnlineState(username, listener))
                .addOnFailureListener(e -> listener.onError(e.getMessage()));
    }

    private void goToOnlineState(String userID, OnLoginCompleteListener listener) {
        FirebaseFirestore.getInstance().collection(Constants.USERS_COLLECTION)
                .document(userID)
                .update(User.IS_ONLINE, true)
                .addOnCompleteListener(task -> listener.onLoginSuccess())
                .addOnFailureListener(e -> listener.onError(e.getMessage()));
    }
}
