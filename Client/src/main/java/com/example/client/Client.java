package com.example.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import share.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    private final String ip;
    private final int port;
    private final String id;
    private ClientData client;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream outputStream;

    private Socket socket;

    private ClientData clientData;

    public FileOutPut fileOutPut;

    public NotificationHandler notification;

    /**
     * constructor
     * @param ip of client
     * @param port of client
     * @param id of client
     */
    public Client(String ip, int port, String id) {
        this.ip = ip;
        this.port = port;
        this.id = id;
        this.client = new ClientData("", "", "", "");
    }

    public void UpdClient() throws IOException, ClassNotFoundException {
        outputStream.writeObject(new MsgClientUpdate(id, client.getUsername(), client.getPassword()));
        MsgClientGet upd = (MsgClientGet) objectInputStream.readObject();
        client = upd.getClientData();
    }

    public void HandleFileOutPut() {
        notification = new NotificationHandler("127.0.0.1", 2001, RandomToken.randomString(20), client.getUsername());
        notification.start();
        fileOutPut = new FileOutPut("127.0.0.1", 2002, RandomToken.randomString(20), client.getUsername());
        fileOutPut.start();
    }

    public void FileHandleStop() throws IOException, InterruptedException {
        fileOutPut.stop();
        notification.stop();
    }

    public void start(Stage stage){
        try {
            System.out.println(System.getProperty("user.dir").toString());
            Socket socket = new Socket(ip, port);
            this.socket = socket;
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            ConnectHandler.client = this;
            MsgJoin msg = new MsgJoin(id, client.getUsername(),"");
            outputStream.writeObject(msg);
            System.out.println(msg);
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("CreateClient.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Discord");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String getId() {
        return id;
    }

    public ClientData getClient() {
        return client;
    }

    public ObjectInputStream getObjectInputStream() {
        return objectInputStream;
    }

    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }

    public Socket getSocket() {
        return socket;
    }

    public ClientData getClientData() {
        return clientData;
    }

    public void setClient(ClientData client) {
        this.client = client;
    }
}
