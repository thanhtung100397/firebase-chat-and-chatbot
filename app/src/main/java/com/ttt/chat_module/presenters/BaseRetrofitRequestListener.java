package com.ttt.chat_module.presenters;

/**
 * Created by TranThanhTung on 26/03/2018.
 */

public interface BaseRetrofitRequestListener extends BaseRequestListener {
    void onNetworkConnectionError();
}
