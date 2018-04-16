package com.ttt.chat_module.views.chat.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;
import com.sangcomz.fishbun.define.Define;
import com.ttt.chat_module.GlideApp;
import com.ttt.chat_module.R;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.common.adapter.recycler_view_adapter.ChatMessageAdapter;
import com.ttt.chat_module.common.recycler_view_adapter.EndlessLoadingRecyclerViewAdapter;
import com.ttt.chat_module.models.ChatRoomInfo;
import com.ttt.chat_module.models.message_models.BaseMessage;
import com.ttt.chat_module.common.custom_view.ClearableEditText;
import com.ttt.chat_module.models.UserInfo;
import com.ttt.chat_module.presenters.chat.fragment.ChatFragmentPresenter;
import com.ttt.chat_module.presenters.chat.fragment.ChatFragmentPresenterImpl;
import com.ttt.chat_module.views.base.fragment.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Map;

import com.ttt.chat_module.bus_event.ImageItemStartUploadingEvent;
import com.ttt.chat_module.bus_event.ImageItemUploadFailureEvent;
import com.ttt.chat_module.bus_event.ImageItemUploadSuccessEvent;
import com.ttt.chat_module.bus_event.SendImageMessageFailureEvent;
import com.ttt.chat_module.bus_event.SendImageMessageSuccessEvent;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public class ChatFragment extends BaseFragment<ChatFragmentPresenter> implements ChatFragmentView, View.OnClickListener, EndlessLoadingRecyclerViewAdapter.OnLoadingMoreListener, TextWatcher {
    private static final int REQUEST_CODE_PICK_IMAGE = 0;

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.txt_friend_name)
    TextView txtFriendName;
    @BindView(R.id.img_friend_avatar)
    CircleImageView imgFriendAvatar;
    @BindView(R.id.txt_friend_online_state)
    TextView txtFriendOnlineState;
    @BindView(R.id.img_friend_online_state)
    ImageView imgFriendOnlineState;

    @BindView(R.id.rc_messages)
    RecyclerView rcMessages;
    @BindView(R.id.edt_message)
    ClearableEditText edtMessage;
    @BindView(R.id.btn_camera)
    ImageButton btnCamera;
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
        String ownerID = bundle.getString(Constants.KEY_OWNER_ID);
        ChatRoomInfo chatRoomInfo = (ChatRoomInfo) bundle.getSerializable(Constants.KEY_CHAT_ROOM_INFO);
        return new ChatFragmentPresenterImpl(getActivity(), this, ownerID, chatRoomInfo);
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
            actionBar.setTitle("");
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }

        btnSend.setOnClickListener(this);
        btnEmoji.setOnClickListener(this);
        btnRetry.setOnClickListener(this);
        btnCamera.setOnClickListener(this);

        edtMessage.addTextChangeListener(this);

        if (getPresenter().isCoupleChatRoom()) {
            initCoupleChatRoom();
        } else {
            //TODO Chat Room
        }

        Context context = getActivity();

        chatMessageAdapter = new ChatMessageAdapter(context,
                getPresenter().getOwnerInfo(),
                getPresenter().getMapFriendsInfo());

        chatMessageAdapter.setLoadingMoreListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setReverseLayout(true);
        rcMessages.setLayoutManager(linearLayoutManager);
        rcMessages.setAdapter(chatMessageAdapter);
    }

    private void initCoupleChatRoom() {
        Map<String, UserInfo> mapFriendsInfo = getPresenter().getMapFriendsInfo();
        UserInfo friendInfo = mapFriendsInfo.entrySet().iterator().next().getValue();

        GlideApp.with(getContext())
                .load(friendInfo.getAvatarUrl())
                .placeholder(R.drawable.avatar_placeholder)
                .into(imgFriendAvatar);

        txtFriendName.setText(friendInfo.getLastName() + " " + friendInfo.getFirstName());

        getPresenter().registerFriendOnlineStateListener(friendInfo.getId());
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
        ChatFragmentPresenter chatFragmentPresenter = getPresenter();
        chatFragmentPresenter.bindNotificationService();
        chatFragmentPresenter.registerOnFriendVisitStageListener();
        chatFragmentPresenter.registerOnMessageAddedListener();
        chatFragmentPresenter.registerFriendTypingListener(chatFragmentPresenter.getOwnerInfo().getId());
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        ChatFragmentPresenter presenter = getPresenter();
        if (!edtMessage.getText().isEmpty()) {
            presenter.changeUserTypingState(true);
        }
        if(!presenter.isUserInChatRoom()) {
           presenter.enterRoom();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        ChatFragmentPresenter presenter = getPresenter();
        presenter.changeUserTypingState(false);
        if(presenter.isUserInChatRoom()) {
            presenter.leftRoom();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ChatFragmentPresenter chatFragmentPresenter = getPresenter();
        chatFragmentPresenter.unbindNotificationService();
        chatFragmentPresenter.unregisterOnFriendVisitStageListener();
        chatFragmentPresenter.unregisterOnMessageAddedListener();
        chatFragmentPresenter.unregisterFriendTypingListener();
        chatFragmentPresenter.unRegisterFriendOnlineStateListener();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send: {
                getPresenter().validateSentTextMessage(edtMessage.getText());
            }
            break;

            case R.id.btn_camera: {
                Context context = getActivity();
                FishBun.with(this)
                        .setImageAdapter(new GlideAdapter())
                        .setMaxCount(6)
                        .setMinCount(1)
                        .setActionBarColor(getResources().getColor(R.color.colorPrimary),
                                getResources().getColor(R.color.colorPrimaryDark),
                                false)
                        .setActionBarTitleColor(getResources().getColor(android.R.color.white))
                        .setButtonInAlbumActivity(false)
                        .setCamera(true)
                        .exceptGif(true)
                        .setHomeAsUpIndicatorDrawable(ContextCompat.getDrawable(context, R.drawable.ic_back))
                        .setOkButtonDrawable(ContextCompat.getDrawable(context, R.drawable.ic_tick))
                        .setAllViewTitle(getResources().getString(R.string.pick_image))
                        .setActionBarTitle(getResources().getString(R.string.pick_image))
                        .textOnNothingSelected(getResources().getString(R.string.must_pick_one_image))
                        .setRequestCode(REQUEST_CODE_PICK_IMAGE)
                        .startAlbum();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_PICK_IMAGE: {
                if (resultCode == Activity.RESULT_OK) {
                    List<Uri> imageUris = data.getParcelableArrayListExtra(Define.INTENT_PATH);
                    getPresenter().validateSentImageMessage(imageUris);
                }
            }
            break;

            default: {
                break;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onImageItemUploadSuccess(ImageItemUploadSuccessEvent event) {
        if (getPresenter().getRoomID().equals(event.getRoomID())) {
            chatMessageAdapter.showOwnerImageMessageUploaded(event.getUrl(), event.getPosition());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onImageItemUploadFailure(ImageItemUploadFailureEvent event) {
        if (getPresenter().getRoomID().equals(event.getRoomID())) {
            chatMessageAdapter.showOwnerImageMessageError();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onImageItemStartUploadingEvent(ImageItemStartUploadingEvent event) {
        if (getPresenter().getRoomID().equals(event.getRoomID())) {
            chatMessageAdapter.showUploadingNextImageMessage();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSendImageMessageSuccessEvent(SendImageMessageSuccessEvent event) {
        if (getPresenter().getRoomID().equals(event.getRoomID())) {
            getPresenter().setHasUploadingTask(false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSendImageMessageFailureEvent(SendImageMessageFailureEvent event) {
        if (getPresenter().getRoomID().equals(event.getRoomID())) {
            getPresenter().setHasUploadingTask(false);
            chatMessageAdapter.showOwnerImageMessageError();
        }
    }

    @Override
    public void addTopMessage(BaseMessage baseMessage) {
        chatMessageAdapter.addTopMessage(baseMessage);
    }

    @Override
    public void addMessages(List<BaseMessage> baseMessages) {
        chatMessageAdapter.addMessages(baseMessages);
    }

    @Override
    public void updateMessageState(BaseMessage baseMessage, int position) {
        chatMessageAdapter.updateTextMessage(baseMessage, position);
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

    @Override
    public void clearTypedMessage() {
        edtMessage.setText("");
    }

    @Override
    public void clearAllMessages() {
        chatMessageAdapter.clear();
    }

    @Override
    public void onLoadMore() {
        getPresenter().loadMoreMessages();
    }

    @Override
    public void showLoadingMoreProgress() {
        chatMessageAdapter.showLoadingItem(true);
    }

    @Override
    public void hideLoadingMoreProgress() {
        chatMessageAdapter.hideLoadingItem();
    }

    @Override
    public ChatFragmentPresenter getPresenter() {
        return super.getPresenter();
    }

    @Override
    public void showTypingStateView(String userID) {
        chatMessageAdapter.showFriendTypingMessage(userID);
    }

    @Override
    public void hideTypingStateView(String userID) {
        chatMessageAdapter.hideFriendTypingMessage(userID);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        if ((start + count) == 0) {
            getPresenter().changeUserTypingState(false);
        } else if (before == 0) {
            getPresenter().changeUserTypingState(true);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void updateFriendOnlineState(boolean isOnline) {
        Context context = getActivity();
        if (isOnline) {
            txtFriendOnlineState.setTextColor(context.getResources().getColor(R.color.dot_online_color));
            txtFriendOnlineState.setText(R.string.active_now);
            imgFriendOnlineState.getDrawable().setLevel(1);
        } else {
            txtFriendOnlineState.setTextColor(context.getResources().getColor(android.R.color.white));
            txtFriendOnlineState.setText(R.string.offline);
            imgFriendOnlineState.getDrawable().setLevel(0);
        }
    }
}
