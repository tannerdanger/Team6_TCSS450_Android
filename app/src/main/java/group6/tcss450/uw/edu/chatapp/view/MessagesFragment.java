package group6.tcss450.uw.edu.chatapp.view;

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
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import group6.tcss450.uw.edu.chatapp.R;
import group6.tcss450.uw.edu.chatapp.messages.Message;
import group6.tcss450.uw.edu.chatapp.utils.Credentials;
import group6.tcss450.uw.edu.chatapp.utils.DataGenerator;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnMessageFragmentInteractionListener}
 * interface.
 */
public class MessagesFragment extends Fragment {

    private List<Message> mMessages;
    private Credentials mCredentials;
    private int mChatId;
    public static final String ARG_CHAT_ID = "chat id";
    public static final String ARG_MESSAGE_LIST = "message list";
    MyMessagesRecyclerViewAdapter mAdapter;

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
        if (getArguments() != null) {
            mMessages = new ArrayList<Message>(Arrays
                    .asList((Message[])
                            getArguments()
                                    .getSerializable(ARG_MESSAGE_LIST)));
            mCredentials = (Credentials) getArguments().getSerializable(getString(R.string.ARGS_CREDENTIALS));
            mChatId = getArguments().getInt(ARG_CHAT_ID);
        } else {
            mMessages = DataGenerator.MESSAGES;
        }
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages_list, container, false);
        Context context = view.getContext();
        RecyclerView rv = view.findViewById(R.id.list_messages_messageslist);
        if (mColumnCount <= 1) {
            rv.setLayoutManager(new LinearLayoutManager(context));
        } else {
            rv.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        //MyMessagesRecyclerViewAdapter adapter = new MyMessagesRecyclerViewAdapter(mMessages, mListener);
        //rv.setAdapter(adapter);
        mAdapter = new MyMessagesRecyclerViewAdapter(mMessages, mListener);

        rv.setAdapter(mAdapter);
        rv.scrollToPosition(mMessages.size() - 1);
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        Button sendButton = view.findViewById(R.id.button_messsages_send);
        EditText messageEntry = view.findViewById(R.id.et_messages_messageinput);
        rv.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    rv.post(new Runnable() {
                        @Override
                        public void run() {
                            if(mMessages.size() > 0) {
                                rv.smoothScrollToPosition(mMessages.size() - 1);
                            }
                        }
                    });
                }
            }
        });

        sendButton.setOnClickListener((View v) -> {

            String text = messageEntry.getText().toString();
            String user = mCredentials.getEmail();
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm");
            String dateStr = sdf.format(d);
            Message m = new Message.Builder(user).addMessage(text).addChatId(mChatId).addTime(dateStr).build();
            mMessages.add(m);
            rv.getAdapter().notifyItemInserted(mMessages.size() - 1);
            messageEntry.setText("");
            rv.scrollToPosition(mMessages.size() - 1);
            mListener.onMessageSendInteraction(m);
        });
        mAdapter.setCredentials(mCredentials);
        return view;
    }


    /**
     * Recieves a message from HomeActivity
     * @param theMessage a new message to be added.
     */
    public void recieveMessage(Message theMessage){


        if(theMessage.getUser().compareTo(mCredentials.getEmail()) != 0 ) {
            RecyclerView rv = getView().findViewById(R.id.list_messages_messageslist);
            mAdapter.addItem(theMessage);
            mAdapter.notifyItemInserted(mAdapter.getItemCount());
            rv.smoothScrollToPosition(mAdapter.getItemCount());
        }





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

        void onMessageSendInteraction(Message theMessage);
    }


}
