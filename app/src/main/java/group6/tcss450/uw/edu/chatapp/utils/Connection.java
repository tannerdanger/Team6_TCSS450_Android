package group6.tcss450.uw.edu.chatapp.utils;

import java.io.Serializable;

public class Connection implements Serializable {
    public final String mUsername;
    public final String mEmail;
    public final String mFirstName;
    public final String mLastName;

    public static class Builder {
        private final String mUsername;
        private final String mEmail;
        private String mFirstName;
        private String mLastName;

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

        public Connection build()  {
            return new Connection(this);
        }
    }

    private Connection(final Builder builder)  {
        this.mUsername = builder.mUsername;
        this.mEmail = builder.mEmail;
        this.mFirstName = builder.mFirstName;
        this.mLastName = builder.mLastName;
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

    @Override
    public boolean equals(Object theOther)  {
        return (theOther instanceof Connection) && (this.hashCode() == theOther.hashCode());
    }

    @Override
    public int hashCode()   {
        int hash = this.mUsername.hashCode() + this.mEmail.hashCode();
        return hash * 31;
    }

}
