package com.ttt.chat_module.models.google_map;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ttt.chat_module.common.Constants;

/**
 * Created by TranThanhTung on 17/12/2017.
 */

public class Address {
    @SerializedName("formatted_address")
    @Expose
    private String formattedAddress;
    @SerializedName("geometry")
    @Expose
    private Geometry geometry;

    public String getFormattedAddress() {
        formattedAddress = formattedAddress.replaceAll(Constants.UNNAMED_ROAD + ", ", "");
        formattedAddress = formattedAddress.replaceAll(", " + Constants.VIETNAM, "");
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }
}
