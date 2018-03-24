package com.ttt.chat_module.views.base.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ttt.chat_module.presenters.BasePresenter;

/**
 * Created by TranThanhTung on 25/01/2018.
 */
public abstract class BaseFragment<T extends BasePresenter> extends Fragment {
    private T presenter;

    protected abstract int getLayoutResources();

    protected abstract void initVariables(Bundle saveInstanceState, View rootView);

    protected abstract void initData(Bundle saveInstanceState);

    protected abstract T initPresenter();

    public T getPresenter() {
        return presenter;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutResources(), container, false);
        presenter = initPresenter();
        initVariables(savedInstanceState, rootView);
        initData(savedInstanceState);
        return rootView;
    }
}

