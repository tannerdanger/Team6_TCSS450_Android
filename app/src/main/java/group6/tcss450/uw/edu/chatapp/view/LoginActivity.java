package group6.tcss450.uw.edu.chatapp.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import group6.tcss450.uw.edu.chatapp.R;
import group6.tcss450.uw.edu.chatapp.utils.Credentials;
import group6.tcss450.uw.edu.chatapp.utils.WaitFragment;

public class LoginActivity extends AppCompatActivity
        implements LoginFragment.OnFragmentInteractionListener,
        RegisterFragment.OnFragmentInteractionListener,
        LoginHelpFragment.OnFragmentInteractionListener,
        PasswordResetFragment.OnFragmentInteractionListener {

    Credentials mCredentials;
    private static boolean mIsWaitFragActive;
    public static final int MIN_PASSWORD_LENGTH = 1;
    private static final String TAG = "MyLoginActivity";
    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    private static final int MY_PERMISSIONS_LOCATIONS = 8414;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private Location mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIsWaitFragActive = false;

        setContentView(R.layout.activity_login);

        if (savedInstanceState == null) {
            if (findViewById(R.id.loginContainer) != null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.loginContainer, new LoginFragment())
                        .commit();
            }
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION
                            , Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_LOCATIONS);
        } else {
            // The user has already allowed the use of Locations. Get the current location.
            requestLocation();
        }
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    mLocation = location;
                    Log.d("LOCATION UPDATE!", location.toString());
                }
            }
        };

        createLocationRequest();
    }

    protected void createLocationRequest() {
        mLocationRequest = LocationRequest.create();
        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("REQUEST LOCATION", "User did NOT allow permission to request location!");
        } else {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                Log.d("LOCATION", location.toString());
                                mLocation = location;
                            }
                        }
                    });
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_LOCATIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // locations-related task you need to do.
                    requestLocation();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d("PERMISSION DENIED", "Nothing to see or do here.");

                    // Shut down the app. In production release, you would let the user
                    // know why the app is shutting down...maybe ask for permission again?
                    finishAndRemoveTask();
                }
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void saveCredentials(final Credentials credentials) {
        SharedPreferences prefs =
                this.getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        //Store the credentials in SharedPrefs
        prefs.edit().putString(getString(R.string.keys_prefs_email), credentials.getEmail()).apply();
        prefs.edit().putString(getString(R.string.keys_prefs_password), credentials.getPassword()).apply();
    }

    private void loadFragment(final Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.loginContainer, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void onLoginSuccess(Credentials credentials) {

        onWaitFragmentInteractionHide();
        saveCredentials(credentials);
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class); //TODO: If you want to change back to drawer layout, change HomeActivity.class => MainActivity.Class

        if(null == mLocation){
            requestLocation();
        }

        //TODO: Don't comment these out. They are supposed to be working. If they are throwing erros it might be a problem on your machine...
        //Todo: ...try reinstalling the app and making sure you enable location settings
        if(null != mLocation && 0 != mLocation.getLatitude()) {
            intent.putExtra("lat", mLocation.getLatitude());
            intent.putExtra("lon", mLocation.getLongitude());
        }
        intent.putExtra(getString(R.string.ARGS_CREDENTIALS), credentials);
        startActivity(intent);
        //End this Activity and remove it from the Activity back stack.
        finish();
    }

    @Override
    public void onRegisterClicked() {
        loadFragment(new RegisterFragment());
    }

    @Override
    public void onRegisterSuccess(Credentials credentials) {
        saveCredentials(credentials);
        loadFragment(new LoginFragment());
    }

    @Override
    public void onHelpClicked() {
        loadFragment(new LoginHelpFragment());
    }

    @Override
    public void onWaitFragmentInteractionShow() {
        Log.wtf(TAG, "STARTED onWaitFragmentInteractionSHOW");
        if(!mIsWaitFragActive) { //if wait fragment isn't active
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.loginContainer, new WaitFragment(), "WAIT")
                    .addToBackStack(null)
                    .commit();

            Log.wtf(TAG, "wait frag shown");
            mIsWaitFragActive = true; //wait fragment is now active
        }
        Log.wtf(TAG, "ENDED onWaitFragmentInteractionSHOW");
    }

    @Override
    public void onWaitFragmentInteractionHide() {
        Log.wtf(TAG, "STARTED onWaitFragmentInteractionHide");
        if(mIsWaitFragActive) {//if wait fragment is active
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(getSupportFragmentManager().findFragmentByTag("WAIT"))
                    .commit();
            Log.wtf(TAG, "wait frag hidden");
            mIsWaitFragActive = false; //wait fragment is now inactive
        }
        Log.wtf(TAG, "ENDED onWaitFragmentInteractionHide");

    }

    @Override
    public void onCancelClicked() {
        loadFragment(new LoginFragment());
    }

    @Override
    public void onSubmitClicked() {
        loadFragment(new PasswordResetFragment());
    }

    @Override
    public void onSubmitButtonClicked() {
        loadFragment(new LoginFragment());
    }

    @Override
    public void onCancelButtonClicked() {
        loadFragment(new LoginFragment());
    }
}
