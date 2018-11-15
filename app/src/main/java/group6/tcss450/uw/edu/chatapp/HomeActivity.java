package group6.tcss450.uw.edu.chatapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import group6.tcss450.uw.edu.chatapp.contacts.ConnectionFragment;
import group6.tcss450.uw.edu.chatapp.contacts.ConnectionRequestsFragment;
import group6.tcss450.uw.edu.chatapp.contacts.ConnectionsSearchFragment;
import group6.tcss450.uw.edu.chatapp.messages.Message;
import group6.tcss450.uw.edu.chatapp.messages.OpenMessage;
import group6.tcss450.uw.edu.chatapp.contacts.Connection;
import group6.tcss450.uw.edu.chatapp.utils.Credentials;
import group6.tcss450.uw.edu.chatapp.utils.JsonHelper;
import group6.tcss450.uw.edu.chatapp.utils.SendPostAsyncTask;
import group6.tcss450.uw.edu.chatapp.utils.WaitFragment;
import group6.tcss450.uw.edu.chatapp.weather.WeatherFragment;

public class HomeActivity extends AppCompatActivity
        implements HomeFragment.OnFragmentInteractionListener,
        OpenMessagesFragment.OnOpenMessageFragmentInteractionListener,
        MessagesFragment.OnMessageFragmentInteractionListener,
        ConnectionFragment.OnConnectionsFragmentInteractionListener,
        WeatherFragment.OnFragmentInteractionListener,
        ConnectionsSearchFragment.OnConnectionSearchFragmentInteractionListener,
        ConnectionRequestsFragment.OnConnectionRequestFragmentInteractionListener,
        WaitFragment.OnFragmentInteractionListener  {

    private TextView mTextMessage;

    private Credentials mCredentials;
    private ActionBar mToolbar;
    private HashMap <String, JSONObject> mJsonData;
    private boolean mWeatherLoaded = false;
    private boolean mChatsLoaded = false;
    private boolean mConnectionsLoaded = false;

    private HomeFragment mHomeFrag;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mToolbar.setTitle("HOME");
                    navigateHome();
                    return true;
                case R.id.nav_connections:
                    mToolbar.setTitle("CONTACTS");
                    navigateConnections();
                    return true;
                case R.id.navigation_notifications:
                    mToolbar.setTitle("NOTIFICATIONS");
                    navigateNotifications();
                    return true;
                case R.id.nav_solo_chat:
                    mToolbar.setTitle("CHAT");
                    navigateChat();
                    return true;
            }

            return false;
        }

    };

    protected void navigateHome()   {
        HomeFragment frag = new HomeFragment();
        Bundle args = new Bundle();
        args.putSerializable("credentials", mCredentials);
        frag.setArguments(args);
        loadFragment(frag);
        //navigateHome();
    }

    protected void navigateConnections()    {
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_conn))
                .appendPath(getString(R.string.ep_getall))
                .build();
        new SendPostAsyncTask.Builder(uri.toString(), mCredentials.asJSONObject())
                .onPreExecute(this::onWaitFragmentInteractionShow)
                .onPostExecute(this::handleConnectionGetOnPostExecute)
                .build()
                .execute();
    }

    protected void navigateNotifications(){}

    protected void navigateChat()   {
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_messaging))
                .appendPath(getString(R.string.ep_getmy))
                .build();
        new SendPostAsyncTask.Builder(uri.toString(), mCredentials.asJSONObject())
                .onPreExecute(this::onWaitFragmentInteractionShow)
                .onPostExecute(this::handleOpenMessageGetOnPostExecute)
                .build()
                .execute();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        
        initializeData();


        setContentView(R.layout.activity_home);
        mToolbar = getSupportActionBar();
        mToolbar.setTitle("Home");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mHomeFrag = new HomeFragment();

    }

    private void initializeData() {

        onWaitFragmentInteractionShow();
        mCredentials = (Credentials) getIntent().getSerializableExtra("credentials");
        mJsonData = new HashMap<>();
        getWeather();
        getContacts();
        getChats();
        
//        int i = 0;
//        while(!mWeatherLoaded){
//            System.out.print(i++ + "waiting...");
//        }
//        System.out.print("done");


    }

    private void getContacts() {
        JSONObject msg = JsonHelper.connections_JsonObject(mCredentials.getID());
        if (null != msg) {
            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath(getString(R.string.ep_base_url))
                    .appendPath(getString(R.string.ep_conn))
                    .appendPath(getString(R.string.ep_getall))
                    .build();

            new SendPostAsyncTask.Builder(uri.toString(), msg)
                    .onPostExecute(this::handleConnectionsPost)
                    .onCancelled(this::handleErrorsInTask)
                    .build()
                    .execute();

        }
    }

    private void handleConnectionsPost(String s) {
        try {
            JSONObject result = new JSONObject(s);
            mJsonData.put("connections", result);
        } catch (JSONException e){
            Log.e("JSON PARSE ERROR", s + System.lineSeparator() + e.getMessage());
        }
        mConnectionsLoaded = true;
    }

    /** For getting weather with a specific lat/lon */
    private void getWeather(double lat, double lon){
        JSONObject msg = JsonHelper.weather_JsonObject(lat, lon);

        if (null != msg) {
            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath(getString(R.string.ep_base_url))
                    .appendPath(getString(R.string.ep_weather))
                    .appendPath(getString(R.string.ep_tenday))
                    .build();

            new SendPostAsyncTask.Builder(uri.toString(), msg)
                    .onPostExecute(this::handleWeatherPost)
                    .onCancelled(this::handleErrorsInTask)
                    .build()
                    .execute();
        }
    }

    /** For getting weather with a the phone's location */
    private void getWeather() {

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }

        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitutde = location.getLongitude();
        double latitude = location.getLatitude();
        JSONObject msg = JsonHelper.weather_JsonObject(latitude, longitutde);

        if (null != msg) {
            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath(getString(R.string.ep_base_url))
                    .appendPath(getString(R.string.ep_weather))
                    .appendPath(getString(R.string.ep_tenday))
                    .build();

            new SendPostAsyncTask.Builder(uri.toString(), msg)
                    .onPostExecute(this::handleWeatherPost)
                    .onCancelled(this::handleErrorsInTask)
                    .build()
                    .execute();
        }


    }

    private void getChats(){
        JSONObject msg = JsonHelper.chats_JsonObject(mCredentials.getID());
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_messaging))
                .appendPath(getString(R.string.ep_getmy))
                .build();

        new SendPostAsyncTask.Builder(uri.toString(), msg)
                .onPostExecute(this::onGetMessagesPost)
                .onCancelled(this::handleErrorsInTask)
                .build()
                .execute();
    }

    private void onGetMessagesPost(String s) {
        try {
            JSONObject result = new JSONObject(s);
            mJsonData.put("chats", result);
        } catch (JSONException e){
            Log.e("JSON PARSE ERROR", s + System.lineSeparator() + e.getMessage());
        }
        mChatsLoaded = true;
        initHomeFrag();
    }

    private void initHomeFrag() {
        Bundle args = new Bundle();
        args.putString("forecast", mJsonData.get("forecast").toString());
        args.putSerializable("credentials", mCredentials);
        mHomeFrag.setArguments(args);
        loadFragment(mHomeFrag);
    }

    private void updateHomeFrag() {
        Bundle args = new Bundle();
        args.putString("forecast", mJsonData.get("forecast").toString());
        mHomeFrag.setArguments(args);
        mHomeFrag.updateContent();
        //args.putJ("forecast", mJsonData.get("forecast"));
    }

    private void handleWeatherPost(String s) {
        try {
            JSONObject result = new JSONObject(s);
            mJsonData.put("forecast", result);
        } catch (JSONException e){
            Log.e("JSON PARSE ERROR", s + System.lineSeparator() + e.getMessage());
        }
        mWeatherLoaded = true;
        onWaitFragmentInteractionHide();
    }
    private void handleErrorsInTask(String result){
        Log.e("ASYNC_TASK_ERROR", result);
    }

    private void loadFragment(Fragment theFragment) {

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, theFragment)
                .addToBackStack(null);

        transaction.commit();
    }


    /** Depreciated by onGetMessagesPost ? */
    protected void handleConnectionGetOnPostExecute(final String result) {
        try {
            JSONObject root = new JSONObject(result);
            if (root.has("connections")) {
                JSONArray response = root.getJSONArray("connections");

                List<Connection> conns = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    JSONObject jsonSet = response.getJSONObject(i);

                    conns.add(new Connection.Builder(jsonSet.getString("username"),
                            jsonSet.getString("email"))
                            .addFirstName(jsonSet.getString("firstname"))
                            .addLastName(jsonSet.getString("lastname"))
                            .build());
                    Log.e("Testing get method", conns.get(i).getUsername());
                }
                Connection[] connectionsAsArray = new Connection[conns.size()];
                connectionsAsArray = conns.toArray(connectionsAsArray);
                Bundle args = new Bundle();
                args.putSerializable(ConnectionFragment.ARG_CONNECTION_LIST, connectionsAsArray);
                Fragment frag = new ConnectionFragment();
//                    args.putSerializable(BlogFragment.ARG_BLOG_LIST, blogsAsArray);
//                    Fragment frag = new BlogFragment();
                frag.setArguments(args);
             //   onWaitFragmentInteractionHide();
                loadFragment(frag);
            } else {
                Log.e("ERROR!", "No data array"); //notify user
                onWaitFragmentInteractionHide();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        //    onWaitFragmentInteractionHide();
        }
    }

    //OPEN MESSAGE SCREEN
    protected void handleOpenMessageGetOnPostExecute(final String result)   {
        try {
            JSONObject root = new JSONObject(result);
            if(root.has("chats"))   {
                JSONArray response = root.getJSONArray("chats");
                List<OpenMessage> open = new ArrayList<>();
                for(int i = 0; i < response.length(); i++)  {
                    JSONObject jsonSet = response.getJSONObject(i);
                    open.add(new OpenMessage.Builder(
                                jsonSet.getString("name"))
                                .addDate("XX/XX/XXXX")
                                .addTime("XX:XX PM")
                                .addChatId(jsonSet.getInt("chatid"))
                                .build());
                }
                OpenMessage[] openMessagesAsArray = new OpenMessage[open.size()];
                openMessagesAsArray = open.toArray(openMessagesAsArray);
                Bundle b = new Bundle();
                b.putSerializable(OpenMessagesFragment.ARG_CONNECTION_LIST, openMessagesAsArray);
                Fragment frag = new OpenMessagesFragment();
                frag.setArguments(b);
          //      onWaitFragmentInteractionHide();
                loadFragment(frag);
            } else {
                Log.e("ERROR!", "No data array");
          //      onWaitFragmentInteractionHide();
            }
        } catch (JSONException e)   {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
         //   onWaitFragmentInteractionHide();
        }
    }

    //MESSAGE SCREEN
    protected void handleMessageGetOnPostExecute(final String result) {
        try {
            JSONObject root = new JSONObject(result);
            if(root.has("messages"))    {
                JSONArray response = root.getJSONArray("messages");
                List<Message> msgs = new ArrayList<>();
                for(int i = 0; i < response.length(); i++)  {
                    JSONObject jsonSet = response.getJSONObject(i);
                    String date = jsonSet.getString("timestamp").substring(0,
                            jsonSet.getString("timestamp").indexOf(" "));
                    String time = jsonSet.getString("timestamp").substring(jsonSet.getString(
                            "timestamp").indexOf(" "),
                            jsonSet.getString("timestamp").indexOf("."));
                    msgs.add(new Message.Builder(
                            jsonSet.getString("email"))
                            .addDate(date)
                            .addTime(time)
                            .addMessage(jsonSet.getString("message"))
                            .addChatId(13)//TODO UNHARDCODE THIS
                            .build());
                }
                Message[] msgsAsArray = new Message[msgs.size()];
                msgsAsArray = msgs.toArray(msgsAsArray);
                Bundle b = new Bundle();
                b.putSerializable(MessagesFragment.ARG_MESSAGE_LIST, msgsAsArray);
                b.putSerializable("credentials", mCredentials);
                Fragment frag = new MessagesFragment();
                frag.setArguments(b);
      //          onWaitFragmentInteractionHide();
                loadFragment(frag);
            } else {
                Log.e("ERROR!", "No data array");
       //         onWaitFragmentInteractionHide();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Error!", e.getMessage());
       //     onWaitFragmentInteractionHide();
        }
    }

    protected void handleMessageSendPost(final String result)   {
        try {
            JSONObject root = new JSONObject(result);
            if(root.has("success")) {
                Log.d("Message", "Message successfully sent.");
            } else {
                Log.d("Message", "Message FAILED to send.");
            }
        } catch (JSONException e)   {
            e.printStackTrace();
            Log.e("Error!", e.getMessage());
        }
    }



//*************** FRAGMENT INTERACTION LISTENERS ***************//


    //Home Fragment
    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    //OpenMessage Fragment
    @Override
    public void onOpenMessageFragmentInteraction(OpenMessage item) {
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_messaging))
                .appendPath(getString(R.string.ep_getall))
                .build();
        new SendPostAsyncTask.Builder(uri.toString(), item.asJSONObject())
          //      .onPreExecute(this::onWaitFragmentInteractionShow)
                .onPostExecute(this::handleMessageGetOnPostExecute)
                .build()
                .execute();
//        MessagesFragment mf = new MessagesFragment();
//        Bundle args = new Bundle();
//        args.putSerializable("key", item);
//        mf.setArguments(args);
//        loadFragment(mf);
    }

    //Messages Fragment
    @Override
    public void onMessageFragmentInteraction(Message item) {
//        MessagesFragment mf = new MessagesFragment();
//        Bundle args = new Bundle();
//        args.putSerializable(MessagesFragment.ARG_MESSAGE_LIST, item);
//        mf.setArguments(args);
//        loadFragment(mf);
    }

    @Override
    public void onMessageSendInteraction(Message theMessage)   {
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_messaging))
                .appendPath(getString(R.string.ep_send))
                .build();
        new SendPostAsyncTask.Builder(uri.toString(), theMessage.asJSONObject())
                .onPostExecute(this::handleMessageSendPost)
                .build()
                .execute();
    }

    @Override
    public void onConnectionFragmentInteraction(Connection item) {

    }

//    //Weather
//    @Override
//    public void onWeatherFragmentInteraction(Uri uri) {
//
//    }

    //unused
    @Override
    public void onConnectionSearchFragmentInteraction(Connection item)  {

    }

    //Connections Screen -> Search Button
    @Override
    public void onConnectionSearchInteraction() {
        ConnectionsSearchFragment csf = new ConnectionsSearchFragment();
        Bundle args = new Bundle();
        args.putSerializable("key", mCredentials);
        csf.setArguments(args);
        loadFragment(csf);
    }

    //Connection screen -> Request button
    @Override
    public void onConnectionRequestInteraction() {
        ConnectionRequestsFragment crf = new ConnectionRequestsFragment();
        Bundle args = new Bundle();
        args.putSerializable("key", mCredentials);
        crf.setArguments(args);
        loadFragment(crf);
    }

    //Connection Request Screen (Will be unused?)
    @Override
    public void onConnectionRequestFragmentInteraction(Connection item) {

    }

    //WAIT SCREEN
    @Override
    public void onWaitFragmentInteractionShow() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentContainer, new WaitFragment(), "WAIT")    //ACTIVITY MAY BE INCORRECT
                .addToBackStack(null)
                .commit();
    }

    //WAIT SCREEN
    @Override
    public void onWaitFragmentInteractionHide() {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(getSupportFragmentManager().findFragmentByTag("WAIT"))
                .commit();
    }


    //*************** ASYNC METHODS ***************//

    private void logout(){
        SharedPreferences prefs =
                getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        //remove the saved credentials from StoredPrefs
        prefs.edit().remove(getString(R.string.keys_prefs_password)).apply();
        prefs.edit().remove(getString(R.string.keys_prefs_email)).apply();
        //close the app
        finishAndRemoveTask();
        //or close this activity and bring back the Login
// Intent i = new Intent(this, MainActivity.class);
// startActivity(i);
// //Ends this Activity and removes it from the Activity back stack.
// finish();
    }




    // Deleting the InstanceId (Firebase token) must be done asynchronously. Good thing
// we have something that allows us to do that.
    class DeleteTokenAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onWaitFragmentInteractionShow();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            //since we are already doing stuff in the background, go ahead
            //and remove the credentials from shared prefs here.
            SharedPreferences prefs =
                    getSharedPreferences(
                            getString(R.string.keys_shared_prefs),
                            Context.MODE_PRIVATE);
            prefs.edit().remove(getString(R.string.keys_prefs_password)).apply();
            prefs.edit().remove(getString(R.string.keys_prefs_email)).apply();
            try {
                //this call must be done asynchronously.
                FirebaseInstanceId.getInstance().deleteInstanceId();
            } catch (IOException e) {
                Log.e("FCM", "Delete error!");
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //close the app
            finishAndRemoveTask();
            //or close this activity and bring back the Login
// Intent i = new Intent(this, MainActivity.class);
// startActivity(i);
// //Ends this Activity and removes it from the Activity back stack.
// finish();
        }
    }
}