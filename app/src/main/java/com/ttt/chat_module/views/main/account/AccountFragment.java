package com.ttt.chat_module.views.main.account;

import android.os.Bundle;
import android.view.View;

import com.ttt.chat_module.R;
import com.ttt.chat_module.presenters.main.account.AccountPresenter;
import com.ttt.chat_module.views.base.fragment.BaseFragment;

/**
 * Created by TranThanhTung on 19/02/2018.
 */

public class AccountFragment extends BaseFragment<AccountPresenter> implements AccountView {

    @Override
    protected int getLayoutResources() {
        return R.layout.fragment_account;
    }

    @Override
    protected void initVariables(Bundle saveInstanceState, View rootView) {

    }

    @Override
    protected void initData(Bundle saveInstanceState) {

    }

    @Override
    protected AccountPresenter initPresenter() {
        return null;
    }
}
