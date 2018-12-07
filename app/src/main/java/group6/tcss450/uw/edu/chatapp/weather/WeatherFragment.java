package group6.tcss450.uw.edu.chatapp.weather;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import group6.tcss450.uw.edu.chatapp.R;
import group6.tcss450.uw.edu.chatapp.utils.WaitFragment;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WeatherFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 *
 * @Author Tanner Brown
 * @Version 20 Nov 2018
 */
public class WeatherFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private Forecast mForecast;
    final String DEGREE  = "\u00b0";
    private ImageView mIcon;
    private TextView mCurrentTemp;
    private TextView mTemp;
    private TextView mLocation;
    private TextView mDate;
    private TextView mLastUpdated;
    private TextView mPercipChance;
    private TextView mWeatherDescription;
    private static boolean mIsWaitFragActive;

    public WeatherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mIsWaitFragActive = false;
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        if(null != getArguments()){
            mForecast = (Forecast) getArguments().getSerializable(getString(R.string.ARGS_FORECAST_DATA));
            this.setArguments(null);
        }

        mIcon = (ImageView) view.findViewById(R.id.forecast_icon);
        mCurrentTemp = (TextView)view.findViewById(R.id.forecast_temp_current);
        mWeatherDescription = (TextView)view.findViewById(R.id.forecast_description);
        mTemp = (TextView)view.findViewById(R.id.forecast_temp);

        mDate = (TextView)view.findViewById(R.id.forecast_date);
        mLocation = (TextView)view.findViewById(R.id.forecast_location);
        mPercipChance = (TextView)view.findViewById(R.id.forecast_percipChance);

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
        updateForecast();
    }

    public boolean hasForecast(){
        return null != mForecast;
    }

    /**
     * Update all the forecast variables from the forecast object
     */
    public void updateForecast(){

        if(null != getContext()) {



            // int ID = R.drawable.
            //int ID = getContext().getResources().getIdentifier(mForecast.iconCode, "drawable", getContext().getPackageName());
            String tempText = "HI " + String.valueOf(mForecast.convertToFahrenheit(mForecast.getMaxTemperature())) + DEGREE
                    + "\nLO " + String.valueOf(mForecast.convertToFahrenheit(mForecast.getMinTemperature())) + DEGREE;
            // mHiTemp.setText("HI: " + String.valueOf(mForecast.convertToFahrenheit(mForecast.getMaxTemperature())) + DEGREE);
            // mLowTemp.setText("LO: " + String.valueOf(mForecast.convertToFahrenheit(mForecast.getMinTemperature())) + DEGREE);
            mTemp.setText(tempText);
            mCurrentTemp.setText(String.valueOf(mForecast.convertToFahrenheit(mForecast.getCurrentTemp())) + DEGREE);
            mWeatherDescription.setText(mForecast.getForecast());
            mIcon.setImageResource(mForecast.getIconResID());
            mLocation.setText(mForecast.getLocationName());
            mDate.setText(mForecast.getForecastDate());
            String percip = "";
            String chance = "Percip Chance ";
            String amount = "\nPercip Amount ";

            if(mForecast.getPercipChance() > 1) {

                chance += mForecast.getPercipChance() + "%";

                if(mForecast.getPercipAmount() > 1) {
                    amount += mForecast.getPercipAmount() + "\"";
                    percip = chance + amount;
                }
                else
                    percip = "Trace Percipitation";



            } else
                percip = "No Percipitation";

            mPercipChance.setText(percip);
        }
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
    public interface OnFragmentInteractionListener extends WaitFragment.OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

    }

    /**
     * Display wait fragment if not already displayed
     */
    private void startAsync(){
        if(!mIsWaitFragActive) { //start wait frag if Async is not already active
            mListener.onWaitFragmentInteractionShow();
            mIsWaitFragActive = true;
        }
    }

    /**
     * Hide wait fragment if not already hidden
     */
    private void endAsync(){
        if(mIsWaitFragActive) { //hide if Async is active
            mListener.onWaitFragmentInteractionHide();
            mIsWaitFragActive = false;
        }
    }
}
