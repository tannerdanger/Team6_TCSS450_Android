package group6.tcss450.uw.edu.chatapp;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import group6.tcss450.uw.edu.chatapp.utils.Connection;
import group6.tcss450.uw.edu.chatapp.utils.GetAsyncTask;
import group6.tcss450.uw.edu.chatapp.utils.OpenMessage;
import group6.tcss450.uw.edu.chatapp.utils.Credentials;
import group6.tcss450.uw.edu.chatapp.utils.Message;
import group6.tcss450.uw.edu.chatapp.utils.SendPostAsyncTask;
import group6.tcss450.uw.edu.chatapp.weather.ForecastFragment;
import group6.tcss450.uw.edu.chatapp.weather.WeatherFragment;
import group6.tcss450.uw.edu.chatapp.weather.WeatherMsg;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        HomeFragment.OnFragmentInteractionListener,
        OpenMessagesFragment.OnOpenMessageFragmentInteractionListener,
        MessagesFragment.OnMessageFragmentInteractionListener,
        ConnectionFragment.OnConnectionsFragmentInteractionListener,
        WeatherTabbedContainer.OnFragmentInteractionListener,
        WeatherFragment.OnFragmentInteractionListener,
        ForecastFragment.OnFragmentInteractionListener,
        ConnectionsSearchFragment.OnConnectionSearchFragmentInteractionListener,
        ConnectionRequestsFragment.OnConnectionRequestFragmentInteractionListener,
        WaitFragment.OnFragmentInteractionListener  {

    Credentials mCredentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO: load basic fragment
        mCredentials = (Credentials) getIntent().getSerializableExtra("credentials");

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView userheadTV = (TextView) headerView.findViewById(R.id.nav_tv_username);
        TextView emailHeadTV = (TextView) headerView.findViewById(R.id.nav_tv_userEmail);
        String usrname = mCredentials.getUsername();
        if ("".compareTo(usrname) == 0) {
            userheadTV.setText("User");
        } else {
            userheadTV.setText(usrname);
        }
        emailHeadTV.setText(mCredentials.getEmail());


        HomeFragment frag = new HomeFragment();
        Bundle args = new Bundle();
        args.putSerializable("credentials", mCredentials);
        frag.setArguments(args);

        loadFragment(frag);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            HomeFragment frag = new HomeFragment();
            Bundle args = new Bundle();
            args.putSerializable("credentials", mCredentials);
            frag.setArguments(args);

            loadFragment(frag);

            //FragmentName fragment = new FragmentName();

            //loadFragment(fragment);

        } else if (id == R.id.nav_weather) {

            //if(!mIsWeatherSet){ //Or location is set to phone location
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

            Log.wtf("lon", "lon: " + longitutde);
            Log.wtf("lat", "lat: " + latitude);

            WeatherMsg wMsg = new WeatherMsg(latitude, longitutde);

            Bundle args = new Bundle();
            args.putSerializable("wmsg", wMsg);
            WeatherTabbedContainer fragment = new WeatherTabbedContainer();
            fragment.setArguments(args);
            loadFragment(fragment);


        } else if (id == R.id.nav_connections) {
//            ConnectionFragment frag = new ConnectionFragment();
//            Bundle args = new Bundle();
//            args.putSerializable("credentials", mCredentials);
//            frag.setArguments(args);
//            loadFragment(frag);
            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath(getString(R.string.ep_base_url))
                    .appendPath(getString(R.string.ep_conn))
                    .appendPath(getString(R.string.ep_getall))
                    .build();
            Log.d("JSON", mCredentials.asJSONObject().toString());
            new SendPostAsyncTask.Builder(uri.toString(), mCredentials.asJSONObject())
                    .onPreExecute(this::onWaitFragmentInteractionShow)
                    .onPostExecute(this::handleConnectionGetOnPostExecute)
                    .build()
                    .execute();


        } else if (id == R.id.nav_solo_chat) {

            OpenMessagesFragment frag = new OpenMessagesFragment();
            Bundle args = new Bundle();
            args.putSerializable("credentials", mCredentials);
            frag.setArguments(args);

            loadFragment(frag);

            //FragmentName fragment = new FragmentName();

            //loadFragment(fragment);

        } else if (id == R.id.nav_group_chat) {

            //FragmentName fragment = new FragmentName();

            //loadFragment(fragment);

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadFragment(Fragment theFragment) {

        //   final Bundle args = new Bundle();
        //    args.putSerializable("credentials", mCredentials);

        //    theFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, theFragment)
                .addToBackStack(null);

        transaction.commit();

    }

    private void handleConnectionGetOnPostExecute(final String result) {
        try {
            JSONObject root = new JSONObject(result);
            if (root.has("connections")) {
                JSONArray response = root.getJSONArray("connections");
//                if (response.has("data")) {
//                    JSONArray data = response.getJSONArray("data");
//                    List<BlogPost> blogs = new ArrayList<>();
//                    for (int i = 0; i < data.length(); i++) {
//                        JSONObject jsonBlog = data.getJSONObject(i);
//                        blogs.add(new BlogPost.Builder(jsonBlog.getString("pubdate"),
//                                jsonBlog.getString("title"))
//                                .addTeaser(jsonBlog.getString("teaser"))
//                                .addUrl(jsonBlog.getString("url"))
//                                .build());
//                    }
//                    BlogPost[] blogsAsArray = new BlogPost[blogs.size()];
//                    blogsAsArray = blogs.toArray(blogsAsArray);
                List<Connection> conns = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    JSONObject jsonSet = response.getJSONObject(i);
//                    conns.add(new Connection.Builder(jsonSet.getString("long_date"),
//                            jsonSet.getString("location"),
//                            jsonSet.getString("venue"))
//                            .addUrl(jsonSet.getString("url"))
//                            .addNotes(jsonSet.getString("setlistnotes"))
//                            .addData(jsonSet.getString("setlistdata"))
//                            .build());
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

    //Connection Search Screen (Will be unused?)
    @Override
    public void onConnectionSearchFragmentInteraction(Connection item) {

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


}
