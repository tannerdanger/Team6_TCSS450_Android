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
    public String iconCode; //icon
    public String forecast;
//    public final int forecastCode; //code
//    public final Icon weatherIcon;

//    //moon/sun
//    public int moonriseTimeStamp; //moonrise_ts
//    public int sunriseTimeStamp;//sunrise_ts
//    public int sunsetTimeStamp;//sunset_ts
//
//    //wind stuff
//    private String windDirection; //wind_cdir_full
//    private double windGustSpeed; //wind_gust_spd
//    private double windSpeed; //wind_spd
//
//    //percipitation
//    public double snowFall; //snow
//    public double percepitation; //precip
//    public double percipChance; //pop
//
//
//    //atmostphere
//    public int cloudCoverage; //clouds
//    public int visability; //vis

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
            iconCode = weather.getString("icon");


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the code identifying the weather icon.
     * Reference: https://www.weatherbit.io/api/codes
     */
    public String getIconCode(){
        return this.iconCode;
    }

    public double getCurrentTemp(){
        return this.curr_tmp;
    }

    public double getMaxTemperature(){
        return this.max_tmp;
    }

    public double getMinTemperature(){
        return this.min_tmp;
    }

    public String getForecastDate(){
        return this.forecastDate;
    }

    /**
     * @return The days forecast. I.e. "Partly Cloudy"
     */
    public String getForecast(){
        return this.forecast;
    }

    /**
     * Converts celcius values to Fahrenheit
     * @param theCelcius input value
     * @return Fahrenheit converted value
     */
    public double convertToFahrenheit(double theCelcius){
        return 32 + (theCelcius * 9 / 5);
    }
}