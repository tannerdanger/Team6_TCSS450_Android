package group6.tcss450.uw.edu.chatapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import group6.tcss450.uw.edu.chatapp.utils.Connections;
import group6.tcss450.uw.edu.chatapp.utils.Credentials;
import group6.tcss450.uw.edu.chatapp.utils.Message;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        HomeFragment.OnFragmentInteractionListener,
        ConnectionsFragment.OnConnectionsFragmentInteractionListener,
        MessagesFragment.OnMessageFragmentInteractionListener   {

    Credentials mCredentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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


        //TODO: load basic fragment
        mCredentials = (Credentials) getIntent().getSerializableExtra("credentials");

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


            //FragmentName fragment = new FragmentName();

            //loadFragment(fragment);

        } else if (id == R.id.nav_connections) {

            ConnectionsFragment frag = new ConnectionsFragment();
            Bundle args = new Bundle();
            args.putSerializable("credentials", mCredentials);
            frag.setArguments(args);

            loadFragment(frag);

        } else if (id == R.id.nav_solo_chat) {

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

    private void loadFragment(Fragment theFragment){

     //   final Bundle args = new Bundle();
    //    args.putSerializable("credentials", mCredentials);

    //    theFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, theFragment)
                .addToBackStack(null);

        transaction.commit();

    }


    //*************** FRAGMENT INTERACTION LISTENERS ***************//


    //Home Fragment
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    //Connections Fragment
    @Override
    public void onConnectionsFragmentInteraction(Connections item) {
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



    //*************** ASYNC METHODS ***************//


}
