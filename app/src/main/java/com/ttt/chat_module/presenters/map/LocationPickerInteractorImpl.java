package com.ttt.chat_module.presenters.map;

import android.content.Context;

import com.ttt.chat_module.R;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.models.google_map.GoogleAddressResponse;
import com.ttt.chat_module.services.google_api.GoogleApiClient;
import com.ttt.chat_module.services.google_api.ResponseObserver;
import com.ttt.chat_module.services.google_map.GetMapAddressService;
import com.ttt.chat_module.services.google_map.GetMapLatLonService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by TranThanhTung on 04/02/2018.
 */

public class LocationPickerInteractorImpl implements LocationPickerInteractor {
    private Context context;
    private CompositeDisposable compositeDisposable;
    private Disposable activeLatLngQueryDisposable;
    private Disposable activeLocatingDisposable;

    public LocationPickerInteractorImpl(Context context) {
        this.context = context;
        this.compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onViewDestroy() {
        compositeDisposable.clear();
    }

    @Override
    public void cancelGetMapAddressRequest() {
        if (activeLocatingDisposable != null && !activeLocatingDisposable.isDisposed()) {
            activeLocatingDisposable.dispose();
        }
    }

    @Override
    public void getMapAddress(double lat, double lon, OnGetMapAddressCompleteListener listener) {
        if (activeLocatingDisposable != null && !activeLocatingDisposable.isDisposed()) {
            activeLocatingDisposable.dispose();
        }
        String latLng = lat + "," + lon;
        activeLocatingDisposable = GoogleApiClient.getClient(Constants.GOOGLE_MAP_API, context)
                .create(GetMapAddressService.class)
                .getAddress(latLng, context.getString(R.string.google_maps_key))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribeWith(new ResponseObserver<GoogleAddressResponse>() {
                    @Override
                    public void onSuccess(GoogleAddressResponse response) {
                        listener.onGetAddressSuccess(response);
                    }

                    @Override
                    public void onServerError(String message) {
                        listener.onRequestError(message);
                    }

                    @Override
                    public void onNetworkConnectionError() {
                        listener.onNetworkConnectionError();
                    }
                });
        compositeDisposable.add(activeLocatingDisposable);
    }

    @Override
    public void listAllMapAddress(String queryAddress, OnGetMapAddressCompleteListener listener) {
        if (activeLatLngQueryDisposable != null && !activeLatLngQueryDisposable.isDisposed()) {
            activeLatLngQueryDisposable.dispose();
        }
        activeLatLngQueryDisposable = GoogleApiClient.getClient(Constants.GOOGLE_MAP_API, context)
                .create(GetMapLatLonService.class)
                .queryAddress(queryAddress, context.getString(R.string.google_maps_key))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribeWith(new ResponseObserver<GoogleAddressResponse>() {
                    @Override
                    public void onSuccess(GoogleAddressResponse response) {
                        listener.onGetAddressSuccess(response);
                    }

                    @Override
                    public void onServerError(String message) {
                        listener.onRequestError(message);
                    }

                    @Override
                    public void onNetworkConnectionError() {
                        listener.onNetworkConnectionError();
                    }
                });
        compositeDisposable.add(activeLatLngQueryDisposable);
    }
}
