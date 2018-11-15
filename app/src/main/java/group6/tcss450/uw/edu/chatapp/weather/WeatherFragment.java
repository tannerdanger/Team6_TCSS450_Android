package group6.tcss450.uw.edu.chatapp.weather;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import group6.tcss450.uw.edu.chatapp.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WeatherFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class WeatherFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private Forecast mForecast;
    final String DEGREE  = "\u00b0";
    private ImageView mIcon;
    private TextView mCurrentTemp;
    private TextView mHiTemp;
    private TextView mLowTemp;
    private TextView mWeatherDescription;

    public WeatherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        if(null != getArguments()){
            mForecast = (Forecast) getArguments().getSerializable("forecast");
        }

        mIcon = (ImageView) view.findViewById(R.id.forecast_icon);
        mCurrentTemp = (TextView)view.findViewById(R.id.forecast_temp_current);
        mWeatherDescription = (TextView)view.findViewById(R.id.forecast_description);
        mLowTemp = (TextView)view.findViewById(R.id.forecast_temp_lo);
        mHiTemp = (TextView)view.findViewById(R.id.forecast_temp_hi);
        updateForecast();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setmForecast(Forecast forecast){
        this.mForecast = forecast;
    }

    /**
     * Update all the forecast variables from the forecast object
     */
    public void updateForecast(){

        //mForecast = forecast;
        int ID = Objects.requireNonNull(getContext()).getResources().getIdentifier(mForecast.iconCode.toString(), "drawable", getContext().getPackageName());
        mHiTemp.setText("HI: " + String.valueOf(mForecast.convertToFahrenheit(mForecast.getMaxTemperature())) + DEGREE);
        mLowTemp.setText("LO: " + String.valueOf(mForecast.convertToFahrenheit(mForecast.getMinTemperature())) + DEGREE);
        mCurrentTemp.setText(String.valueOf(mForecast.convertToFahrenheit(mForecast.getCurrentTemp())) + DEGREE);
        mWeatherDescription.setText(mForecast.getForecast());
        mIcon.setImageResource(ID);



    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
