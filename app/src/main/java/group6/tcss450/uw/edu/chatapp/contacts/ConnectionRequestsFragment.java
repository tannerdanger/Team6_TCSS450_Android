package group6.tcss450.uw.edu.chatapp.contacts;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import group6.tcss450.uw.edu.chatapp.R;
import group6.tcss450.uw.edu.chatapp.view.MyConnectionRequestsRecyclerViewAdapter;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnConnectionRequestFragmentInteractionListener}
 * interface.
 */
public class ConnectionRequestsFragment extends Fragment {

    // TODO: Customize parameters
    private int mColumnCount = 1;

    private List<Connection> mConnections;

    private RecyclerView mRecyclerView;

    private OnConnectionRequestFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ConnectionRequestsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mConnections = new ArrayList<Connection>(Arrays.asList((Connection[])
                    getArguments().getSerializable(ConnectionFragment.ARG_CONNECTION_LIST)));
            List<Connection> removeMe = new ArrayList<Connection>();
            for (Connection c : mConnections) {
                if (c.getVerified() == 1) {
                    //Remove all connections that are already verified.
                    removeMe.add(c);
                }
            }
            //This is to avoid a ConcurrentModificationException.
            mConnections.removeAll(removeMe);
        } else {
            Log.e("BROKEN POST", "UNABLE TO FETCH CONNECTIONS.");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connectionrequests_list, container, false);
        Context context = view.getContext();
        mRecyclerView = view.findViewById(R.id.list_connectionrequests);
        // Set the adapter
        if (mColumnCount <= 1) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        TextView noRequests = view.findViewById(R.id.tv_connectionrequest_none);
        mRecyclerView.setAdapter(new MyConnectionRequestsRecyclerViewAdapter(mConnections, mListener, noRequests));

        if(mConnections.size() == 0)    {
            noRequests.setVisibility(TextView.VISIBLE);
        } else {
            noRequests.setVisibility(TextView.INVISIBLE);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnConnectionRequestFragmentInteractionListener) {
            mListener = (OnConnectionRequestFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnConnectionRequestFragmentInteractionListener");
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnConnectionRequestFragmentInteractionListener {
        // TODO: Update argument type and name
        void onConnectionRequestAccept(Connection receiver);
        void onConnectionRequestReject(Connection receiver);
    }
}
