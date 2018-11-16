package group6.tcss450.uw.edu.chatapp;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import group6.tcss450.uw.edu.chatapp.utils.Credentials;
import group6.tcss450.uw.edu.chatapp.utils.JsonHelper;
import group6.tcss450.uw.edu.chatapp.utils.WeatherPagerAdapter;
import group6.tcss450.uw.edu.chatapp.weather.Forecast;
import group6.tcss450.uw.edu.chatapp.weather.WeatherFragment;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class HomeFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Credentials mCredentials;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ArrayList<WeatherFragment> mWeatherFrags;
    private Forecast[] mForecast;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        if(getArguments() != null){
            Credentials cred = (Credentials)getArguments().getSerializable("credentials");
            updatecontent(cred);
        }
    }

    private void updatecontent(Credentials theCredentials){
//        TextView tv = getActivity().findViewById(R.id.homefrag_tv_username);
//        tv.setText(theCredentials.getUsername());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        //get credentials and forecast
        if(getArguments() != null){
            mCredentials = (Credentials)getArguments().getSerializable(getString(R.string.ARGS_CREDENTIALS));
            mForecast = JsonHelper.parse_Forecast(getArguments().getString(getString(R.string.ARGS_FORECAST_DATA)));
        }
        mWeatherFrags = new ArrayList<>();
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) view.findViewById(R.id.weather_tabs);
        setupViewPager(mViewPager);
        mTabLayout.setupWithViewPager(mViewPager);

        setupTabIcons();
        updateWeather();


        return view;
    }



    private void setupViewPager(ViewPager mViewPager) {
        WeatherPagerAdapter adapter = new WeatherPagerAdapter(getActivity().getSupportFragmentManager(), 5);

        int dateOffset = Calendar.DAY_OF_WEEK;
        String[] dates = new String[]{"MON", "TUES", "WED", "THU", "FRI", "SAT", "SUN"};

        for(int i = 0; i < 5; i++){
            int fragdate = dateOffset + i - 1;
            if(fragdate > 6){
                fragdate = fragdate - 7;
            }
            WeatherFragment frag = new WeatherFragment();
            Bundle args = new Bundle();
            args.putSerializable(getString(R.string.ARGS_FORECAST_DATA) ,mForecast[i]);
            frag.setArguments(args);
            mWeatherFrags.add(frag);
            adapter.addFragment(frag, dates[fragdate]);

        }

        mViewPager.setAdapter(adapter);
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

    public void updateContent(){

        mForecast = new Forecast[10];
        JSONObject forcastJson;
        if (getArguments() != null){
            mForecast = JsonHelper.parse_Forecast(getArguments().getString(getString(R.string.ARGS_FORECAST_DATA)));
        }
        int i = 0;

        setupTabIcons();
        updateWeather();
    }

    private void setupTabIcons() {

        if(mTabLayout != null ) {
            Resources res = getContext().getResources();
            for (int i = 0; i < 5; i++) {
                mTabLayout.getTabAt(i).setIcon(res.getIdentifier(mForecast[i].iconCode.toString(), "drawable", getContext().getPackageName()));
            }
        }
    }

    private void updateWeather(){
        int i = 0;
        for(WeatherFragment frag : mWeatherFrags){
            //frag.setArguments();
            frag.setmForecast(mForecast[i]);
            i++;
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
        void onFragmentInteraction(Uri uri);
    }
}
