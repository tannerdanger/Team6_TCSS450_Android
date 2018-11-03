package group6.tcss450.uw.edu.chatapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import group6.tcss450.uw.edu.chatapp.utils.OpenMessage;
import group6.tcss450.uw.edu.chatapp.utils.DataGenerator;

import java.util.Arrays;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnOpenMessageFragmentInteractionListener}
 * interface.
 */
public class OpenMessagesFragment extends Fragment {

    private List<OpenMessage> mConnections;
    public static final String ARG_CONNECTION_LIST = "open messages list";

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnOpenMessageFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OpenMessagesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_openmessages_list, container,
                false);
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyOpenMessagesRecyclerViewAdapter(mConnections, mListener));
        }
        return view;
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static OpenMessagesFragment newInstance(int columnCount) {
        OpenMessagesFragment fragment = new OpenMessagesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO
        //THIS CAN ONLY BE IMPLEMENTED ONCE WE'RE PULLING DATA FROM DATABASE
//        if (getArguments() != null) {
//            mConnections = new ArrayList<OpenMessage>(Arrays
//                    .asList((OpenMessage[])
//                            getArguments()
//                                    .getSerializable(ARG_CONNECTION_LIST)));
//        } else {
            mConnections = Arrays.asList(DataGenerator.OPEN_MESSAGES);
        //}
//        if (getArguments() != null) {
//            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
//        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnOpenMessageFragmentInteractionListener) {
            mListener = (OnOpenMessageFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMessageFragmentInteractionListener");
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
    public interface OnOpenMessageFragmentInteractionListener {
        // TODO: Update argument type and name
        void onOpenMessageFragmentInteraction(OpenMessage item);
    }
}
