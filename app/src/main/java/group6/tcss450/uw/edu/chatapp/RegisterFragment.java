package group6.tcss450.uw.edu.chatapp;

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
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import org.json.JSONObject;

import group6.tcss450.uw.edu.chatapp.utils.Credentials;
import group6.tcss450.uw.edu.chatapp.utils.SendPostAsyncTask;


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

        Button registerButton = (Button) view.findViewById(R.id.button_registerfragment_register);//getActivity().findViewById(R.id.button_registerfragment_register);
        registerButton.setOnClickListener(v -> onRegisterButtonClicked());

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onRegisterButtonClicked() {
        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
            EditText emailEdit = getActivity().findViewById(R.id.edittext_registerfragment_email);
            EditText usernameEdit =
                    getActivity().findViewById(R.id.edittext_registerfragment_username);
            EditText passwordEdit =
                    getActivity().findViewById(R.id.edittext_registerfragment_password1);
            EditText passwordEdit2 =
                    getActivity().findViewById(R.id.edittext_registerfragment_password2);
            EditText firstnameEdit =
                    getActivity().findViewById(R.id.edittext_registerfragment_firstname);
            EditText lastnameEdit =
                    getActivity().findViewById(R.id.edittext_registerfragment_lastname);
            boolean hasError = false;

            if (emailEdit.getText().length() == 0) {
                hasError = true;
                emailEdit.setError("Email must not be empty.");
            } else if (emailEdit.getText().toString().chars().filter(ch -> ch == '@')
                    .count() != 1) {
                hasError = true;
                emailEdit.setError("Email must be valid.");
            } else if (passwordEdit.getText().length() == 0) {
                passwordEdit.setError("Password must not be empty.");
            } else if (!passwordEdit2.getText().toString().equals(passwordEdit.getText().toString())) {
                passwordEdit.setError("Passwords must match.");
            } else {

            }
            if(!hasError){
                Credentials cred = new Credentials.Builder(
                        emailEdit.getText().toString(),
                        passwordEdit.getText().toString())
                        .addFirstName(firstnameEdit.getText().toString())
                        .addLastName(lastnameEdit.getText().toString())
                        .addUsername(usernameEdit.getText().toString())
                        .build();

                //build web service URL
                Uri uri = new Uri.Builder()
                        .scheme("https")
                        .appendPath(getString(R.string.ep_base_url))
                        .appendPath(getString(R.string.ep_register))
                        .build();

                //Build JSON object
                //JSONObject msg = cred.asJSONObject(); //TODO: Fix backend to match cred JSON values
                JSONObject msg = new JSONObject();
                try {
                    msg.put("first", firstnameEdit.getText().toString());
                    msg.put("last", lastnameEdit.getText().toString());
                    msg.put("username", usernameEdit.getText().toString());
                    msg.put("email", emailEdit.getText().toString());
                    msg.put("password", passwordEdit.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
        //TODO: enable once a wait fragment is created
        //mListener.onWaitFragmentInteractionShow();
    }

    private void handleRegisterOnPost(String result) {
        try {
            Log.d("JSON result", result);
            JSONObject resultsJSON = new JSONObject(result);
            boolean success= resultsJSON.getBoolean("success");

            //TODO: enable once a wait fragment is created
            //mListener.onWaitFragmentInteractionHide();

            if (success) {
                mListener.onRegistration(mCredentials);
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onRegistration(Credentials credentials);
    }
}
