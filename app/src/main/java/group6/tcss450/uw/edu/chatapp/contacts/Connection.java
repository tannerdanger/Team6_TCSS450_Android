package group6.tcss450.uw.edu.chatapp.contacts;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Connection implements Serializable {
    public final String mUsername;
    public final String mEmail;
    public final String mFirstName;
    public final String mLastName;
    public final int mId;
    public final int mVerified;

    public static class Builder {
        private final String mUsername;
        private final String mEmail;
        private String mFirstName;
        private String mLastName;
        private int mId;
        private int mVerified;

        public Builder(String username, String email) {
            this.mUsername = username;
            this.mEmail = email;
        }

        public Builder addFirstName(final String fName)   {
            this.mFirstName = fName;
            return this;
        }

        public Builder addLastName(final String lName)  {
            this.mLastName = lName;
            return this;
        }

        public Builder addId(final int id)  {
            this.mId = id;
            return this;
        }

        public Builder addVerified(final int v) {
            this.mVerified = v;
            return this;
        }

        public Connection build()  {
            return new Connection(this);
        }
    }

    private Connection(final Builder builder)  {
        this.mUsername = builder.mUsername;
        this.mEmail = builder.mEmail;
        this.mFirstName = builder.mFirstName;
        this.mLastName = builder.mLastName;
        this.mId = builder.mId;
        this.mVerified = builder.mVerified;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getEmail()    {
        return mEmail;
    }

    public String getFirstName()    {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public int getId()  {
        return mId;
    }

    public int getVerified()    {
        return mVerified;
    }

    @Override
    public boolean equals(Object theOther)  {
        return (theOther instanceof Connection) && (this.hashCode() == theOther.hashCode());
    }

    @Override
    public int hashCode()   {
        int hash = this.mId;
        return hash * 31;
    }

    public JSONObject asJSONObject()    {
        JSONObject msg = new JSONObject();
        try{
            msg.put("username", getUsername());
            msg.put("email", getEmail());
            msg.put("firstname", getFirstName());
            msg.put("lastname", getLastName());
            msg.put("memberId", getId());
        } catch (JSONException e)   {
            Log.wtf("MESSAGE", "Error creating Message JSON");
        }
        return msg;
    }

}
