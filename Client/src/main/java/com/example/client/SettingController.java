package com.example.client;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

import javax.print.attribute.standard.DialogTypeSelection;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingController implements Initializable {
    private Boolean doing;
    private FileChooser fileChooser = new FileChooser();
    private Client connectedClient;
    @FXML
    private Text username2;
    @FXML
    private Circle userProfile;
    @FXML
    public void changeAvatar(MouseEvent event) throws IOException {
        File file = fileChooser.showOpenDialog(new Stage());
        if(file.toPath() != null) {
            ShowPictureHandler.makeStandardPath(file.toPath().toString());
            Image image = new Image(ShowPictureHandler.avatarMakeStandardPath(file.toPath().toString()));
            userProfile.setFill(new ImagePattern(image));
            connectedClient.fileOutPut.sendFile("photo", connectedClient.getClient().getUsername() + ":new photo", file.toPath().toString());
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ConnectHandler.client.UpdClient();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        doing = true;
        settingVbox.setVisible(false);
        connectedClient = ConnectHandler.client;
        changeExceptPass.setVisible(false);
        username.setText(connectedClient.getClient().getUsername());
        username2.setText(connectedClient.getClient().getUsername());
        phoneNumber.setText(connectedClient.getClient().getPhoneNumber());
        email.setText(connectedClient.getClient().getEmail());

        if(connectedClient.getClient().getPhoto() == null || connectedClient.getClient().getPhoto().equals("")) {
            Image image = new Image(ShowPictureHandler.makeStandardPath("discordpic.png"));
            userProfile.setFill(new ImagePattern(image));
        }
        else {
            try {
                connectedClient.fileOutPut.getFile(connectedClient.getClient().getPhoto());
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            Image image = new Image(ShowPictureHandler.FileMakeStandardPath(connectedClient.getClient().getUsername() + "#" + connectedClient.getClient().getPhoto()));
            userProfile.setFill(new ImagePattern(image));
        }
    }

    @FXML
    private Text email;

    @FXML
    private Text phoneNumber;

    @FXML
    private VBox settingVbox;

    @FXML
    private Text username;

    @FXML
    private TextField currentPassword;

    @FXML
    private TextField newPassword;

    @FXML
    private TextField confirmPassword;

    @FXML
    public void LogOut(MouseEvent event) throws IOException, InterruptedException {
        if(!doing) return;
        ConnectHandler.client.FileHandleStop();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        ConnectHandler.logOut();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateClient.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void changePassword(MouseEvent event) {
        if(!doing) return;
        doing = false;
        changePassword.setVisible(true);
    }

    @FXML
    public void editEmail(MouseEvent event) {
        if(!doing) return;
        doing = false;
        type.setText("change email");
        enterParameter.setText("Enter your current password and new email");
        firstParameter.setText("        email");
        changeExceptPass.setVisible(true);
    }

    @FXML
    public void editPhoneNumber(MouseEvent event) {
        if(!doing) return;
        doing = false;
        type.setText("change phone number");
        enterParameter.setText("Enter your current password and new phone number");
        firstParameter.setText("        phone number");
        changeExceptPass.setVisible(true);
        System.out.println("G");
    }

    @FXML
    public void editUserName(MouseEvent event) {
        if(!doing) return;
        doing = false;
        type.setText("change username");
        enterParameter.setText("Enter your current password and new username");
        firstParameter.setText("        username");
        changeExceptPass.setVisible(true);
    }

    @FXML
    public void exit(MouseEvent event) throws IOException, InterruptedException {
        if(!doing) return;
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FirstMenu.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void openMyAccount(MouseEvent event) {
        settingVbox.setVisible(true);
    }
    @FXML
    private TextField clientFirstParameter;
    @FXML
    private TextField clientPassword;
    @FXML
    private Text enterParameter;
    @FXML
    private Text firstParameter;
    @FXML
    private Text type;
    @FXML
    private VBox changeExceptPass;
    @FXML
    private Text warnText;
    @FXML
    public void cancel(MouseEvent event) {
        changeExceptPass.setVisible(false);
        changePassword.setVisible(false);
        doing = true;
    }
    public void done(MouseEvent event) throws IOException {
        System.out.println("FUck this world");
        String userInput = clientFirstParameter.getText();
        String pass = clientPassword.getText();
        //check if user pass word in valid and new input is valid too
        if(checkValid(type.getText(), userInput, pass)) {
            doing = true;
            changeExceptPass.setVisible(false);
            if(type.getText().equals("change username")) {
                username.setText(userInput);
                username2.setText(userInput);
            }
            if(type.getText().equals("change email")) {
                connectedClient.getClient().setEmail(userInput);
                connectedClient.getOutputStream().writeObject(new MsgChangeEmail(connectedClient.getId(), connectedClient.getClient().getUsername(), userInput));
                email.setText(userInput);
            }
            if(type.getText().equals("change phone number")) {
                connectedClient.getClient().setPhoneNumber(userInput);
                connectedClient.getOutputStream().writeObject(new MsgChangePhoneNumber(connectedClient.getId(), connectedClient.getClient().getUsername(), userInput));
                phoneNumber.setText(userInput);
            }
        }
        else {
            if(!pass.equals(connectedClient.getClient().getPassword()))
                warnText.setText("Invalid Password");
            else if(type.getText().equals("change username") && !userInput.matches("^[A-Za-z\\d]{6,}$")) {
                warnText.setText("Invalid Username Form");
            }
            else if(type.getText().equals("change email") && !userInput.matches("^[a-zA-Z\\d_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z\\d.-]+$")) {
                warnText.setText("Invalid Email Form");
            }
            else if(type.getText().equals("change phone number") && !userInput.matches("^(\\+98|0)?9\\d{9}$")) {
                warnText.setText("Invalid PhoneNumber Form");
            }
        }
        clientFirstParameter.setText("");
        clientPassword.setText("");
    }

    public boolean checkValid(String type, String userInput, String password) {
        System.out.println(type);
        if(!password.equals(connectedClient.getClient().getPassword()))
            return false;
        if(type.equals("change username") && !userInput.matches("^[A-Za-z\\d]{6,}$")) {
            return false;
        }
        if(type.equals("change email") && !userInput.matches("^[a-zA-Z\\d_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z\\d.-]+$")) {
            return false;
        }
        if(type.equals("change phone number") && !userInput.matches("^(\\+98|0)?9\\d{9}$")) {
            return false;
        }
        return true;
    }


    @FXML
    private TextField newAgainPasswordForChangePass;
    @FXML
    private TextField newPasswordForChangePass;
    @FXML
    private TextField currentPasswordForChangePass;
    @FXML
    private Text warnForPassword;
    @FXML
    private VBox changePassword;
    @FXML
    void donePassword(MouseEvent event) throws IOException {
        if(currentPasswordForChangePass.getText().equals(connectedClient.getClient().getPassword())) {
            if(newAgainPasswordForChangePass.getText().equals(newPasswordForChangePass.getText())){
                String password = newPasswordForChangePass.getText();
                if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$")) {
                    warnForPassword.setText("weak password");
                }
                else {
                    changePassword.setVisible(false);
                    doing = true;
                    connectedClient.getClient().setPassword(password);
                    connectedClient.getOutputStream().writeObject(new MsgChangePassword(connectedClient.getId(), connectedClient.getClient().getUsername(), password));
                }
            }
            else {
                warnForPassword.setText("Confirm Pass Is not Same");
            }
        }
        else {
            warnForPassword.setText("Current Password Is not correct");
        }
    }
    @FXML
    public void changeStatus(MouseEvent mouseEvent) throws InterruptedException {
        String[] state =  {"Online", "Idle ", "Do Not Disturb", "Invisible"};
        ChoiceDialog d = new ChoiceDialog(null, state);
        EventHandler<DialogEvent> event = new EventHandler<DialogEvent>()
        {
            @Override
            public void handle(DialogEvent dialogEvent) {
                System.out.println();
                String newStatus = d.getSelectedItem().toString();
                connectedClient.getClient().setStatus(newStatus);
                try {
                    connectedClient.getOutputStream().writeObject(new MsgSetStatus(connectedClient.getId(), connectedClient.getClient().getUsername(), connectedClient.getClient().getStatus()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        d.setOnCloseRequest(event);
        d.show();
    }
}