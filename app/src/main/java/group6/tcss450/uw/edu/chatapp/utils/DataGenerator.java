package group6.tcss450.uw.edu.chatapp.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Tanner Brown
 * This class creates basic data for the app until real data can be recieved
 */
public final class DataGenerator {

    //TODO: Create same thing for chats and weather
    public static final Connections[] CONNECTIONS;
    public static final int CONNECTION_COUNT = 10;

    static {
        CONNECTIONS = new Connections[CONNECTION_COUNT];
        for(int i = 0; i < CONNECTIONS.length; i++){
            CONNECTIONS[i] = new Connections.Builder("Other User")
                    .addDate("01-20-2015")
                    .addTime("12:35")
                    .addLastMessage("Pretend last message")
                    .build();
        }
    }

    private DataGenerator(){}


}
