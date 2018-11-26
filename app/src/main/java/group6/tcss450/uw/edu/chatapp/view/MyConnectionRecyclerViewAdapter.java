package group6.tcss450.uw.edu.chatapp.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import group6.tcss450.uw.edu.chatapp.R;
import group6.tcss450.uw.edu.chatapp.contacts.Connection;
import group6.tcss450.uw.edu.chatapp.contacts.ConnectionFragment;
import group6.tcss450.uw.edu.chatapp.contacts.ConnectionFragment.OnConnectionsFragmentInteractionListener;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Connection} and makes a call to the
 * specified {@link OnConnectionsFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyConnectionRecyclerViewAdapter extends RecyclerView.Adapter<MyConnectionRecyclerViewAdapter.ViewHolder> {

    private List<Connection> mValues;
    private final ConnectionFragment.OnConnectionsFragmentInteractionListener mListener;

    public MyConnectionRecyclerViewAdapter(List<Connection> items, OnConnectionsFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_connection, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mConnection = mValues.get(position);
        Connection thisConnection = mValues.get(position);
        holder.mEmail.setText(mValues.get(position).getEmail());
        holder.mUsername.setText(mValues.get(position).getUsername());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onConnectionFragmentRemove(holder.mConnection);
                }
            }
        });
        holder.mRemoveUser.setOnClickListener((View v) ->   {
            this.notifyItemRemoved(mValues.indexOf(thisConnection));
            mValues.remove(thisConnection);
            mListener.onConnectionFragmentRemove(thisConnection);
        });
        holder.mStartChat.setOnClickListener((View v) ->    {
            mListener.onConnectionFragmentStartChat(thisConnection);
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mEmail;
        public final TextView mUsername;
        public final Button mRemoveUser;
        public final Button mStartChat;
        public Connection mConnection;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mEmail = view.findViewById(R.id.tv_connectionsfragment_email);
            mUsername = view.findViewById(R.id.tv_connectionsfragment_username);
            mRemoveUser = view.findViewById(R.id.button_connectionsfragment_remove);
            mStartChat = view.findViewById(R.id.button_connectionfragment_startchat);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mUsername.getText() + "'";
        }
    }
}
