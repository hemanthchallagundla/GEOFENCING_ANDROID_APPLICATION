package com.example.login;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.login.ui.setg.SetgFragment;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;


public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "GeofenceBroadcastReceiver";


    @Override
    public void onReceive(Context context, Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent == null) {
            Log.e(TAG, "GeofencingEvent is null");
       //     showNotification(context, "Geofence Alert", "You have entered the geofence");

            return;
        }
        SetgFragment setgFragment = new SetgFragment();
        setgFragment.testGeofence();

        if (geofencingEvent.hasError()) {
            int errorCode = geofencingEvent.getErrorCode();
            Log.e(TAG, "GeofencingEvent error: " + errorCode);
            return;
        }

        // Get the geofence transition type
        int transitionType = geofencingEvent.getGeofenceTransition();

        // Handle each type of geofence transition
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                Log.d(TAG, "Geofence transition: ENTER");
                showNotification(context, "Geofence Alert", "You have entered the geofence");

                // Add your logic for entering the geofence here
                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                Log.d(TAG, "Geofence transition: EXIT");
                showNotification(context, "Geofence Alert", "You have  exited geofence");
                // Add your logic for exiting the geofence here
                break;
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                Log.d(TAG, "Geofence transition: DWELL");
                showNotification(context, "Geofence Alert", "You have dwell the geofence");
                // Add your logic for dwelling inside the geofence here
                break;
            default:
                Log.e(TAG, "Unknown geofence transition type: " + transitionType);
                break;
        }
    }
    private void showNotification(Context context, String title, String message) {
        NotificationHelper notificationHelper = new NotificationHelper(context);
        notificationHelper.sendHighPriorityNotification(title, message, MainActivity.class);
    }
}

