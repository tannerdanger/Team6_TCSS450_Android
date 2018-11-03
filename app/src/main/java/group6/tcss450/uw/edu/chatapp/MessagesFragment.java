package group6.tcss450.uw.edu.chatapp;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import group6.tcss450.uw.edu.chatapp.utils.DataGenerator;
import group6.tcss450.uw.edu.chatapp.utils.Message;

import java.util.Arrays;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnMessageFragmentInteractionListener}
 * interface.
 */
public class MessagesFragment extends Fragment {

    private List<Message> mMessages;
    public static final String ARG_MESSAGE_LIST = "message list";

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnMessageFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MessagesFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MessagesFragment newInstance(int columnCount) {
        MessagesFragment fragment = new MessagesFragment();
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
        mMessages = Arrays.asList(DataGenerator.MESSAGES);
        //}
//        if (getArguments() != null) {
//            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages_list, container, false);
        // Set the adapter
//        if (view instanceof RecyclerView) {
//            Context context = view.getContext();
//            RecyclerView recyclerView = (RecyclerView) view;
//            if (mColumnCount <= 1) {
//                recyclerView.setLayoutManager(new LinearLayoutManager(context));
//            } else {
//                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
//            }
//            recyclerView.setAdapter(new MyMessagesRecyclerViewAdapter(mMessages, mListener));
//            recyclerView.scrollToPosition(mMessages.size() - 1);
//        }
        Context context = view.getContext();
        RecyclerView rv = view.findViewById(R.id.list_messages_messageslist);
        if(mColumnCount <= 1)   {
            rv.setLayoutManager(new LinearLayoutManager(context));
        } else {
            rv.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        rv.setAdapter(new MyMessagesRecyclerViewAdapter(mMessages, mListener));
        rv.scrollToPosition(mMessages.size() - 1);
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.hide();
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMessageFragmentInteractionListener) {
            mListener = (OnMessageFragmentInteractionListener) context;
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
    public interface OnMessageFragmentInteractionListener {
        // TODO: Update argument type and name
        void onMessageFragmentInteraction(Message theMessage);
    }
}
