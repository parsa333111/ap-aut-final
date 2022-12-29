package com.example.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import share.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ServerChangeController implements Initializable {

    @FXML
    private Text warn;
    @FXML
    private TextField textField;

    private Client client;

    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        client = ConnectHandler.client;
        try {
            ConnectHandler.client.UpdClient();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            client.getOutputStream().writeObject(new MsgGetServer(client.getId(), client.getClient().getUsername(), ConnectHandler.server.getId(), null));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            Msg msg = (Msg) client.getObjectInputStream().readObject();
            if(msg instanceof MsgGetServer) {
                MsgGetServer msgGetServer = (MsgGetServer) msg;
                ConnectHandler.setServer(msgGetServer.getServer());
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void done(MouseEvent event) throws IOException {
        ConnectHandler.client.getOutputStream().writeObject(new MsgChangeServerName(ConnectHandler.client.getId(), ConnectHandler.server.getId(), textField.getText()));
        try {
            client.getOutputStream().writeObject(new MsgGetServer(client.getId(), client.getClient().getUsername(), ConnectHandler.server.getId(), null));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            Msg msg = (Msg) client.getObjectInputStream().readObject();
            if(msg instanceof MsgGetServer) {
                MsgGetServer msgGetServer = (MsgGetServer) msg;
                ConnectHandler.setServer(msgGetServer.getServer());
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Server.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}