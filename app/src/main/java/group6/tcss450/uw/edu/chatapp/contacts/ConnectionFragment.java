package group6.tcss450.uw.edu.chatapp.contacts;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import group6.tcss450.uw.edu.chatapp.MyConnectionRecyclerViewAdapter;
import group6.tcss450.uw.edu.chatapp.R;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnConnectionsFragmentInteractionListener}
 * interface.
 */
public class ConnectionFragment extends Fragment {

    public List<Connection> mConnections;
    public static final String ARG_CONNECTION_LIST = "connections list";


    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnConnectionsFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ConnectionFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ConnectionFragment newInstance(int columnCount) {
        ConnectionFragment fragment = new ConnectionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
           // mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
//            mSets = new ArrayList<Setlist>(Arrays.asList((Setlist[])
//                    getArguments().getSerializable(ARG_SET_LIST)));
            mConnections = new ArrayList<Connection>(Arrays.asList((Connection[])
                        getArguments().getSerializable(ARG_CONNECTION_LIST)));
            for(Connection c : mConnections)    {
                if(c.getVerified() == 0)    {
                    mConnections.remove(c);
                }
            }
        } else {
            Log.e("BROKEN POST", "UNABLE TO FETCH CONNECTIONS.");
        }
        //mConnections = DataGenerator.CONNECTIONS;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connection_list, container, false);

        // Set the adapter
//        if (view instanceof RecyclerView) {
//            Context context = view.getContext();
//            RecyclerView recyclerView = (RecyclerView) view;
//            if (mColumnCount <= 1) {
//                recyclerView.setLayoutManager(new LinearLayoutManager(context));
//            } else {
//                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
//            }
//            recyclerView.setAdapter(new MyConnectionRecyclerViewAdapter(mConnections, mListener));
//        }
        Context context = view.getContext();
        RecyclerView rv = view.findViewById(R.id.list_connections_connectionslist);
        if(mColumnCount <= 1)   {
            rv.setLayoutManager(new LinearLayoutManager(context));
        } else {
            rv.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        rv.setAdapter(new MyConnectionRecyclerViewAdapter(mConnections, mListener));
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        if(null != fab) {
            fab.hide();
        }
        Button search = view.findViewById(R.id.button_connectionsfragment_search);
        Button requests = view.findViewById(R.id.button_connectionsfragment_requests);
        //Button removeInstance = view.findViewById(R.id.button_connectionsfragment_remove);
        search.setOnClickListener((View v) ->   {
            mListener.onConnectionSearchInteraction();
        });
        requests.setOnClickListener((View v) -> {
            mListener.onConnectionRequestInteraction(getArguments());
        });
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnConnectionsFragmentInteractionListener) {
            mListener = (OnConnectionsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnOpenMessageFragmentInteractionListener");
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
    public interface OnConnectionsFragmentInteractionListener {
        // TODO: Update argument type and name
        void onConnectionFragmentInteraction(Connection connection);
        void onConnectionSearchInteraction();
        void onConnectionRequestInteraction(Bundle b);
    }
}
