package group6.tcss450.uw.edu.chatapp.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import group6.tcss450.uw.edu.chatapp.R;
import group6.tcss450.uw.edu.chatapp.contacts.Connection;
import group6.tcss450.uw.edu.chatapp.contacts.ConnectionFragment;
import group6.tcss450.uw.edu.chatapp.contacts.ConnectionRequestsFragment;
import group6.tcss450.uw.edu.chatapp.contacts.ConnectionsSearchFragment;
import group6.tcss450.uw.edu.chatapp.messages.Message;
import group6.tcss450.uw.edu.chatapp.messages.OpenMessage;
import group6.tcss450.uw.edu.chatapp.utils.Credentials;
import group6.tcss450.uw.edu.chatapp.utils.DataHandler;
import group6.tcss450.uw.edu.chatapp.utils.MyFirebaseMessagingService;
import group6.tcss450.uw.edu.chatapp.utils.Notification;
import group6.tcss450.uw.edu.chatapp.utils.PlaceAutocompleteAdapter;
import group6.tcss450.uw.edu.chatapp.utils.SendPostAsyncTask;
import group6.tcss450.uw.edu.chatapp.utils.UriHelper;
import group6.tcss450.uw.edu.chatapp.utils.WaitFragment;
import group6.tcss450.uw.edu.chatapp.weather.WeatherFragment;
import group6.tcss450.uw.edu.chatapp.weather.WeatherSeattingMapsActivity;
import group6.tcss450.uw.edu.chatapp.weather.weatherSeattingFragment;

import static group6.tcss450.uw.edu.chatapp.view.NotificationFragment.ARG_FRIEND_REQUEST_NOTIFICATION_LIST;
import static group6.tcss450.uw.edu.chatapp.view.NotificationFragment.OnListFragmentInteractionListener;

/**
 * This is the main activity that hosts all of the app's fragments.
 * @Author Tanner Brown
 * @Version 15 Nov 2018
 */
public class HomeActivity extends AppCompatActivity
        implements ChatRoomSelectionFragment.OnOpenMessageFragmentInteractionListener,
        HomeFragment.OnFragmentInteractionListener,
        MessagesFragment.OnMessageFragmentInteractionListener,
        ConnectionFragment.OnConnectionsFragmentInteractionListener,
        ConnectionsSearchFragment.OnConnectionSearchFragmentInteractionListener,
        ConnectionRequestsFragment.OnConnectionRequestFragmentInteractionListener,
        OnListFragmentInteractionListener,
        WeatherFragment.OnFragmentInteractionListener,
        WaitFragment.OnFragmentInteractionListener,
        DataHandler.OnDataLoadedListener, NewChatFragment.OnListFragmentInteractionListener,
        weatherSeattingFragment.OnSettingsFragmentInteractionListener,
        GoogleApiClient.OnConnectionFailedListener
{

    private TextView mTextMessage;
    private FirebaseMessageReceiver mFirebaseMessageReceiver;
    private Credentials mCredentials;
    private DataHandler mDataHandler;
    private ActionBar mToolbar;
    private HashMap <String, JSONObject> mJsonData;
    private boolean mIsInit;
    private int mChatId;
    private String mOpenChatWith;
    private HomeFragment mHomeFrag;
    private double mLon;
    private double mLat;
    private HashMap<Integer, ArrayList<Message>> mMessageListMap;
    public static final int MIN_PASSWORD_LENGTH = 3;
    public FloatingActionButton mFab;
    private GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mToolbar.setTitle("HOME");
                    navigateHome();
                    mFab.show();
                    return true;
                case R.id.nav_connections:
                    mToolbar.setTitle("CONTACTS");
                    navigateConnections();
                    mFab.show();
                    return true;
                case R.id.navigation_notifications:
                    // TODO ELI
                    mToolbar.setTitle("NOTIFICATIONS");
                    navigateNotifications();
                    mFab.show();
                    return true;
                case R.id.nav_solo_chat:
                    mToolbar.setTitle("CHAT");
                    mDataHandler.getChats(true);
                    navigateChat(); //called by async task
                    mFab.show();
                    return true;
                case R.id.nav_settings:
                    mToolbar.setTitle("SETTINGS");
                    navigateSettings();

//                case R.id.navigation_logout:
//                    logout();
            }

            return false;
        }

    };


    /**
     * Navigate to the settings page.
     */
    protected void navigateSettings(){
        Bundle args = new Bundle();
        args.putSerializable(getString(R.string.ARGS_CREDENTIALS), mCredentials);
        weatherSeattingFragment frag = new weatherSeattingFragment();
        frag.setArguments(args);
        loadFragment(frag);

    }


    /** For navigating to the home fragment */
    public void navigateHome()   {


        Bundle args = new Bundle();
        args.putString(getString(R.string.ARGS_FORECAST_DATA), mJsonData.get(getString(R.string.ARGS_FORECAST_DATA)).toString());
        args.putSerializable(getString(R.string.ARGS_CREDENTIALS), mCredentials);
        mHomeFrag.setArguments(args);
        loadFragment(mHomeFrag);

    }

    /** For navigating to the connections fragment */
    protected void navigateConnections() {



        mDataHandler.updateContacts(); //THIS REPLACED EVERYTHING
        Fragment frag = new ConnectionFragment();
        Connection[] conns = mDataHandler.getContactList(mJsonData.get(getString(R.string.ARGS_CONNECTIONS))); //TODO: MAY RETURN NULL IF CONNECTIONS
        if(null == conns){
            conns = new Connection[0];
        }
        Bundle args = new Bundle();
        args.putSerializable(ConnectionFragment.ARG_CONNECTION_LIST, conns);

        frag.setArguments(args);



        loadFragment(frag);

    }

    /** For navigating to the notifications fragment */
    protected void navigateNotifications() {
        //TODO
        Fragment fragment = new NotificationFragment();
        List<Connection> l = new ArrayList<>(Arrays.asList(mDataHandler
                .getContactList(mJsonData.get(getString(R.string.ARGS_CONNECTIONS)))));
        l.removeIf(c ->(c.getVerified() == 1));

        Notification[] n = new Notification[l.size()];
        for (int i = 0; i < n.length; i++) {
            n[i] = new Notification.Builder(l.get(i).mUsername,
                    Notification.NotificationType.FRIEND_REQUEST)
                    .build();
        }

        mDataHandler.updateContacts(); //THIS REPLACED EVERYTHING
        Connection[] connections = mDataHandler
                .getContactList(mJsonData.get(getString(R.string.ARGS_CONNECTIONS)));


        Bundle args = new Bundle();

        args.putSerializable(ARG_FRIEND_REQUEST_NOTIFICATION_LIST, n);
        fragment.setArguments(args);
        loadFragment(fragment);
    }

    /** For navigating to the chat fragment */
    public void navigateChat()   {

        OpenMessage[] chats = mDataHandler.getChatArray(mJsonData.get(getString(R.string.ARGS_CHATROOMS)));

        Fragment frag = new ChatRoomSelectionFragment();

        if(chats != null){
            Bundle b = new Bundle();
            b.putSerializable(ChatRoomSelectionFragment.ARG_CONNECTION_LIST, chats);
            frag.setArguments(b);
        }
        loadFragment(frag);
    }

    /**
     * For navigating to a chatid
     * @param chatID the id of the chat to open
     */
    public void navigateMessages(int chatID) {


        System.out.print("Breakpoint");

        Message[] messages = new Message[0];

        if(mMessageListMap.containsKey(chatID)) {

            int msgCount = mMessageListMap.get(chatID).size();
            if (msgCount > 0) {
                messages = new Message[msgCount];
                mMessageListMap.get(chatID).toArray(messages);
            }
        }else{

            mMessageListMap.put(chatID, new ArrayList<>());

        }

        Bundle b = new Bundle();
        b.putSerializable(MessagesFragment.ARG_MESSAGE_LIST, messages);
        b.putSerializable(getString(R.string.ARGS_CREDENTIALS), mCredentials);
        b.putInt(MessagesFragment.ARG_CHAT_ID, chatID);

        Fragment frag = new MessagesFragment();


        frag.setArguments(b);
        mFab.hide();
        loadFragment(frag);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseMessageReceiver = new FirebaseMessageReceiver();
        IntentFilter iFilter = new IntentFilter(MyFirebaseMessagingService.RECEIVED_NEW_MESSAGE);
        registerReceiver(mFirebaseMessageReceiver, iFilter);




        mCredentials = (Credentials) getIntent().getSerializableExtra("credentials");
        mLat = getIntent().getDoubleExtra("lat", -1 );
        mLon = getIntent().getDoubleExtra("lon", -1 );

        mJsonData = new HashMap<>();
        mMessageListMap = new HashMap<>();
        mIsInit = false;
        mDataHandler = new DataHandler(this, mCredentials, mLat, mLon, true);




        setContentView(R.layout.activity_home);
        mToolbar = getSupportActionBar();
        mToolbar.setTitle("Home");

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //  mChatId = -1;

        mFab = findViewById(R.id.fab);

        mFab.setOnClickListener((View v) -> {
            Fragment frag = new NewChatFragment();
            Bundle args = new Bundle();
            args.putSerializable(getString(R.string.ARGS_CONNECTIONS),
                    mDataHandler.getContactList(mJsonData.get(getString(R.string.ARGS_CONNECTIONS))));
            args.putSerializable(getString(R.string.ARGS_CREDENTIALS), mCredentials);
            frag.setArguments(args);
            loadFragment(frag);
            mFab.hide();
        });

        AutocompleteFilter filter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .build();
        mGeoDataClient = Places.getGeoDataClient(this);
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this);
        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGeoDataClient, LAT_LNG_BOUNDS, filter);
    }


    /**
     * This method is called from the DataHandler class to update data in the mJsonData map.
     *
     * @param key the key identifying the data in the hashmap
     * @param obj the JSONObject beting added to the map.
     */
    public void updateJsonData(String key, JSONObject obj){
        if(null != obj){
            mJsonData.put(key, obj);
        }
    }

    /**
     * Adds a message to the proper message list
     * @param chatID the id the message belongs to
     * @param message the message to be added.
     */
    public void addMessage(Integer chatID, Message message){
        if (!mMessageListMap.containsKey(chatID)) {
            ArrayList<Message> tmp = new ArrayList<>();
            mMessageListMap.put(chatID, tmp);
        }

        if(null != message){

            if(!mMessageListMap.get(chatID).contains(message))   {
                mMessageListMap.get(chatID).add(message);
            }

        }



    }

    /**
     * Insures data is loaded after data initialization, then loads next fragment.
     */
    public void finishInit() {
        if(!mIsInit) {
            mHomeFrag = new HomeFragment();
            onWaitFragmentInteractionHide();
            mIsInit = true;
            navigateHome();
        }
    }


    /**
     * Switches to a new fragment.
     * @param theFragment the new fragment to be displayed.
     */
    private void loadFragment(Fragment theFragment) {

        String fragtag = getTag(theFragment);
        FragmentTransaction transaction;

        if(null == fragtag) {

            transaction = getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, theFragment)
                    .addToBackStack(null);
        } else {
            transaction = getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, theFragment, fragtag)
                    .addToBackStack(null);
        }

        transaction.commit();
    }

    /**
     * Helper method that sets a tag name based on the fragment type.
     * @param theFragment the fragment type
     * @return a tag name or null if one doesn't exist
     */
    private String getTag(Fragment theFragment) {
        if(theFragment instanceof HomeFragment) return getString(R.string.TAG_HomeActivity);
        if(theFragment instanceof ConnectionFragment) return getString(R.string.TAG_ConnectionActivity);
        if(theFragment instanceof MessagesFragment) return getString(R.string.TAG_MessageActivity);
        if(theFragment instanceof weatherSeattingFragment)return getString(R.string.TAG_SETTINGS);
        return null;
    }


    /**
     * Handler for after a message was sent.
     * @param result
     */
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

    /**
     * Helper method for updating the data structure that holds messages with a new message;
     * @param chatID the chatID that the message belongs to.
     */
    public void receiveMessage(int chatID) {
        Message m = null;
        ArrayList l = mMessageListMap.getOrDefault(chatID, null);
        if (l != null) {
            if(l.size() > 0) {
                m = (Message) l.get(l.size() - 1);//l.get(mMessageListMap.get(chatID).size());
            }
            if (null != m) {
                MessagesFragment frag = (MessagesFragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.TAG_MessageActivity));
                if (null != frag) {
                    frag.recieveMessage(m);
                }
            }
        }
    }




    //*************** FRAGMENT INTERACTION LISTENERS ***************//
    @Override
    public GeoDataClient getClient(){return this.mGeoDataClient; }

    @Override
    public PlaceAutocompleteAdapter getAdapter(){return this.mPlaceAutocompleteAdapter; }



    //OpenMessage Fragment
    @Override
    public void onOpenMessageFragmentInteraction(OpenMessage item) {
        mDataHandler.getMessages(item.getChatId(), true, false); //gets messages then navigates to it.
    }

    //Messages Fragment
    @Override
    public void onMessageFragmentInteraction(Message item) {
    }

    @Override
    public void onMessageSendInteraction(Message theMessage)   {
        String uri = UriHelper.MESSAGES_SEND();
        mChatId = theMessage.getChatId();

        new SendPostAsyncTask.Builder(uri, theMessage.asJSONObject())
                .onPostExecute(this::handleMessageSendPost)
                .build()
                .execute();
    }

    @Override
    public void onConnectionFragmentStartChat(Connection connection)    {
        // mOpenChatWith = connection.getUsername();
        mDataHandler.createOrOpenChatRoom(connection.getId(), connection.getUsername(), true);
    }

    @Override
    public void onConnectionFragmentRemove(Connection item) {
        mDataHandler.updateContacts();
        onConnectionRequestReject(item);
    }



    //unused
    @Override
    public void onConnectionSearchFragmentInteraction(Connection item)  {
        mDataHandler.proposeConnection(mCredentials.getID(), item.getUsername(), item.getId());
    }

    //Connections Screen -> Search Button
    @Override
    public void onConnectionSearchInteraction(Bundle b) {
        mDataHandler.updateContacts();

        ConnectionsSearchFragment csf = new ConnectionsSearchFragment();
        Bundle args = new Bundle();
        args.putSerializable(getString(R.string.ARGS_CREDENTIALS), mCredentials);

        Connection[] conns = mDataHandler.getContactList(mJsonData.get(getString(R.string.ARGS_CONNECTIONS))); //TODO: MAY RETURN NULL IF CONNECTIONS
        if(null == conns){
            conns = new Connection[0];
        }


        args.putSerializable(ConnectionFragment.ARG_CONNECTION_LIST, conns);
        csf.setArguments(args);
        loadFragment(csf);
    }

    //Connection screen -> Request button
    @Override
    public void onConnectionRequestInteraction(Bundle b) {
        mDataHandler.updateContacts();

        ConnectionRequestsFragment crf = new ConnectionRequestsFragment();
        Bundle args = new Bundle();
        args.putSerializable(getString(R.string.ARGS_CREDENTIALS), mCredentials);

        Connection[] conns = mDataHandler.getContactList(mJsonData.get(getString(R.string.ARGS_CONNECTIONS))); //TODO: MAY RETURN NULL IF CONNECTIONS
        if(null == conns){
            conns = new Connection[0];
        }

        args.putSerializable(ConnectionFragment.ARG_CONNECTION_LIST, conns);
        crf.setArguments(args);
        loadFragment(crf);
    }

    //Connection Request Screen (Will be unused?)
    @Override
    public void onConnectionRequestAccept(Connection receiver) {
        //mDataHandler.updateContacts();
        mDataHandler.acceptOrDenyConnectionRequest(mCredentials.getID(), receiver.getId(), true);
    }

    @Override
    public void onConnectionRequestReject(Connection receiver)  {
        //mDataHandler.updateContacts();
        mDataHandler.acceptOrDenyConnectionRequest(mCredentials.getID(), receiver.getId(), false);
    }


    @Override
    public void onNewChatFragmentInteraction(List<Connection> chosen) {
        mDataHandler.createMultiChat(chosen, mCredentials);
        navigateChat();
    }

    @Override
    public void onChatRoomDeleteInteraction(OpenMessage om) {
        mDataHandler.deleteChatroom(om);
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

    @Override
    public void onWeatherUpdated(JSONObject result) {
        if(null != result){
            mJsonData.put(getString(R.string.ARGS_FORECAST_DATA), result);
        }
    }

    //WAIT SCREEN
    @Override
    public void onWaitFragmentInteractionHide() {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(getSupportFragmentManager().findFragmentByTag("WAIT"))
                .commit();
    }




    @Override
    public void onNewZipcode(int zip) {
        mDataHandler.updateWeatherByZip(zip);
    }

    @Override
    public void onNewCity(String city) {
        Log.wtf("HOME ACTIVITY", "ON NEW CITY STARTED");


        mDataHandler.updateWeatherByCity(city);

        Log.wtf("HOME ACTIVITY", "ON NEW CITY ENDED");
    }

    /**
     * Recieved a new lat/long from settings page to update weather.
     * @param lat the latitude
     * @param lon the longitude.
     */
    @Override
    public void onNewLatLon(double lat, double lon) {
        Log.wtf("HOME ACTIVITY", "ON NEW LAT LONG STARTED");
        mLat = lat; mLon = lon;
        mDataHandler.updateWeatherByLatLon(lat, lon, true, false
        );
        Log.wtf("HOME ACTIVITY", "ON NEW LAT LONG STARTED");
    }

    @Override
    public void loadMap() {
        Intent intent = new Intent(getApplicationContext(), WeatherSeattingMapsActivity.class); //TODO: If you want to change back to drawer layout, change HomeActivity.class => MainActivity.Class
        intent.putExtra("lat", mLat);
        intent.putExtra("lon", mLon);
        intent.putExtra(getString(R.string.ARGS_CREDENTIALS), mCredentials);
        startActivityForResult(intent, 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.print("BREAKPOINT");
        if(requestCode == 1){
            if(resultCode == RESULT_OK){

                double lat = data.getDoubleExtra("lat", -1);
                double lon = data.getDoubleExtra("lon", -1);

                if( -1 != lat && -1 != lon){

                    Fragment frag = (weatherSeattingFragment)getSupportFragmentManager().findFragmentByTag(getString(R.string.TAG_SETTINGS));
                    ((weatherSeattingFragment) frag).getCoordsFromMap(lat, lon);
                }

            }
        }
    }

    /**
     * Updates the homefragment with new weather data when it is recieved.
     *
     * @param transition if true, also navigate to Homefragment.
     */
    public void weatherLoaded(Boolean transition) {

        mHomeFrag.updateContent(mJsonData.get(getString(R.string.ARGS_FORECAST_DATA)));
        if(transition)
            navigateHome();
    }

    @Override
    public void onLogout(){
        new DeleteTokenAsyncTask().execute();
    }

    /**
     * @Author Tanner Brown
     * @param connectionResult
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.wtf("SETTINGS", "connection failed");
    }

    @Override
    public void onListNotificationFragmentInteraction(Notification item) {
        // todo
        onConnectionRequestInteraction(new Bundle());
    }


    // ============= GETTERS AND SETTERS ============= //
    public double getLat(){return this.mLat; }
    public double getLom(){return this.mLon; }
    public void setLat(double lat){mLat = lat;}
    public void setLon(double lon){mLon = lon;}

    @Override
    public void onFragmentInteraction(Uri uri) {
        //Does nothing. Can't delete because messy code and people don't feel like contributing to the debugging process.
    }


    /**
     * A BroadcastReceiver setup to listen for messages sent from MyFirebaseMessagingService
     * that Android allows to run all the time.
     */
    private class FirebaseMessageReceiver extends BroadcastReceiver {
        private static final String CONNECTION_REQ_TYPE = "connection_req";
        private static final String MESSAGE_TYPE = "msg";
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.print("ON RECIEVE");
            if(intent.hasExtra("DATA")) {

                String data = intent.getStringExtra("DATA");
                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(data);
                    System.out.println("jObj: " + jObj.toString());

                    if(jObj.has("type")){
                        String type = jObj.getString("type");

                        if(CONNECTION_REQ_TYPE.compareTo(type)== 0){ //it is a new contact notification

                            if(jObj.has("sender") ){

                                int senderID = Integer.valueOf(jObj.getString("sender"));
                                System.out.print("New contact request from: " + senderID);
                                mDataHandler.updateContacts();

                                //TODO: Add notification object to notification page.

                            }

                        }else if(MESSAGE_TYPE.compareTo(type) == 0){ //it is a new text message


                            if(jObj.has("message") && jObj.has("sender") && jObj.has("chatid")) {

                                String sender = jObj.getString("sender");
                                String msg = jObj.getString("message");

                                int chatid = Integer.valueOf(jObj.getString("chatid"));


                                //mDataHandler.
                                mDataHandler.getMessages(chatid, false, true); //does not transition after loading message

                                Log.i("New Message", sender + " " + msg);
                            }
                        }//TODO: delete connection? Delete chat?
                    }
                } catch (JSONException e) {
                    Log.e("JSON PARSE", e.toString());
                }
            }
        }
    }


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
             Intent i = new Intent(getApplicationContext(), LoginActivity.class);
             startActivity(i);
            // //Ends this Activity and removes it from the Activity back stack.
            onWaitFragmentInteractionShow();
             finish();
        }
    }

}