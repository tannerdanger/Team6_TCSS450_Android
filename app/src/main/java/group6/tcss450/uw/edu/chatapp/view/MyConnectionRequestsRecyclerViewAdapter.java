package group6.tcss450.uw.edu.chatapp.view;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import group6.tcss450.uw.edu.chatapp.R;
import group6.tcss450.uw.edu.chatapp.contacts.Connection;
import group6.tcss450.uw.edu.chatapp.contacts.ConnectionRequestsFragment;
import group6.tcss450.uw.edu.chatapp.contacts.ConnectionRequestsFragment.OnConnectionRequestFragmentInteractionListener;

/**
 * {@link RecyclerView.Adapter} that can display a {@link android.telecom.ConnectionRequest} and makes a call to the
 * specified {@link ConnectionRequestsFragment.OnConnectionRequestFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyConnectionRequestsRecyclerViewAdapter extends RecyclerView.Adapter<MyConnectionRequestsRecyclerViewAdapter.ViewHolder> {

    private final List<Connection> mValues;
    private final OnConnectionRequestFragmentInteractionListener mListener;
    private TextView noRequests;

    public MyConnectionRequestsRecyclerViewAdapter(List<Connection> items, ConnectionRequestsFragment.OnConnectionRequestFragmentInteractionListener listener, TextView tv) {
        mValues = items;
        mListener = listener;
        noRequests = tv;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_connectionrequests, parent, false);
        //noRequests = view.findViewById(R.id.tv_connectionrequest_none);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mConnection = mValues.get(position);
        holder.mUsername.setText(mValues.get(position).getUsername());
        holder.mEmail.setText(mValues.get(position).getEmail());
        Connection thisConnection = mValues.get(position);


//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != mListener) {
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
//                    //mListener.onConnectionRequestAccept(holder.mConnection);
//                }
//            }
//        });
        holder.mAccept.setOnClickListener((View v) ->   {
           this.notifyItemRemoved(mValues.indexOf(thisConnection));
           mValues.remove(thisConnection);
           //TODO backend
            mListener.onConnectionRequestAccept(thisConnection);
           Log.d("CONNECTION REQUEST", "ACCEPT REQUEST");
           if(noRequests.getVisibility() == TextView.INVISIBLE && mValues.isEmpty()) {
               noRequests.setVisibility(TextView.VISIBLE);
           }
        });
        holder.mReject.setOnClickListener((View v) ->   {
            this.notifyItemRemoved(mValues.indexOf(thisConnection));
            mValues.remove(thisConnection);
            mListener.onConnectionRequestReject(thisConnection);
            Log.d("CONNECTION REQUEST", "REJECT REQUEST");
            if(noRequests.getVisibility() == TextView.INVISIBLE && mValues.isEmpty()) {
                noRequests.setVisibility(TextView.VISIBLE);
            }
        });

        if (thisConnection.getThisUserSent()) {
            holder.mAccept.setVisibility(TextView.INVISIBLE); //hide accept button
            holder.mReject.setText("CANCEL REQUEST");
        }

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mUsername;
        public final TextView mEmail;
        public final Button mAccept;
        public final Button mReject;
        public final TextView mPending;
        public Connection mConnection;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mUsername = view.findViewById(R.id.tv_connectionrequest_username);
            mEmail = view.findViewById(R.id.tv_connectionrequest_email);
            mAccept = view.findViewById(R.id.button_connectionrequest_accept);
            mReject = view.findViewById(R.id.button_connectionrequest_reject);
            mPending = view.findViewById(R.id.tv_connectionrequest_pending);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mEmail.getText() + "'";
        }
    }
}
