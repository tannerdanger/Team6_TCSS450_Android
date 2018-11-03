package group6.tcss450.uw.edu.chatapp.utils;

import java.io.Serializable;

public class Message implements Serializable    {

    /**
     * The user the connection was established with.
     */
    private final String mUser;

    /**
     * The date the last message was received / sent.
     */
    private final String mDate;

    /**
     * The time the last message was received / sent.
     */
    private final String mTime;

    /**
     * The message itself.
     */
    private final String mMessage;

    public static class Builder {
        private final String mUser;
        private String mDate;
        private String mTime;
        private String mMessage = "";

        /**
         * Constructor for message.
         *
         * @param theUser The user that sent the message.
         * @param theDate the date the message was sent.
         * @param theTime the time the message was sent.
         */
        public Builder(String theUser, String theDate, String theTime) {
            this.mUser = theUser;
            this.mDate = theDate;
            this.mTime = theTime;
        }

        public Builder addMessage(final String theMessage)    {
            this.mMessage = theMessage;
            return this;
        }

        public Message build() {
            return new Message(this);
        }
    }

    private Message(final Builder builder) {
        this.mUser = builder.mUser;
        this.mDate = builder.mDate;
        this.mTime = builder.mTime;
        this.mMessage = builder.mMessage;
    }

    public String getUser() {
        return mUser;
    }

    public String getDate() {
        return mDate;
    }

    public String getTime() {
        return mTime;
    }

    public String getMessage()  {
        return mMessage;
    }

    public String toString()    {
        return getUser() + " - " + getMessage();
    }

}

