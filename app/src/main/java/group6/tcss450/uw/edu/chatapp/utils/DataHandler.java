package group6.tcss450.uw.edu.chatapp.utils;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import group6.tcss450.uw.edu.chatapp.contacts.Connection;
import group6.tcss450.uw.edu.chatapp.messages.Message;
import group6.tcss450.uw.edu.chatapp.messages.OpenMessage;
import group6.tcss450.uw.edu.chatapp.view.HomeActivity;


public class DataHandler {

    private static final String ARGS_CHATROOMS = "chats";
    private static final String ARGS_CREDENTIALS = "credentials";
    private static final String ARGS_CONNECTIONS = "connections";
    private static final String ARGS_FORECASTDATA = "forecast";
    private static final String ARGS_MESSAGES = "messages";



    private HomeActivity mHomeActivity;
    private Credentials mCredentials;
    private double mLat, mLon;
    private static boolean mIsWaitFragActive;

    /**
     * When created, the datahandler starts by initializing some data
     * @param activity a reference to the activity for calling update methods.
     * @param cred credentials for accessing the users information
     * @param lat latitude of the phone's location for weather
     * @param lon longitude of the phone's location for weather
     * @param isInit boolean determining if the class should run its initialization or not.
     */
    public DataHandler(HomeActivity activity, Credentials cred, double lat, double lon, boolean isInit){
        mIsWaitFragActive = false;
        if(null != activity) {
            mHomeActivity = activity;
        }
        if(null != cred){
            mCredentials=cred;
        }
        mLat = lat;
        mLon = lon;
        if(isInit)
            initAll();

        // getMessages(14, false);
        // getMessages(10, false);
    }


    ///////////// PUBLIC UPDATE METHODS ///////////////////////


    /**
     * When called, this method updates all of the user's contacts from the database, and adds them
     * to the mJsonData hashmap in main activity.
     */
    public void updateContacts(){

        JSONObject msg = JsonHelper.connections_JsonObject(mCredentials.getID());
        if (null != msg){
            String uri = UriHelper.CONNECTIONS_GETALL();

            new SendPostAsyncTask.Builder(uri, msg)
                    .onPostExecute(this::updateConnectionsJsonData)
                    .onCancelled(this::handleErrorsInTask)
                    .build()
                    .execute();
        }
    }

    public void updateWeatherByCity(String city) {
        startAsync();
        JSONObject msg = JsonHelper.weather_JsonObject(city);
        if(null != msg){
            String uri = UriHelper.WEATHER_BY_CITY();

            new SendPostAsyncTask.Builder(uri, msg)
                    //.onPreExecute() //todo: wait fragment
                    .onPostExecute(this::updateForecastJsonData)
                    .onCancelled(this::handleErrorsInTask)
                    .build()
                    .execute();
        }
    }



    public void updateWeatherByLatLon(double lat, double lon, boolean updateFrags, boolean transition){

        startAsync();
        JSONObject msg = JsonHelper.weather_JsonObject(lat, lon);

        if(null != msg){

            String uri = UriHelper.WEATHER_BY_LAT_LONG();

            if(updateFrags && transition){
                new SendPostAsyncTask.Builder(uri, msg)
                        //.onPreExecute() //todo: wait fragment
                        .onPostExecute(this::updateForecastAndFragsTransition)
                        .onCancelled(this::handleErrorsInTask)
                        .build()
                        .execute();
            } else if (updateFrags && !transition){
                new SendPostAsyncTask.Builder(uri, msg)
                        //.onPreExecute() //todo: wait fragment
                        .onPostExecute(this::updateForecastAndFrags)
                        .onCancelled(this::handleErrorsInTask)
                        .build()
                        .execute();

                new SendPostAsyncTask.Builder(uri, msg)
                        //.onPreExecute() //todo: wait fragment
                        .onPostExecute(this::updateForecastJsonData)
                        .onCancelled(this::handleErrorsInTask)
                        .build()
                        .execute();
            }
        }
    }

    private void updateForecastAndFragsTransition(String s) {
        updateForecastJsonData(s);
        mHomeActivity.weatherLoaded(true);
        endAsync();
    }

    private void updateForecastAndFrags(String s) {
        updateForecastJsonData(s);
        mHomeActivity.weatherLoaded(false);
        endAsync();
    }

    public void getChats(boolean isFragTransition){
        JSONObject msg = JsonHelper.chats_JsonObject(mCredentials.getID());

        String uri = UriHelper.MESSAGING_GETMY();

        if(!isFragTransition) {

            new SendPostAsyncTask.Builder(uri, msg)
                    .onPostExecute(this::updateChatsJsonData)
                    .onCancelled(this::handleErrorsInTask)
                    .build()
                    .execute();
        } else {
            new SendPostAsyncTask.Builder(uri, msg)
                    .onPostExecute(this::updateChatsTransition)
                    .onCancelled(this::handleErrorsInTask)
                    .build()
                    .execute();
        }
    }


    /**
     * Loads messages into the message data structure
     * @param chatid the id of the chat that the messages belong to
     * @param isFragTransition is a new fragment loading after this?
     */
    public void getMessages(int chatid, boolean isFragTransition, boolean isRefresh){
        JSONObject msg = JsonHelper.messages_JsonObject(chatid);

        String uri = UriHelper.MESSAGING_GETALL();

        if(isFragTransition) {

            new SendPostAsyncTask.Builder(uri, msg)
                    .onPostExecute(this::updateMessagesTransition)
                    .onCancelled(this::handleErrorsInTask)
                    .build()
                    .execute();

        } else if (isRefresh){

            new SendPostAsyncTask.Builder(uri, msg)
                    .onPostExecute(this::updateMessagesRefresh)
                    .onCancelled(this::handleErrorsInTask)
                    .build()
                    .execute();

        } else {

            new SendPostAsyncTask.Builder(uri, msg)
                    .onPostExecute(this::updateMessages)
                    .onCancelled(this::handleErrorsInTask)
                    .build()
                    .execute();

        }
    }


    public void proposeConnection(int memberid, String senderUsername, int recieverID){
        String uri = UriHelper.CONNECTION_PROPOSE();
        JSONObject msg = JsonHelper.conn_ProposeAndApprove_JsonObject(memberid, senderUsername, recieverID);

        if(null != uri && null != msg){
            new SendPostAsyncTask.Builder(uri, msg)
                    .onPostExecute(this::handleProposeFriend)
                    .build()
                    .execute();
        }
    }

    public void acceptOrDenyConnectionRequest(int memberid, int their_id, boolean isApproved){
        String uri;
        if(isApproved)
            uri = UriHelper.CONNECTION_APPROVE();
        else
            uri = UriHelper.CONNECTION_REMOVE();

        JSONObject msg = JsonHelper.conn_ProposeAndApprove_JsonObject(memberid, "", their_id); //uname not needed for approve

        if(null != uri && null != msg){
            new SendPostAsyncTask.Builder(uri, msg  )
                    .onPostExecute(this::handleConnectionAcceptReject)
                    .build()
                    .execute();
        }
        updateContacts();
    }

    public void createOrOpenChatRoom(int therid, String theirUsername, boolean isTransition){

        startAsync();

        String uri = UriHelper.MESSAGES_NEW();
        JSONObject msg = JsonHelper.create_newChat_JsonObject(
                mCredentials.getID(),
                mCredentials.getUsername(),
                therid, theirUsername);

        if(null != uri && null != msg){

            if(isTransition) {

                new SendPostAsyncTask.Builder(uri, msg)
                        .onPostExecute(this::createChatTransition)
                        .onCancelled(this::handleErrorsInTask)
                        .build()
                        .execute();

            } else {

                new SendPostAsyncTask.Builder(uri, msg)
                        .onPostExecute(this::createChat)
                        .onCancelled(this::handleErrorsInTask)
                        .build()
                        .execute();

            }

        }


    }

    public void createMultiChat(List<Connection> members, Credentials user)   {
        JSONObject jResult = new JSONObject();
        JSONArray jArr = new JSONArray();
        try {
            for(Connection c : members) {
                JSONObject temp = new JSONObject();
                temp.put("memberid", c.getId());
                temp.put("username",c.getUsername());
                jArr.put(temp);
            }
            JSONObject userInfo = new JSONObject();
            userInfo.put("memberid", user.getID());
            userInfo.put("username",user.getUsername());
            jArr.put(userInfo);
            jResult.put("users", jArr);
            String uri = UriHelper.MESSAGING_MULTI();
            new SendPostAsyncTask.Builder(uri, jResult)
                    .onPostExecute(this::createChat)
                    .onCancelled(this::handleErrorsInTask)
                    .build()
                    .execute();
        } catch (JSONException e)   {
            e.printStackTrace();
        }
        getChats(true);
    }

    private int createChat(String s){

        try {
            JSONObject result = new JSONObject(s);
            int chatid = result.getInt("chatid");
            getChats(false); //reload chats behind the scenes
            getMessages(chatid, false, false);
            return chatid;

        }catch (JSONException e){
            e.printStackTrace();
        }
        return -1;
    }

    private void createChatTransition(String s){
        int chatid = createChat(s);
        if(chatid != -1){
            getMessages(chatid, true, false); //update messages and navigate
        }
    }

    private void updateMessagesTransition(String s) {
        int chatid = updateMessages(s);
        mHomeActivity.navigateMessages(chatid);
        endAsync();
    }

    private void updateMessagesRefresh(String s) {
        int chatid = updateMessages(s);
        mHomeActivity.receiveMessage(chatid);
    }

    private void updateChatsTransition(String s){
        updateChatsJsonData(s);
        mHomeActivity.navigateChat();
        endAsync();
    }





    /////////////// METHODS FOR CONVERTING JSON INTO OBJECTS ////////////////

    public Connection[] getContactList(JSONObject root){

        if(root.has("connections")) {

            //List<Connection> conns = new ArrayList<>();

            try {

                JSONArray response = root.getJSONArray("connections");

                Connection[] conns = new Connection[response.length()];

                for(int i = 0; i < response.length(); i++) {
                    JSONObject jsonSet = response.getJSONObject(i);
                    boolean requestedByMe = (jsonSet.getInt("requester_id") == mCredentials.getID());
                    conns[i] = new Connection.Builder(jsonSet.getString("username"),
                            jsonSet.getString("email"))
                            .addFirstName(jsonSet.getString("firstname"))
                            .addLastName(jsonSet.getString("lastname"))
                            .addId(jsonSet.getInt("memberid"))
                            .addVerified(jsonSet.getInt("verified"))
                            .addUserSent(requestedByMe)
                            .build();
                }
                return conns;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;


    }

    public OpenMessage[] getChatArray(JSONObject root){

        if( root.has("chats")) {
            try {
                JSONArray response = root.getJSONArray("chats");
                OpenMessage[] chats = new OpenMessage[response.length()];

                for(int i = 0; i < response.length(); i++){
                    JSONObject jsonSet = response.getJSONObject(i);
                    System.out.print(" ");
                    OpenMessage m = new OpenMessage.Builder(jsonSet.getString("name"))
                            .addChatId(jsonSet.getInt("chatid"))
                            // .addLastMessage("fake last message")
                            .build();
                    chats[i] = m;
                }

                return chats;

            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return null;

    }

    ////////////// UPDATE JSON DATA METHODS /////////////////////

    private void updateChatsJsonData(String s){
        try {
            JSONObject result = new JSONObject(s);
            mHomeActivity.updateJsonData(ARGS_CHATROOMS, result);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void updateForecastJsonData(String s){
        try {
            JSONObject result = new JSONObject(s);
            mHomeActivity.updateJsonData(ARGS_FORECASTDATA, result);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void updateConnectionsJsonData(String s){
        try {
            JSONObject result = new JSONObject(s);
            mHomeActivity.updateJsonData(ARGS_CONNECTIONS, result);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private int updateMessages(String s){
        // messages for a chat id load in here

        try {
            JSONObject root = new JSONObject(s);
            int chatid = root.getInt("chatid"); //parse out the chatid so the JSON data can be identified by its chat
            if(root.has("messages")) {
                JSONArray response = root.getJSONArray("messages");
                Message message;

                if(response.length() < 1){
                    mHomeActivity.addMessage(chatid, null);
                }else {

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonSet = response.getJSONObject(i);
                        String[] timedate = jsonSet.getString("timestamp").split(" ", 2);
                        timedate[1] = timedate[1].substring(0, timedate[1].lastIndexOf(":"));

                        message = new Message.Builder(jsonSet.getString("username"))
                                .addDate(timedate[0])
                                .addTime(timedate[1])
                                .addChatId(chatid)
                                .addMessage(jsonSet.getString("message"))
                                .build();

                        mHomeActivity.addMessage(chatid, message);
                    }
                }

            }



            //mHomeActivity.addMessage(chatid, new Message("", "", "")); //add the message data to the JsonMap

            return chatid;
        }catch (JSONException e){
            e.printStackTrace();
        }

        return -1;
    }

    private void handleProposeFriend(final String result){
        try {
            JSONObject root = new JSONObject(result);
            if(root.getBoolean("success")) {
                Log.d("Propose", "Propose friend successful");
            } else {
                Log.d("Propose", "Propose friend failed");
            }
        } catch (JSONException e)   {
            Log.e("ERROR!", "Propose friend failed");
        }
    }

    private void handleWeatherPost(String s){
        System.out.print("");
        try {
            JSONObject result = new JSONObject(s);
            mHomeActivity.updateJsonData("forecast", result);
        } catch (JSONException e){
            Log.e("JSON PARSE ERROR", s + System.lineSeparator() + e.getMessage());
        }
    }

    //TODO: DOES THIS NEED TO DO SOMETHING WHEN REQUEST APPROVED?
    private void handleConnectionAcceptReject(String s){
        try {
            JSONObject root = new JSONObject(s);
            if(root.getBoolean("success"))   {
                Log.d("ConnectIon", "Connection successfully ADDED/REMOVED.");
            } else {
                Log.d("ConnectIon", "Connection ADD/REMOVE failed.");
            }
        } catch (JSONException e)   {
            e.printStackTrace();
            Log.e("Error!", e.getMessage());
        }
    }



    public interface OnDataLoadedListener {
        void onWeatherUpdated(JSONObject result);

    }


    private void handleErrorsInTask(String result) {
        Log.e("ASYNCT_TASK_ERROR", result);
        endAsync();
    }




    // *********** METHODS CALLED THE FIRST TIME THE APP RUNS TO INITIALIZE DATA ********//
    private void initAll(){
        startAsync();
        //init weather
        JSONObject wMsg = JsonHelper.weather_JsonObject(mLat, mLon);
        String uri = UriHelper.WEATHER_BY_LAT_LONG();

        new SendPostAsyncTask.Builder(uri, wMsg)
                //.onPreExecute() //todo: wait fragment
                .onPostExecute(this::initForecast)
                .onCancelled(this::handleErrorsInTask)
                .build()
                .execute();
    }

    private void initForecast(String s){


        try {
            JSONObject result = new JSONObject(s);
            mHomeActivity.updateJsonData("forecast", result);

            //init contacts
            JSONObject msg = JsonHelper.connections_JsonObject(mCredentials.getID());
            if (null != msg) {
                String uri = UriHelper.CONNECTIONS_GETALL();


                new Uri.Builder()
                        .scheme("https")
                        .appendPath("tcss450group6-backend.herokuapp.com")
                        .appendPath("conn")
                        .appendPath("getall")
                        .build();

                new SendPostAsyncTask.Builder(uri, msg)
                        .onPostExecute(this::initConnections)
                        .onCancelled(this::handleErrorsInTask)
                        .build()
                        .execute();
            }
        } catch (JSONException e){
            Log.e("JSON PARSE ERROR", s + System.lineSeparator() + e.getMessage());
        }
    }

    private void initConnections(String s){

        try {
            JSONObject result = new JSONObject(s);
            mHomeActivity.updateJsonData("connections", result);
            JSONObject msg = JsonHelper.chats_JsonObject(mCredentials.getID());

            if(null != msg){
                Uri uri = new Uri.Builder()
                        .scheme("https")
                        .appendPath("tcss450group6-backend.herokuapp.com")
                        .appendPath("messaging")
                        .appendPath("getmy")
                        .build();

                new SendPostAsyncTask.Builder(uri.toString(), msg)
                        .onPostExecute(this::initChats)
                        .onCancelled(this::handleErrorsInTask)
                        .build()
                        .execute();
            }

        } catch (JSONException e){
            Log.e("JSON PARSE ERROR", s + System.lineSeparator() + e.getMessage());
        }
    }

    private void initChats(String s){
        try {
            JSONObject result = new JSONObject(s);
            mHomeActivity.updateJsonData("chats", result);

            endAsync();

            mHomeActivity.finishInit();

        } catch (JSONException e){
            Log.e("JSON PARSE ERROR", s + System.lineSeparator() + e.getMessage());
        }
    }

    /**
     * Display wait fragment if not already displayed
     */
    private void startAsync(){
        if(!mIsWaitFragActive) { //start wait frag if Async is not already active
            mHomeActivity.onWaitFragmentInteractionShow();
            mIsWaitFragActive = true;
        }
    }

    /**
     * Hide wait fragment if not already hidden
     */
    private void endAsync(){
        if(mIsWaitFragActive) { //hide if Async is active
            mHomeActivity.onWaitFragmentInteractionHide();
            mIsWaitFragActive = false;
        }
    }
}
