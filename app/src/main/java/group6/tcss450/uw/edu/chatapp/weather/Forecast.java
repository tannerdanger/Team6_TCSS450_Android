package group6.tcss450.uw.edu.chatapp.weather;

import java.io.Serializable;

/**
 * @Author Tanner Brown
 * @Version 11/3/2018
 * This class holds weather data for a forecast
 */
public class Forecast implements Serializable {


    private final String windDirection;
    private final double windGustSpeed;
    private final double windSpeed;
    private final double max_tmp_c; //celcius
    private final double max_tmp_f; //todo: convert to farenheighalijth (check spelling of this word)

    public Forecast(){
        windDirection = "";
        windGustSpeed = 0.1;
        windSpeed = 0.0;
        max_tmp_c = 0.0;
        max_tmp_f = 0.0;

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





