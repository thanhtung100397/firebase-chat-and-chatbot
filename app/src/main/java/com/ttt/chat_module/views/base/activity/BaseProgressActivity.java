package com.ttt.chat_module.views.base.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.ttt.chat_module.R;
import com.ttt.chat_module.presenters.base_progress.BaseProgressActivityPresenter;
import com.ttt.chat_module.views.base.LoadingFragment;

/**
 * Created by TranThanhTung on 02/02/2018.
 */

public abstract class BaseProgressActivity extends AppCompatActivity implements BaseProgressActivityPresenter.OnFetchDataProgressListener {
    private BaseProgressActivityPresenter presenter;

    protected abstract Fragment initPrimaryFragment();

    protected abstract BaseProgressActivityPresenter initPresenter();

    protected BaseProgressActivityPresenter getPresenter() {
        return presenter;
    }

    protected void onRetry() {
        getPresenter().fetchData(this);
    }

    private LoadingFragment loadingFragment;
    private Fragment primaryFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blank_layout);

        primaryFragment = initPrimaryFragment();
        initLoadingFragment();
        presenter = initPresenter();

        showFragment(loadingFragment);
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchData();
    }

    private void initLoadingFragment() {
        loadingFragment = new LoadingFragment();
        loadingFragment.setOnRetryListener(loadingFragment -> {
            fetchData();
        });
    }

    private void fetchData() {
        showProgress();
        getPresenter().fetchData(this);
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

    @Override
    public void onFetchDataSuccess(Bundle args) {
        hideProgress();
        showFragment(primaryFragment, args);
    }

    @Override
    public void onFetchDataFailure(String message) {
        hideProgress();
        showError();
    }
}
