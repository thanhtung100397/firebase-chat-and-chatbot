package com.ttt.chat_module.views.map;

import com.ttt.chat_module.models.google_map.Address;
import com.ttt.chat_module.models.google_map.Location;

import java.util.List;

/**
 * Created by TranThanhTung on 04/02/2018.
 */

public interface LocationPickerActivityView {
    void showLocatingProgress();
    void hideLocatingProgress();
    void saveCurrentLocation(Location location, Address address);
    void showAddressLabel(String address);
    void hideAddressLabel();
    void refreshListAddressResult(List<Address> results);
}
