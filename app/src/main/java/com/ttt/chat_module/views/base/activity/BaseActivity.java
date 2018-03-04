package com.ttt.chat_module.views.base.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ttt.chat_module.presenters.BasePresenter;

/**
 * Created by TranThanhTung on 25/01/2018.
 */
public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity {
    private T presenter;

    protected abstract int getLayoutResources();

    protected abstract void initVariables(Bundle savedInstanceState);

    protected abstract void initData(Bundle savedInstanceState);

    protected abstract T initPresenter();

    protected T getPresenter() {
        return presenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResources());

        initVariables(savedInstanceState);
        initData(savedInstanceState);

        this.presenter = initPresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(presenter != null) {
            presenter.onViewDestroy();
        }
    }
}
