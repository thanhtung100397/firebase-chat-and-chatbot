package com.ttt.chat_module.presenters.auth.login;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.models.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by TranThanhTung on 19/02/2018.
 */

public class LoginInteractorImpl implements LoginInteractor {
    private Context context;

    public LoginInteractorImpl(Context context) {
        this.context = context;
    }

    @Override
    public void onViewDestroy() {

    }

    @Override
    public void login(String username, String password, OnLoginCompleteListener listener) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(username, password)
                .addOnSuccessListener(authResult -> updateUserData(authResult.getUser().getUid(), listener))
                .addOnFailureListener(e -> listener.onRequestError(e.getMessage()));
    }

    private void updateUserData(String userID, OnLoginCompleteListener listener) {
        DocumentReference userRef = FirebaseFirestore.getInstance()
                .collection(Constants.USERS_COLLECTION)
                .document(userID);
        FirebaseFirestore.getInstance().runTransaction(transaction -> {
            User user = transaction.get(userRef).toObject(User.class);
            Map<String, Object> onlineStateData = new HashMap<>(1);
            onlineStateData.put(User.IS_ONLINE, true);
            transaction.set(userRef, onlineStateData, SetOptions.merge());
            return user;
        }).addOnSuccessListener(user -> {
            FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();
            firebaseMessaging.subscribeToTopic(userID);
            listener.onLoginSuccess(user);
        }).addOnFailureListener(e -> listener.onRequestError(e.getMessage()));
    }

//    @SuppressLint("CheckResult")
//    private void updateUserData(String userID, OnLoginCompleteListener listener) {
//        DocumentReference userRef = FirebaseFirestore.getInstance()
//                .collection(Constants.USERS_COLLECTION)
//                .document(userID);
//        userRef.get().addOnSuccessListener(documentSnapshot -> {
//            Map<String, Object> updatedData = new HashMap<>();
//            User user = documentSnapshot.toObject(User.class);
//            String userFCMToken = user.getFcmToken();
//            Observable<FirebaseDeviceGroupApiResponse> firebaseDeviceGroupApiResponseObservable = null;
//            if (userFCMToken == null) {
//                firebaseDeviceGroupApiResponseObservable = Utils.createUserNotificationKey(context, userID, FirebaseInstanceId.getInstance().getToken());
//            } else {
//                String currentFcmToken = FirebaseInstanceId.getInstance().getToken();
//                if (currentFcmToken != null && !userFCMToken.equals(currentFcmToken)) {
//                    firebaseDeviceGroupApiResponseObservable = Utils.addUSerNotificationKey(context, userID, currentFcmToken);
//                }
//            }
//            if (firebaseDeviceGroupApiResponseObservable != null) {
//                firebaseDeviceGroupApiResponseObservable.subscribeWith(new ResponseObserver<FirebaseDeviceGroupApiResponse>() {
//                    @Override
//                    public void onSuccess(FirebaseDeviceGroupApiResponse response) {
//                        updatedData.put(User.IS_ONLINE, true);
//                        updatedData.put(User.FCM_TOKEN, userFCMToken);
//                        saveUserData(userRef, user, updatedData, listener);
//                    }
//
//                    @Override
//                    public void onServerError(String message) {
//                        listener.onRequestError(message);
//                    }
//
//                    @Override
//                    public void onNetworkConnectionError() {
//
//                    }
//                });
//            } else {
//                updatedData.put(User.IS_ONLINE, true);
//                saveUserData(userRef, user, updatedData, listener);
//            }
//        }).addOnFailureListener(e -> listener.onRequestError(e.getMessage()));
//    }

//    private void saveUserData(DocumentReference userRef, User user,
//                              Map<String, Object> data, OnLoginCompleteListener listener) {
//        userRef.set(data, SetOptions.merge())
//                .addOnCompleteListener(task -> listener.onLoginSuccess(user))
//                .addOnFailureListener(e -> listener.onRequestError(e.getMessage()));
//    }
}
