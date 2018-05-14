package com.ttt.chat_module;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ProcessLifecycleOwner;

import com.ttt.chat_module.presenters.main.ApplicationPresenter;
import com.ttt.chat_module.presenters.main.ApplicationPresenterImpl;

/**
 * Created by Admin on 7/7/2017.
 */

public class AppStatusApplication extends Application implements LifecycleObserver {
    private ApplicationPresenter applicationPresenter;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationPresenter = new ApplicationPresenterImpl();

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    public ApplicationPresenter getApplicationPresenter() {
        return applicationPresenter;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onAppBackgrounded() {
        getApplicationPresenter().changeOnlineState(false, null);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void onAppForegrounded() {
        getApplicationPresenter().changeOnlineState(true, null);
    }
}
