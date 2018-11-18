package group6.tcss450.uw.edu.chatapp.contacts;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import group6.tcss450.uw.edu.chatapp.MyConnectionsSearchRecyclerViewAdapter;
import group6.tcss450.uw.edu.chatapp.R;
import group6.tcss450.uw.edu.chatapp.utils.Credentials;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnConnectionSearchFragmentInteractionListener}
 * interface.
 */
public class ConnectionsSearchFragment extends Fragment {


    //TODO COMPARE SEARCH RESULTS WITH EXISTING CONNECTIONS. ONLY DISPLAY RESULTS NOT IN YOUR CONNECTIONS.


    /** Connection search results. */
    private List<Connection> mConnections;

    private List<Connection> currentConnections;

    private Credentials mCredentials;

    /** Default List View parameter. */
    private int mColumnCount = 1;

    /** The RecyclerView holding the connection search results list. */
    private RecyclerView mRecyclerView;

    /** Listener to interact with each item in the list. */
    private OnConnectionSearchFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ConnectionsSearchFragment() {
    }

    /**
     * On create we initialize the Connection search result array and change the input mode
     * to avoid the keyboard messing up the layout when it appears.
     * @param savedInstanceState Default parameter
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConnections = new ArrayList<Connection>();
        mCredentials = (Credentials) getArguments().getSerializable(getString(R.string.ARGS_CREDENTIALS));
        currentConnections = new ArrayList<Connection>(Arrays.asList((Connection[])
                getArguments().getSerializable(ConnectionFragment.ARG_CONNECTION_LIST)));
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

    }

    /**
     * Sets everything up: RecyclerView, Button, and the Buttons onClickListener.
     * @param inflater Default parameter
     * @param container Default paramenter
     * @param savedInstanceState Default parameter
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connectionssearch_list, container, false);
        Context context = view.getContext();
        mRecyclerView = view.findViewById(R.id.list_connectionsearch_users);
        if (mColumnCount <= 1) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        mRecyclerView.setAdapter(new MyConnectionsSearchRecyclerViewAdapter(mConnections, mListener));
        Button searchButton = view.findViewById(R.id.button_connectionsearch_search);
        EditText searchPrompt = view.findViewById(R.id.et_connectionsearch_searchbar);
        searchButton.setOnClickListener((View v) -> {
            //Build POST query URL
            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath(getString(R.string.ep_base_url))
                    .appendPath(getString(R.string.ep_conn))
                    .appendPath(getString(R.string.ep_search))
                    .appendQueryParameter("query", searchPrompt.getText().toString())
                    .build();
            //Initialize AsyncTask
            AsyncTask<String, Void, String> mTask = new PostWebServiceTask();
            //Execute POST query
            mTask.execute(uri.toString(), String.valueOf(mCredentials.getID()));
        });
        return view;
    }

    /**
     * Default generated onAttach method. Nothing changed here.
     * @param context Default parameter.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnConnectionSearchFragmentInteractionListener) {
            mListener = (OnConnectionSearchFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnConnectionSearchFragmentInteractionListener");
        }
    }

    /** Default generated onDetach method. Nothing changed here. */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Inner class for posting the URL and retrieving the search query results.
     * Modified from TCSS 450 Lab 3 - authored by Charles Bryan.
     */
    private class PostWebServiceTask extends AsyncTask<String, Void, String> {

        /**
         * Retrieves results from our search query.
         * Slightly modified from Lab 3 to use 1 string parameter instead of 3.
         * @param string The search query.
         * @return Search query result.
         */
        @Override
        protected String doInBackground(String... string) {
            if (string.length != 2) {
                throw new IllegalArgumentException("One String argument required.");
            }
            String response = "";
            HttpURLConnection urlConnection = null;
            String url = string[0];
            String args = string[1];
            //build the url
            Uri uri = Uri.parse(url);
            //Construct a JSONObject to build a formatted message to send.
            JSONObject msg = new JSONObject();
            try {
                msg.put(getString(R.string.JSON_USERS_MEMBER_ID), Integer.parseInt(args));
            } catch (JSONException e)   {
                e.printStackTrace();
            }
            try {
                URL urlObject = new URL(uri.toString());
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setDoOutput(true);
                OutputStreamWriter wr =
                        new OutputStreamWriter(urlConnection.getOutputStream());
                wr.write(msg.toString());
                wr.flush();
                wr.close();
                InputStream content = urlConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";
                while ((s = buffer.readLine()) != null) {
                    response += s;
                }
            } catch (Exception e) {
                //cancel will result in onCanceled not onPostExecute cancel(true);
                return "Unable to connect, Reason: " + e.getMessage();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return response;
        }

        /**
         * Parses the result from our search query. Adds it to connection array and notifies
         * the RecyclerView that the Connection list has been modified.
         * @param result The search query POST result (a JSON Array in string form).
         */
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject root = new JSONObject(result);
                //If search query is valid and returned a user
                if (root.getBoolean("success")) {
                    JSONArray response = root.getJSONArray("user");
                    List<Connection> conns = new ArrayList<>();
                    //Get all the users returned from the search query
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonSet = response.getJSONObject(i);
                        //if(!currentConnections.contains)
                        Connection temp = new Connection.Builder(jsonSet.getString("username"),
                                jsonSet.getString("email"))
                                .addFirstName(jsonSet.getString("firstname"))
                                .addLastName(jsonSet.getString("lastname"))
                                .addId(jsonSet.getInt("memberid"))
                                .build();
                        if(!currentConnections.contains(temp))  {
                            conns.add(temp);
                        }
                    }
                    //Add them to our connection array
                    Connection[] connectionsAsArray = new Connection[conns.size()];
                    connectionsAsArray = conns.toArray(connectionsAsArray);
                    mConnections.clear();
                    for(Connection c : connectionsAsArray)  {
                        mConnections.add(c);
                    }
                    //Notify the RecyclerView that the data set it should display has changed.
                    mRecyclerView.getAdapter().notifyDataSetChanged();
                } else {
                    Log.e("ERROR!", "No data array!");
                    //No results found, clear the result array.
                    mConnections.clear();
                    mRecyclerView.getAdapter().notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("ERROR!", e.getMessage());
            }
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnConnectionSearchFragmentInteractionListener {
        // TODO: Update argument type and name
        void onConnectionSearchFragmentInteraction(Connection item);
    }
}
