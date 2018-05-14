package com.ttt.chat_module.presenters.map;

import com.ttt.chat_module.presenters.BasePresenter;

/**
 * Created by TranThanhTung on 04/02/2018.
 */

public interface LocationPickerPresenter extends BasePresenter {
    void fetchMapAddress(double lat, double lon);
    void queryMapAddress(String queryKey);
}
