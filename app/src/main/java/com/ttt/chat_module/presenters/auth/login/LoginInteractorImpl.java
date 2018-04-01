package com.ttt.chat_module.presenters.auth.login;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.models.User;

import java.util.HashMap;
import java.util.Map;

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
                .addOnSuccessListener(authResult -> goToOnlineState(authResult.getUser().getUid(), listener))
                .addOnFailureListener(e -> listener.onRequestError(e.getMessage()));
    }

    private void goToOnlineState(String userID, OnLoginCompleteListener listener) {
        DocumentReference userRef = FirebaseFirestore.getInstance()
                .collection(Constants.USERS_COLLECTION)
                .document(userID);

        FirebaseFirestore.getInstance().runTransaction(transaction -> {
            User user = transaction.get(userRef).toObject(User.class);
            Map<String, Object> onlineState = new HashMap<>();
            onlineState.put(User.IS_ONLINE, true);
            transaction.update(userRef, onlineState);
            return user;
        })
        .addOnSuccessListener(listener::onLoginSuccess)
        .addOnFailureListener(e -> listener.onRequestError(e.getMessage()));

//        FirebaseFirestore.getInstance().collection(Constants.USERS_COLLECTION)
//                .document(userID)
//                .update(User.IS_ONLINE, true)
//                .addOnCompleteListener(task -> listener.onLoginSuccess())
//                .addOnFailureListener(e -> listener.onRequestError(e.getMessage()));
    }
}
