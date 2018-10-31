package group6.tcss450.uw.edu.chatapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import group6.tcss450.uw.edu.chatapp.MessagesFragment.OnMessageFragmentInteractionListener;
import group6.tcss450.uw.edu.chatapp.utils.Message;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Message} and makes a call to the
 * specified {@link OnMessageFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyMessagesRecyclerViewAdapter extends RecyclerView.Adapter<MyMessagesRecyclerViewAdapter.ViewHolder> {

    private final List<Message> mValues;
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

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mUsername = (TextView) view.findViewById(R.id.tv_messages_user);
            mDate = (TextView) view.findViewById(R.id.tv_messages_date);
            mTime = (TextView) view.findViewById(R.id.tv_messages_time);
            mMessageView = (TextView) view.findViewById(R.id.tv_messages_message);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mItem.toString() + "'";
        }
    }
}
