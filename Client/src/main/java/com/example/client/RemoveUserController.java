package com.example.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import share.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class RemoveUserController implements Initializable {

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
        String userId = "";
        for(String user  : ConnectHandler.server.getUsers()) {
            if(user.equals(textField.getText())) {
                userId = user;
            }
        }
        if(userId.equals("")) {
            warn.setText("UserName Name Invalid");
            warn.setFill(Color.RED);
            return;
        }
        ConnectHandler.client.getOutputStream().writeObject(new MsgRemoveUserFromServer(ConnectHandler.client.getId(), ConnectHandler.server.getId(), userId));
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