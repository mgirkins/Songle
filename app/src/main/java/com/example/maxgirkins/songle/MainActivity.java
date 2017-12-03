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
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
    //private static SongList songs;
    private static  final String PREFS = "PreferencesFile";
    private transient Date dater;
    private Boolean mapReady = false;
    private LatLng lastPosition;
    SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupView();
        dater =  new Date();

    }

    private void setupView() {
        setTitle("Songle");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawer,         /* DrawerLayout object */
                toolbar,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                TextView distance_for_song = findViewById(R.id.drawer_header_distnace_for_song);
                Double total_dist = songle.getSongs().getActiveSong().getDistanceWalked();
                distance_for_song.setText(String.format("%.02f",total_dist) + songle.getSettings().getUnits());
                ProgressBar song_completion = findViewById(R.id.drawer_header_song_completion);
                song_completion.setMax(songle.getSongs().getActiveSong().getLyrics().size());
                song_completion.setProgress(songle.getSongs().getActiveSong().getCompletedLyricsCount());
            }
        };
        drawer.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMap.clear();
        try {
            songle.saveData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i(TAG,"MapsActivity Paused");
    }
    @Override
    public  void onResume(){
        super.onResume();
        if (songle.getSettings().getNightMode()){
            setTheme(R.style.Songle_Dark);
        } else {
            setTheme(R.style.Songle_Light);
        }
        settings = songle.getSharedPreferences();
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
            mGoogleApiClient.connect();
            populateMap();

        }

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
        Integer lyricsLength = songle.getSongs().getActiveSong().getLyrics().size();

        for (int i = 0; i<lyricsLength; i++){
            Lyric l = songle.getSongs().getActiveSong().getLyrics().get(i);
            LatLng coords = l.getCoords(songle.getSettings().getDifficulty());
            Boolean collected = l.isCollected();
            if (!coords.equals(new LatLng(0.0,0.0)) && !collected && mapReady) {
                String classification = l.getClassification(songle.getSettings().getDifficulty());
                switch (classification){
                    case "unclassified":
                        l.setMapMarker(this.mMap.addMarker(new MarkerOptions().position(coords).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_unclassified))));
                        break;
                    case "boring":
                        l.setMapMarker(this.mMap.addMarker(new MarkerOptions().position(coords).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_boring))));
                        break;
                    case  "notboring":
                        l.setMapMarker(this.mMap.addMarker(new MarkerOptions().position(coords).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_notboring))));
                        break;
                    case "interesting":
                        l.setMapMarker(this.mMap.addMarker(new MarkerOptions().position(coords).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_interesting))));
                        break;
                    case "veryinteresting":
                        l.setMapMarker(this.mMap.addMarker(new MarkerOptions().position(coords).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_veryinteresting))));
                        break;
                    default:
                        break;
                }

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
        if (lastPosition != null){
            double travel = getDistanceBetween(b,lastPosition);
            songle.getSongs().getActiveSong().setDistanceWalked(travel);
            songle.getStats().addToTotalDistance(travel);
            songle.getStats().addTravels(travel,dater);
        }
        lastPosition = b;
        for (int i = 0; i<songle.getSongs().getActiveSong().getLyrics().size(); i++){
            Lyric l = songle.getSongs().getActiveSong().getLyrics().get(i);
            LatLng a = l.getCoords(songle.getSettings().getDifficulty());
            if (getDistanceBetween(b,a) < 20){
                if (!(l.isCollected())){
                    String message = "Congrats You found: " + l.getLyric().toUpperCase();
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                    l.setCollected();
                }

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
        } else if (id == R.id.statistics) {
            Intent goStats = new Intent(this, UserStatisticsActivity.class);
            startActivity(goStats);
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
        return false;
    }

    public void onLyricsDownloaded() throws InterruptedException {
        populateMap();
        Log.i(TAG,"onLyricsDownloaded Called!");
    }


}

