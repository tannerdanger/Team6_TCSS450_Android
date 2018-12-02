package group6.tcss450.uw.edu.chatapp.weather;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Random;

import group6.tcss450.uw.edu.chatapp.R;
import group6.tcss450.uw.edu.chatapp.utils.PlaceAutocompleteAdapter;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link weatherSeattingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class weatherSeattingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private weatherSeattingFragment.OnSettingsFragmentInteractionListener mListener;

    // TODO: Rename and change types of parameters

    int mycolor = Color.WHITE;
    private TextView txtColor;
    Button myButton;
    private double mLat;
    private double mLong;
    private AutoCompleteTextView mCityTextView;
    private EditText zipcode;
    private Button Map;
    private Button Search;
    private Button myThemeButton;
    private GeoDataClient mGeoDataClient;
    PlaceAutocompleteAdapter mAdapter;
    private String myPrmaryColor;
    private String SecondaryColor;
    private String ThirdColor;
    private String BackGroundColor;
    private TextView mLatView;
    private TextView mLonView;
    private String mCityName;



    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));


    public weatherSeattingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment weatherSeattingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static weatherSeattingFragment newInstance(String param1, String param2) {
        weatherSeattingFragment fragment = new weatherSeattingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLat = getArguments().getDouble("lat");
            mLong = getArguments().getDouble("lon");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_weather_seatting, container, false);
        mCityTextView = (AutoCompleteTextView) view.findViewById(R.id.settings_autoCompleteTextView);
        zipcode = (EditText) view.findViewById(R.id.zipcode_editText);
        Map = (Button) view.findViewById(R.id.Map_button);
        Search = (Button) view.findViewById(R.id.search_button);
        txtColor = (TextView) view.findViewById(R.id.txtColor);
        myButton = (Button) view.findViewById(R.id.myButton);
        myThemeButton = (Button) view.findViewById(R.id.button_settings_commit);
        mLatView = (TextView) view.findViewById(R.id.lat_textview);
        mLonView = (TextView) view.findViewById(R.id.lon_textview);

        myButton.setOnClickListener(l -> {
            pickColor(l);
        });

        //this is for the button setting commit
        new ThemeColors(this.getContext());
        myThemeButton.setOnClickListener(l2 -> {

            int red= new Random().nextInt(255);
            int green= new Random().nextInt(255);
            int blue= new Random().nextInt(255);
            ThemeColors.setNewThemeColor(this.getActivity(), red, green, blue);

        });

        Search.setOnClickListener(l -> {

            //if map has selected coords, update coords
            if(mLatView.getText().toString().compareTo("") != 0){
                mListener.onNewLatLon(mLat, mLong);

            } else {

                //if city has been selected, update city
                if (null != mCityName) {


                    mListener.onNewCity(mCityName);
                    mCityName = null; //return city name to null for next search
                    //else update zip code
                } else if ("".compareTo(zipcode.getText().toString()) != 0
                        && zipcode.getText().toString().length() > 4) {


                    int zip = Integer.parseInt(zipcode.getText().toString());


                    mListener.onNewZipcode(zip);

                }
            }

        });

        mGeoDataClient = mListener.getClient();
        if( null != mGeoDataClient) {
            mAdapter = mListener.getAdapter();
            mCityTextView.setAdapter(mAdapter);
            mCityTextView.setThreshold(3);
            mCityTextView.setOnItemClickListener(mAutocompleteClickListener);

        }



        Map.setOnClickListener(l ->{
            mListener.loadMap();
        });

        return view;
    }

    @SuppressLint("SetTextI18n")
    public void getCoordsFromMap(double lat, double lon){
        //TODO: Change this so it updates a lat/long box instead. Until then, it will just update without checking with the user first.

        mLat = lat;
        mLong = lon;

        TextView tv = getView().findViewById(R.id.lat_textview);
        tv.setText("lat:  " + lat);
        tv = getView().findViewById(R.id.lon_textview);
        tv.setText("lon:  " + lon);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof weatherSeattingFragment.OnSettingsFragmentInteractionListener) {
            mListener = (weatherSeattingFragment.OnSettingsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnOpenMessageFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    /**
     * @author Tanner Brown
     */
    public interface OnSettingsFragmentInteractionListener {
        void onNewZipcode(int zip);
        void onNewCity(String city);
        void onNewLatLon(double lat, double lon);
        void loadMap();
        GeoDataClient getClient();
        PlaceAutocompleteAdapter getAdapter();
    }

    /**
     * Listener that handles selections from suggestions from the AutoCompleteTextView that
     * displays Place suggestions.
     * Gets the place id of the selected item and issues a request to the Places Geo Data Client
     * to retrieve more details about the place.
     *
     * @see GeoDataClient#getPlaceById(String...)
     */
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            Log.i(TAG, "Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data Client to retrieve a Place object with
             additional details about the place.
              */
            Task<PlaceBufferResponse> placeResult = mGeoDataClient.getPlaceById(placeId);
            placeResult.addOnCompleteListener(mUpdatePlaceDetailsCallback);

            Toast.makeText(getContext(), "Clicked: " + primaryText,
                    Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    };

    /**
     * Callback for results from a Places Geo Data Client query that shows the first place result in
     * the details view on screen.
     */
    private OnCompleteListener<PlaceBufferResponse> mUpdatePlaceDetailsCallback
            = new OnCompleteListener<PlaceBufferResponse>() {
        @Override
        public void onComplete(Task<PlaceBufferResponse> task) {
            try {
                PlaceBufferResponse places = task.getResult();

                // Get the Place object from the buffer.
                final Place place = places.get(0);

                // Format details of the place for display and show it in a TextView.
//                mCityTextView.setText(formatPlaceDetails(getResources(), place.getName(),
//                        place.getId(), place.getAddress(), place.getPhoneNumber(),
//                        place.getWebsiteUri()));
                mCityTextView.setText(place.getName().toString());
                mCityName = place.getName().toString();


//                // Display the third party attributions if set.
//                final CharSequence thirdPartyAttribution = places.getAttributions();
//                if (thirdPartyAttribution == null) {
//                    mPlaceDetailsAttribution.setVisibility(View.GONE);
//                } else {
//                    mPlaceDetailsAttribution.setVisibility(View.VISIBLE);
//                    mPlaceDetailsAttribution.setText(
//                            Html.fromHtml(thirdPartyAttribution.toString()));
//                }

                Log.i(TAG, "Place details received: " + place.getName());

                places.release();
            } catch (RuntimeRemoteException e) {
                // Request did not complete successfully
                Log.e(TAG, "Place query did not complete.", e);
                return;
            }
        }
    };
    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        Log.e(TAG, res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));

    }

    public void pickColor(View v){
        ColorPickerDialogBuilder
                .with(getContext())
                .setTitle("Choose color")
                .initialColor(mycolor)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
//                        (onColorSelected: 0x" + Integer.toHexString(selectedColor));
                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        Log.d("Debug", String.valueOf(selectedColor));
                        Toast.makeText(getActivity().getApplicationContext(),"onColorSelected: 0x" + Integer.toHexString(selectedColor).toUpperCase(), Toast.LENGTH_SHORT).show();

                        //this set the color of the background
                       txtColor.setBackgroundColor(selectedColor);

                        // we can add if statement and check if the drop down list matches and store color hex # on those variables
                        // to be passed each value to
                        myPrmaryColor = Integer.toHexString(selectedColor);
                        Log.d("PrimaryColor", String.valueOf(myPrmaryColor));
                        SecondaryColor =  Integer.toHexString(selectedColor);
                        Log.d("SecondaryColor", String.valueOf(SecondaryColor));
                        ThirdColor  =  Integer.toHexString(selectedColor);
                        Log.d("ThridColor", String.valueOf(ThirdColor));
                        BackGroundColor =  Integer.toHexString(selectedColor);
                        Log.d("BackGroundColor", String.valueOf(BackGroundColor));

                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }


    // this is an inter class
    public static class ThemeColors {
        private static final String NAME = "ThemeColors", KEY = "color";
        public int color;
        //this create the multiple  themes colors
        private ThemeColors(Context context) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
            String stringColor = sharedPreferences.getString(KEY, "004bff");
            color = Color.parseColor("#" + stringColor);

            if (isLightActionBar()) context.setTheme(R.style.AppTheme);
            context.setTheme(context.getResources().getIdentifier("T_" + stringColor, "style", context.getPackageName()));
        }

        public static void setNewThemeColor(Activity activity, int red, int green, int blue) {
            int colorStep = 15;
            red = Math.round(red / colorStep) * colorStep;
            green = Math.round(green / colorStep) * colorStep;
            blue = Math.round(blue / colorStep) * colorStep;

            String stringColor = Integer.toHexString(Color.rgb(red, green, blue)).substring(2);
            SharedPreferences.Editor editor = activity.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit();
            editor.putString(KEY, stringColor);
            editor.apply();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) activity.recreate();
            else {
                Intent i = activity.getPackageManager().getLaunchIntentForPackage(activity.getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(i);
            }
        }

        private boolean isLightActionBar() {// Checking if title text color will be black
            int rgb = (Color.red(color) + Color.green(color) + Color.blue(color)) / 3;
            return rgb > 210;
        }


    }
}