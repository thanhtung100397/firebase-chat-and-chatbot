package com.ttt.chat_module.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.ttt.chat_module.bus_event.UserChangeEvent;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.models.User;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserChangeListenerService extends Service {
    private IBinder binder = new UserInfoChangeListenerServiceBinder();
    private List<ListenerRegistration> userInfoListenerRegistrations;
    private Map<String, Boolean> mapUserOnlineState;

    public class UserInfoChangeListenerServiceBinder extends Binder {
        public UserChangeListenerService getService() {
            return UserChangeListenerService.this;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (ListenerRegistration userInfoListenerRegistration : userInfoListenerRegistrations) {
            userInfoListenerRegistration.remove();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        userInfoListenerRegistrations = new ArrayList<>();
        mapUserOnlineState = new HashMap<>();
        return START_STICKY;
    }

    public void registerUserInfoStateChange(String userID) {
        if(mapUserOnlineState.containsKey(userID)) {
            return;
        }
        ListenerRegistration userInfoListenerRegistration = FirebaseFirestore.getInstance().collection(Constants.USERS_COLLECTION)
                .document(userID)
                .addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        return;
                    }
                    User user = documentSnapshot.toObject(User.class);
                    mapUserOnlineState.put(user.getId(), user.getIsOnline());
                    EventBus.getDefault().post(new UserChangeEvent(user));
                });
        userInfoListenerRegistrations.add(userInfoListenerRegistration);
    }

    public boolean isOnline(String userID) {
        Boolean isOnline = mapUserOnlineState.get(userID);
        return isOnline == null? false : isOnline;
    }
}
