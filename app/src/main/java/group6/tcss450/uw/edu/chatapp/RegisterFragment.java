package group6.tcss450.uw.edu.chatapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import group6.tcss450.uw.edu.chatapp.utils.Credentials;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class RegisterFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        Button registerButton = getActivity().findViewById(R.id.button_registerfragment_register);
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
            }
            if (passwordEdit.getText().length() == 0) {
                hasError = true;
                passwordEdit.setError("Password must not be empty.");
            }
            if (!passwordEdit2.getText().toString().equals(passwordEdit.getText().toString())) {
                hasError = true;
                passwordEdit.setError("Passwords must match.");
            } else {

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
