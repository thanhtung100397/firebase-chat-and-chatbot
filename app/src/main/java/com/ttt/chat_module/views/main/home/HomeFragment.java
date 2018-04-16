package com.ttt.chat_module.views.main.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ttt.chat_module.R;
import com.ttt.chat_module.bus_event.ChatRoomLastMessageChangeEvent;
import com.ttt.chat_module.bus_event.UserOnlineStateChangeEvent;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.common.adapter.recycler_view_adapter.ChatRoomsAdapter;
import com.ttt.chat_module.common.recycler_view_adapter.EndlessLoadingRecyclerViewAdapter;
import com.ttt.chat_module.common.recycler_view_adapter.RecyclerViewAdapter;
import com.ttt.chat_module.common.utils.UserAuth;
import com.ttt.chat_module.models.ChatRoom;
import com.ttt.chat_module.models.UserInfo;
import com.ttt.chat_module.presenters.main.home.HomePresenter;
import com.ttt.chat_module.presenters.main.home.HomePresenterImpl;
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

public class HomeFragment extends BaseFragment<HomePresenter> implements HomeView, SwipeRefreshLayout.OnRefreshListener, RecyclerViewAdapter.OnItemClickListener, EndlessLoadingRecyclerViewAdapter.OnLoadingMoreListener {
    @BindView(R.id.rc_messages)
    RecyclerView rcMessages;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    private ChatRoomsAdapter chatRoomsAdapter;

    @Override
    protected int getLayoutResources() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initVariables(Bundle saveInstanceState, View rootView) {
        ButterKnife.bind(this, rootView);
    }

    @Override
    protected void initData(Bundle saveInstanceState) {
        Context context = getActivity();

        chatRoomsAdapter = new ChatRoomsAdapter(context);
        chatRoomsAdapter.setLoadingMoreListener(this);
        chatRoomsAdapter.addOnItemClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rcMessages.setLayoutManager(linearLayoutManager);
        rcMessages.addItemDecoration(new DividerItemDecoration(context, linearLayoutManager.getOrientation()));
        rcMessages.setAdapter(chatRoomsAdapter);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryLight, R.color.colorPrimary, R.color.colorPrimaryDark);
        swipeRefreshLayout.setOnRefreshListener(this);

        getPresenter().refreshChatRooms();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserOnlineStateChangeEvent(UserOnlineStateChangeEvent event) {
        chatRoomsAdapter.updateOnlineState(event.getUserID(), event.isOnline());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChatRoomLastMessageChangedListener(ChatRoomLastMessageChangeEvent event) {
        chatRoomsAdapter.updateLastMessage(event.getRoomID(), event.getLastMessage());
    }

    @Override
    protected HomePresenter initPresenter() {
        return new HomePresenterImpl(getActivity(), this);
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
    public void enableRefreshing(boolean enable) {
        swipeRefreshLayout.setEnabled(enable);
    }

    @Override
    public void refreshChatRooms(Map<String, Integer> roomPositionMap, List<ChatRoom> chatRooms) {
        chatRoomsAdapter.refreshChatRooms(roomPositionMap, chatRooms);
    }

    @Override
    public void showLoadingMoreProgress() {
        chatRoomsAdapter.showLoadingItem(true);
    }

    @Override
    public void hideLoadingMoreProgress() {
        chatRoomsAdapter.hideLoadingItem();
    }

    @Override
    public void enableLoadingMore(boolean enable) {
        chatRoomsAdapter.enableLoadingMore(enable);
    }

    @Override
    public void addMoreChatRooms(Map<String, Integer> roomPositionMap, List<ChatRoom> chatRooms) {
        chatRoomsAdapter.addChatRooms(roomPositionMap, chatRooms);
    }

    @Override
    public void onRefresh() {
        getPresenter().refreshChatRooms();
    }

    @Override
    public void onLoadMore() {
        getPresenter().loadMoreChatRooms();
    }

    @Override
    public void onItemClick(RecyclerView.Adapter adapter, RecyclerView.ViewHolder viewHolder, int viewType, int position) {
        Context context = getActivity();
        Intent intent = new Intent(context, ChatActivity.class);
        ArrayList<UserInfo> usersInfo = new ArrayList<>(2);
        usersInfo.add(UserAuth.getUserInfo(context));
        Map<String, UserInfo> friendsInfo = chatRoomsAdapter.getItem(position, ChatRoom.class).getFriendsInfo();
        for (Map.Entry<String, UserInfo> friendInfoEntry : friendsInfo.entrySet()) {
            usersInfo.add(friendInfoEntry.getValue());
        }
        intent.putParcelableArrayListExtra(Constants.KEY_USERS_INFO, usersInfo);

        context.startActivity(intent);
    }
}
