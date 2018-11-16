package group6.tcss450.uw.edu.chatapp.messages;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import group6.tcss450.uw.edu.chatapp.R;

//Not sure if this will need serializable. Just basing this off of Credentials.
//@author Nathan Rueschenberg

public class OpenMessage extends AppCompatActivity implements Serializable {

    /** The user the connection was established with. */
    private final String mOtherUser;

    /** The date the last message was received / sent. */
    private final String mDate;

    /** The time the last message was received / sent. */
    private final String mTime;

    /** A preview of the last message received / sent. */
    private final String mLastMessage;

    private final int mChatId;

    public final String CHATID = "chatid";

    public static class Builder {
        private final String mOtherUser;
        private String mDate = "";
        private String mTime = "";
        private String mLastMessage = "";
        private int mChatId;

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

        public Builder addChatId(final int theId)   {
            this.mChatId = theId;
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
        this.mChatId = builder.mChatId;
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

    public int getChatId()  {
        return mChatId;
    }

    public JSONObject asJSONObject()    {
        JSONObject msg = new JSONObject();
        try{
            msg.put(CHATID, getChatId());
        } catch (JSONException e)   {
            Log.wtf("CREDENTIALS", "Error creating JSON in OpenMessage");
        }
        return msg;
    }


}
