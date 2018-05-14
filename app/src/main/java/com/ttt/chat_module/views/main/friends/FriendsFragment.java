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
import com.ttt.chat_module.bus_event.UserOnlineStateChangeEvent;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.common.adapter.recycler_view_adapter.ListFriendsAdapter;
import com.ttt.chat_module.common.recycler_view_adapter.EndlessLoadingRecyclerViewAdapter;
import com.ttt.chat_module.common.recycler_view_adapter.RecyclerViewAdapter;
import com.ttt.chat_module.common.utils.UserAuth;
import com.ttt.chat_module.models.UserInfo;
import com.ttt.chat_module.presenters.main.friends.FriendsPresenter;
import com.ttt.chat_module.presenters.main.friends.FriendsPresenterImpl;
import com.ttt.chat_module.views.base.fragment.BaseFragment;
import com.ttt.chat_module.views.chat.activity.ChatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

        if(listFriendsAdapter == null) {
            listFriendsAdapter = new ListFriendsAdapter(context);
        }
        listFriendsAdapter.addOnItemClickListener(this);
        listFriendsAdapter.setLoadingMoreListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rcFriends.addItemDecoration(new DividerItemDecoration(context, linearLayoutManager.getOrientation()));
        rcFriends.setLayoutManager(linearLayoutManager);
        rcFriends.setAdapter(listFriendsAdapter);

        getPresenter().refreshFriends();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserOnlineStateChangeEvent(UserOnlineStateChangeEvent event) {
        listFriendsAdapter.updateOnlineState(event.getUserID(), event.isOnline());
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
    public void refreshUsers(Map<String, Integer> userPositionMap, List<UserInfo> usersInfo) {
        listFriendsAdapter.refreshFriends(userPositionMap, usersInfo);
    }

    @Override
    public void addMoreUsers(Map<String, Integer> userPositionMap, List<UserInfo> usersInfo) {
        listFriendsAdapter.addFriends(userPositionMap, usersInfo);
    }

    @Override
    public void onItemClick(RecyclerView.Adapter adapter, RecyclerView.ViewHolder viewHolder, int viewType, int position) {
        Context context = getActivity();
        Intent intent = new Intent(context, ChatActivity.class);
        ArrayList<UserInfo> usersInfo = new ArrayList<>(2);
        usersInfo.add(UserAuth.getUserInfo(context));
        usersInfo.add(listFriendsAdapter.getItem(position, UserInfo.class));
        intent.putParcelableArrayListExtra(Constants.KEY_USERS_INFO, usersInfo);

        context.startActivity(intent);
    }
}
