package com.example.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import share.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GetRoleController implements Initializable {

    @FXML
    private Text warn;
    @FXML
    private TextField textField;

    @FXML
    private ListView<Text> listView;

    @FXML
    private TextField ChannelName;

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
        for(String role : ConnectHandler.server.getRolesName().keySet()) {
            Text textFriend = new Text(role);
            textFriend.setStyle("-fx-font: 15 arial;");;
            textFriend.setFill(Color.WHITE);
            listView.getItems().add(textFriend);
        }
    }

    @FXML
    void done(MouseEvent event) throws IOException {
        String chatId = "";
        String userId = "";
        for(String role : ConnectHandler.server.getRolesName().keySet()) {
            if(role.equals(ChannelName.getText())) {
                chatId = role;
            }
        }
        if(chatId.equals("")) {
            warn.setText("Channel Name Invalid");
            warn.setFill(Color.RED);
            return;
        }
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
        ConnectHandler.client.getOutputStream().writeObject(new MsgSetRole(ConnectHandler.client.getId(), ConnectHandler.server.getId(), chatId, userId));
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