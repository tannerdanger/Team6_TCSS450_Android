package group6.tcss450.uw.edu.chatapp.weather;


<<<<<<< HEAD
=======
import android.annotation.SuppressLint;
import android.content.Context;
>>>>>>> 30fd5e3a4146fc36f518853d534d3fb37bef1b5a
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import group6.tcss450.uw.edu.chatapp.R;
<<<<<<< HEAD
=======
import group6.tcss450.uw.edu.chatapp.contacts.ConnectionFragment;
>>>>>>> 30fd5e3a4146fc36f518853d534d3fb37bef1b5a


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

<<<<<<< HEAD
    // TODO: Rename and change types of parameters
    private String lat;
    private String log;
=======
    private weatherSeattingFragment.OnSettingsFragmentInteractionListener mListener;

    // TODO: Rename and change types of parameters
    private double mLat;
    private double mLong;
>>>>>>> 30fd5e3a4146fc36f518853d534d3fb37bef1b5a
    private EditText city;
    private EditText zipcode;
    private Button Map;
    private Button Search;


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
<<<<<<< HEAD
            lat = getArguments().getString(ARG_PARAM1);
            log = getArguments().getString(ARG_PARAM2);
=======
            mLat = getArguments().getDouble("lat");
            mLong = getArguments().getDouble("lon");
>>>>>>> 30fd5e3a4146fc36f518853d534d3fb37bef1b5a
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather_seatting, container, false);
        city = (EditText) view.findViewById(R.id.city_editText);
        zipcode = (EditText) view.findViewById(R.id.zipcode_editText2);
        Map = (Button) view.findViewById(R.id.Map_button);
        Search = (Button) view.findViewById(R.id.search_button);
<<<<<<< HEAD
        return view;
    }
=======

        Search.setOnClickListener(l -> {

            if("".compareTo(city.getText().toString()) == 0 ) {



                mListener.onNewCity(city.getText().toString());

            } else if("".compareTo(zipcode.getText().toString())  == 0
                    && zipcode.getText().toString().length() > 4){


                int zip = Integer.parseInt(zipcode.getText().toString());


                mListener.onNewZipcode(zip);

            }

        });

        Map.setOnClickListener(l ->{
            mListener.loadMap();
        });

        return view;
    }

    @SuppressLint("SetTextI18n")
    public void getCoordsFromMap(double lat, double lon){
        //TODO: Change this so it updates a lat/long box instead. Until then, it will just update without checking with the user first.
        city.setText("Lat: " + lat + "  | Lon: " + lon);
        mLat = lat;
        mLong = lon;
        mListener.onNewLatLon(lat, lon);
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

    }


>>>>>>> 30fd5e3a4146fc36f518853d534d3fb37bef1b5a
}