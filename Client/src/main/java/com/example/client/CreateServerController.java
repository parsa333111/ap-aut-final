package com.example.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
import java.net.URL;
import java.util.ResourceBundle;

public class CreateServerController implements Initializable {
    private FileChooser fileChooser = new FileChooser();

    private String photoLocation = "";

    @FXML
    private TextField serverName;

    @FXML
    private Circle serverPhoto;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image image = new Image(ShowPictureHandler.makeStandardPath("addPicture.png"));
        serverPhoto.setFill(new ImagePattern(image));
        serverPhoto.setStroke(Color.WHITE);
    }

    @FXML
    public void changeAvatar(MouseEvent event) throws IOException {
        File file = fileChooser.showOpenDialog(new Stage());
        if(file.toPath() != null) {
            ShowPictureHandler.makeStandardPath(file.toPath().toString());
            Image image = new Image(ShowPictureHandler.avatarMakeStandardPath(file.toPath().toString()));
            serverPhoto.setFill(new ImagePattern(image));
            photoLocation = file.toPath().toString();
        }
    }

    @FXML
    public void exit(MouseEvent event) throws IOException, InterruptedException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FirstMenu.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void create(MouseEvent event) throws IOException, ClassNotFoundException, InterruptedException {
        String serverId = RandomToken.randomString(20);
        MsgCreateServer msgCreateServer = new MsgCreateServer(ConnectHandler.client.getId(), ConnectHandler.client.getClient().getUsername(), serverName.getText(), serverId);
        ConnectHandler.client.getOutputStream().writeObject(msgCreateServer);
        Thread.sleep(500);
        if(!photoLocation.equals(""))
            ConnectHandler.client.fileOutPut.sendFile("server", serverId, photoLocation);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FirstMenu.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}