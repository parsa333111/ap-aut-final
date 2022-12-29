package com.example.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import share.Msg;
import share.MsgBlockUser;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ShowProfileController implements Initializable {
    private Client connectedClient;
    private String usernameOfProfile;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ConnectHandler.client.UpdClient();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        connectedClient = ConnectHandler.client;
        usernameOfProfile = ShowProflieHandler.usernameOfProfile;
        blockVbox.setVisible(false);
        usernameOfProfileText.setText(usernameOfProfile);
        if(connectedClient.getClient().getPhoto() == null || connectedClient.getClient().getPhoto().equals("")) {
            Image image = new Image(ShowPictureHandler.makeStandardPath("discordpic.png"));
            profilePic.setFill(new ImagePattern(image));
        }
        else {
            try {
                connectedClient.fileOutPut.getFile(connectedClient.getClient().getPhoto());
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            Image image = new Image(ShowPictureHandler.FileMakeStandardPath(connectedClient.getClient().getUsername() + "#" + connectedClient.getClient().getPhoto()));
            profilePic.setFill(new ImagePattern(image));
        }
    }

    @FXML
    private VBox blockVbox;
    @FXML
    private Circle profilePic;
    @FXML
    private Text usernameOfProfileText;
    @FXML
    public void back(MouseEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FirstMenu.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void blockUser(MouseEvent event) throws IOException, ClassNotFoundException {
        MsgBlockUser blockUser = new MsgBlockUser(connectedClient.getId(), connectedClient.getClient().getUsername(), usernameOfProfile);
        connectedClient.getOutputStream().writeObject(blockUser);
        Msg msg = (Msg) connectedClient.getObjectInputStream().readObject();
        if(msg.getText().equals("SuccessfullyAddBlockList")) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FirstMenu.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    boolean canSee;
    @FXML
    public void openMoreOption(MouseEvent event) {
        if(!canSee) {
            blockVbox.setVisible(true);
            canSee = true;
        } else {
            blockVbox.setVisible(false);
            canSee = false;
        }
    }
}
