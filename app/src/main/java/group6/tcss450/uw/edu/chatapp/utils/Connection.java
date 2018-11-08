package group6.tcss450.uw.edu.chatapp.utils;

import java.io.Serializable;

public class Connection implements Serializable {
    public final String mUsername;
    public final String mEmail;

    public static class Builder {
        private final String mUsername;
        private final String mEmail;

        public Builder(String username, String email) {
            this.mUsername = username;
            this.mEmail = email;
        }

        public Connection build()  {
            return new Connection(this);
        }
    }

    private Connection(final Builder builder)  {
        this.mUsername = builder.mUsername;
        this.mEmail = builder.mEmail;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getEmail()    {
        return mEmail;
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
