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

public class InviteFriendHandler implements Initializable {

    @FXML
    private Text warn;
    @FXML
    private TextField textField;

    private Client client;

    private Stage stage;

    @FXML
    private ListView<HBox> listOfFriends;

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
        client = ConnectHandler.client;
        for(String friend : client.getClient().getFriends()) {
            if(ConnectHandler.getServer().getUsers().contains(friend))
                continue;
            System.out.println(friend);
            HBox friendInfo = new HBox();
            ImageView userProfile = new ImageView();
            userProfile.setFitHeight(30);
            userProfile.setFitWidth(30);
            Circle image;
            try {
                image = GetClientPhoto.GetImage(friend);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            image.setOnMouseClicked(mouseEvent -> {
                ShowProflieHandler.usernameOfProfile = friend;
                Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ShowProfile.fxml"));
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
            friendInfo.getChildren().add(image);
            friendInfo.getChildren().add(new Text("  "));
            Text textFriend = new Text(friend);
            textFriend.setStyle("-fx-font: 15 arial;");;
            textFriend.setFill(Color.WHITE);
            friendInfo.getChildren().add(textFriend);
            String path = ShowPictureHandler.getPath("invite.png");
            File file = new File(path);
            String localUrl;
            try {
                localUrl = file.toURI().toURL().toString();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            ImageView sms = new ImageView(localUrl);
            sms.setId(RandomToken.randomString(21));
            sms.setOnMouseClicked(mouseEvent -> {
                stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                try {
                    sendRequest(friend);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            sms.setFitHeight(30);
            sms.setFitWidth(30);
            friendInfo.getChildren().add(sms);
            listOfFriends.getItems().add(friendInfo);
        }
    }

    @FXML
    void requestFriend(MouseEvent event) throws IOException {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        String friend = textField.getText();
        if(!client.getClient().getFriends().contains(friend)) {
            warn.setText("Friend Not Found");
            warn.setFill(Color.RED);
        }
        else {
            warn.setText("Successfully Add");
            warn.setFill(Color.GREEN);
            sendRequest(textField.getText());
        }
    }

    @FXML
    void done(MouseEvent event) throws IOException {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Server.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    void sendRequest(String friend) throws IOException {
        client.getOutputStream().writeObject(new MsgSendRequest(client.getId(), client.getClient().getUsername(), ConnectHandler.server.getId(), friend));
        loadAgain();
    }

    public void loadAgain() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("InviteFriend.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}