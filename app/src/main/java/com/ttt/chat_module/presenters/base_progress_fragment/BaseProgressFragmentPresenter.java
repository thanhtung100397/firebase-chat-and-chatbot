package com.ttt.chat_module.presenters.base_progress_fragment;

import android.os.Bundle;

import com.ttt.chat_module.presenters.BasePresenter;

/**
 * Created by TranThanhTung on 19/02/2018.
 */

public abstract class BaseProgressFragmentPresenter implements BasePresenter {

    public abstract void fetchData(OnFetchDataProgressListener listener);

    public interface OnFetchDataProgressListener {
        void onFetchDataStart();
        void onFetchDataSuccess(Bundle args);
        void onFetchDataFailure(String messsage);
    }
}
