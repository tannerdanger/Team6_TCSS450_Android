package group6.tcss450.uw.edu.chatapp.weather;

import android.graphics.drawable.Icon;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author Tanner Brown
 * @Version 11/3/2018
 * This class holds weather data for a forecast
 */
public class Forecast implements Serializable {

    //general data
    public String forecastDate; //datetime
    public Date date;
    public String iconCode; //icon
    public String forecast;
    public int icon;
    private Date lastUpdated;
    private String mForecastLocationName;
    private Double mPercipChance;
    private Double mPercipAmount;

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
        SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            curr_tmp = forecastJSON.getDouble("temp");
            min_tmp = forecastJSON.getDouble("min_temp");
            max_tmp = forecastJSON.getDouble("max_temp");
            forecastDate = forecastJSON.getString("datetime");
            JSONObject weather = forecastJSON.getJSONObject("weather");
            forecast = weather.getString("description");
            iconCode = weather.getString("icon");
            lastUpdated = Calendar.getInstance().getTime();
            date = inFormat.parse(forecastDate);
          //  mPercipAmount = forecastJSON.getDouble("precip") / 25.4; //Convert to inches
            BigDecimal bd = new BigDecimal(forecastJSON.getDouble("precip") / 25.4 ).setScale(1, RoundingMode.HALF_UP);
            mPercipAmount = bd.doubleValue();
            mPercipChance = forecastJSON.getDouble("pop");





        } catch (JSONException e) {
            Log.wtf("Creating Forecast Object", "Failed parsing json data!");
            e.printStackTrace();
        } catch (ParseException e) {
            Log.wtf("Creating Forecast Object", "Failed parsing date!");
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

    public double getPercipChance() { return this.mPercipChance; }

    public double getMaxTemperature(){
        return this.max_tmp;
    }

    public double getMinTemperature(){
        return this.min_tmp;
    }

    public String getForecastDate(){
        return this.forecastDate;
    }

    public void setLocationName(String theName){mForecastLocationName = theName;}

    public String getLocationName(){return this.mForecastLocationName; }

    /**
     * @return The days forecast. I.e. "Partly Cloudy"
     */
    public String getForecast(){
        return this.forecast;
    }

    public String getDay(){

        SimpleDateFormat sdf = new SimpleDateFormat("EEE");

        return sdf.format(date);
    }

    public String getShortDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");

        return sdf.format(date);
    }

    public int getIconResID(){ return this.icon; }

    public Date getLastUpdated(){
        return this.lastUpdated;
    }

    /** Returns the percipitation amount, converted to inches because America */
    public Double getPercipAmount() { return this.mPercipAmount; }

    public void setIcon(int theIcon){ this.icon = theIcon;  }

    /**
     * Converts celcius values to Fahrenheit
     * @param theCelcius input value
     * @return Fahrenheit converted value
     */
    public double convertToFahrenheit(double theCelcius){
        BigDecimal bd = new BigDecimal(32 + (theCelcius * 9 / 5)).setScale(1, RoundingMode.HALF_UP);

        return bd.doubleValue();
    }
}