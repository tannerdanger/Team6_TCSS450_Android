package group6.tcss450.uw.edu.chatapp.weather;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * @Author Tanner Brown
 * @Version 11/3/2018
 */
public class WeatherMsg implements Serializable {

    private double mLat;
    private double mLon;

    private String LATITUDE = "lat";
    private String LONGITUDE = "lon";

    public WeatherMsg(double theLat, double theLon){
        mLat = theLat;
        mLon = theLon;
    }

    public void setCoords(double theLat, double theLon){
        this.mLat = theLat;
        this.mLon = theLon;
    }

    public JSONObject asJsonObject(){
        //build the JSONObject
        JSONObject msg = new JSONObject();
        try {
            msg.put(LATITUDE, mLat);
            msg.put(LONGITUDE, mLon);
        } catch (JSONException e) {
            Log.wtf("CREDENTIALS", "Error creating JSON: " + e.getMessage());
        }
        return msg;
    }


}
