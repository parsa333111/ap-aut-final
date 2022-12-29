package com.example.client;

import share.Msg;
import share.MsgGetFile;
import share.MsgJoin;
import share.MsgSendFile;

import java.io.*;
import java.net.Socket;

public class FileOutPut {
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
     * @param ip for create connection between client and server
     * @param port  for create connection between client and server
     * @param id of client
     * @param client username of client
     */
    public FileOutPut(String ip, int port, String id, String client) {
        this.ip = ip;
        this.port = port;
        this.id = id;
        this.client = client;
    }

    /**
     * create connection between server and client
     */
    public void start() {
        try {
            socket = new Socket(ip, port);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            outputStream.writeObject(new MsgJoin(id, client,""));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * send file between client and server
     * @param chatId is id of chatChannel
     * @param name of file
     * @param location of file
     * @throws IOException may throw that
     */
    public void sendFile(String chatId, String name, String location) throws IOException {
        File myFile = new File(location);
        FileInputStream fis = new FileInputStream(myFile);
        BufferedInputStream bis = new BufferedInputStream(fis);
        MsgSendFile msgSendFile = new MsgSendFile(id, client, name, chatId, bis.readAllBytes());
        outputStream.writeObject(msgSendFile);
        bis.close();
    }

    /**
     *
     * @param location of file which receive from server to save
     * @throws IOException may throw that
     * @throws ClassNotFoundException may throw that
     */
    public void getFile(String location) throws IOException, ClassNotFoundException {
        outputStream.writeObject(new MsgGetFile(id, client, location));

        OutputStream os = new FileOutputStream(ShowPictureHandler.getPathFile(client + "#" + location));
        Msg msg = (Msg)objectInputStream.readObject();
        if(msg instanceof MsgGetFile) {
            MsgGetFile msgGetFile = (MsgGetFile) msg;
            byte[] byteArray = msgGetFile.getBytes();
            os.write(byteArray, 0, byteArray.length);
            os.close();
        }
        else {
            System.out.println("Server Error");
        }
    }

    /**
     * close socket
     * @throws IOException may throw that
     * @throws InterruptedException may throw that
     */
    public void stop() throws IOException, InterruptedException {
        socket.close();
    }
}
