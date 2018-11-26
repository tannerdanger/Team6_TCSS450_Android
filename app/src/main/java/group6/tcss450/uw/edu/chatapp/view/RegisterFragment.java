package group6.tcss450.uw.edu.chatapp.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import group6.tcss450.uw.edu.chatapp.R;
import group6.tcss450.uw.edu.chatapp.utils.Credentials;
import group6.tcss450.uw.edu.chatapp.utils.SendPostAsyncTask;
import group6.tcss450.uw.edu.chatapp.utils.WaitFragment;

import static group6.tcss450.uw.edu.chatapp.view.LoginActivity.MIN_PASSWORD_LENGTH;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class RegisterFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Credentials mCredentials;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        Button registerButton = view.findViewById(R.id.button_registerfragment_register);
        registerButton.setOnClickListener(v -> onRegisterClicked());

        return view;
    }

    public void onRegisterClicked() {
        if (mListener != null) {
            EditText firstNameEdit =
                    getActivity().findViewById(R.id.edittext_registerfragment_firstname);
            EditText lastNameEdit =
                    getActivity().findViewById(R.id.edittext_registerfragment_lastname);
            EditText usernameEdit =
                    getActivity().findViewById(R.id.edittext_registerfragment_username);
            EditText emailEdit =
                    getActivity().findViewById(R.id.edittext_registerfragment_email);
            EditText passwordEdit1 =
                    getActivity().findViewById(R.id.edittext_registerfragment_password1);
            EditText passwordEdit2 =
                    getActivity().findViewById(R.id.edittext_registerfragment_password2);
            boolean isValid = true;

            if (firstNameEdit.getText().length() == 0) {
                isValid = false;
                firstNameEdit.setError("Please enter your first name.");
            }
            if (lastNameEdit.getText().length() == 0) {
                isValid = false;
                lastNameEdit.setError("Please enter your last name.");
            }
            if (usernameEdit.getText().length() == 0) {
                isValid = false;
                usernameEdit.setError("Please enter a username.");
            }
            if (emailEdit.getText().length() == 0) {
                isValid = false;
                emailEdit.setError("Please provide an email address.");
            } else if (!emailEdit.getText().toString().contains("@")) {
                isValid = false;
                emailEdit.setError("The email address you provided is invalid.");
            }
            if (passwordEdit1.getText().length() < MIN_PASSWORD_LENGTH) {
                isValid = false;
                passwordEdit1.setError("Please provide a password that is at least "
                        + MIN_PASSWORD_LENGTH + " characters long.");
            }
            if (passwordEdit2.getText().length() < MIN_PASSWORD_LENGTH) {
                isValid = false;
                passwordEdit2.setError("Please provide a password that is at least "
                        + MIN_PASSWORD_LENGTH + " characters long.");
            } else if (isValid && !passwordEdit1.getText().toString()
                    .equals(passwordEdit2.getText().toString())) {
                isValid = false;
                passwordEdit2.setError("Please make sure your passwords match.");
            }

            if (isValid){
                Credentials cred = new Credentials.Builder(
                        emailEdit.getText().toString(),
                        passwordEdit1.getText().toString())
                        .addFirstName(firstNameEdit.getText().toString())
                        .addLastName(lastNameEdit.getText().toString())
                        .addUsername(usernameEdit.getText().toString())
                        .build();

                //build web service URL
                Uri uri = new Uri.Builder()
                        .scheme("https")
                        .appendPath(getString(R.string.ep_base_url))
                        .appendPath(getString(R.string.ep_register))
                        .build();

                //Build JSON object
                JSONObject msg = cred.asJSONObject();

                mCredentials = cred;
                new SendPostAsyncTask.Builder(uri.toString(), msg)
                        .onPreExecute(this::handleRegisterPre)
                        .onPostExecute(this::handleRegisterOnPost)
                        .onCancelled(this::handleErrorsInTask)
                        .build().execute();

            }
        }
    }

    private void handleErrorsInTask(String result) {
        Log.e("ASYNCT_TASK_ERROR", result);
    }

    private void handleRegisterPre(){
        mListener.onWaitFragmentInteractionShow();
    }

    private void handleRegisterOnPost(String result) {
        try {
            Log.d("JSON result", result);
            JSONObject resultsJSON = new JSONObject(result);
            mListener.onWaitFragmentInteractionHide();

            if (resultsJSON.getBoolean("success")) {
                mListener.onRegisterSuccess(mCredentials);
            } else {
                ((EditText) getView().findViewById(R.id.edittext_registerfragment_email))
                        .setError("Register unsuccessful.");
            }
        } catch (JSONException e) {
            Log.e("JSON_PARSE_ERROR", result + System.lineSeparator() + e.getMessage());
            ((EditText) getView().findViewById(R.id.edittext_registerfragment_email))
                    .setError("Register unsuccessful.");
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        void onRegisterSuccess(Credentials credentials);
    }
}
