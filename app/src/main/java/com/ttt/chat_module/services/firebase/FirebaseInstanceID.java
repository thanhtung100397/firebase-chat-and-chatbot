package com.ttt.chat_module.services.firebase;

import android.annotation.SuppressLint;

import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by TranThanhTung on 12/7/2017.
 */

public class FirebaseInstanceID extends FirebaseInstanceIdService {

    @SuppressLint("CheckResult")
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (firebaseUser != null) {
//            String userNotificationKey = UserAuth.getUserNotificationKey(this);
//            Observable<FirebaseDeviceGroupApiResponse> firebaseDeviceGroupApiResponseObservable;
//            if (userNotificationKey == null) {
//                firebaseDeviceGroupApiResponseObservable = Utils.createUserNotificationKey(this, firebaseUser.getUid(), refreshedToken);
//            } else {
//                firebaseDeviceGroupApiResponseObservable = Utils.addUSerNotificationKey(this, firebaseUser.getUid(), refreshedToken);
//            }
//            firebaseDeviceGroupApiResponseObservable.subscribeWith(new ResponseObserver<FirebaseDeviceGroupApiResponse>() {
//                @Override
//                public void onSuccess(FirebaseDeviceGroupApiResponse response) {
//                    UserAuth.saveUserNotificationKey(FirebaseInstanceID.this, response.getNotificationKey());
//                    Map<String, String> userFCMToken = new HashMap<>();
//                    userFCMToken.put(User.FCM_TOKEN, refreshedToken);
//                    FirebaseFirestore.getInstance().collection(Constants.USERS_COLLECTION)
//                            .document(firebaseUser.getUid())
//                            .set(userFCMToken, SetOptions.merge());
//                }
//
//                @Override
//                public void onServerError(String message) {
//
//                }
//
//                @Override
//                public void onNetworkConnectionError() {
//
//                }
//            });
//        }
    }


}

