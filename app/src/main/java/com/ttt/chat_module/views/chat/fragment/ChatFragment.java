package com.ttt.chat_module.views.chat.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.ttt.chat_module.R;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.common.adapter.recycler_view_adapter.ChatMessageAdapter;
import com.ttt.chat_module.models.Message;
import com.ttt.chat_module.common.custom_view.ClearableEditText;
import com.ttt.chat_module.models.UserInfo;
import com.ttt.chat_module.presenters.chat.fragment.ChatFragmentPresenter;
import com.ttt.chat_module.presenters.chat.fragment.ChatFragmentPresenterImpl;
import com.ttt.chat_module.views.base.fragment.BaseFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public class ChatFragment extends BaseFragment<ChatFragmentPresenter> implements ChatFragmentView, View.OnClickListener {
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.rc_messages)
    RecyclerView rcMessages;
    @BindView(R.id.edt_message)
    ClearableEditText edtMessage;
    @BindView(R.id.btn_emoji)
    ImageButton btnEmoji;
    @BindView(R.id.btn_send)
    ImageButton btnSend;
    @BindView(R.id.progress_first_loading_messages)
    ProgressBar firstLoadingMessagesProgress;
    @BindView(R.id.ln_retry)
    LinearLayout lnRetry;
    @BindView(R.id.btn_retry)
    Button btnRetry;

    private ChatMessageAdapter chatMessageAdapter;

    @Override
    protected int getLayoutResources() {
        return R.layout.fragment_chat;
    }

    @Override
    protected ChatFragmentPresenter initPresenter() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            return null;
        }
        String roomID = bundle.getString(Constants.KEY_ROOM_ID);
        List<UserInfo> usersInfo = bundle.getParcelableArrayList(Constants.KEY_USERS_INFO);
        return new ChatFragmentPresenterImpl(this, roomID, usersInfo);
    }

    @Override
    protected void initVariables(Bundle savedInstanceState, View rootView) {
        ButterKnife.bind(this, rootView);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.setSupportActionBar(toolbar);
        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }

        btnSend.setOnClickListener(this);
        btnEmoji.setOnClickListener(this);
        btnRetry.setOnClickListener(this);

        if (getPresenter().isCoupleChatRoom()) {
            initCoupleChatRoom();
        } else {
            //TODO Chat Room
        }

        Context context = getActivity();

        chatMessageAdapter = new ChatMessageAdapter(context,
                getPresenter().getOwnerInfo(),
                getPresenter().getFriendsInfo());

        rcMessages.setLayoutManager(new LinearLayoutManager(context));
        rcMessages.setAdapter(chatMessageAdapter);

        getPresenter().registerOnMessageAddedListener();
    }

    private void initCoupleChatRoom() {
        List<UserInfo> friendsInfo = getPresenter().getFriendsInfo();
        UserInfo friendInfo = friendsInfo.get(0);
        toolbar.setTitle(friendInfo.getLastName() + " " + friendInfo.getFirstName());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                getActivity().onBackPressed();
            }
            break;

            default: {
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getPresenter().registerOnMessageAddedListener();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getPresenter().unregisterOnMessageAddedListener();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send: {

            }
            break;

            case R.id.btn_emoji: {

            }
            break;

            case R.id.btn_retry: {
                lnRetry.setVisibility(View.GONE);
                getPresenter().fetchLatestMessages();
            }
            break;

            default: {
                break;
            }
        }
    }

    @Override
    public void addOwnerMessage(Message message) {

    }

    @Override
    public void addFriendMessage(Message message) {

    }

    @Override
    public void onMessageSeen() {

    }

    @Override
    public void showFirstLoadingMessagesProgress() {
        firstLoadingMessagesProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideFirstLoadingMessagesProgress() {
        firstLoadingMessagesProgress.setVisibility(View.GONE);
    }

    @Override
    public void showFirstLoadingMessagesError() {
        lnRetry.setVisibility(View.VISIBLE);
    }
}
