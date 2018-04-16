package com.ttt.chat_module.presenters.main;

import android.util.Log;

import com.ttt.chat_module.bus_event.ChatRoomLastMessageChangeEvent;
import com.ttt.chat_module.bus_event.UserOnlineStateChangeEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

public class MainActivityPresenterImpl implements MainActivityPresenter {
    public static final String TAG = "MainActPresenterImpl";
    private MainActivityInteractor mainActivityInteractor;

    public MainActivityPresenterImpl() {
        this.mainActivityInteractor = new MainActivityInteractorImpl();
    }

    @Override
    public void onViewDestroy() {
        mainActivityInteractor.onViewDestroy();
    }

    @Override
    public void registerUsersOnlineStateChangeListener() {
        mainActivityInteractor.registerUsersOnlineStateChangeListener(new OnUsersOnlineStateChangeListener() {
            @Override
            public void onUserOnlineStateChanged(String usedID, boolean isOnline) {
                EventBus.getDefault().post(new UserOnlineStateChangeEvent(usedID, isOnline));
            }

            @Override
            public void onRequestError(String message) {
                Log.e(TAG, "onRequestError: " + message);
            }
        });
    }

    @Override
    public void unregisterUsersOnlineStateChangeListener() {
        mainActivityInteractor.unregisterUsersOnlineStateChangeListener();
    }

    @Override
    public void registerChatRoomLastMessageChangeListener() {
        mainActivityInteractor.registerChatRoomLastMessageChangeListener(new OnChatRoomLastMessageChangeListener() {
            @Override
            public void onChatRoomLastMessageChanged(String roomID, Map<String, Object> lastMessage) {
                EventBus.getDefault().post(new ChatRoomLastMessageChangeEvent(roomID, lastMessage));
            }

            @Override
            public void onRequestError(String message) {
                Log.e(TAG, "onRequestError: " + message);
            }
        });
    }

    @Override
    public void unregisterChatRoomLastMessageChangeListener() {
        mainActivityInteractor.unregisterChatRoomLastMessageChangeListener();
    }
}
