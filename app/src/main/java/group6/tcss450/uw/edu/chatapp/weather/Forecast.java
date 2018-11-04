package group6.tcss450.uw.edu.chatapp.weather;

import android.graphics.drawable.Icon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * @Author Tanner Brown
 * @Version 11/3/2018
 * This class holds weather data for a forecast
 */
public class Forecast implements Serializable {

    //general data
    public String forecastDate; //datetime
//    public final String iconCode; //icon
//    public final int forecastCode; //code
//    public final Icon weatherIcon;
    public String forecast;


    //moon/sun
    public int moonriseTimeStamp; //moonrise_ts
    public int sunriseTimeStamp;//sunrise_ts
    public int sunsetTimeStamp;//sunset_ts

    //wind stuff
    private String windDirection; //wind_cdir_full
    private double windGustSpeed; //wind_gust_spd
    private double windSpeed; //wind_spd

    //percipitation
    public double snowFall; //snow
    public double percepitation; //precip
    public double percipChance; //pop


    //atmostphere
    public int cloudCoverage; //clouds
    public int visability; //vis

    //temp
    //all values are in celcius. Convert to F as neccessary
    private double max_tmp; //max_temp
    private double curr_tmp; //temp
    private double min_tmp; //min_temp

    public Forecast(JSONObject forecastJSON){

        try {
            curr_tmp = forecastJSON.getDouble("temp");
            min_tmp = forecastJSON.getDouble("min_temp");
            max_tmp = forecastJSON.getDouble("max_temp");
            forecastDate = forecastJSON.getString("datetime");
            JSONObject weather = forecastJSON.getJSONObject("weather");
            forecast = weather.getString("description");

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }






}