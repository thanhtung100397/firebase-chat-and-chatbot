package com.ttt.chat_module.views.base.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.ttt.chat_module.R;
import com.ttt.chat_module.presenters.BasePresenter;
import com.ttt.chat_module.views.base.LoadingFragment;

/**
 * Created by TranThanhTung on 02/02/2018.
 */

public abstract class BaseProgressActivity<T extends BasePresenter> extends AppCompatActivity {
    private T presenter;

    protected abstract void initVariables(Bundle savedInstanceState);

    protected abstract void initData(Bundle savedInstanceState);

    protected abstract T initPresenter();

    protected T getPresenter() {
        return presenter;
    }

    protected abstract void onRetry();

    private LoadingFragment loadingFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blank_layout);
        loadingFragment = new LoadingFragment();
        loadingFragment.setOnRetryListener(loadingFragment -> {
            onRetry();
        });
        showFragment(loadingFragment);

        initVariables(savedInstanceState);
        initData(savedInstanceState);

        this.presenter = initPresenter();
    }

    protected void showFragment(Fragment fragment) {
        this.showFragment(fragment, null);
    }

    protected void showFragment(Fragment fragment, Bundle args) {
        if (args != null) {
            fragment.setArguments(args);
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_root, fragment)
                .commit();
    }

    protected void showProgress() {
        loadingFragment.showProgress();
    }

    protected void hideProgress() {
        loadingFragment.hideProgress();
    }

    protected void showError() {
        loadingFragment.showError();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onViewDestroy();
        }
    }
}
