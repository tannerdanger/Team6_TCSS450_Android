package group6.tcss450.uw.edu.chatapp.view;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import group6.tcss450.uw.edu.chatapp.R;
import group6.tcss450.uw.edu.chatapp.contacts.Connection;
import group6.tcss450.uw.edu.chatapp.view.NewChatFragment.OnListFragmentInteractionListener;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Connection} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyNewChatRecyclerViewAdapter extends RecyclerView.Adapter<MyNewChatRecyclerViewAdapter.ViewHolder> {

    private final List<Connection> mValues;
    private List<Connection> mSelected;
    private final OnListFragmentInteractionListener mListener;

    private int mRowIndex;

    public MyNewChatRecyclerViewAdapter(List<Connection> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        mRowIndex = -1;
        mSelected = new ArrayList<Connection>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_newchat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //Credit to:
        // https://stackoverflow.com/questions/40692214/changing-background-color-of-selected-item-in-recyclerview
        holder.mConnection = mValues.get(position);
        holder.mUsernameTv.setText(mValues.get(position).getUsername());

//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != mListener) {
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
//                    mListener.onNewChatFragmentInteraction(holder.mConnection);
//                    holder.
//                }
//            }
//        });
        holder.row_linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRowIndex = position;
                notifyDataSetChanged();
            }
        });
        if (mRowIndex == position) {
            holder.isSelected = !holder.isSelected;
            if (holder.isSelected) {
                //holder.row_linearlayout.setBackgroundColor(Color.parseColor("#567845"));
                //holder.mUsernameTv.setTextColor(Color.parseColor("#ffffff"));
                holder.row_linearlayout.setBackgroundResource(R.drawable.rectangleborder_filled);
                mSelected.add(holder.mConnection);

            } else {
                //holder.row_linearlayout.setBackgroundColor(Color.parseColor("#ffffff"));
                holder.row_linearlayout.setBackgroundResource(R.drawable.rectangleborder_nofill);
                mSelected.remove(holder.mConnection);
                //holder.mUsernameTv.setTextColor(Color.parseColor("#000000"));
            }
        }
//        else
//        {
//            holder.row_linearlayout.setBackgroundColor(Color.parseColor("#ffffff"));
//            holder.mUsernameTv.setTextColor(Color.parseColor("#000000"));
//        }

    }

    public List<Connection> getSelectedConnections()    {
        return mSelected;
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mUsernameTv;
        public Connection mConnection;
        public boolean isSelected;
        ConstraintLayout row_linearlayout;
        RecyclerView rv;

        public ViewHolder(View view) {
            super(view);
            isSelected = false;
            mView = view;
            mUsernameTv = view.findViewById(R.id.tv_newchat_username);
            row_linearlayout = itemView.findViewById(R.id.linearLayout_newchat_individual);
            rv = itemView.findViewById(R.id.list_newchat);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
