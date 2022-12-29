package com.example.client;

import javafx.scene.media.MediaPlayer;
import share.ChatChannel;
import share.ClientData;
import share.Server;

import java.io.IOException;

public class ConnectHandler {
    public static Client client;
    public static MediaPlayer mediaPlayer;
    public static ChatChannel currentChannel;

    public static Server server;

    public static String notifSender;

    public static String notifText;
    public static void setClientData(ClientData clientData) {
        client.setClient(clientData);
    }
    public static void updateClient() throws IOException, ClassNotFoundException {
        client.UpdClient();
    }
    public static void setCurrentChannel(ChatChannel currentChannel) {
        ConnectHandler.currentChannel = currentChannel;
    }

    public static void logOut() {
        client.setClient(new ClientData("","","",""));
    }

    public static Server getServer() {
        return server;
    }

    public static void setServer(Server server) {
        ConnectHandler.server = server;
    }
}
