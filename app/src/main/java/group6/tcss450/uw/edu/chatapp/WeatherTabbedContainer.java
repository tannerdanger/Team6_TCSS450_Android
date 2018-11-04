package group6.tcss450.uw.edu.chatapp;

import android.content.Context;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import group6.tcss450.uw.edu.chatapp.utils.PagerAdapter;
import group6.tcss450.uw.edu.chatapp.utils.SendPostAsyncTask;
import group6.tcss450.uw.edu.chatapp.weather.Forecast;
import group6.tcss450.uw.edu.chatapp.weather.ForecastFragment;
import group6.tcss450.uw.edu.chatapp.weather.WeatherFragment;
import group6.tcss450.uw.edu.chatapp.weather.WeatherMsg;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WeatherTabbedContainer.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class WeatherTabbedContainer extends Fragment {

    private OnFragmentInteractionListener mListener;
    //Weather message object for sending lat/long to api
    private WeatherMsg mWeatherMessage;
    private boolean mIsWeatherSet;
    private Forecast[] mTenDayForecast;
    private Fragment mWeatherFragment;
    private Fragment mForecastFragment;

    public WeatherTabbedContainer() {
        // Required empty public constructor
    }

    @Override
    public void onStart(){
        super.onStart();

        if(getArguments() != null){
            mWeatherMessage = (WeatherMsg)getArguments().getSerializable("wmsg");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //TODO: have a way to determine what the desired weather location is
        mIsWeatherSet = false;
        //TODO: update forecast
        if(getArguments() != null){
            mWeatherMessage = (WeatherMsg)getArguments().getSerializable("wmsg");
        }
        mTenDayForecast = new Forecast[10];
        tryGetWeather();




    }

    private void tryGetWeather(){


        //if(null != longitutde && null != latitude){

        if(null != mWeatherMessage) {

            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath(getString(R.string.ep_base_url))
                    .appendPath(getString(R.string.ep_weather))
                    .appendPath(getString(R.string.ep_tenday))
                    .build();

            JSONObject jMsg = mWeatherMessage.asJsonObject();

            new SendPostAsyncTask.Builder(uri.toString(), jMsg)
                    .onPreExecute(this::handleWeatherPre)
                    .onPostExecute(this::handleWeatherPost)
                    .onCancelled(this::handleErrorsInTask)
                    .build()
                    .execute();

            //}
        }
    }

    //*********** ASYNC HANDLE METHODS ********************//
    private void handleWeatherPre(){
        //TODO: implement this
        //mListener.onWaitFragmentInteractionShow();
    }

    private void handleWeatherPost(String result){
        try{
            JSONObject resultsJSON = new JSONObject(result);

            //TODO: Implement this
            //mListener.onWaitFragmentInteractionHide();
            //TODO: grab location info from JSON and store it in preferences

            JSONArray tenForecastObject = resultsJSON.getJSONArray("data");
            System.out.print("");
            for(int i = 0; i<10; i++){

                JSONObject f = tenForecastObject.getJSONObject(i);
                mTenDayForecast[i] = new Forecast((JSONObject)f);
            }
            System.out.print("");

        }catch (JSONException e){
            Log.e("JSON_PARSE_ERROR", result
            + System.lineSeparator()
            + e.getMessage());

            //TODO: Handle the error
        }
    }

    private void handleErrorsInTask(String result){
        Log.e("ASYNC_TASK_ERROR", result);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather_tabbed_container, container, false);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Current Weather"));
        tabLayout.addTab(tabLayout.newTab().setText("10-day Forecast"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager)view.findViewById(R.id.weather_pager);
        final PagerAdapter adapter = new PagerAdapter(getFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab){
            }
        });

        mWeatherFragment = (WeatherFragment)adapter.getItem(0);
        mForecastFragment = (ForecastFragment)adapter.getItem(1);
        updateForecasts();

        return view;
    }

    /**
     * This method can be called to update the fields in the weather fragments.
     * This method has access to a class variable for both fragments,
     * as well as an a reference to an array of forecast objects for the next 10 days.
     */
    public void updateForecasts(){
        //mWeatherFragment = the weather fragment
        //mForecastFragment = the 10day forecast fragment
        //mTenDayForecast[0...9] each item represents a forecast object.
        //TODO: here is where all the text fields/icons should be updated from the forecast array
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onWeatherFragmentInteraction(uri);
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
        void onWeatherFragmentInteraction(Uri uri);
    }
}
