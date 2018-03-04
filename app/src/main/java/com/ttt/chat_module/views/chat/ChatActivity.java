package com.ttt.chat_module.views.chat;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.ttt.chat_module.R;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.common.adapter.recycler_view_adapter.Message;
import com.ttt.chat_module.common.custom_view.ClearableEditText;
import com.ttt.chat_module.presenters.BasePresenter;
import com.ttt.chat_module.presenters.chat.ChatPresenter;
import com.ttt.chat_module.presenters.chat.ChatPresenterImpl;
import com.ttt.chat_module.views.base.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public class ChatActivity extends BaseActivity<ChatPresenter> implements ChatView, View.OnClickListener {
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

    private String roomID;

    @Override
    protected int getLayoutResources() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        ButterKnife.bind(this);

        roomID = getIntent().getStringExtra(Constants.KEY_ROOM_ID);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        btnSend.setOnClickListener(this);
        btnEmoji.setOnClickListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPresenter().registerOnMessageAddedListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getPresenter().unregisterOnMessageAddedListener();
    }

    @Override
    protected ChatPresenter initPresenter() {
        return new ChatPresenterImpl(this, this, roomID);
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
}
