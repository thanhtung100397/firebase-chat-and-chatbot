package com.ttt.chat_module.presenters.map;

import com.ttt.chat_module.models.google_map.GoogleAddressResponse;
import com.ttt.chat_module.presenters.BaseRetrofitRequestListener;

/**
 * Created by TranThanhTung on 04/02/2018.
 */

public interface OnGetMapAddressCompleteListener extends BaseRetrofitRequestListener {
    void onGetAddressSuccess(GoogleAddressResponse addressResponse);
}
