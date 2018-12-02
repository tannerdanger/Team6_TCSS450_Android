package group6.tcss450.uw.edu.chatapp.utils;

import android.support.v7.app.AppCompatActivity;

import java.io.Serializable;

public class Notification extends AppCompatActivity implements Serializable {


    private final int mID;
    private final String mNotifier;
    private final NotificationType mType;

    public enum NotificationType {
        MESSAGE, FRIEND_REQUEST, CONVERSATION_REQUEST
    }

    public static class Builder {

        private final String mNotifier;
        private final NotificationType mType;
        private int mID;

        public Builder(String notifier, NotificationType type) {
            mNotifier = notifier;
            mType = type;
        }

        public Builder addID(final int id) {
            mID = id;
            return this;
        }

        public Notification build() {
            return new Notification(this);
        }
    }

    private Notification(final Builder builder) {
        mNotifier = builder.mNotifier;
        mType = builder.mType;
        mID = builder.mID;
    }

    public String getNotifier() {
        return mNotifier;
    }


    public NotificationType getType() {
        return mType;
    }

    public int getID() {
        return mID;
    }

    @Override
    public String toString() {
        String s = mNotifier;

        switch (mType) {
            case MESSAGE:
                s += " has sent you a message.";
                break;

            case FRIEND_REQUEST:
                s += " has sent you a friend request.";
                break;

            case CONVERSATION_REQUEST:
                s += " has sent you a conversation request.";
                break;
        }

        return s;
    }
}