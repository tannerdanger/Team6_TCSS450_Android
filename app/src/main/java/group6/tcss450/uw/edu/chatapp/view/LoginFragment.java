package group6.tcss450.uw.edu.chatapp.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import group6.tcss450.uw.edu.chatapp.R;
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

    private static final String TAG = LoginFragment.class.getSimpleName();

    private OnFragmentInteractionListener mListener;
    private Credentials mCredentials;
    private String mFirebaseToken;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences prefs =
                Objects.requireNonNull(getActivity()).getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);

        // Retrieve the stored credentials from SharedPrefs
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        Button loginButton = (Button) view.findViewById(R.id.button_loginfragment_login);
        loginButton.setOnClickListener(v -> onLoginAttempt());

        Button registerButton = (Button) view.findViewById(R.id.button_loginfragment_register);
        registerButton.setOnClickListener(v -> mListener.onRegisterClicked());

        return view;
    }

    private void onLoginAttempt() {
        EditText emailEdit = getActivity().findViewById(R.id.edittext_loginfragment_email);
        EditText passwordEdit = getActivity().findViewById(R.id.edittext_loginFragment_password);
        boolean areValidCredentials = true;

        if (emailEdit.getText().length() == 0) {
            areValidCredentials = false;
            emailEdit.setError("Please provide an email address.");
        } else if (emailEdit.getText().toString().chars().filter(ch -> ch == '@').count() != 1) {
            areValidCredentials = false;
            emailEdit.setError("The email address you provided is invalid.");
        }
        if (passwordEdit.getText().length() == 0) {
            areValidCredentials = false;
            passwordEdit.setError("Please enter a password.");
        }

        if (areValidCredentials) {
            getFirebaseToken(emailEdit.getText().toString(), passwordEdit.getText().toString());
        }
    }

    private void getFirebaseToken(final String email, final String password) {
        Log.wtf(TAG, "STARTED getFirebaseToken");
        mListener.onWaitFragmentInteractionShow();

        //add this app on this device to listen for the topic all
        FirebaseMessaging.getInstance().subscribeToTopic("all");

        //the call to getInstanceId happens asynchronously. task is an onCompleteListener
        //similar to a promise in JS.
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("FCM: ", "getInstanceId() failed.", task.getException());
                        mListener.onWaitFragmentInteractionHide();
                        return;
                    }

                    // Get new Instance ID token
                    mFirebaseToken = task.getResult().getToken();
                    Log.d("FCM: ", mFirebaseToken);
                    System.out.println("======= FB TOKEN ======");
                    System.out.println(mFirebaseToken);
                    //the helper method that initiates login service
                    loginInWithCredentials(email, password);
                });
        //no code here. wait for the Task to complete.
        Log.wtf(TAG, "ENDED getFirebaseToken");
    }

    private void loginInWithCredentials(final String email, final String password) {
        Log.wtf(TAG, "STARTED loginInWithCredentials");

        final Credentials cred = new Credentials.Builder(email, password)
                .addFirebaseToken(mFirebaseToken)
                .build();

        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_login))
                .appendPath(getString(R.string.ep_withToken))
                .build();

        JSONObject msg = new JSONObject();
        try {
            msg.put(getString(R.string.JSON_TOKEN), mFirebaseToken);
            msg.put(getString(R.string.JSON_EMAIL), email);
            msg.put(getString(R.string.JSON_PASSWORD), password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mCredentials = cred;
        System.out.print(uri.toString());

        //instantiate and execute AsyncTask
        //Feel free to add a handler for onPreExecution so that a progress bar
        //is displayed or maybe disable buttons.
        new SendPostAsyncTask.Builder(uri.toString(), msg)
                .onPreExecute(this::handleLoginOnPre)
                .onPostExecute(this::handleLoginOnPost)
                .onCancelled(this::handleErrorsInTask)
                .build()
                .execute();

        Log.wtf(TAG, "ENDED loginInWithCredentials");
    }

    private void handleErrorsInTask(String result) {
        Log.e("ASYNCT_TASK_ERROR", result);
    }

    /**
     * Handle the setup of the UI before the HTTP call to the webservice.
     */
    private void handleLoginOnPre() {
//        mListener.onWaitFragmentInteractionShow();
    }

    private void handleLoginOnPost(String result) {
        mListener.onWaitFragmentInteractionHide();

        try {
            Log.d("JSON result", result);
            JSONObject resultsJSON = new JSONObject(result);
            boolean success = resultsJSON.getBoolean("success");
            JSONObject userdata = resultsJSON.getJSONObject("user");

            System.out.print(userdata);

            if (success) {
                //Create new credentials with information from database
                mCredentials = new Credentials.Builder(userdata.getString("email"), mCredentials.getPassword())
                        .addID(userdata.getInt("id"))
                        .addFirstName(userdata.getString("firstname"))
                        .addLastName(userdata.getString("lastname"))
                        .addUsername(userdata.getString("username"))
                        .build();
                mCredentials.addToken(mFirebaseToken);
                if (userdata.getInt("verified") == 0) {
                    sendVerificationRequest(mCredentials.getEmail());
                }
                mListener.onLoginSuccess(mCredentials);

                if (userdata.getInt("verification") == 0 ) {
                    
                    sendVerificationRequest(mCredentials.getEmail());
                }

            } else {
                ((EditText) getView().findViewById(R.id.edittext_loginfragment_email))
                        .setError("Log In unsuccessful.");
            }
        } catch (JSONException e) {
            Log.e("JSON_PARSE_ERROR", result + System.lineSeparator() + e.getMessage());
            ((EditText) getView().findViewById(R.id.edittext_loginfragment_email))
                    .setError("Log In unsuccessful.");
        }
    }

    private void sendVerificationRequest(final String email) {
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_register))
                .appendPath(getString(R.string.ep_resend))
                .build();
        JSONObject msg = new JSONObject();

        try {
            msg.put(getString(R.string.JSON_EMAIL), email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new SendPostAsyncTask.Builder(uri.toString(), msg)
                .onPostExecute(this::sendVerificationToEmailAddress)
                .onCancelled(this::handleErrorsInTask)
                .build()
                .execute();
    }

    private void sendVerificationToEmailAddress(final String result) {
        try {
            JSONObject resultsJSON = new JSONObject(result);
            if (resultsJSON.getBoolean("success")) {
                Toast.makeText(
                        getContext(),
                        "Verification email sent. Please check your email.",
                        Toast.LENGTH_LONG
                ).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
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
    public interface OnFragmentInteractionListener
            extends WaitFragment.OnFragmentInteractionListener {
        void onLoginSuccess(Credentials credentials);
        void onRegisterClicked();
    }
}
