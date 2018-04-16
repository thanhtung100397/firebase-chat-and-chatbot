package com.ttt.chat_module.presenters.main.home;

import android.content.Context;

import com.ttt.chat_module.R;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.common.utils.ToastUtils;
import com.ttt.chat_module.common.utils.UserAuth;
import com.ttt.chat_module.models.ChatRoom;
import com.ttt.chat_module.models.ChatRoomInfo;
import com.ttt.chat_module.views.main.home.HomeView;

import java.util.List;
import java.util.Map;

/**
 * Created by TranThanhTung on 19/02/2018.
 */

public class HomePresenterImpl implements HomePresenter {
    private Context context;
    private HomeInteractor homeInteractor;
    private HomeView homeView;
    private ChatRoomInfo lastChatRoomInfo;

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
        homeInteractor.getChatRooms(null, Constants.PAGE_SIZE, UserAuth.getUserID(),
                new OnGetChatRoomsCompleteListener() {
                    @Override
                    public void onGetChatRoomsSuccess(Map<String, Integer> roomPositionMap, List<ChatRoom> chatRoomMaps) {
                        homeView.hideRefreshingProgress();
                        homeView.refreshChatRooms(roomPositionMap, chatRoomMaps);
                    }

                    @Override
                    public void onLastElementFetched(ChatRoomInfo element, boolean hasNoElementLeft) {
                        lastChatRoomInfo = element;
                        homeView.enableLoadingMore(!hasNoElementLeft);
                    }

                    @Override
                    public void onRequestError(String message) {
                        ToastUtils.quickToast(context, R.string.unexpected_error_occurred);
                    }
                });
    }

    @Override
    public void loadMoreChatRooms() {
        homeView.showLoadingMoreProgress();
        homeView.enableRefreshing(false);
        homeInteractor.getChatRooms(lastChatRoomInfo, Constants.PAGE_SIZE, UserAuth.getUserID(),
                new OnGetChatRoomsCompleteListener() {
                    @Override
                    public void onGetChatRoomsSuccess(Map<String, Integer> roomPositionMap, List<ChatRoom> chatRoomMaps) {
                        homeView.hideLoadingMoreProgress();
                        homeView.addMoreChatRooms(roomPositionMap, chatRoomMaps);
                    }

                    @Override
                    public void onLastElementFetched(ChatRoomInfo element, boolean hasNoElementLeft) {
                        lastChatRoomInfo = element;
                        homeView.enableRefreshing(true);
                        homeView.enableLoadingMore(!hasNoElementLeft);
                    }

                    @Override
                    public void onRequestError(String message) {
                        ToastUtils.quickToast(context, R.string.unexpected_error_occurred);
                    }
                });
    }
}
