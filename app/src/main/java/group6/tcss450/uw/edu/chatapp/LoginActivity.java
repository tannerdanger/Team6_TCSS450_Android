package group6.tcss450.uw.edu.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import android.widget.Button;

import group6.tcss450.uw.edu.chatapp.utils.Credentials;

public class LoginActivity extends AppCompatActivity implements
        LoginFragment.OnFragmentInteractionListener,
        RegisterFragment.OnFragmentInteractionListener {

    Credentials mCredentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        //TODO: this is just a temporary login option
//        Button b = findViewById(R.id.tmp_login_button);
//        b.setOnClickListener(v ->{
//            Credentials cred = new Credentials.Builder("test@test.com", "test123")
//                    .addUsername("Test UserName")
//                    .addFirstName("Firstname")
//                    .addLastName("Lastname")
//                    .build();
//            onLoginSuccess(cred);
//        });
//
//        //TODO: when login fragment is created, uncomment and change ID's below
////        if (savedInstanceState == null) {
////            if (findViewById(R.id.frame_main_fragment_container) != null) { //Good
////                getSupportFragmentManager().beginTransaction()
////                        .add(R.id.frame_main_fragment_container, new LoginFragment())
////                        .commit();
////            }

        if (savedInstanceState == null) {
            if (findViewById(R.id.loginContainer) != null) { // Good
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.loginContainer, new LoginFragment())
                        .commit();
            }
        }

    }



    public void onLoginSuccess(Credentials credentials) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("credentials", credentials);
        startActivity(intent);
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
}
