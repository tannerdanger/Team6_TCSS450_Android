package group6.tcss450.uw.edu.chatapp.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import group6.tcss450.uw.edu.chatapp.weather.Forecast;

public class JsonHelper {

    public static JSONObject weather_JsonObject(double lat, double lon){
        //build the JSONObject
        JSONObject msg = new JSONObject();
        try {
            msg.put("lat", lat);
            msg.put("lon", lon);
        } catch (JSONException e) {
            Log.wtf("CREDENTIALS", "Error creating JSON: " + e.getMessage());
        }
        return msg;
    }

    public static JSONObject chats_JsonObject(int id){
        //build the JSONObject
        JSONObject msg = new JSONObject();
        try {
            msg.put("id", id);
        } catch (JSONException e) {
            Log.wtf("CREDENTIALS", "Error creating JSON: " + e.getMessage());
        }
        return msg;
    }

    public static JSONObject connections_JsonObject(int id){
        //build the JSONObject
        JSONObject msg = new JSONObject();
        try {
            msg.put("sender_id", id);
        } catch (JSONException e) {
            Log.wtf("CREDENTIALS", "Error creating JSON: " + e.getMessage());
        }
        return msg;
    }

    public static Forecast[] parse_Forecast(String s){
        Forecast[] forecasts = new Forecast[10];
        try {
            JSONObject jObject = new JSONObject(s);
            JSONArray tendayforecast = jObject.getJSONArray("data");

            for ( int i = 0; i < forecasts.length; i++){
                forecasts[i] = new Forecast((JSONObject)tendayforecast.get(i));
            }

        } catch (JSONException e){
            Log.e("JSON_PARSE_ERROR", s
                    + System.lineSeparator()
                    + e.getMessage());

            //TODO: Handle the error
        }


        return forecasts;
    }

}
