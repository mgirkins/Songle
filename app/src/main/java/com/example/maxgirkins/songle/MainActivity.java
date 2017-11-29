package com.example.maxgirkins.songle;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import com.google.android.gms.location.LocationListener;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Date;

import static com.example.maxgirkins.songle.Songle.songle;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted = false;
    private Location mLastLocation;
    private NetworkReceiver receiver = new NetworkReceiver();
    private static final String TAG = "MapsActivity";
    private static SongList songs;
    private static  final String PREFS = "PreferencesFile";
    private transient Date dater;
    private Boolean mapReady = false;
    SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Songle");
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        dater =  new Date();
        songs = songle.getSongs();

    }

    @Override
    protected void onPause() {
        super.onPause();
        songle.setSongs(songs);
        Log.i(TAG,"MapsActivity Paused");
    }
    @Override
    public  void onResume(){
        super.onResume();
        songs = songle.getSongsWhenExist();
        settings = songle.getSettings();
        Log.i(TAG,"MapsAcvtivity resumed");
        if (mMap == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync( this);
            if (mGoogleApiClient == null) {
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();
            }
            mGoogleApiClient.connect();

        }
        if (mapReady){
            populateMap();

        }

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

        }

        return super.onOptionsItemSelected(item);
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.i(TAG, "Map ready");
        mapReady = true;
        // Add a marker in Edinburgh and move the camera
        LatLng edinburghCentral = new LatLng(55.944425, -3.188396);
        //mMap.addMarker(new MarkerOptions().position(edinburghCentral));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(edinburghCentral, 16));


        try {
            // Visualise current position with a small blue circle
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } catch (SecurityException se) {
            System.out.println("Security exception thrown [onMapReady]");
        }

        // Add ‘‘My location’’ button to the user interface
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
        populateMap();


    }

    public void populateMap() {
        Log.i(TAG, "PopulateMap Called");
        Integer lyricsLength = songs.getActiveSong().getLyrics().size();

        for (int i = 0; i<lyricsLength; i++){
            Lyric l = songle.getSongsWhenExist().getActiveSong().getLyrics().get(i);
            LatLng coords = l.getCoords(songle.getLevel());
            Boolean collected = l.isCollected();
            if (!coords.equals(new LatLng(0.0,0.0)) && !collected && mapReady) {
                l.setMapMarker(this.mMap.addMarker(new MarkerOptions().position(coords)));
            }
        }
    }
    public static double getDistanceBetween(LatLng first, LatLng second) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(first.latitude - second.latitude);
        double lonDistance = Math.toRadians(first.longitude - second.longitude);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(first.latitude)) * Math.cos(Math.toRadians(second.latitude))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters


        distance = Math.pow(distance, 2);

        return Math.sqrt(distance);
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        try {
            songle.saveData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void createLocationRequest() {
        // Set the parameters for the location request
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); // preferably every 5 seconds
        mLocationRequest.setFastestInterval(1000); // at most every second
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Can we access the user’s current location?
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        try {
            createLocationRequest();
        } catch (java.lang.IllegalStateException ise) {
            System.out.println("IllegalStateException thrown [onConnected]");
        }
        // Can we access the user’s current location?
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            mLastLocation =
                    LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        LatLng b = new LatLng(location.getLatitude(), location.getLongitude());
        for (int i=0; i<songs.getActiveSong().getLyrics().size();i++){
            Lyric l = songs.getActiveSong().getLyrics().get(i);
            LatLng a = l.getCoords(songle.getLevel());
            if (getDistanceBetween(b,a) < 20){
                l.setCollectedAt(dater.getTime());
                try {
                    l.getMapMarker().remove();
                } catch (NullPointerException n){
                    n.printStackTrace();
                }

            }
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(b));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_bag) {
            Intent goBag = new Intent(this, word_bag.class);
            startActivity(goBag);
        } else if (id == R.id.nav_guess) {
            Intent goGuess = new Intent(this, Guess.class);
            startActivity(goGuess);
        } else if (id == R.id.nav_help) {
            Intent goHelp = new Intent(this, Help.class);
            startActivity(goHelp);
        } else if (id == R.id.nav_manage) {
            Intent goSettings = new Intent(this, SettingsActivity.class);
            startActivity(goSettings);
        } else if (id == R.id.completed_songs) {
            Intent goGuess = new Intent(this, Completed_songs_activity.class);
            startActivity(goGuess);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void onLyricsDownloaded() throws InterruptedException {
        songs = songle.getSongsWhenExist();
        populateMap();
        Log.i(TAG,"onLyricsDownloaded Called!");
    }


}

