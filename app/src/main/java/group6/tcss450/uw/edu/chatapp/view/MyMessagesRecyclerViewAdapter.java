package group6.tcss450.uw.edu.chatapp.view;

import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.widget.RecyclerView;
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


    public MyMessagesRecyclerViewAdapter(List<Message> items, OnMessageFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_messages, parent, false);
        return new ViewHolder(view);
    }

    public void addItem(Message theMessage) {
        mValues.add(theMessage);
        notifyItemInserted(0);
        notifyDataSetChanged();
        if(theMessage.getUser().compareTo(mCredentials.getEmail()) == 0 ){

        }



    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mMessageView.setText(mValues.get(position).getMessage());
        holder.mDate.setText(mValues.get(position).getDate());
        holder.mTime.setText(mValues.get(position).getTime());
        holder.mUsername.setText(mValues.get(position).getUser());


//
//        int fourDP = (int) (4 / Resources.getSystem().getDisplayMetrics().density);
//        int sixtyfourDP = (int) (64 / Resources.getSystem().getDisplayMetrics().density);
//        int eightDP = (int) (8 / Resources.getSystem().getDisplayMetrics().density);

//        FrameLayout.LayoutParams otherUserSends = (FrameLayout.LayoutParams) holder
//                .row_constraintlayout.getLayoutParams();
//        FrameLayout.LayoutParams thisUserSends = (FrameLayout.LayoutParams) holder
//                .row_constraintlayout.getLayoutParams();
//        otherUserSends.setMargins(4, 0, 64, 8);
//        thisUserSends.setMargins(64, 0, 4, 8);
        //l, t, r, b
        //default margins = 4, 0, 4, 8
        //they send = 4, 0, 64, 8
        //user send = 64, 0, 4, 8
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
//        //Log.e("Messages", "mItem user = " + holder.mItem.getUser() + ", Credentials user = " + mCredentials.getEmail());
//        if(holder.mItem.getUser().equals(mCredentials.getEmail()))   {
//            holder.row_constraintlayout.setLayoutParams(thisUserSends);
//            Log.e("MESSAGES", "RIGHT");
//        } else {
//            holder.row_constraintlayout.setLayoutParams(otherUserSends);
//            Log.e("MESSAGES", "LEFT");
//        }
    }

    public void setCredentials(Credentials theCredentials)  {
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
