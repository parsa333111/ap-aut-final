package com.example.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import share.Msg;
import share.MsgDeleteCallThread;
import share.MsgSetServerForCall;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class CallController implements Initializable {
    private Client connectedClient;
    Thread inputThread;
    Thread outputThread;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connectedClient = ConnectHandler.client;
        Msg send = new MsgSetServerForCall("", "", "");
        try {
            connectedClient.getOutputStream().writeObject(send);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Socket client = null;
        try {
            client = new Socket("127.0.0.1", 5454);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            inputThread = new Thread(new InputThread(client));
            outputThread = new Thread(new OutputThread(client));
            inputThread.start();
            outputThread.start();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @FXML
    private ListView<VBox> listViewOfProfilePic;
    @FXML
    void exitFromCall(MouseEvent event) throws IOException {
        inputThread.stop();
        outputThread.stop();
        Msg msg = new MsgDeleteCallThread("", "", "");
        connectedClient.getOutputStream().writeObject(msg);
    }
}
