package group6.tcss450.uw.edu.chatapp.utils;

import java.util.ArrayList;

import group6.tcss450.uw.edu.chatapp.messages.Message;
import group6.tcss450.uw.edu.chatapp.messages.OpenMessage;

/**
 * Tanner Brown
 * This class creates basic data for the app until real data can be recieved
 */
public final class DataGenerator {

    //TODO: Create same thing for chats and weather
    public static final OpenMessage[] OPEN_MESSAGES;
    public static final int OPEN_MESSAGE_COUNT = 10;
    public static final ArrayList<Message> MESSAGES;
    public static final int MESSAGE_COUNT = 20;
    public static final ArrayList<Connection> CONNECTIONS;
    public static final int CONNECTION_COUNT = 10;

    static {
        OPEN_MESSAGES = new OpenMessage[OPEN_MESSAGE_COUNT];
        for(int i = 0; i < OPEN_MESSAGES.length; i++){
            OPEN_MESSAGES[i] = new OpenMessage.Builder("Other User")
                    .addDate("01-20-2015")
                    .addTime("12:35")
                    .addLastMessage("Pretend last message")
                    .build();
        }
    }

    static {
        MESSAGES = new ArrayList<Message>();
        for(int i = 0; i < MESSAGE_COUNT; i++)    {
            String tempMessage = "This is a sample message constructed by the DataGenerator. [" + i + "]";
            MESSAGES.add(new Message.Builder("UserSendingMessage")
                    .addMessage(tempMessage)
                    .build());
        }
    }

    static  {
        CONNECTIONS = new ArrayList<Connection>();
        for(int i = 0; i < CONNECTION_COUNT; i++) {
            String username = "user" + i;
            String email = "email" + i + "@testing.com";
            CONNECTIONS.add(new Connection.Builder(username, email).build());
        }
    }

    private DataGenerator(){}


}
