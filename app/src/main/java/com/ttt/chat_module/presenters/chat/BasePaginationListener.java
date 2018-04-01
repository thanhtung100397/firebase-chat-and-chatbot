package com.ttt.chat_module.presenters.chat;

import com.ttt.chat_module.presenters.BaseRequestListener;

/**
 * Created by TranThanhTung on 21/03/2018.
 */

public interface BasePaginationListener<T> extends BaseRequestListener {
    void onLastElementFetched(T element, boolean hasNoElementLeft);
}
