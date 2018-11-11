package group6.tcss450.uw.edu.chatapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;

import group6.tcss450.uw.edu.chatapp.utils.Credentials;
import group6.tcss450.uw.edu.chatapp.utils.WaitFragment;

public class LoginActivity extends AppCompatActivity implements
        LoginFragment.OnFragmentInteractionListener,
        RegisterFragment.OnFragmentInteractionListener {

    Credentials mCredentials;
    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (savedInstanceState == null) {
            if (findViewById(R.id.loginContainer) != null) { // Good
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.loginContainer, new LoginFragment())
                        .commit();
            }
        }

    }

    private void saveCredentials(final Credentials credentials) {
        SharedPreferences prefs =
                this.getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        //Store the credentials in SharedPrefs
        prefs.edit().putString(getString(R.string.keys_prefs_email), credentials.getEmail()).apply();
        prefs.edit().putString(getString(R.string.keys_prefs_password), credentials.getPassword()).apply();
    }



    public void onLoginSuccess(Credentials credentials) {
        saveCredentials(credentials);
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class); //TODO: If you want to change back to drawer layout, change HomeActivity.class => MainActivity.Class
        intent.putExtra("credentials", credentials);
        startActivity(intent);
        //End this Activity and remove it from the Activity back stack.
        finish();
    }

    @Override
    public void onRegisterClicked() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.loginContainer, new RegisterFragment());
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onRegistration(Credentials credentials) {
        onLoginSuccess(credentials);
    }

    @Override
    public void onWaitFragmentInteractionShow() {
        Log.wtf(TAG, "STARTED onWaitFragmentInteractionSHOW");
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.loginContainer, new WaitFragment(), "WAIT")
                .addToBackStack(null)
                .commit();
        Log.wtf(TAG, "ENDED onWaitFragmentInteractionSHOW");
    }

    @Override
    public void onWaitFragmentInteractionHide() {
        Log.wtf(TAG, "STARTED onWaitFragmentInteractionHide");
        getSupportFragmentManager()
                .beginTransaction()
                .remove(getSupportFragmentManager().findFragmentByTag("WAIT"))
                .commit();
        Log.wtf(TAG, "ENDED onWaitFragmentInteractionHide");
    }
}
