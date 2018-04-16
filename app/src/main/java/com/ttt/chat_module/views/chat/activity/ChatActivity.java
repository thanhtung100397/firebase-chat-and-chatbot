package com.ttt.chat_module.views.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.common.utils.UserAuth;
import com.ttt.chat_module.models.UserInfo;
import com.ttt.chat_module.models.VisitState;
import com.ttt.chat_module.presenters.base_progress.BaseProgressActivityPresenter;
import com.ttt.chat_module.presenters.chat.activity.ChatActivityPresenterImpl;
import com.ttt.chat_module.services.LeftChatRoomService;
import com.ttt.chat_module.views.base.activity.BaseProgressActivity;
import com.ttt.chat_module.views.chat.fragment.ChatFragment;

import java.util.List;

/**
 * Created by TranThanhTung on 18/03/2018.
 */

public class ChatActivity extends BaseProgressActivity {
    private String roomID;

    @Override
    protected Fragment initPrimaryFragment() {
        return new ChatFragment();
    }

    @Override
    protected BaseProgressActivityPresenter initPresenter() {
        Intent intent = getIntent();
        String roomID = intent.getStringExtra(Constants.KEY_ROOM_ID);
        if(roomID == null) {
            List<UserInfo> usersInfo = getIntent().getParcelableArrayListExtra(Constants.KEY_USERS_INFO);
            return new ChatActivityPresenterImpl(this, usersInfo);
        } else {
            return new ChatActivityPresenterImpl(this, roomID);
        }
    }

    @Override
    public void onFetchDataSuccess(Bundle args) {
        this.roomID = args.getString(Constants.KEY_ROOM_ID);
        super.onFetchDataSuccess(args);
    }

    @Override
    public void onBackPressed() {
        if(roomID != null) {
            Intent intent = new Intent(this, LeftChatRoomService.class);
            intent.putExtra(Constants.KEY_ROOM_ID, roomID);
            intent.putExtra(Constants.KEY_USER_ID, UserAuth.getUserID());
            intent.putExtra(Constants.KEY_IN_ROOM_STATE, VisitState.LEFT_ROOM_STATE);
            startService(intent);
        }
        super.onBackPressed();
    }
}
