package group6.tcss450.uw.edu.chatapp.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import group6.tcss450.uw.edu.chatapp.weather.ForecastFragment;
import group6.tcss450.uw.edu.chatapp.weather.WeatherFragment;

/**
 * @author Tanner Brown
 * @version 11/3/2018
 */
public class PagerAdapter extends FragmentPagerAdapter {

    int numberOfTabs;

    public PagerAdapter(FragmentManager fm, int tabCount){
        super(fm);
        this.numberOfTabs = tabCount;
    }

    @Override
    public Fragment getItem(int thePosition) {
        switch (thePosition){
            case 0:
                WeatherFragment tab1 = new WeatherFragment();
                return tab1;
            case 1:
                ForecastFragment tab2 = new ForecastFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
