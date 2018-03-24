package com.ttt.chat_module.views.main.account;

import android.support.v4.app.Fragment;

import com.ttt.chat_module.presenters.base_progress.BaseProgressFragmentPresenter;
import com.ttt.chat_module.presenters.main.account.AccountProgressFragmentPresenter;
import com.ttt.chat_module.views.base.fragment.BaseProgressFragment;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public class AccountProgressFragment extends BaseProgressFragment {
    @Override
    protected Fragment initPrimaryFragment() {
        return new AccountFragment();
    }

    @Override
    protected BaseProgressFragmentPresenter initPresenter() {
        return new AccountProgressFragmentPresenter();
    }
}
