package group6.tcss450.uw.edu.chatapp.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import group6.tcss450.uw.edu.chatapp.R;
import group6.tcss450.uw.edu.chatapp.contacts.Connection;
import group6.tcss450.uw.edu.chatapp.contacts.ConnectionsSearchFragment;
import group6.tcss450.uw.edu.chatapp.contacts.ConnectionsSearchFragment.OnConnectionSearchFragmentInteractionListener;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Connection} and makes a call to the
 * specified {@link OnConnectionSearchFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyConnectionsSearchRecyclerViewAdapter extends RecyclerView.Adapter<MyConnectionsSearchRecyclerViewAdapter.ViewHolder> {

    private final List<Connection> mValues;
    private final OnConnectionSearchFragmentInteractionListener mListener;
    private Context mContext;

    public MyConnectionsSearchRecyclerViewAdapter(List<Connection> items, ConnectionsSearchFragment.OnConnectionSearchFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_connectionssearch, parent, false);
        mContext = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mConnection = mValues.get(position);
        holder.mUsername.setText(mValues.get(position).getUsername());
        holder.mEmail.setText(mValues.get(position).getEmail());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                }
            }
        });
        Connection thisConnection = mValues.get(position);
        holder.mAdd.setOnClickListener((View v) ->  {
            mListener.onConnectionSearchFragmentInteraction(thisConnection);
            Toast toast = Toast.makeText(mContext, "Friend request sent.", Toast.LENGTH_SHORT);
            toast.show();
            mValues.remove(position);
            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mUsername;
        public final TextView mEmail;
        public final Button mAdd;
        public Connection mConnection;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mUsername = view.findViewById(R.id.tv_connectionsearch_user);
            mEmail = view.findViewById(R.id.tv_connectionsearch_email);
            mAdd = view.findViewById(R.id.button_connectionsearch_add);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mUsername.getText() + "'";
        }
    }
}
