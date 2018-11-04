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
    }




}
