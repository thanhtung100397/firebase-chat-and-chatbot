package com.ttt.chat_module.presenters.base_progress;

import android.os.Bundle;

import com.ttt.chat_module.presenters.BasePresenter;

/**
 * Created by TranThanhTung on 19/02/2018.
 */

public abstract class BaseProgressActivityPresenter implements BasePresenter {

    public abstract void fetchData(OnFetchDataProgressListener listener);

    public interface OnFetchDataProgressListener {
        void onFetchDataSuccess(Bundle args);
        void onFetchDataFailure(String message);
    }
}
