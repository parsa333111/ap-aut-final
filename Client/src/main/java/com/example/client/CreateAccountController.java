package com.example.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import share.ClientData;
import share.Msg;
import share.MsgAddUser;
import share.MsgCheckUser;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class CreateAccountController implements Initializable {
    private Client connectClient;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connectClient = ConnectHandler.client;
        ppassword.setText("");
        clientPassword.setText("");
        ppassword.setVisible(true);
        clientPassword.setVisible(false);
    }

    @FXML
    private PasswordField ppassword;

    @FXML
    private TextField clientEmail;

    @FXML
    private TextField clientPassword;

    @FXML
    private TextField clientUsername;

    @FXML
    private Button continueButton;

    @FXML
    private Text invalidMessage;

    @FXML
    private Text login;

    @FXML
    private Text privacy;

    @FXML
    private RadioButton termAccept;

    @FXML
    public void createAccount(MouseEvent event) throws IOException, ClassNotFoundException {
        invalidMessage.setText("");
        String username = clientUsername.getText();
        String password;
        if (checkBox.isSelected()) password = clientPassword.getText();
        else password = ppassword.getText();
        System.out.println("## " + password);
        String email = clientEmail.getText();
        if (!username.matches("^[A-Za-z\\d]{6,}$")) {
            invalidMessage.setText("Invalid username");
        }
        else if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$")) {
            invalidMessage.setText("weak password");
        }
        else if(!email.matches("^[a-zA-Z\\d_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z\\d.-]+$")) {
            invalidMessage.setText("invalid email");
        }
        else if(!termAccept.isSelected()) {
            invalidMessage.setText("Accept our term");
        }
        else {
            String id = connectClient.getId();
            ObjectOutputStream outputStream = connectClient.getOutputStream();
            outputStream.writeObject(new MsgCheckUser(id, "", username));
            ObjectInputStream inputStream = connectClient.getObjectInputStream();
            Msg msg = (Msg) inputStream.readObject();
            if(msg.getText().equals("False")) {
                System.out.println("!@# " + msg.toString());
                MsgAddUser send = new MsgAddUser(id, "", "", new ClientData(username, password, email, ""));
                outputStream.writeObject(send);
                invalidMessage.setText("Successfully created");
            }
            else {
                invalidMessage.setText("username already exist");
            }
            clientEmail.setText("");
            clientUsername.setText("");
            clientPassword.setText("");
        }
    }

    @FXML
    public void openPrivacy(MouseEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TermAndPrivacy.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private CheckBox checkBox;

    @FXML
    public void showPass() {
        if(checkBox.isSelected()) {
            clientPassword.setText(ppassword.getText());
            ppassword.setVisible(false);
            clientPassword.setVisible(true);
        }
        else {
            ppassword.setText(clientPassword.getText());
            clientPassword.setVisible(false);
            ppassword.setVisible(true);
        }
    }
    @FXML
    public void openLoginMenu(MouseEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginClient.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
