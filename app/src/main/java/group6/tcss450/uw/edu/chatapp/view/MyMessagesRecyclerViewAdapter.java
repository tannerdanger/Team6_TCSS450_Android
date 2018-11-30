package group6.tcss450.uw.edu.chatapp.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import group6.tcss450.uw.edu.chatapp.R;
import group6.tcss450.uw.edu.chatapp.messages.Message;
import group6.tcss450.uw.edu.chatapp.utils.Credentials;
import group6.tcss450.uw.edu.chatapp.view.MessagesFragment.OnMessageFragmentInteractionListener;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Message} and makes a call to the
 * specified {@link OnMessageFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyMessagesRecyclerViewAdapter extends RecyclerView.Adapter<MyMessagesRecyclerViewAdapter.ViewHolder> {

    private final List<Message> mValues;
    private Credentials mCredentials;
    private final OnMessageFragmentInteractionListener mListener;
    private Context mContext;

    public MyMessagesRecyclerViewAdapter(List<Message> items, OnMessageFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_messages, parent, false);
        mContext = parent.getContext();
        return new ViewHolder(view);
    }

    public void addItem(Message theMessage) {
        mValues.add(theMessage);
        Log.e("Messages", "adding a message in1");
        notifyItemInserted(0);
        notifyDataSetChanged();


    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mMessageView.setText(mValues.get(position).getMessage());
        holder.mDate.setText(mValues.get(position).getDate());
        holder.mTime.setText(mValues.get(position).getTime());
        holder.mUsername.setText(mValues.get(position).getUser());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onMessageFragmentInteraction(holder.mItem);
                }
            }
        });
        if (!holder.mItem.getUser().equals(mCredentials.getEmail())) {
            //SOMEONE THAT IS NOT THE USER SENDS A MESSAGE
            ConstraintLayout.LayoutParams otherUserSends = (ConstraintLayout.LayoutParams) holder
                    .row_constraintlayout.getLayoutParams();
            otherUserSends.startToEnd = ConstraintLayout.LayoutParams.UNSET;
            otherUserSends.endToEnd = ConstraintLayout.LayoutParams.UNSET;
            otherUserSends.startToStart = holder.mView.findViewById(R.id.constraintlayout_messages_holder).getId();
            otherUserSends.endToStart = holder.mView.findViewById(R.id.space_messages_them).getId();
            otherUserSends.horizontalBias = 0;
            //otherUserSends.constrainedWidth = true;
            holder.mUsername.setTextColor(Color.BLACK);
            holder.mDate.setTextColor(Color.BLACK);
            holder.mTime.setTextColor(Color.BLACK);
            holder.mMessageView.setTextColor(Color.BLACK);
            holder.row_constraintlayout.setBackgroundResource(R.drawable.incoming_chat);
            holder.row_constraintlayout.setLayoutParams(otherUserSends);
            //Log.e("Message", "Email: " + holder.mItem.getUser() + ", going on left side. Should be NOT user." + holder.mItem.getMessage());
        } else {
            //THIS USER SENDS A MESSAGE
            //Log.e("Message", "Email: " + holder.mItem.getUser() + ", going on right side. Should be user." + holder.mItem.getMessage());
            ConstraintLayout.LayoutParams thisUserSends = (ConstraintLayout.LayoutParams) holder
                    .row_constraintlayout.getLayoutParams();
            thisUserSends.startToStart = ConstraintLayout.LayoutParams.UNSET;
            thisUserSends.endToStart = ConstraintLayout.LayoutParams.UNSET;
            thisUserSends.startToEnd = holder.mView.findViewById(R.id.space_messages_you).getId();
            thisUserSends.endToEnd = holder.mView.findViewById(R.id.constraintlayout_messages_holder).getId();
            thisUserSends.horizontalBias = 1.0f;
            holder.mUsername.setTextColor(Color.WHITE);
            holder.mDate.setTextColor(Color.WHITE);
            holder.mTime.setTextColor(Color.WHITE);
            holder.mMessageView.setTextColor(Color.WHITE);
            holder.row_constraintlayout.setBackgroundResource(R.drawable.outgoing_chat);
            holder.row_constraintlayout.setLayoutParams(thisUserSends);
        }
    }

    public void setCredentials(Credentials theCredentials) {
        mCredentials = theCredentials;
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mUsername;
        public final TextView mDate;
        public final TextView mTime;
        public final TextView mMessageView;
        public Message mItem;
        ConstraintLayout row_constraintlayout;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mUsername = view.findViewById(R.id.tv_messages_user);
            mDate = view.findViewById(R.id.tv_messages_date);
            mTime = view.findViewById(R.id.tv_messages_time);
            mMessageView = view.findViewById(R.id.tv_messages_message);
            row_constraintlayout = itemView.findViewById(R.id.constraintlayout_messages);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mItem.toString() + "'";
        }
    }
}
