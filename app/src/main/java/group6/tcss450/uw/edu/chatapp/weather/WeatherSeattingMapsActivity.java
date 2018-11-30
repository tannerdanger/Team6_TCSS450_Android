package group6.tcss450.uw.edu.chatapp.weather;

<<<<<<< HEAD
=======
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
>>>>>>> 30fd5e3a4146fc36f518853d534d3fb37bef1b5a
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
<<<<<<< HEAD
=======
import android.widget.Toast;
>>>>>>> 30fd5e3a4146fc36f518853d534d3fb37bef1b5a

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import group6.tcss450.uw.edu.chatapp.R;

<<<<<<< HEAD
=======
import static android.location.LocationManager.GPS_PROVIDER;

>>>>>>> 30fd5e3a4146fc36f518853d534d3fb37bef1b5a
public class WeatherSeattingMapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private Location mCurrentLocation;
<<<<<<< HEAD
=======
    private double mLat;
    private double mLon;
>>>>>>> 30fd5e3a4146fc36f518853d534d3fb37bef1b5a

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_seatting_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
<<<<<<< HEAD
        mCurrentLocation = (Location) getIntent().getParcelableExtra("LOCATION");
=======
        mCurrentLocation = new Location(GPS_PROVIDER);

        mLat = getIntent().getDoubleExtra("lat", -1 );
        mLon = getIntent().getDoubleExtra("lon", -1 );

        if( -1 != mLat && -1 != mLon) {
            mCurrentLocation.setLatitude(mLat);
            mCurrentLocation.setLongitude(mLon);
        }

>>>>>>> 30fd5e3a4146fc36f518853d534d3fb37bef1b5a
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
<<<<<<< HEAD
=======

>>>>>>> 30fd5e3a4146fc36f518853d534d3fb37bef1b5a
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in the current device location and move the camera
        LatLng current = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
<<<<<<< HEAD
=======



>>>>>>> 30fd5e3a4146fc36f518853d534d3fb37bef1b5a
        mMap.addMarker(new MarkerOptions().position(current).title("Current Location"));
        //Zoom levels are from 2.0f (zoomed out) to 21.f (zoomed in)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 15.0f));
        mMap.setOnMapClickListener(this);
    }


    @Override
    public void onMapClick(LatLng latLng) {
<<<<<<< HEAD
        Log.d("LAT/LONG", latLng.toString());
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("New Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.0f));
    }
=======

        mMap.clear(); //remove old marker





        Log.d("LAT/LONG", latLng.toString());
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("New Marker")
                );

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 8.0f));
        updateWeather(marker.getPosition());

    }

    private void updateWeather(LatLng position) {
        Intent i = new Intent();
        i.putExtra("lat", position.latitude);
        i.putExtra("lon", position.longitude);
        setResult(RESULT_OK, i);
        finish();
    }




>>>>>>> 30fd5e3a4146fc36f518853d534d3fb37bef1b5a
}


