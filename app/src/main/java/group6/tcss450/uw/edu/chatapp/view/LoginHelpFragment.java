package group6.tcss450.uw.edu.chatapp.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import group6.tcss450.uw.edu.chatapp.R;
import group6.tcss450.uw.edu.chatapp.utils.SendPostAsyncTask;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginHelpFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LoginHelpFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public LoginHelpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login_help, container, false);

        Button button = view.findViewById(R.id.button_loginhelp_submit);
        button.setOnClickListener(v -> submitUserRequest());

        button = view.findViewById(R.id.button_loginhelp_cancel);
        button.setOnClickListener(v -> mListener.onCancelClicked());

        return view;
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


    /* TODO: Send email to user with verification code. ActivityLogin loads verification page. */
    private void submitUserRequest() {
        if (mListener != null) {
            EditText emailEdit = (EditText) getActivity().findViewById(R.id.editText_loginhelp_email);

            if (emailEdit.getText().length() == 0) {
                emailEdit.setError("Please enter your email address.");
            } else if (!emailEdit.getText().toString().contains("@")) {
                emailEdit.setError("Please enter a valid email address.");
            } else {
                Uri uri = new Uri.Builder().scheme("https")
                        .appendPath(getString(R.string.ep_base_url))
                        .appendPath(getString(R.string.ep_register))
                        .appendPath(getString(R.string.ep_recover))
                        .build();

                JSONObject msg = new JSONObject();

                try {
                    msg.put(getString(R.string.JSON_EMAIL), emailEdit.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new SendPostAsyncTask.Builder(uri.toString(), msg).build().execute();
                mListener.onSubmitClicked();
            }
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
        void onCancelClicked();
        void onSubmitClicked();
    }
}
