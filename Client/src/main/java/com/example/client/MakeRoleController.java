package com.example.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import share.Msg;
import share.MsgAddRole;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MakeRoleController implements Initializable {
    private Client connectedClient;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("gAV");
        connectedClient = ConnectHandler.client;
    }

    @FXML
    private CheckBox CreateChannel;

    @FXML
    private CheckBox CreateRoles;

    @FXML
    private CheckBox GetRole;

    @FXML
    private CheckBox HistoryOfChat;

    @FXML
    private CheckBox OpenChannels;

    @FXML
    private CheckBox PinMassage;

    @FXML
    private CheckBox RemoveChannel;

    @FXML
    private CheckBox RemoveUser;

    @FXML
    private CheckBox RenameServer;

    @FXML
    private CheckBox blockUserFromChannel;
    @FXML
    private TextField nameOfRole;
    @FXML
    public void createRole(MouseEvent event) throws IOException {
        if(!nameOfRole.getText().equals("")) {
            String role = "";
            if (OpenChannels.isSelected()) role = role + "1";
            else role = role + "0";
            if (CreateChannel.isSelected()) role = role + "1";
            else role = role + "0";
            if (RemoveChannel.isSelected()) role = role + "1";
            else role = role + "0";
            if (RemoveUser.isSelected()) role = role + "1";
            else role = role + "0";
            if (blockUserFromChannel.isSelected()) role = role + "1";
            else role = role + "0";
            if (RenameServer.isSelected()) role = role + "1";
            else role = role + "0";
            if (CreateRoles.isSelected()) role = role + "1";
            else role = role + "0";
            if (GetRole.isSelected()) role = role + "1";
            else role = role + "0";
            if (HistoryOfChat.isSelected()) role = role + "1";
            else role = role + "0";
            if (PinMassage.isSelected()) role = role + "1";
            else role = role + "0";
            Msg send = new MsgAddRole(connectedClient.getId(), ConnectHandler.getServer().getId(), nameOfRole.getText(), role);
            ConnectHandler.client.getOutputStream().writeObject(send);
            nameOfRole.setText("created");
        }
        else {
            nameOfRole.setText("Fill this box");
        }
    }

    @FXML
    public void back(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Server.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
