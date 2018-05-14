package com.ttt.chat_module.views.chat_bot;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ttt.chat_module.R;
import com.ttt.chat_module.common.adapter.recycler_view_adapter.ChatBotMessageAdapter;
import com.ttt.chat_module.common.custom_view.ClearableEditText;
import com.ttt.chat_module.models.BotInfo;
import com.ttt.chat_module.models.chat_bot_message.ContactQueryMessage;
import com.ttt.chat_module.models.chat_bot_message.SimpleMessage;
import com.ttt.chat_module.presenters.chat_bot.ChatBotPresenter;
import com.ttt.chat_module.presenters.chat_bot.ChatBotPresenterImpl;
import com.ttt.chat_module.views.base.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatBotActivity extends BaseActivity<ChatBotPresenter> implements ChatBotActivityView, View.OnClickListener {
    private static final int REQUEST_RECORD_AUDIO_REQUEST_CODE = 0;
    private static final int REQUEST_PHONE_CALL_REQUEST_CODE = 1;
    private static final int REQUEST_CAMERA_REQUEST_CODE = 2;
    private static final int REQUEST_READ_CONTACTS_REQUEST_CODE = 3;
    private static final int REQUEST_READ_CONTACTS_AND_PHONE_CALL_REQUEST_CODE = 4;

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.txt_bot_name)
    TextView txtBotName;
    @BindView(R.id.img_bot_avatar)
    CircleImageView imgBotAvatar;

    @BindView(R.id.rc_messages)
    RecyclerView rcMessages;
    @BindView(R.id.edt_message)
    ClearableEditText edtMessage;
    @BindView(R.id.btn_send)
    ImageButton btnSend;
    @BindView(R.id.progress_first_loading_messages)
    ProgressBar firstLoadingMessagesProgress;

    private ChatBotMessageAdapter chatBotMessageAdapter;

    @Override
    protected int getLayoutResources() {
        return R.layout.activity_chat_bot;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }

        btnSend.setOnClickListener(this);

        txtBotName.setText("Chat bot");
        BotInfo botInfo = new BotInfo(null);

        chatBotMessageAdapter = new ChatBotMessageAdapter(this, botInfo);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        rcMessages.setLayoutManager(linearLayoutManager);
        rcMessages.setAdapter(chatBotMessageAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
            }
            break;

            default: {
                break;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_REQUEST_CODE);
        } else {
            getPresenter().registerApiAiService();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_REQUEST_CODE: {
                for (int grantResult : grantResults) {
                    if (grantResult == PackageManager.PERMISSION_DENIED) {
                        return;
                    }
                    getPresenter().registerApiAiService();
                }
            }
            break;

            case REQUEST_PHONE_CALL_REQUEST_CODE:
            case REQUEST_CAMERA_REQUEST_CODE:
            case REQUEST_READ_CONTACTS_REQUEST_CODE:
            case REQUEST_READ_CONTACTS_AND_PHONE_CALL_REQUEST_CODE: {
                for (int grantResult : grantResults) {
                    if (grantResult == PackageManager.PERMISSION_DENIED) {
                        showChatBotPermissionDeniedMessage();
                        return;
                    }
                }
                getPresenter().handleChatBotPendingAction();
            }
            break;

            default: {
                break;
            }
        }
    }

    @Override
    protected ChatBotPresenter initPresenter() {
        return new ChatBotPresenterImpl(this, this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send: {
                getPresenter().sendQueryMessage(edtMessage.getText());
            }
            break;
        }
    }

    @Override
    public void showLoadingProgress() {
        firstLoadingMessagesProgress.setVisibility(View.VISIBLE);
        edtMessage.setEnable(false);
    }

    @Override
    public void hideLoadingProgress() {
        firstLoadingMessagesProgress.setVisibility(View.GONE);
        edtMessage.setEnable(true);
    }

    @Override
    public void showChatBotMessage(String message) {
        chatBotMessageAdapter.addModel(0, new SimpleMessage(message),
                ChatBotMessageAdapter.VIEW_TYPE_BOT_TEXT_MESSAGE, true);
    }

    @Override
    public void showChatBotMessageAndItems(ContactQueryMessage message) {
        chatBotMessageAdapter.addModel(0, message,
                ChatBotMessageAdapter.VIEW_TYPE_BOT_TEXT_AND_ITEMS_MESSAGE, true);
    }

    @Override
    public void showUserMessage(String message) {
        chatBotMessageAdapter.addModel(0, new SimpleMessage(message), true);
    }

    @Override
    public void clearInputMessage() {
        edtMessage.setText("");
    }

    @Override
    public void showChatBotPermissionDeniedMessage() {
        chatBotMessageAdapter.addModel(0, new SimpleMessage(getString(R.string.sorry_i_cant_do_this_without_your_permission)),
                ChatBotMessageAdapter.VIEW_TYPE_BOT_TEXT_MESSAGE, true);
    }

    @Override
    public boolean requestPhoneCallPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            String permissions[] = new String[1];
            permissions[0] = Manifest.permission.CALL_PHONE;
            ActivityCompat.requestPermissions(this, permissions, REQUEST_PHONE_CALL_REQUEST_CODE);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean requestCameraPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            String permissions[] = new String[1];
            permissions[0] = Manifest.permission.CAMERA;
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CAMERA_REQUEST_CODE);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean requestReadContactsPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            String permissions[] = new String[1];
            permissions[0] = Manifest.permission.READ_CONTACTS;
            ActivityCompat.requestPermissions(this, permissions, REQUEST_READ_CONTACTS_REQUEST_CODE);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean requestReadContactsAndPhoneCallPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            String permissions[] = new String[2];
            permissions[0] = Manifest.permission.READ_CONTACTS;
            permissions[1] = Manifest.permission.CALL_PHONE;
            ActivityCompat.requestPermissions(this, permissions, REQUEST_READ_CONTACTS_AND_PHONE_CALL_REQUEST_CODE);
            return false;
        } else {
            return true;
        }
    }
}
