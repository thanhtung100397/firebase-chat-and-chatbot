package com.ttt.chat_module.presenters.main.home;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.ttt.chat_module.R;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.common.adapter.recycler_view_adapter.UserInfoChatRooms;
import com.ttt.chat_module.common.utils.ToastUtils;
import com.ttt.chat_module.common.utils.UserAuth;
import com.ttt.chat_module.models.ChatRoom;
import com.ttt.chat_module.models.ChatRoomInfo;
import com.ttt.chat_module.models.UserInfo;
import com.ttt.chat_module.services.UserChangeListenerService;
import com.ttt.chat_module.views.main.home.HomeView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TranThanhTung on 19/02/2018.
 */

public class HomePresenterImpl implements HomePresenter {
    private static final String TAG = "HomePresenterImpl";

    private Context context;
    private HomeInteractor homeInteractor;
    private HomeView homeView;
    private int pageNumber;
    private ChatRoomInfo lastChatRoomInfo;
    private ServiceConnection userInfoChangeListenerServiceConnection;
    private UserChangeListenerService userChangeListenerServiceInstance;

    public HomePresenterImpl(Context context, HomeView homeView) {
        this.context = context;
        this.homeView = homeView;
        this.homeInteractor = new HomeInteractorImpl(context);
    }

    @Override
    public void onViewDestroy() {
        this.homeInteractor.onViewDestroy();
    }

    @Override
    public void refreshChatRooms() {
        homeView.showRefreshingProgress();
        homeView.enableLoadingMore(false);
        String userID = UserAuth.getUserID();
        homeInteractor.getChatRooms(null, Constants.PAGE_SIZE, userID,
                new OnGetChatRoomsCompleteListener() {
                    @Override
                    public void onGetChatRoomsSuccess(List<ChatRoom> chatRooms) {
                        pageNumber = 1;
                        homeView.hideRefreshingProgress();
                        Map<String, UserInfoChatRooms> userInfoChatRoomsMap = registerUserInfoChangeListener(chatRooms);
                        homeView.refreshChatRooms(userInfoChatRoomsMap, chatRooms);
                        reRegisterChatRoomChangeListener(userID, Constants.PAGE_SIZE);
                    }

                    @Override
                    public void onLastElementFetched(ChatRoomInfo element, boolean hasNoElementLeft) {
                        lastChatRoomInfo = element;
                        homeView.enableLoadingMore(!hasNoElementLeft);
                    }

                    @Override
                    public void onRequestError(String message) {
                        homeView.hideLoadingMoreProgress();
                        homeView.enableLoadingMore(true);
                        ToastUtils.quickToast(context, R.string.unexpected_error_occurred);
                    }
                });
    }

    @Override
    public void loadMoreChatRooms() {
        homeView.showLoadingMoreProgress();
        homeView.enableRefreshing(false);
        String userID = UserAuth.getUserID();
        homeInteractor.getChatRooms(lastChatRoomInfo, Constants.PAGE_SIZE, userID,
                new OnGetChatRoomsCompleteListener() {
                    @Override
                    public void onGetChatRoomsSuccess(List<ChatRoom> chatRooms) {
                        pageNumber++;
                        homeView.hideLoadingMoreProgress();
                        Map<String, UserInfoChatRooms> userInfoChatRoomsMap = registerUserInfoChangeListener(chatRooms);
                        homeView.addMoreChatRooms(userInfoChatRoomsMap, chatRooms);
                        reRegisterChatRoomChangeListener(userID, pageNumber * Constants.PAGE_SIZE);
                    }

                    @Override
                    public void onLastElementFetched(ChatRoomInfo element, boolean hasNoElementLeft) {
                        lastChatRoomInfo = element;
                        homeView.enableRefreshing(true);
                        homeView.enableLoadingMore(!hasNoElementLeft);
                    }

                    @Override
                    public void onRequestError(String message) {
                        homeView.hideLoadingMoreProgress();
                        homeView.enableRefreshing(true);
                        ToastUtils.quickToast(context, R.string.unexpected_error_occurred);
                    }
                });
    }

    private void reRegisterChatRoomChangeListener(String userID, int roomNumber) {
        homeInteractor.unregisterChatRoomsChangeListener();
        homeInteractor.registerChatRoomsChangeListener(userID, roomNumber, new OnChatRoomChangeListener() {
            @Override
            public void onChatRoomChanged(ChatRoomInfo chatRoomInfo) {
                homeView.updateChatRoom(chatRoomInfo);
            }

            @Override
            public void onRequestError(String message) {
                Log.i(TAG, "reRegisterChatRoomChangeListener:onRequestError: " + message);
            }
        });
    }

    private Map<String, UserInfoChatRooms> registerUserInfoChangeListener(List<ChatRoom> chatRooms) {
        Map<String, UserInfoChatRooms> result = new HashMap<>(chatRooms.size());
        for (ChatRoom chatRoom : chatRooms) {
            for (Map.Entry<String, UserInfo> entry : chatRoom.getFriendsInfo().entrySet()) {
                String userID = entry.getKey();
                UserInfo userInfo = entry.getValue();
                UserInfoChatRooms userInfoChatRooms = result.get(userID);
                if (userInfoChatRooms == null) {
                    userInfoChatRooms = new UserInfoChatRooms(userInfo);
                    result.put(userID, userInfoChatRooms);
                    if(userChangeListenerServiceInstance != null) {
                        userChangeListenerServiceInstance.registerUserInfoStateChange(entry.getKey());
                        userInfo.setIsOnline(userChangeListenerServiceInstance.isOnline(userID));
                    }
                }
                userInfoChatRooms.addRoomID(chatRoom.getRoomID());
            }
        }
        return result;
    }

    @Override
    public void bindServices() {
        bindUserInfoChangeListenerService();
    }

    private void bindUserInfoChangeListenerService() {
        userInfoChangeListenerServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                userChangeListenerServiceInstance =
                        ((UserChangeListenerService.UserInfoChangeListenerServiceBinder) iBinder)
                                .getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                userChangeListenerServiceInstance = null;
            }
        };
        Intent intent = new Intent(context, UserChangeListenerService.class);
        context.bindService(intent, userInfoChangeListenerServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void unbindServices() {
        unbindUserInfoChangeListenerService();
    }

    private void unbindUserInfoChangeListenerService() {
        if (userInfoChangeListenerServiceConnection != null) {
            context.unbindService(userInfoChangeListenerServiceConnection);
        }
    }
}
