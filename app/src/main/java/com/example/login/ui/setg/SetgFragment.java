package com.example.login.ui.setg;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.login.R;
import com.example.login.databinding.FragmentSetgBinding;
import com.example.login.ui.GeofenceHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SetgFragment extends Fragment implements GoogleMap.OnMapLongClickListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback {
    private static final String TAG = "SetgFragment";

    private FragmentSetgBinding binding;
    private GeofencingClient geofencingClient;
    private GeofenceHelper geofenceHelper;

    private GoogleMap myMap;
    private final int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
    private final int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002;
    private static final float GEOFENCE_RADIUS = 100;
    private static final String GEOFENCE_ID = "SOME_ID";
    private Context context;

    private LatLng geofenceLatLng;

    public SetgFragment(Context context) {
        this.context = context;
    }

    public SetgFragment() {
        // Required empty public constructor
    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSetgBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        geofenceHelper = new GeofenceHelper(getActivity());
        geofencingClient = LocationServices.getGeofencingClient(requireContext());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        return root;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;

        enableUserLocation();

        if (geofenceLatLng != null) {
            addMarker(geofenceLatLng);
            addCircle(geofenceLatLng, GEOFENCE_RADIUS);
            addGeofence(geofenceLatLng, GEOFENCE_RADIUS);
        }

        myMap.setOnMapLongClickListener(this);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (geofenceLatLng != null) {
            outState.putParcelable("geofenceLatLng", geofenceLatLng);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            geofenceLatLng = savedInstanceState.getParcelable("geofenceLatLng");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableUserLocation();
            }
        }

        if (requestCode == BACKGROUND_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "You can add geofences...", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Background location access is necessary for geofences to trigger...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void enableUserLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            myMap.setMyLocationEnabled(true);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
            }
        }
    }


    @Override
    public void onMapLongClick(LatLng latLng) {
        handleMapLongClick(latLng);
        // Get the context from the View parameter
        Context context = binding.getRoot().getContext();
        // Check if the current location is inside the geofence
        testGeofence();
    }

    // Change the method signature to accept Context as a parameter
    public void testGeofence() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location lastKnownLocation = myMap.getMyLocation();
            if (lastKnownLocation != null) {
                LatLng currentLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                if (geofenceLatLng != null) {
                    float[] distance = new float[1];
                    Location.distanceBetween(geofenceLatLng.latitude, geofenceLatLng.longitude, currentLocation.latitude, currentLocation.longitude, distance);
                    if (distance[0] <= GEOFENCE_RADIUS) {
                        Log.d(TAG, "testGeofence: Inside the geofence");
                        // User is inside the geofence
                        // Add your logic here
                    } else {
                        Log.d(TAG, "testGeofence: Outside the geofence");
                        // User is outside the geofence
                        // Add your logic here
                    }
                }
            }
        } else {
            Log.d(TAG, "testGeofence: Permission not granted");
            // Permission not granted
            // You can request the permission here
        }
    }


    private void handleMapLongClick(LatLng latLng) {
        myMap.clear();
        addMarker(latLng);
        addCircle(latLng, GEOFENCE_RADIUS);
        addGeofence(latLng, GEOFENCE_RADIUS);
        geofenceLatLng = latLng;
    }

    private void addGeofence(LatLng latLng, float radius) {
        Geofence geofence = geofenceHelper.getGeofence(GEOFENCE_ID, latLng, radius);
        GeofencingRequest geofencingRequest = geofenceHelper.getGeofencingRequest(geofence);
        PendingIntent pendingIntent = geofenceHelper.getPendingIntent();

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                .addOnSuccessListener(getActivity(), aVoid -> Log.d(TAG, "onsuccess: Geofence Added...."))
                .addOnFailureListener(getActivity(), e -> {
                    String errorMessage = geofenceHelper.getErrorString(e);
                    Log.d(TAG, "onFailure: " + errorMessage);
                });
    }

    private void addMarker(LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions().position(latLng);
        myMap.addMarker(markerOptions);
    }

    private void addCircle(LatLng latLng, float radius) {
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(radius);
        circleOptions.strokeColor(Color.argb(255, 255, 0, 00));
        circleOptions.fillColor(Color.argb(64, 255, 0, 0));
        myMap.addCircle(circleOptions);
    }

    private void checkLocationInGeofence(LatLng currentLocation) {
        float[] distance = new float[2];

        Location.distanceBetween(
                currentLocation.latitude,
                currentLocation.longitude,
                geofenceLatLng.latitude,
                geofenceLatLng.longitude,
                distance
        );

        if (distance[0] <= GEOFENCE_RADIUS) {
            Log.d(TAG, "Current location is inside the geofence.");
        } else {
            Log.d(TAG, "Current location is outside the geofence.");
        }
    }

    // Call this method when you want to check if the current location is within the geofence.


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResult(@NonNull Result result) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
