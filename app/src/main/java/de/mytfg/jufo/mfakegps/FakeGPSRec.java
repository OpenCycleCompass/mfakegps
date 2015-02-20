/**
 * Created by raphael on 19.02.15.
 */
package de.mytfg.jufo.mfakegps;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class FakeGPSRec extends Service implements LocationListener, OnConnectionFailedListener, ConnectionCallbacks {
        // Log TAG
        protected static final String TAG = "IBisTracking-class";
        // The desired interval for location updates. Inexact. Updates may be more or less frequent.
        private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
        // The fastest rate for active location updates. Exact. Updates will never be more frequent than this value.
        private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
                UPDATE_INTERVAL_IN_MILLISECONDS / 2;
        // Provides the entry point to Google Play services.
        protected GoogleApiClient mGoogleApiClient;
        //Stores parameters for requests to the FusedLocationProviderApi.
        protected LocationRequest mLocationRequest;
        //location vars
        protected Location mCurrentLocation;

        private FakeGPSDatabase mGPSDb;

        // Notification
        // Sets an ID for the notification
        private int mNotificationId = 42;
        private NotificationCompat.Builder mBuilder;
        // Gets an instance of the NotificationManager service
        private NotificationManager mNotifyMgr;

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

    public void stopLocationUpdates() {
        Log.i(TAG, "stopLocationUpdates()");
        // Stop LocationListener
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
        int mNotificationId = 42;
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.cancel(mNotificationId);
    }

    protected void startLocationUpdates() {
        Log.i(TAG, "startLocationUpdates()");
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     */
    protected void buildGoogleApiClient() {
        Log.i(TAG, "buildGoogleApiClient()");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        createLocationRequest();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        // Positionsbestimmung mindestens ca. alle 5 Sekunden (5000ms)
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        // Positionsbestimmung höchstens jede Sekunde (1000ms)
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        // Hohe Genauigkeit
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private String roundDecimals(double d) {
        return String.format("%.2f", d);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "onLocationChanged()");
        updateDatabase(location);
        //update Notification
        int num_rows = mGPSDb.getNumRows();
        // Update notification
        mBuilder.setContentText(Integer.toString(num_rows) + " Coordinates");
        // Sets an ID for the notification
        int mNotificationId = 42;
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }



    private void updateDatabase(Location location) {
        Log.i(TAG, "updateDatabase()");
        mGPSDb.insertLocation(location);
    }


    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate()");
        super.onCreate();

        // Create Database
        mGPSDb = new FakeGPSDatabase(this.getApplicationContext());
        // Delete old data from database
        mGPSDb.deleteDatabase();

        // Create Notification
        Intent fakeGps_showIntent = new Intent(this, FakeGPSRec.class);
        fakeGps_showIntent.setFlags(fakeGps_showIntent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        PendingIntent fakeGps_showPendingIntent = PendingIntent.getActivity(this, 0, fakeGps_showIntent, 0);
        mBuilder = new NotificationCompat.Builder(this);

        mBuilder.setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(getString(R.string.app_name) + " Recording")
                .setContentIntent(fakeGps_showPendingIntent) // default action (sole action on
                        // android < 4.2) is to start ShowDataActivity
                .setOngoing(true); // notification is permanent

        // Gets an instance of the NotificationManager service
        mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        //build and connect Api Client
        buildGoogleApiClient();
        mGoogleApiClient.connect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy()");
        stopLocationUpdates();
        mGoogleApiClient.disconnect();
        super.onDestroy();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "onConnected()");
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "onConnectionSuspended()");
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "onConnectionFailed()");
        /* Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        onConnectionFailed.
        -> Wir tun nix
        */
    }

}

