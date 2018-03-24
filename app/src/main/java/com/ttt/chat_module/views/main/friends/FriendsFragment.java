package com.ttt.chat_module.views.main.friends;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ttt.chat_module.R;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.common.adapter.recycler_view_adapter.ListFriendsAdapter;
import com.ttt.chat_module.common.recycler_view_adapter.EndlessLoadingRecyclerViewAdapter;
import com.ttt.chat_module.common.recycler_view_adapter.RecyclerViewAdapter;
import com.ttt.chat_module.common.utils.UserAuth;
import com.ttt.chat_module.models.User;
import com.ttt.chat_module.models.UserInfo;
import com.ttt.chat_module.presenters.main.friends.FriendsPresenter;
import com.ttt.chat_module.presenters.main.friends.FriendsPresenterImpl;
import com.ttt.chat_module.views.base.fragment.BaseFragment;
import com.ttt.chat_module.views.chat.activity.ChatActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by TranThanhTung on 19/02/2018.
 */

public class FriendsFragment extends BaseFragment<FriendsPresenter> implements FriendsView, SwipeRefreshLayout.OnRefreshListener, EndlessLoadingRecyclerViewAdapter.OnLoadingMoreListener, RecyclerViewAdapter.OnItemClickListener {
    @BindView(R.id.rc_friends)
    RecyclerView rcFriends;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    private ListFriendsAdapter listFriendsAdapter;

    @Override
    protected int getLayoutResources() {
        return R.layout.fragment_friends;
    }

    @Override
    protected void initVariables(Bundle saveInstanceState, View rootView) {
        ButterKnife.bind(this, rootView);
    }

    @Override
    protected void initData(Bundle saveInstanceState) {
        Context context = getActivity();

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryLight, R.color.colorPrimary, R.color.colorPrimaryDark);

        listFriendsAdapter = new ListFriendsAdapter(context);
        listFriendsAdapter.addOnItemClickListener(this);
        listFriendsAdapter.setLoadingMoreListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rcFriends.addItemDecoration(new DividerItemDecoration(context, linearLayoutManager.getOrientation()));
        rcFriends.setLayoutManager(linearLayoutManager);
        rcFriends.setAdapter(listFriendsAdapter);

        Bundle args = getArguments();
        if (args == null) {
            return;
        }

        List<User> users = args.getParcelableArrayList(Constants.KEY_USERS);
        if (users == null) {
            getPresenter().refreshFriends();
        } else {
            listFriendsAdapter.addModels(users, false);
        }
    }

    @Override
    protected FriendsPresenter initPresenter() {
        return new FriendsPresenterImpl(this);
    }

    @Override
    public void onRefresh() {
        getPresenter().refreshFriends();
    }

    @Override
    public void onLoadMore() {
        getPresenter().loadMoreFriends();
    }

    @Override
    public void showRefreshingProgress() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideRefreshingProgress() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showLoadingMoreProgress() {
        listFriendsAdapter.showLoadingItem(true);
    }

    @Override
    public void hideLoadingMoreProgress() {
        listFriendsAdapter.hideLoadingItem();
    }

    @Override
    public void enableLoadingMore(boolean enable) {
        listFriendsAdapter.enableLoadingMore(enable);
    }

    @Override
    public void enableRefreshing(boolean enable) {
        swipeRefreshLayout.setEnabled(enable);
    }

    @Override
    public void refreshUsers(List<User> users) {
        listFriendsAdapter.refresh(users);
    }

    @Override
    public void addMoreUsers(List<User> users) {
        listFriendsAdapter.addModels(users, false);
    }

    @Override
    public void onItemClick(RecyclerView.Adapter adapter, RecyclerView.ViewHolder viewHolder, int viewType, int position) {
        Context context = getActivity();
        Intent intent = new Intent(context, ChatActivity.class);
        String[] userIDs = new String[2];
        userIDs[0] = UserAuth.getUserID();
        User friend = listFriendsAdapter.getItem(position, User.class);
        userIDs[1] = friend.getEmail();
        intent.putExtra(Constants.KEY_USER_IDS, userIDs);

        context.startActivity(intent);
    }
}
