package group6.tcss450.uw.edu.chatapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import group6.tcss450.uw.edu.chatapp.utils.Credentials;
import group6.tcss450.uw.edu.chatapp.utils.SendPostAsyncTask;
import group6.tcss450.uw.edu.chatapp.utils.WaitFragment;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LoginFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Credentials mCredentials;
    private String mFirebaseToken;
    private static final String TAG = LoginFragment.class.getSimpleName();

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart(){
        super.onStart();
        SharedPreferences prefs =
                getActivity().getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        //retrieve the stored credentials from SharedPrefs
        if (prefs.contains(getString(R.string.keys_prefs_email)) &&
                prefs.contains(getString(R.string.keys_prefs_password))) {
            final String email = prefs.getString(getString(R.string.keys_prefs_email), "");
            final String password = prefs.getString(getString(R.string.keys_prefs_password), "");

            if (email.compareTo("") != 0 && "".compareTo(password) != 0) {
                getFirebaseToken(email, password);
            } else {
                //Load the two login EditTexts with the credentials found in SharedPrefs
                EditText emailEdit = getActivity().findViewById(R.id.edittext_loginfragment_email);
                emailEdit.setText(email);
                EditText passwordEdit = getActivity().findViewById(R.id.edittext_loginFragment_password);
                passwordEdit.setText(password);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        Button loginButton = (Button) view.findViewById(R.id.button_loginfragment_login);
        loginButton.setOnClickListener(v -> attemptLogin());

        Button registerButton = (Button) view.findViewById(R.id.button_loginfragment_register);
        registerButton.setOnClickListener(v -> mListener.onRegisterClicked());

        return view;
    }

    private void attemptLogin() {
        EditText emailEdit = getActivity().findViewById(R.id.edittext_loginfragment_email);
        EditText passwordEdit = getActivity().findViewById(R.id.edittext_loginFragment_password);
        boolean hasError = false;

        if (emailEdit.getText().length() == 0) {
            hasError = true;
            emailEdit.setError("Email must not be empty.");
        } else if (emailEdit.getText().toString().chars().filter(ch -> ch == '@').count() != 1) {
            hasError = true;
            emailEdit.setError("Email must be valid.");
        }
        if (passwordEdit.getText().length() == 0) {
            hasError = true;
            passwordEdit.setError("Password must not be empty.");
        }

        /*
         * TEST MARKER
         */
        boolean isATest = false;

        // TODO CHANGE THIS LATER!!!
        if (!hasError) {
            if (isATest) {
                Credentials creds = new Credentials.Builder(emailEdit.getText().toString(),
                        passwordEdit.getText().toString())
                        .addUsername("Test UserName")
                        .addFirstName("First name")
                        .addLastName("Last name")
                        .build();
                mListener.onLoginSuccess(creds);
            } else {

                /*Tanner commented out these things to more closely match lab 5 for messaging.*/

                /*
                Credentials credentials = new Credentials.Builder(
                        emailEdit.getText().toString(),
                        passwordEdit.getText().toString())
                        .build();

                Uri uri = new Uri.Builder()
                        .scheme("https")
                        .appendPath(getString(R.string.ep_base_url))
                        .appendPath(getString(R.string.ep_login))
                        .build();

                JSONObject msg = credentials.asJSONObject();

                mCredentials = credentials;

                new SendPostAsyncTask.Builder(uri.toString(), msg)
                        .onPostExecute(this::handleLoginOnPost)
                        .onCancelled(this::handleErrorsInTask)
                        .build().execute();
                        */

                getFirebaseToken(emailEdit.getText().toString(), passwordEdit.getText().toString());
            }
        }
    }

    private void getFirebaseToken(final String email, final String password){
        Log.wtf(TAG, "STARTED getFirebaseToken");
        //TODO: Enable this when wait fragment created
        mListener.onWaitFragmentInteractionShow();

        //add this app on this device to listen for the topic all
        FirebaseMessaging.getInstance().subscribeToTopic("all");

        //the call to getInstanceId happens asynchronously. task is an onCompleteListener
        //similar to a promise in JS.
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("FCM: ", "getInstanceId failed", task.getException());
                        mListener.onWaitFragmentInteractionHide();
                        return;
                    }

                    // Get new Instance ID token
                    mFirebaseToken = task.getResult().getToken();
                    Log.d("FCM: ", mFirebaseToken);
                    System.out.println("======= FB TOKEN ======");
                    System.out.println(mFirebaseToken.toString());
                    //the helper method that initiates login service
                    doLogin(email, password);
                });
        //no code here. wait for the Task to complete.
        Log.wtf(TAG, "ENDED getFirebaseToken");

    }

    private void doLogin(String email, String password){
        Log.wtf(TAG, "STARTED doLogin");
        //create credentials
        Credentials cred = new Credentials.Builder(email, password).build();

        //build web service URL
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_login))
                .build();

        //build JSON Object
        JSONObject msg = cred.asJSONObject();
        try{
            msg.put("token", mFirebaseToken);
        }catch (JSONException e) {
            e.printStackTrace();
        }

        mCredentials = cred;

        //instantiate and execute AsyncTask
        //Feel free to add a handler for onPreExecution so that a progress bar
        //is displayed or maybe disable buttons.
        new SendPostAsyncTask.Builder(uri.toString(), msg)
                .onPreExecute(this::handleLoginOnPre)
                .onPostExecute(this::handleLoginOnPost)
                .onCancelled(this::handleErrorsInTask)
                .build()
                .execute();
        Log.wtf(TAG, "ENDED doLogin");
    }

    private void handleErrorsInTask(String result) {
        Log.e("ASYNCT_TASK_ERROR", result);
    }

    /**
     * Handle the setup of the UI before the HTTP call to the webservice.
     */
    private void handleLoginOnPre() {
        //mListener.onWaitFragmentInteractionShow();
    }

    private void handleLoginOnPost(String result) {
        mListener.onWaitFragmentInteractionHide();
        try {
            Log.d("JSON result", result);
            JSONObject resultsJSON = new JSONObject(result);
            boolean success= resultsJSON.getBoolean("success");

            JSONObject userdata = resultsJSON.getJSONObject("user");

            boolean isVerified = true;

            //if (userdata.getInt("verified") == 0){ isVerified = false); //this was missed, will fix the node.js endpoint to return verified

            if(!isVerified){
                //TODO: Create a fragment or popup thing that prompts user to resend verification email.
                //endpoint: tcss450group6-backend.herokuapp.com/register/resend
                //body just needs email address passed in { "email": "user@gmail.com" }
            }

            System.out.print(userdata);

            if (success) {

                //Create new credentials with information from database
                Credentials cred = new Credentials.Builder(userdata.getString("email"), mCredentials.getPassword())
                        .addID(userdata.getInt("id"))
                        .addFirstName(userdata.getString("firstname"))
                        .addLastName(userdata.getString("lastname"))
                        .addUsername(userdata.getString("username"))
                        .build();
                mCredentials = cred;
                mListener.onLoginSuccess(mCredentials);

            } else {
                ((TextView) getView().findViewById(R.id.edittext_loginfragment_email))
                        .setError("Log In unsuccessful.");
            }
        } catch (JSONException e) {
            Log.e("JSON_PARSE_ERROR", result + System.lineSeparator() + e.getMessage());
            ((TextView) getView().findViewById(R.id.edittext_loginfragment_email))
                    .setError("Log In unsuccessful.");
        }
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener extends WaitFragment.OnFragmentInteractionListener {
        void onLoginSuccess(Credentials credentials);
        void onRegisterClicked();
    }
}
