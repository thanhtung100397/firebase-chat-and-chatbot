package com.ttt.chat_module.views.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ttt.chat_module.R;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.common.adapter.recycler_view_adapter.AddressAdapter;
import com.ttt.chat_module.common.recycler_view_adapter.RecyclerViewAdapter;
import com.ttt.chat_module.common.utils.ToastUtils;
import com.ttt.chat_module.models.google_map.Address;
import com.ttt.chat_module.models.google_map.Location;
import com.ttt.chat_module.models.google_map.SingleShotLocationProvider;
import com.ttt.chat_module.presenters.map.LocationPickerPresenter;
import com.ttt.chat_module.presenters.map.LocationPickerPresenterImpl;
import com.ttt.chat_module.views.base.activity.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LocationPickerActivity extends BaseActivity<LocationPickerPresenter> implements LocationPickerActivityView,
        OnMapReadyCallback,
        SearchView.OnQueryTextListener,
        GoogleMap.OnMapClickListener,
        View.OnClickListener,
        RecyclerViewAdapter.OnItemClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final int REQUEST_ACCESS_LOCATION_REQUEST_CODE = 0;
    private static final int REQUEST_CODE_ENABLE_GPS = 1;

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.map_view)
    MapView mapView;
    @BindView(R.id.card_address)
    CardView cardAddress;
    @BindView(R.id.txt_address)
    TextView textAddress;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.fab_my_location)
    FloatingActionButton fabMyLocation;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    GoogleMap googleMap;
    Marker shopMarker;

    private boolean isMarkerUnChanged = false;
    private boolean isMarkerAtMyLocation = false;

    private AddressAdapter addressAdapter;
    private Location location;
    private String address;
    private SearchView searchView;
    private MenuItem searchItem;

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected int getLayoutResources() {
        return R.layout.activity_location_picker;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        ButterKnife.bind(this);

        Intent intent = getIntent();
        location = (Location) intent.getSerializableExtra(Constants.LOCATION);
        address = intent.getStringExtra(Constants.ADDRESS);

        initToolBar();

        initListFoundAddressResult();

        initMap(savedInstanceState);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected LocationPickerPresenter initPresenter() {
        return new LocationPickerPresenterImpl(this, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_tool_bar, menu);
        searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                if (addressAdapter.getItemCount() != 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                }
            } else {
                recyclerView.setVisibility(View.INVISIBLE);
            }
        });

        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getString(R.string.enter_address));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
            }
            break;

            case R.id.action_submit: {
                Intent intent = new Intent();
                intent.putExtra(Constants.ADDRESS, address);
                intent.putExtra(Constants.LOCATION, location);
                setResult(RESULT_OK, intent);
                finish();
            }
            break;

            default: {
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void initListFoundAddressResult() {
        addressAdapter = new AddressAdapter(this);
        addressAdapter.addOnItemClickListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, linearLayoutManager.getOrientation()));
        recyclerView.setAdapter(addressAdapter);
    }

    private void initMap(Bundle savedInstanceState) {
        fabMyLocation.setOnClickListener(this);
        fabMyLocation.setVisibility(View.GONE);

        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        MapsInitializer.initialize(this);
        mapView.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    private void initToolBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.pick_address);
        }
    }

    public void showShopMarkerAt(LatLng latLng, String address) {
        if (shopMarker == null) {
            shopMarker = googleMap.addMarker(new MarkerOptions()
                    .position(latLng));
        } else {
            shopMarker.setPosition(latLng);
        }

        if (address == null || address.isEmpty()) {
            getPresenter().fetchMapAddress(latLng.latitude, latLng.longitude);
        } else {
            showAddressLabel(address);
            this.address = address;
            this.location.setLat(latLng.latitude);
            this.location.setLng(latLng.longitude);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        googleMap.setOnMapClickListener(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String permissions[] = new String[2];
            permissions[0] = Manifest.permission.ACCESS_FINE_LOCATION;
            permissions[1] = Manifest.permission.ACCESS_COARSE_LOCATION;
            ActivityCompat.requestPermissions(this, permissions, REQUEST_ACCESS_LOCATION_REQUEST_CODE);
        } else {
            initMapViews(true);
        }
        showCompassButton();
    }

    private void initMapViews(boolean hasPermission) {
        if(hasPermission) {
            setupButton();

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            mGoogleApiClient.connect();
        } else {
            fabMyLocation.setVisibility(View.GONE);
        }
        if (location != null) {
            translateToLocation(location, address);
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if(location == null) {
            return;
        }
        this.location.setLat(latLng.latitude);
        this.location.setLng(latLng.longitude);
        showShopMarkerAt(latLng, null);
        isMarkerUnChanged = false;
        isMarkerAtMyLocation = false;
    }

    private boolean isGPSEnable() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (manager == null) {
            ToastUtils.quickToast(this, "LOCATION_SERVICE not available");
            return false;
        }
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void showGPSEnableRequestDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.gps_title)
                .setMessage(R.string.gps_message)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, (dialog, id) -> startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), REQUEST_CODE_ENABLE_GPS))
                .setNegativeButton(R.string.cancel, (dialog, id) -> {
                    fabMyLocation.setVisibility(View.GONE);
                    if (location != null) {
                        translateToLocation(location, address);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void showCompassButton() {
        View compassButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("5"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) compassButton.getLayoutParams();
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 0, 100);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.isEmpty()) {
            addressAdapter.clear();
            recyclerView.setVisibility(View.INVISIBLE);
        } else {
            getPresenter().queryMapAddress(newText);
        }
        return false;
    }

    @SuppressLint("MissingPermission")
    private void translateToMyLocationIfPossible() {
        if (isMarkerAtMyLocation) {
            return;
        }

        SingleShotLocationProvider.getCurrentLocation(this, location -> {
            if (location == null) {
                Toast.makeText(this, R.string.auto_locate_failed, Toast.LENGTH_SHORT).show();
                return;
            }

            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(15).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            isMarkerAtMyLocation = true;

            showShopMarkerAt(new LatLng(location.getLatitude(), location.getLongitude()), null);
        });
    }

    private void translateToLocation(Location location, String address) {
        if (!isMarkerUnChanged) {
            isMarkerUnChanged = true;
            LatLng latLng = new LatLng(location.getLat(), location.getLng());
            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(15).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            showShopMarkerAt(new LatLng(location.getLat(), location.getLng()), address);
        }
    }

    @SuppressLint("MissingPermission")
    public void setupButton() {
        googleMap.setMyLocationEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ACCESS_LOCATION_REQUEST_CODE: {
                boolean hasPermission = true;
                for (int grantResult : grantResults) {
                    if (grantResult == PackageManager.PERMISSION_DENIED) {
                        hasPermission = false;
                        break;
                    }
                }
                initMapViews(hasPermission);
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_ENABLE_GPS: {
                if (resultCode == RESULT_OK) {
                    translateToMyLocationIfPossible();
                }
            }
            break;

            default: {
                break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_my_location: {
                translateToMyLocationIfPossible();
            }
            break;

            default: {
                break;
            }
        }
    }

    @Override
    public void onItemClick(RecyclerView.Adapter adapter, RecyclerView.ViewHolder viewHolder, int viewType, int position) {
        searchView.clearFocus();
        Address address = addressAdapter.getItem(position, Address.class);
        isMarkerUnChanged = false;
        translateToLocation(address.getGeometry().getLocation(), address.getFormattedAddress());
        searchView.onActionViewCollapsed();
    }

    @Override
    public void showLocatingProgress() {
        cardAddress.setVisibility(View.VISIBLE);
        textAddress.setText(R.string.locating);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLocatingProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void saveCurrentLocation(Location location, Address address) {
        this.address = address.getFormattedAddress();
        this.location = location;
    }

    @Override
    public void showAddressLabel(String address) {
        if (cardAddress.getVisibility() == View.INVISIBLE) {
            cardAddress.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
        textAddress.setText(address);
    }

    @Override
    public void hideAddressLabel() {
        cardAddress.setVisibility(View.INVISIBLE);
    }

    @Override
    public void refreshListAddressResult(List<Address> results) {
        if (results == null || results.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
        }
        addressAdapter.refresh(results);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        fabMyLocation.setVisibility(View.VISIBLE);
        if (isGPSEnable()) {
            translateToMyLocationIfPossible();
        } else {
            showGPSEnableRequestDialog();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        ToastUtils.quickToast(this, R.string.cannot_locate);
        fabMyLocation.setVisibility(View.VISIBLE);
    }
}
