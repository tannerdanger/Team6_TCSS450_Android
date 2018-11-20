package group6.tcss450.uw.edu.chatapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

import group6.tcss450.uw.edu.chatapp.contacts.ConnectionFragment;
import group6.tcss450.uw.edu.chatapp.contacts.ConnectionRequestsFragment;
import group6.tcss450.uw.edu.chatapp.contacts.ConnectionsSearchFragment;
import group6.tcss450.uw.edu.chatapp.messages.Message;
import group6.tcss450.uw.edu.chatapp.messages.OpenMessage;
import group6.tcss450.uw.edu.chatapp.contacts.Connection;
import group6.tcss450.uw.edu.chatapp.utils.Credentials;
import group6.tcss450.uw.edu.chatapp.utils.DataHandler;
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
        WaitFragment.OnFragmentInteractionListener,
        DataHandler.OnDataLoadedListener {

    private TextView mTextMessage;

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
                    mDataHandler.getChats(true);
//                    navigateChat(); //called by async task
                    return true;

                case R.id.navigation_logout:
                    logout();
            }

            return false;
        }

    };

    protected void navigateHome()   {



        Bundle args = new Bundle();
        args.putString(getString(R.string.ARGS_FORECAST_DATA), mJsonData.get(getString(R.string.ARGS_FORECAST_DATA)).toString());
        args.putSerializable(getString(R.string.ARGS_CREDENTIALS), mCredentials);
        mHomeFrag.setArguments(args);
        loadFragment(mHomeFrag);

    }

    protected void navigateConnections() {



        mDataHandler.updateContacts(); //THIS REPLACED EVERYTHING
        Connection[] conns = mDataHandler.getContactList(mJsonData.get(getString(R.string.ARGS_CONNECTIONS))); //TODO: MAY RETURN NULL IF CONNECTIONS
        if(null == conns){
            conns = new Connection[0];
        }
        Bundle args = new Bundle();
        args.putSerializable(ConnectionFragment.ARG_CONNECTION_LIST, conns);
        Fragment frag = new ConnectionFragment();
        frag.setArguments(args);



        loadFragment(frag);

    }

    protected void navigateNotifications(){}

    public void navigateChat()   {

        OpenMessage[] chats = mDataHandler.getChatArray(mJsonData.get(getString(R.string.ARGS_CHATROOMS)));

        Fragment frag = new OpenMessagesFragment();

        if(chats != null){
            Bundle b = new Bundle();
            b.putSerializable(OpenMessagesFragment.ARG_CONNECTION_LIST, chats);
            frag.setArguments(b);
        }
        loadFragment(frag);

    }

    public void navigateMessages(int chatid){


        Message[] messages = new Message[mMessageListMap.get(chatid).size()];
        mMessageListMap.get(chatid).toArray(messages);
        System.out.print(" BREAKPOINT ");

        Bundle b = new Bundle();
        b.putSerializable(MessagesFragment.ARG_MESSAGE_LIST, messages);
        b.putSerializable(getString(R.string.ARGS_CREDENTIALS), mCredentials);
        b.putInt(MessagesFragment.ARG_CHAT_ID, chatid);
        Fragment frag = new MessagesFragment();
        frag.setArguments(b);
        loadFragment(frag);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mCredentials = (Credentials) getIntent().getSerializableExtra("credentials");
        mLat = (Double) getIntent().getDoubleExtra("lat", -1 );
        mLon = (Double) getIntent().getDoubleExtra("lon", -1 );

        mJsonData = new HashMap<>();
        mMessageListMap = new HashMap<>();
        mIsInit = false;
        mDataHandler = new DataHandler(this, mCredentials, mLat, mLon, true);




        setContentView(R.layout.activity_home);
        mToolbar = getSupportActionBar();
        mToolbar.setTitle("Home");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mChatId = -1;

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
     * @param chatid the id the message belongs to
     * @param message the message to be added.
     */
    public void addMessage(Integer chatid, Message message){

        if(null != message){

            // If map doesn't contain messages for this chat id, create empty LL
            if (!mMessageListMap.containsKey(chatid)) {
                ArrayList<Message> tmp = new ArrayList<>();
                mMessageListMap.put(chatid, tmp);
            }

            mMessageListMap.get(chatid).add(message);

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

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, theFragment)
                .addToBackStack(null);

        transaction.commit();
    }


    protected void handleConnectionAcceptReject(final String result)  {
        try {
            JSONObject root = new JSONObject(result);
            if(root.getBoolean("success"))   {
                Log.d("ConnectIon", "Connection successfully ADDED/REMOVED.");
            } else {
                Log.d("ConnectIon", "Connection ADD/REMOVE failed.");
            }
        } catch (JSONException e)   {
            e.printStackTrace();
            Log.e("Error!", e.getMessage());
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

    protected void handleConnectionToMessaging(final String result) {
        try {
            JSONObject root = new JSONObject(result);
            if(root.getBoolean("success"))  {
                mChatId = root.getInt("chatid");
            } else {
                JSONObject currentConnections = mJsonData.get("chats");
                JSONArray currentConnectionArray = currentConnections.getJSONArray("chats");
                for(int i = 0; i < currentConnectionArray.length(); i++)    {
                    JSONObject j = currentConnectionArray.getJSONObject(i);
                    if(j.getString("name").contains((mOpenChatWith)))   {
                        mChatId = j.getInt("chatid");
                    }
                }
            }
            OpenMessage om = new OpenMessage.Builder(mOpenChatWith).addChatId(mChatId).build();
            onOpenMessageFragmentInteraction(om);
        } catch (JSONException e)   {
            e.printStackTrace();
            Log.e("Error!", e.getMessage());
        }
    }

    protected void handleProposeFriend(final String result) {
        try {
            JSONObject root = new JSONObject(result);
            if(root.getBoolean("success")) {
                Log.d("Propose", "Propose friend successful");
            } else {
                Log.d("Propose", "Propose friend failed");
            }
        } catch (JSONException e)   {
            Log.e("ERROR!", "Propose friend failed");
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

        mDataHandler.getMessages(item.getChatId(), true);

//        Uri uri = new Uri.Builder()
//                .scheme("https")
//                .appendPath(getString(R.string.ep_base_url))
//                .appendPath(getString(R.string.ep_messaging))
//                .appendPath(getString(R.string.ep_getall))
//                .build();
//        mChatId = item.getChatId();
//        new SendPostAsyncTask.Builder(uri.toString(), item.asJSONObject())
//                .onPostExecute(this::handleMessageGetOnPostExecute)
//                .build()
//                .execute();
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
        mChatId = theMessage.getChatId();
        new SendPostAsyncTask.Builder(uri.toString(), theMessage.asJSONObject())
                .onPostExecute(this::handleMessageSendPost)
                .build()
                .execute();
    }

    @Override
    public void onConnectionFragmentStartChat(Connection connection)    {
        mOpenChatWith = connection.getUsername();
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_messages))
                .appendPath(getString(R.string.ep_new))
                .build();
        JSONObject msg = new JSONObject();
        try {
            msg.put("memberid", mCredentials.getID());
            msg.put("username", mCredentials.getUsername());
            msg.put("their_id", connection.getId());
            msg.put("their_username", connection.getUsername());
        } catch (JSONException e)   {
            Log.e("ERROR!", "Unable to create new chat JSON to send");
            e.printStackTrace();
        }
        new SendPostAsyncTask.Builder(uri.toString(), msg)
                .onPostExecute(this::handleConnectionToMessaging)
                .build()
                .execute();
    }

    @Override
    public void onConnectionFragmentRemove(Connection item) {
        onConnectionRequestReject(item);
    }



    //unused
    @Override
    public void onConnectionSearchFragmentInteraction(Connection item)  {
        //onConnectionRequestReject(item);
        Uri uri = new Uri.Builder()
                .scheme("http")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_conn))
                .appendPath(getString(R.string.ep_propose))
                .build();
        JSONObject msg = new JSONObject();
        try {
            msg.put("memberid", mCredentials.getID());
            msg.put("their_id", item.getId());
        } catch (JSONException e)   {
            Log.e("ERROR!", "Search add button failed creating JSON to send");
            e.printStackTrace();
        }
        new SendPostAsyncTask.Builder(uri.toString(), msg)
                .onPostExecute(this::handleProposeFriend)
                .build()
                .execute();
    }

    //Connections Screen -> Search Button
    @Override
    public void onConnectionSearchInteraction(Bundle b) {
        ConnectionsSearchFragment csf = new ConnectionsSearchFragment();
        Bundle args = new Bundle();
        args.putSerializable(getString(R.string.ARGS_CREDENTIALS), mCredentials);
        args.putSerializable(ConnectionFragment.ARG_CONNECTION_LIST,
                b.getSerializable(ConnectionFragment.ARG_CONNECTION_LIST));
        csf.setArguments(args);
        loadFragment(csf);
    }

    //Connection screen -> Request button
    @Override
    public void onConnectionRequestInteraction(Bundle b) {
        ConnectionRequestsFragment crf = new ConnectionRequestsFragment();
        Bundle args = new Bundle();
        args.putSerializable(getString(R.string.ARGS_CREDENTIALS), mCredentials);
        args.putSerializable(ConnectionFragment.ARG_CONNECTION_LIST,
                b.getSerializable(ConnectionFragment.ARG_CONNECTION_LIST));
        crf.setArguments(args);
        loadFragment(crf);
    }

    //Connection Request Screen (Will be unused?)
    @Override
    public void onConnectionRequestAccept(Connection receiver) {
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_conn))
                .appendPath(getString(R.string.ep_approve))
                .build();
        JSONObject msg = new JSONObject();
        try {
            msg.put(getString(R.string.JSON_USERS_MEMBER_ID), mCredentials.getID());
            msg.put(getString(R.string.JSON_OTHERS_MEMBER_ID), receiver.getId());
        } catch (JSONException e) {
            Log.e("Connection:Accept", "Unable to accept connection request");
            e.printStackTrace();
        }
        new SendPostAsyncTask.Builder(uri.toString(), msg)
                .onPostExecute(this::handleConnectionAcceptReject)
                .build()
                .execute();
    }

    @Override
    public void onConnectionRequestReject(Connection receiver)  {
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_conn))
                .appendPath(getString(R.string.ep_remove))
                .build();
        JSONObject msg = new JSONObject();
        try {
            msg.put(getString(R.string.JSON_USERS_MEMBER_ID), mCredentials.getID());
            msg.put(getString(R.string.JSON_OTHERS_MEMBER_ID), receiver.getId());
        } catch (JSONException e) {
            Log.e("Connection:Reject", "Unable to reject/remove connection ");
            e.printStackTrace();
        }
        new SendPostAsyncTask.Builder(uri.toString(), msg)
                .onPostExecute(this::handleConnectionAcceptReject)
                .build()
                .execute();
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


















/*
        SAVED OLD CODE


            //OPEN MESSAGE SCREEN (displaying all message groups)
    protected void handleOpenMessageGetOnPostExecute(final String result)   {
        try {
            JSONObject root = new JSONObject(result);
            if(root.has("chats"))   {
                JSONArray response = root.getJSONArray("chats");
                List<OpenMessage> open = new ArrayList<>();
                for(int i = 0; i < response.length(); i++)  {
                    JSONObject jsonSet = response.getJSONObject(i);
                    OpenMessage om = new OpenMessage.Builder(
                            jsonSet.getString("name"))
                            .addDate("XX/XX/XXXX")
                            .addTime("XX:XX PM")
                            .addChatId(jsonSet.getInt("chatid"))
                            .build();
                    open.add(om);
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

    //MESSAGE SCREEN (displaying all messages in a chat)
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
                            .addChatId(mChatId)
                            .build());
                }
                Message[] msgsAsArray = new Message[msgs.size()];
                msgsAsArray = msgs.toArray(msgsAsArray);
                Bundle b = new Bundle();
                b.putSerializable(MessagesFragment.ARG_MESSAGE_LIST, msgsAsArray);
                b.putSerializable(getString(R.string.ARGS_CREDENTIALS), mCredentials);
                b.putInt(MessagesFragment.ARG_CHAT_ID, mChatId);
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
 */