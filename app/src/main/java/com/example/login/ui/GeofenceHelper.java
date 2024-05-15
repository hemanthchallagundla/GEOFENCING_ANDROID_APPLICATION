package com.example.login.ui;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.example.login.GeofenceBroadcastReceiver;

public class GeofenceHelper {

    private static final String TAG = "GeofenceHelper";
    private Context context;

    public GeofenceHelper(Context context) {
        this.context = context;
    }



    public PendingIntent getPendingIntent() {
        Intent intent = new Intent(context, GeofenceBroadcastReceiver.class);
        return PendingIntent.getBroadcast(context, 2607, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    public GeofencingRequest getGeofencingRequest(Geofence geofence) {
        return new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER | GeofencingRequest.INITIAL_TRIGGER_DWELL | GeofencingRequest.INITIAL_TRIGGER_EXIT)
                .addGeofence(geofence)
                .build();
    }

    public Geofence getGeofence(String ID, LatLng lat, float radius) {
        return new Geofence.Builder()
                .setCircularRegion(lat.latitude, lat.longitude, radius)
                .setRequestId(ID)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT)
                .setLoiteringDelay(1000)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();
    }

    public String getErrorString(Exception e) {
        // Handle error codes
        return e.getLocalizedMessage();
    }

    public Context getContext() {
        return context;
    }
}
