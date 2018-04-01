package com.ttt.chat_module.presenters.main.home;

import android.os.Bundle;
import android.view.View;

import com.ttt.chat_module.R;
import com.ttt.chat_module.views.base.fragment.BaseFragment;
import com.ttt.chat_module.views.main.home.HomeView;

/**
 * Created by TranThanhTung on 19/02/2018.
 */

public class HomePresenterImpl extends BaseFragment<HomePresenter> implements HomeView {
    @Override
    protected int getLayoutResources() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initVariables(Bundle saveInstanceState, View rootView) {

    }

    @Override
    protected void initData(Bundle saveInstanceState) {

    }

    @Override
    protected HomePresenter initPresenter() {
        return null;
    }
}
