package group6.tcss450.uw.edu.chatapp.contacts;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import group6.tcss450.uw.edu.chatapp.MyConnectionsSearchRecyclerViewAdapter;
import group6.tcss450.uw.edu.chatapp.R;
import group6.tcss450.uw.edu.chatapp.utils.Connection;
import group6.tcss450.uw.edu.chatapp.utils.DataGenerator;

import java.util.Arrays;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnConnectionSearchFragmentInteractionListener}
 * interface.
 */
public class ConnectionsSearchFragment extends Fragment {

    // TODO: Customize parameters
    private List<Connection> mConnections;

    private int mColumnCount = 1;

    private OnConnectionSearchFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ConnectionsSearchFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConnections = DataGenerator.CONNECTIONS;
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connectionssearch_list, container, false);

//        // CANNOT set the stuff this way because we have the RV within another layout
//        if (view instanceof RecyclerView) {
//            Context context = view.getContext();
//            RecyclerView recyclerView = (RecyclerView) view;
//            if (mColumnCount <= 1) {
//                recyclerView.setLayoutManager(new LinearLayoutManager(context));
//            } else {
//                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
//            }
//            recyclerView.setAdapter(new MyConnectionsSearchRecyclerViewAdapter(Arrays.asList(DataGenerator.CONNECTIONS), mListener));
//        }
        Context context = view.getContext();
        RecyclerView rv = view.findViewById(R.id.list_connectionsearch_users);
        if(mColumnCount <= 1)   {
            rv.setLayoutManager(new LinearLayoutManager(context));
        } else {
            rv.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        rv.setAdapter(new MyConnectionsSearchRecyclerViewAdapter(mConnections, mListener));
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnConnectionSearchFragmentInteractionListener) {
            mListener = (OnConnectionSearchFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnConnectionSearchFragmentInteractionListener");
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
    public interface OnConnectionSearchFragmentInteractionListener {
        // TODO: Update argument type and name
        void onConnectionSearchFragmentInteraction(Connection item);
    }
}
