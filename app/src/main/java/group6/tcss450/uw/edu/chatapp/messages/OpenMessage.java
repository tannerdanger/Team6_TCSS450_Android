package group6.tcss450.uw.edu.chatapp.messages;

import java.io.Serializable;

//Not sure if this will need serializable. Just basing this off of Credentials.
//@author Nathan Rueschenberg

public class OpenMessage implements Serializable {

    /** The user the connection was established with. */
    private final String mOtherUser;

    /** The date the last message was received / sent. */
    private final String mDate;

    /** The time the last message was received / sent. */
    private final String mTime;

    /** A preview of the last message received / sent. */
    private final String mLastMessage;

    public static class Builder {
        private final String mOtherUser;
        private String mDate = "";
        private String mTime = "";
        private String mLastMessage = "";

        /**
         * Constructor for connections.
         * @param theOtherUser The user the connection is going to.
         */
        public Builder(String theOtherUser) {
            this.mOtherUser = theOtherUser;
        }

        public Builder addDate(final String theDate)    {
            this.mDate = theDate;
            return this;
        }

        public Builder addTime(final String theTime)    {
            this.mTime = theTime;
            return this;
        }

        public Builder addLastMessage(final String theMessage)  {
            this.mLastMessage = theMessage;
            return this;
        }

        public OpenMessage build()  {
            return new OpenMessage(this);
        }
    }

    private OpenMessage(final Builder builder)  {
        this.mOtherUser = builder.mOtherUser;
        this.mDate = builder.mDate;
        this.mTime = builder.mTime;
        this.mLastMessage = builder.mLastMessage;
    }

    public String getOtherUser() {
        return mOtherUser;
    }

    public String getDate() {
        return mDate;
    }

    public String getTime() {
        return mTime;
    }

    public String getLastMessage() {
        return mLastMessage;
    }






}
