package com.example.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import share.ClientData;
import share.Msg;
import share.MsgClientGet;
import share.MsgSingInUser;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginAccountController implements Initializable {

    private Client connectClient;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connectClient = ConnectHandler.client;
        clientPasswordPrivate.setText("");
        clientPassword.setText("");
        clientPasswordPrivate.setVisible(true);
        clientPassword.setVisible(false);
    }


    @FXML
    private CheckBox checkBox;

    @FXML
    private TextField clientPassword;

    @FXML
    private PasswordField clientPasswordPrivate;

    @FXML
    private TextField clientUsername;

    @FXML
    private Text invalidMessage;

    @FXML
    void loginAccount(MouseEvent event) throws IOException, ClassNotFoundException {
        invalidMessage.setText("");
        String username = clientUsername.getText();
        String password;
        if (checkBox.isSelected()) password = clientPassword.getText();
        else password = clientPasswordPrivate.getText();
        System.out.println("## " + password);
        if (!username.matches("^[A-Za-z\\d]{6,}$")) {
            invalidMessage.setText("Invalid username");
        }
        else if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$")) {
            invalidMessage.setText("invalid password");
        }
        else {
            String id = connectClient.getId();
            ObjectOutputStream outputStream = connectClient.getOutputStream();
            outputStream.writeObject(new MsgSingInUser(id, username, password));
            ObjectInputStream inputStream = connectClient.getObjectInputStream();
            MsgClientGet msg = (MsgClientGet) inputStream.readObject();
            if(msg.getText().equals("InvalidUsername")){
                invalidMessage.setText("Invalid Username");
            }
            else if(msg.getText().equals("InvalidPassword")) {
                invalidMessage.setText("Invalid Password");
            }
            else {
                ConnectHandler.setClientData(msg.getClientData());
                ConnectHandler.client.HandleFileOutPut();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("loading.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
        }
    }

    @FXML
    void openCreateAccount(MouseEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateClient.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void showPass(MouseEvent event) {
        if(checkBox.isSelected()) {
            clientPassword.setText(clientPasswordPrivate.getText());
            clientPasswordPrivate.setVisible(false);
            clientPassword.setVisible(true);
        }
        else {
            clientPasswordPrivate.setText(clientPassword.getText());
            clientPassword.setVisible(false);
            clientPasswordPrivate.setVisible(true);
        }
    }

}
