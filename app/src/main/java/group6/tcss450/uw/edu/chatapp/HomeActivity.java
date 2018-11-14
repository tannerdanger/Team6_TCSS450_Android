package group6.tcss450.uw.edu.chatapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
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
import java.util.Arrays;
import java.util.List;

import group6.tcss450.uw.edu.chatapp.contacts.ConnectionFragment;
import group6.tcss450.uw.edu.chatapp.contacts.ConnectionRequestsFragment;
import group6.tcss450.uw.edu.chatapp.contacts.ConnectionsSearchFragment;
import group6.tcss450.uw.edu.chatapp.messages.Message;
import group6.tcss450.uw.edu.chatapp.messages.OpenMessage;
import group6.tcss450.uw.edu.chatapp.utils.Connection;
import group6.tcss450.uw.edu.chatapp.utils.Credentials;
import group6.tcss450.uw.edu.chatapp.utils.SendPostAsyncTask;
import group6.tcss450.uw.edu.chatapp.utils.WaitFragment;
import group6.tcss450.uw.edu.chatapp.weather.ForecastFragment;
import group6.tcss450.uw.edu.chatapp.weather.WeatherFragment;

public class HomeActivity extends AppCompatActivity
        implements HomeFragment.OnFragmentInteractionListener,
        OpenMessagesFragment.OnOpenMessageFragmentInteractionListener,
        MessagesFragment.OnMessageFragmentInteractionListener,
        ConnectionFragment.OnConnectionsFragmentInteractionListener,
        WeatherTabbedContainer.OnFragmentInteractionListener,
        WeatherFragment.OnFragmentInteractionListener,
        ForecastFragment.OnFragmentInteractionListener,
        ConnectionsSearchFragment.OnConnectionSearchFragmentInteractionListener,
        ConnectionRequestsFragment.OnConnectionRequestFragmentInteractionListener,
        WaitFragment.OnFragmentInteractionListener  {

    private TextView mTextMessage;

    private Credentials mCredentials;
    private ActionBar mToolbar;
    private Connection[] mSearchResults;

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

    protected void navigateChat(){
        OpenMessagesFragment frag = new OpenMessagesFragment();
        Bundle args = new Bundle();
        args.putSerializable("credentials", mCredentials);
        frag.setArguments(args);

        loadFragment(frag);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCredentials = (Credentials) getIntent().getSerializableExtra("credentials");


        setContentView(R.layout.activity_home);
        mToolbar = getSupportActionBar();
        mToolbar.setTitle("Home");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        HomeFragment frag = new HomeFragment();
        Bundle args = new Bundle();
        args.putSerializable("credentials", mCredentials);
        frag.setArguments(args);

        loadFragment(frag);
    }

    private void loadFragment(Fragment theFragment) {

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, theFragment)
                .addToBackStack(null);

        transaction.commit();
    }


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
                onWaitFragmentInteractionHide();
                loadFragment(frag);
            } else {
                Log.e("ERROR!", "No data array"); //notify user
                onWaitFragmentInteractionHide();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
            onWaitFragmentInteractionHide();
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
        MessagesFragment mf = new MessagesFragment();
        Bundle args = new Bundle();
        args.putSerializable("key", item);
        mf.setArguments(args);
        loadFragment(mf);
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
    public void onConnectionFragmentInteraction(Connection item) {

    }

    //Weather
    @Override
    public void onWeatherFragmentInteraction(Uri uri) {

    }

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