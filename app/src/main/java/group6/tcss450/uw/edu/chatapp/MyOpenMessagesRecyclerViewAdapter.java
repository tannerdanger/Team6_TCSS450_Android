package group6.tcss450.uw.edu.chatapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import group6.tcss450.uw.edu.chatapp.OpenMessagesFragment.OnOpenMessageFragmentInteractionListener;
import group6.tcss450.uw.edu.chatapp.utils.OpenMessage;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link OpenMessage} and makes a call to the
 * specified {@link OnOpenMessageFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyOpenMessagesRecyclerViewAdapter extends RecyclerView.Adapter<MyOpenMessagesRecyclerViewAdapter.ViewHolder> {

    private final List<OpenMessage> mValues;
    private final OnOpenMessageFragmentInteractionListener mListener;

    public MyOpenMessagesRecyclerViewAdapter(List<OpenMessage> items, OnOpenMessageFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_openmessages, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mOtherUser.setText(mValues.get(position).getOtherUser());
        holder.mMessage.setText(mValues.get(position).getLastMessage());
        holder.mDate.setText(mValues.get(position).getDate());
        holder.mTime.setText(mValues.get(position).getTime());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onOpenMessageFragmentInteraction(holder.mItem);
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
        public final TextView mOtherUser;
        public final TextView mDate;
        public final TextView mTime;
        public final TextView mMessage;
        public OpenMessage mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mOtherUser = (TextView) view.findViewById(R.id.tv_connectionsfragment_otheruser);
            mDate = (TextView) view.findViewById(R.id.tv_connectionsfragment_date);
            mTime = (TextView) view.findViewById(R.id.tv_connectionsfragment_time);
            mMessage = (TextView) view.findViewById(R.id.tv_connectionsfragment_message);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mDate.getText() + "'";
        }
    }
}
