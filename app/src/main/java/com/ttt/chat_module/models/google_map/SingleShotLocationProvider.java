package com.ttt.chat_module.models.google_map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * Created by TranThanhTung on 17/12/2017.
 */

public class SingleShotLocationProvider {

    @SuppressLint("MissingPermission")
    public static void getCurrentLocation(Context context, OnSuccessListener<Location> callback) {
        LocationServices.getFusedLocationProviderClient(context)
                .getLastLocation()
                .addOnSuccessListener(callback);
    }
}
