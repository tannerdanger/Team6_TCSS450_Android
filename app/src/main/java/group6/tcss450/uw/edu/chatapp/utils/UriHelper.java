package group6.tcss450.uw.edu.chatapp.utils;

import android.net.Uri;



/**
 * This is a helper class for building URIs
 */
public class UriHelper {
    private static final String SCHEME = "https";
    private static final String BASE ="tcss450group6-backend.herokuapp.com";
    private static final String MESSAGING = "messaging";
    private static final String GETMY = "getmy";
    private static final String GETALL = "getall";
    private static final String CONN = "conn";
    private static final String WEATHER = "weather";
    private static final String NEW = "new";

    public static String CONNECTIONS_GETALL(){

        return new Uri.Builder()
                .scheme(SCHEME)
                .appendPath(BASE)
                .appendPath(CONN)
                .appendPath(GETALL)
                .build().toString();
    }

    public static String MESSAGING_GETMY(){
        return new Uri.Builder()
                .scheme(SCHEME)
                .appendPath(BASE)
                .appendPath(MESSAGING)
                .appendPath(GETMY)
                .build().toString();
    }

    public static String MESSAGING_GETALL(){
        return new Uri.Builder()
                .scheme(SCHEME)
                .appendPath(BASE)
                .appendPath(MESSAGING)
                .appendPath(GETALL)
                .build().toString();
    }

    public static String WEATHER_BY_LAT_LONG(){
        return new Uri.Builder()
                .scheme(SCHEME)
                .appendPath(BASE)
                .appendPath(WEATHER)
                .appendPath("tenday") //todo: adjust this to latlon
                .build().toString();
    }

    public static String MESSAGES_NEW(){
        return new Uri.Builder()
                .scheme(SCHEME)
                .appendPath(BASE)
                .appendPath(MESSAGING)
                .appendPath(NEW)
                .build().toString();
    }


}
