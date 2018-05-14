package com.ttt.chat_module.models.google_map;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by TranThanhTung on 18/12/2017.
 */

public class Geometry {
    @SerializedName("location")
    @Expose
    Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
