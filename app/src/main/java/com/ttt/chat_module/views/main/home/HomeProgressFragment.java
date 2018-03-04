package com.ttt.chat_module.views.main.home;

import android.support.v4.app.Fragment;

import com.ttt.chat_module.presenters.base_progress_fragment.BaseProgressFragmentPresenter;
import com.ttt.chat_module.presenters.main.home.HomeProgressFragmentPresenter;
import com.ttt.chat_module.views.base.fragment.BaseProgressFragment;

/**
 * Created by TranThanhTung on 19/02/2018.
 */

public class HomeProgressFragment extends BaseProgressFragment {
    @Override
    protected Fragment initPrimaryFragment() {
        return new HomeFragment();
    }

    @Override
    protected BaseProgressFragmentPresenter initPresenter() {
        return new HomeProgressFragmentPresenter();
    }
}
