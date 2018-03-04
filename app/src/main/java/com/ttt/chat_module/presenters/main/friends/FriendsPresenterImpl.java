package com.ttt.chat_module.presenters.main.friends;

import android.content.Context;
import android.util.Log;

import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.models.User;
import com.ttt.chat_module.views.main.friends.FriendsView;

import java.util.List;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public class FriendsPresenterImpl implements FriendsPresenter {
    private static final String TAG = "FriendsPresenterImpl";

    private Context context;
    private FriendsView friendsView;
    private FriendsInteractor friendsInteractor;
    private int currentPage = 0;

    public FriendsPresenterImpl(Context context, FriendsView friendsView) {
        this.context = context;
        this.friendsView = friendsView;
        friendsInteractor = new FriendsInteractorImpl();
    }

    @Override
    public void onViewDestroy() {
        context = null;
        friendsInteractor.onViewDestroy();
    }

    @Override
    public void refreshFriends() {
        friendsView.showRefreshingProgress();
        friendsInteractor.getFriends(0, Constants.PAGE_SIZE,
                new OnGetFriendsCompleteListener() {
                    @Override
                    public void onGetFriendsSuccess(List<User> users) {
                        currentPage = 0;
                        if (users.size() < Constants.PAGE_SIZE) {
                            friendsView.enableLoadingMore(false);
                        } else {
                            friendsView.enableLoadingMore(true);
                        }
                        friendsView.hideRefreshingProgress();
                        friendsView.refreshUsers(users);
                    }

                    @Override
                    public void onError(String message) {
                        Log.i(TAG, "onError: " + message);
                        friendsView.hideRefreshingProgress();
                    }
                });
    }

    @Override
    public void loadMoreFriends() {
        friendsView.showLoadingMoreProgress();
        friendsInteractor.getFriends(currentPage + 1, Constants.PAGE_SIZE,
                new OnGetFriendsCompleteListener() {
                    @Override
                    public void onGetFriendsSuccess(List<User> users) {
                        currentPage++;
                        if (users.size() < Constants.PAGE_SIZE) {
                            friendsView.enableLoadingMore(false);
                        }
                        friendsView.hideLoadingMoreProgress();
                        friendsView.addMoreUsers(users);
                    }

                    @Override
                    public void onError(String message) {
                        Log.i(TAG, "onError: " + message);
                        friendsView.hideLoadingMoreProgress();
                    }
                });
    }
}
