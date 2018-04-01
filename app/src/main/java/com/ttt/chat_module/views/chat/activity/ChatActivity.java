package com.ttt.chat_module.views.chat.activity;

import android.support.v4.app.Fragment;

import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.models.User;
import com.ttt.chat_module.presenters.base_progress.BaseProgressActivityPresenter;
import com.ttt.chat_module.presenters.chat.activity.ChatActivityPresenterImpl;
import com.ttt.chat_module.views.base.activity.BaseProgressActivity;
import com.ttt.chat_module.views.chat.fragment.ChatFragment;

import java.util.List;

/**
 * Created by TranThanhTung on 18/03/2018.
 */

public class ChatActivity extends BaseProgressActivity {

    @Override
    protected Fragment initPrimaryFragment() {
        return new ChatFragment();
    }

    @Override
    protected BaseProgressActivityPresenter initPresenter() {
        List<User> users = getIntent().getParcelableArrayListExtra(Constants.KEY_USERS);
        return new ChatActivityPresenterImpl(users);
    }
}
