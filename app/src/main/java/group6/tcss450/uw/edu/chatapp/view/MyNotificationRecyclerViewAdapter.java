package group6.tcss450.uw.edu.chatapp.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import group6.tcss450.uw.edu.chatapp.R;
import group6.tcss450.uw.edu.chatapp.utils.Notification;
import group6.tcss450.uw.edu.chatapp.view.NotificationFragment.OnListFragmentInteractionListener;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Notification} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyNotificationRecyclerViewAdapter extends RecyclerView.Adapter<MyNotificationRecyclerViewAdapter.ViewHolder> {

    private final List<Notification> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyNotificationRecyclerViewAdapter(List<Notification> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_notification, parent, false);
        return new ViewHolder(view);
    }

    // TODO
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mSender.setText(mValues.get(position).getNotifier());
        holder.mDetails.setText(mValues.get(position).toString());
        switch (mValues.get(position).getType()) {
            case MESSAGE:
                holder.mType.setText("New message");
                break;

            case FRIEND_REQUEST:
                holder.mType.setText("New friend request");
                break;

            case CONVERSATION_REQUEST:
                holder.mType.setText("New conversation request");
                break;
        }


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListNotificationFragmentInteraction(holder.mItem);
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
        public final TextView mSender;
        public final TextView mType;
        public final TextView mDetails;
        public Notification mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mSender = (TextView) view.findViewById(R.id.tv_notificationsfragment_sender);
            mType = (TextView) view.findViewById(R.id.tv_notificationsfragment_type);
            mDetails = (TextView) view.findViewById(R.id.tv_notificationsfragment_details);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mDetails.getText() + "'";
        }
    }
}
