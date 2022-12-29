package server;

import share.Msg;

import java.util.Iterator;

public class Discord {
    public static DataBase dataBase;

    /**
     * send a massage to a client
     * @param sender of massage
     * @param msg to send
     */
    public static void msgSendToClient(String sender, Msg msg) {}

    /**
     * check that a user is online or not
     * @param clientId ofuser
     * @return true if online else false
     */
    public static boolean IsOnline(String clientId) {
        return true;
    }

    /**
     * send to all in chat a massage
     * @param sender of sender
     * @param msg to send
     */
    public static void sendToAllInChat(String sender, Msg msg) {
    }
}
