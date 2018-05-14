package com.ttt.chat_module.services.google_map;

import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.models.google_map.GoogleAddressResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by TranThanhTung on 17/12/2017.
 */

public interface GetMapAddressService {
    @GET("maps/api/geocode/json")
    Observable<GoogleAddressResponse> getAddress(@Query("latlng") String latlon, @Query(Constants.KEY) String apiKey);
}
