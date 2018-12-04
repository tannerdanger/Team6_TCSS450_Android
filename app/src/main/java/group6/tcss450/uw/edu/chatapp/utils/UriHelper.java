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
    private static final String SEND = "send";
    private static final String PROPOSE = "propose";
    private static final String APPROVE = "approve";
    private static final String REMOVE = "remove";
    private static final String MULTI = "newmulti";
    private static final String COORDS = "coords";
    private static final String CITY = "city";
    private static final String ZIP = "zip";

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

    public static String MESSAGING_MULTI()  {
        return new Uri.Builder()
                .scheme(SCHEME)
                .appendPath(BASE)
                .appendPath(MESSAGING)
                .appendPath(MULTI)
                .build().toString();
    }

    public static String WEATHER_BY_LAT_LONG(){
        return new Uri.Builder()
                .scheme(SCHEME)
                .appendPath(BASE)
                .appendPath(WEATHER)
                .appendPath(COORDS)
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

    public static String MESSAGES_SEND(){
        return new Uri.Builder()
                .scheme(SCHEME)
                .appendPath(BASE)
                .appendPath(MESSAGING)
                .appendPath(SEND)
                .build().toString();
    }

    public static String CONNECTION_PROPOSE(){
        return new Uri.Builder()
                .scheme(SCHEME)
                .appendPath(BASE)
                .appendPath(CONN)
                .appendPath(PROPOSE)
                .build().toString();
    }

    public static String CONNECTION_APPROVE(){
        return new Uri.Builder()
                .scheme(SCHEME)
                .appendPath(BASE)
                .appendPath(CONN)
                .appendPath(APPROVE)
                .build().toString();
    }

    public static String CONNECTION_REMOVE(){
        return new Uri.Builder()
                .scheme(SCHEME)
                .appendPath(BASE)
                .appendPath(CONN)
                .appendPath(REMOVE)
                .build().toString();
    }

    public static String CHATROOM_REMOVE()  {
        return new Uri.Builder()
                .scheme(SCHEME)
                .appendPath(BASE)
                .appendPath(MESSAGING)
                .appendPath(REMOVE)
                .build().toString();
    }


    public static String WEATHER_BY_CITY() {
        return new Uri.Builder()
                .scheme(SCHEME)
                .appendPath(BASE)
                .appendPath(WEATHER)
                .appendPath(CITY)
                .build().toString();
    }

    public static String WEATHER_BY_ZIP() {
        return new Uri.Builder()
                .scheme(SCHEME)
                .appendPath(BASE)
                .appendPath(WEATHER)
                .appendPath(ZIP)
                .build().toString();
    }
}
