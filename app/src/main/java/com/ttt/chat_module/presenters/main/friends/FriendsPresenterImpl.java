package com.ttt.chat_module.presenters.main.friends;

import com.google.firebase.firestore.DocumentSnapshot;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.models.User;
import com.ttt.chat_module.views.main.friends.FriendsView;

import java.util.List;

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
                    public void onGetFriendsSuccess(List<User> users) {
                        friendsView.enableLoadingMore(true);
                        friendsView.hideRefreshingProgress();
                        friendsView.refreshUsers(users);
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
                    public void onGetFriendsSuccess(List<User> users) {
                        friendsView.enableRefreshing(true);
                        friendsView.hideLoadingMoreProgress();
                        friendsView.addMoreUsers(users);
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
