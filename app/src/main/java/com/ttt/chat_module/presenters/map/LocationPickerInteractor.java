package com.ttt.chat_module.presenters.map;

import com.ttt.chat_module.presenters.BaseInteractor;

/**
 * Created by TranThanhTung on 04/02/2018.
 */

public interface LocationPickerInteractor extends BaseInteractor {
    void getMapAddress(double lat, double lon, OnGetMapAddressCompleteListener listener);
    void listAllMapAddress(String queryAddress, OnGetMapAddressCompleteListener listener);
    void cancelGetMapAddressRequest();
}
