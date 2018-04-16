package com.ttt.chat_module.presenters.main.friends;

import com.google.firebase.firestore.DocumentSnapshot;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.models.UserInfo;
import com.ttt.chat_module.views.main.friends.FriendsView;

import java.util.List;
import java.util.Map;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public class FriendsPresenterImpl implements FriendsPresenter {
    private FriendsView friendsView;
    private FriendsInteractor friendsInteractor;
    private boolean hasNoFriendLeft;
    private DocumentSnapshot lastFriendDocumentSnapshot;

    public FriendsPresenterImpl(FriendsView friendsView) {
        this.friendsView = friendsView;
        friendsInteractor = new FriendsInteractorImpl();
    }

    @Override
    public void onViewDestroy() {
        friendsInteractor.onViewDestroy();
    }

    @Override
    public void refreshFriends() {
        friendsView.showRefreshingProgress();
        friendsView.enableLoadingMore(false);
        friendsInteractor.getFriends(null, Constants.PAGE_SIZE,
                new OnGetFriendsCompleteListener() {
                    @Override
                    public void onGetFriendsSuccess(Map<String, Integer> userPositionMap, List<UserInfo> usersInfo) {
                        friendsView.enableLoadingMore(true);
                        friendsView.hideRefreshingProgress();
                        friendsView.refreshUsers(userPositionMap, usersInfo);
                    }

                    @Override
                    public void onLastElementFetched(DocumentSnapshot element, boolean hasNoElementLeft) {
                        friendsView.enableLoadingMore(true);
                        friendsView.hideRefreshingProgress();
                        lastFriendDocumentSnapshot = element;
                        hasNoFriendLeft = hasNoElementLeft;
                    }

                    @Override
                    public void onRequestError(String message) {
                        friendsView.enableLoadingMore(true);
                        friendsView.hideRefreshingProgress();
                    }
                });
    }

    @Override
    public void loadMoreFriends() {
        if(hasNoFriendLeft) {
            return;
        }
        friendsView.enableRefreshing(false);
        friendsView.showLoadingMoreProgress();
        friendsInteractor.getFriends(lastFriendDocumentSnapshot, Constants.PAGE_SIZE,
                new OnGetFriendsCompleteListener() {
                    @Override
                    public void onGetFriendsSuccess(Map<String, Integer> userPositionMap, List<UserInfo> usersInfo) {
                        friendsView.enableRefreshing(true);
                        friendsView.hideLoadingMoreProgress();
                        friendsView.addMoreUsers(userPositionMap, usersInfo);
                    }

                    @Override
                    public void onLastElementFetched(DocumentSnapshot element, boolean hasNoElementLeft) {
                        friendsView.enableRefreshing(true);
                        friendsView.hideLoadingMoreProgress();
                        lastFriendDocumentSnapshot = element;
                        hasNoFriendLeft = hasNoElementLeft;
                    }

                    @Override
                    public void onRequestError(String message) {
                        friendsView.hideLoadingMoreProgress();
                        friendsView.enableRefreshing(true);
                    }
                });
    }

    @Override
    public void setHasNoFriendLeft(boolean value) {
        this.hasNoFriendLeft = value;
    }
}
