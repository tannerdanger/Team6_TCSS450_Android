package group6.tcss450.uw.edu.chatapp.utils;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import group6.tcss450.uw.edu.chatapp.R;
import group6.tcss450.uw.edu.chatapp.weather.Forecast;

public class JsonHelper extends AppCompatActivity {

    private static final String MYID = "memberid";
    private static final String THERID = "their_id";
    private static final String QUERY = "query";
    private static final String EMAIL = "email";
    private static final String TOKEN = "token";
    private static final String PASSWORD = "password";
    private static final String SALT = "salt";
    private static final String MSG = "message";
    private static final String CHAT = "chatid";
    private static final String MYUN = "username";
    private static final String THEIRUN = "their_username";
    private static final String LAT = "lat";
    private static final String LON = "lon";
    private static final String KEY = "key";

    public static JSONObject messages_JsonObject(int chatid){
        JSONObject msg = new JSONObject();
        try {
            msg.put(CHAT, chatid);
        } catch (JSONException e) {
            Log.wtf("JSON HELPER", "Error creating JSON: " + e.getMessage());
        }
        return msg;
    }

    public static JSONObject create_newChat_JsonObject(int memberid, String username, int their_id, String their_Username){

        JSONObject msg = new JSONObject();

        try{
            msg.put(MYID,memberid);
            msg.put(MYUN, username);
            msg.put(THERID, their_id);
            msg.put(THEIRUN, their_Username);

        }catch (JSONException e){
            e.printStackTrace();
        }

        return msg;

    }

    public static JSONObject weather_JsonObject(double lat, double lon){
        //build the JSONObject
        JSONObject msg = new JSONObject();
        try {
            msg.put(LAT, lat);
            msg.put(LON, lon);
        } catch (JSONException e) {
            Log.wtf("CREDENTIALS", "Error creating JSON: " + e.getMessage());
        }
        return msg;
    }

    public static JSONObject chats_JsonObject(int id){
        //build the JSONObject
        JSONObject msg = new JSONObject();
        try {
            msg.put(MYID, id);
        } catch (JSONException e) {
            Log.wtf("JSON HELPER", "Error creating JSON: " + e.getMessage());
        }
        return msg;
    }

    public static JSONObject connections_JsonObject(int id){
        //build the JSONObject
        JSONObject msg = new JSONObject();
        try {
            msg.put(MYID, id);
        } catch (JSONException e) {
            Log.wtf("CREDENTIALS", "Error creating JSON: " + e.getMessage());
        }
        return msg;
    }

    public static JSONObject conn_ProposeAndApprove_JsonObject(int memberid, String username, int their_id){

        JSONObject msg = new JSONObject();

        try{
            msg.put(MYID,memberid);
            msg.put(MYUN, username);
            msg.put(THERID, their_id);
            msg.put(CHAT, -1);
        }catch (JSONException e){
            e.printStackTrace();
        }

        return msg;

    }

    public static Forecast[] parse_Forecast(String s){
        Forecast[] forecasts = new Forecast[10];
        try {
            JSONObject jObject = new JSONObject(s);

            String city = jObject.getString("city_name");
            String state = jObject.getString("state_code");

            JSONArray tendayforecast = jObject.getJSONArray("data");

            for ( int i = 0; i < forecasts.length; i++){
                forecasts[i] = new Forecast((JSONObject)tendayforecast.get(i));
                forecasts[i].setLocationName( city + ", "+ state.toUpperCase() );
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
