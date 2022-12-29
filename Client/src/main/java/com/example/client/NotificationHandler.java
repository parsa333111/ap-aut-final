package com.example.client;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import share.Msg;
import share.MsgJoin;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import static java.lang.System.exit;

public class NotificationHandler {
    private final String ip;
    private final int port;
    private final String id;
    public ObjectInputStream objectInputStream;
    public ObjectOutputStream outputStream;

    private String client;

    private volatile boolean shutdown;

    private Socket socket;
    /**
     * constructor
     * @param ip of client which we want send notification
     * @param port which notification listen on
     * @param id of client which we want send notification
     * @param client of client which we want send notification
     */
    public NotificationHandler(String ip, int port, String id, String client) {
        this.ip = ip;
        this.port = port;
        this.id = id;
        this.client = client;
    }

    /**
     * make connection in this method between client and server
     */
    public void start() {
        try {
            socket = new Socket(ip, port);
            //ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());

            //outputStream.writeObject(new Msg(name,"","join"));
            //Scanner scanner = new Scanner(System.in);
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            MsgJoin msgJoin = new MsgJoin(id, client,"");
            outputStream.writeObject(msgJoin);
            Listener listener = new Listener(objectInputStream);
            Thread listenerThread = new Thread(listener);
            listenerThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * listener method for notification
     */
    private class Listener implements Runnable{
        ObjectInputStream objInputStream;

        public Listener(ObjectInputStream objInputStream) {
            this.objInputStream = objInputStream;
        }

        @Override
        public void run() {
            while (!shutdown) {
                try {
                    Msg msg = (Msg) objInputStream.readObject();
                    ConnectHandler.notifSender = msg.getOwner();
                    ConnectHandler.notifText = msg.getText();
                    Platform.runLater(() -> {
                        Stage stage = new Stage();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Notification.fxml"));
                        Parent root = null;
                        try {
                            root = loader.load();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                    });
                    Thread.sleep(500);
                } catch (IOException | ClassNotFoundException | InterruptedException e) {
                    //e.printStackTrace();
                }
            }
        }

    }
    /**
     * stop and close socket of notification
     * @throws IOException may throw that
     * @throws InterruptedException may throw that
     */
    public void stop() throws IOException, InterruptedException {
        shutdown = true;
        socket.close();
    }
}
