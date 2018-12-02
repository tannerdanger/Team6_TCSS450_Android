package group6.tcss450.uw.edu.chatapp.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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
 * {@link PasswordResetFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class PasswordResetFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private String userEmail;

    public PasswordResetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_password_reset, container, false);

        final Button cancelBtn =
                (Button) view.findViewById(R.id.button_resetpasswordfragment_cancel);
        cancelBtn.setOnClickListener(v -> onButtonPressed(cancelBtn));

        final Button submitBtn =
                (Button) view.findViewById(R.id.button_resetpasswordfragment_submit);
        submitBtn.setOnClickListener(v -> onButtonPressed(submitBtn));

        return view;
    }

    public void onButtonPressed(final Button button) {
        switch (button.getId()) {
            case R.id.button_resetpasswordfragment_submit:
                String email, code, newPassword, newPasswordVerify;
                boolean isValid = true;
                EditText emailEdit = (EditText) getActivity()
                        .findViewById(R.id.edittext_passwordresetfragment_email);
                email = emailEdit.getText().toString();

                EditText codeEdit = (EditText) getActivity()
                        .findViewById(R.id.edittext_passwordresetfragment_code);
                code = codeEdit.getText().toString();

                EditText newPasswordEdit = (EditText) getActivity()
                        .findViewById(R.id.edittext_passwordresetfragment_password1);
                newPassword = newPasswordEdit.getText().toString();

                EditText newPasswordVerifyEdit = (EditText) getActivity()
                        .findViewById(R.id.edittext_passwordresetfragment_password2);
                newPasswordVerify = newPasswordVerifyEdit.getText().toString();

                if (email.isEmpty()) {
                    isValid = false;
                    emailEdit.setError("Please enter your email address.");
                } else if (!email.contains("@")) {
                    isValid = false;
                    emailEdit.setError("Please enter a valid email address.");
                }

                if (code.isEmpty()) {
                    isValid = false;
                    codeEdit.setError("Please enter your verification code.");
                }

                if (newPassword.isEmpty()) {
                    isValid = false;
                    newPasswordEdit.setError("Please enter a new password");
                }
                if (newPasswordVerify.isEmpty()) {
                    isValid = false;
                    newPasswordVerifyEdit.setError("Please enter a new password.");
                }
                if (!newPassword.isEmpty() && !newPasswordVerify.isEmpty()) {
                    if (!newPassword.equals(newPasswordVerify)) {
                        isValid = false;
                        newPasswordVerifyEdit.setError("Your passwords must match.");
                    }
                }

                if (isValid) {
                    Uri uri = new Uri.Builder()
                            .scheme("https")
                            .appendPath(getString(R.string.ep_base_url))
                            .appendPath(getString(R.string.ep_register))
                            .appendPath(getString(R.string.ep_update_password))
                            .build();

                    JSONObject json = new JSONObject();

                    try {
                        json.put(getString(R.string.JSON_EMAIL), email);
                        json.put(getString(R.string.JSON_CODE), code);
                        json.put(getString(R.string.JSON_PASSWORD), newPassword);
                        json.put(getString(R.string.JSON_MEMBER_ID), -1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    new SendPostAsyncTask.Builder(uri.toString(), json).build().execute();
                    // TODO Add check to make sure new password thing worked
                    mListener.onSubmitButtonClicked();
                }
                break;

            case R.id.button_resetpasswordfragment_cancel:
                mListener.onCancelButtonClicked();
                break;
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
        void onSubmitButtonClicked();
        void onCancelButtonClicked();
    }
}
