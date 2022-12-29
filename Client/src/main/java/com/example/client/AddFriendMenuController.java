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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Stage;
import share.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddFriendMenuController implements Initializable {
    private Client connectedClient;

    @FXML
    private TextField textFieldUserName;

    @FXML
    private Text warnText;

    @FXML
    private ListView<StackPane> listOfServers;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connectedClient = ConnectHandler.client;
        failvBox.setVisible(false);
        doing = true;
        connectedClient = ConnectHandler.client;
        StackPane stackAdd = new StackPane();
        final Circle circleAdd = createCircle();
        circleAdd.setFill(new ImagePattern(new Image(ShowPictureHandler.makeStandardPath("serverAdd.png"))));
        stackAdd.setOnMouseClicked(mouseEvent -> {
            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateServer.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            return;
        });
        stackAdd.getChildren().add(circleAdd);
        listOfServers.getItems().add(stackAdd);
        for (String server : connectedClient.getClient().getServers()) {
            final Circle circle = createCircle();
            try {
                connectedClient.getOutputStream().writeObject(new MsgGetServer(connectedClient.getId(), connectedClient.getClient().getUsername(), server, null));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            MsgGetServer msgGetServer = null;
            try {
                msgGetServer = (MsgGetServer) connectedClient.getObjectInputStream().readObject();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            final Text text = createText(msgGetServer.getServer().getName());
            StackPane stack = new StackPane();
            if(msgGetServer.getServer().getPhoto() == null || msgGetServer.getServer().getPhoto().equals("")) {
                stack.getChildren().addAll(circle, text);
            }
            else {
                try {
                    System.out.println(msgGetServer.getServer().getPhoto());
                    ConnectHandler.client.fileOutPut.getFile(msgGetServer.getServer().getPhoto());
                    System.out.println(ShowPictureHandler.FileMakeStandardPath(ConnectHandler.client.getClient().getUsername() + "#" + msgGetServer.getServer().getPhoto()));
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                Image image = new Image(ShowPictureHandler.FileMakeStandardPath(ConnectHandler.client.getClient().getUsername() + "#" + msgGetServer.getServer().getPhoto()));
                circle.setFill(new ImagePattern(image));
                stack.getChildren().add(circle);
            }
            listOfServers.getItems().add(listOfServers.getItems().size() - 1, stack);
        }
        for(String friend : connectedClient.getClient().getFriends()) {
            MsgGetChannel msg;
            try {
                connectedClient.getOutputStream().writeObject(new MsgGetPrivateChat(connectedClient.getId(), connectedClient.getClient().getUsername(), friend));
                msg = (MsgGetChannel)connectedClient.getObjectInputStream().readObject();

            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            if(msg.getChatChannel().getMassages().size() == 0)
                continue;
            HBox friendInfo = new HBox();
            ImageView userProfile = new ImageView();
            Circle image;
            try {
                image = GetClientPhoto.GetImage(friend);
            } catch (IOException | ClassNotFoundException e) {
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

            friendInfo.getChildren().add(new Text("  \n"));
            Text textFriend = new Text(friend);
            textFriend.setStyle("-fx-font: 15 arial;");;
            textFriend.setFill(Color.WHITE);
            textFriend.setOnMouseClicked(mouseEvent -> {
                MsgGetChannel msgGetChannel;
                try {
                    connectedClient.getOutputStream().writeObject(new MsgGetPrivateChat(connectedClient.getId(), connectedClient.getClient().getUsername(), friend));
                    msgGetChannel = (MsgGetChannel)connectedClient.getObjectInputStream().readObject();
                    connectedClient.getOutputStream().writeObject(new MsgCurrentChat(connectedClient.getId(), connectedClient.getClient().getUsername(), msgGetChannel.getChatChannel().getId(), msgGetChannel.getChatChannel()));
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                ConnectHandler.setCurrentChannel(msgGetChannel.getChatChannel());
                Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Chat.fxml"));
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
            friendInfo.getChildren().add(textFriend);
            listOfDirectMessage.getItems().add(friendInfo);
        }
    }
    @FXML
    private ListView<HBox> listOfDirectMessage;

    private Circle createCircle() {
        final Circle circle = new Circle(20);
        circle.setFill(Color.rgb(54,57,62));
        circle.relocate(0, 0);
        return circle;
    }

    private Text createText(String server) {
        Text text = new Text(server);
        text.setStyle("color: white; -fx-font-size: 12px; -fx-font-weight: bold;");
        text.setBoundsType(TextBoundsType.VISUAL);
        text.setFill(Color.WHITE);
//        centerText(text);

        return text;
    }

    public void addFriend(MouseEvent event) throws IOException, ClassNotFoundException {
        if(!doing) return;
        String user = textFieldUserName.getText();
        if (!user.matches("^[A-Za-z\\d]{6,}$")) {
            warnText.setText("Invalid Username form");
            textFieldUserName.setText("");
            return;
        }
        if (connectedClient.getClient().getUsername().equals(user)) {
            warnText.setText("Absolutely You are friend with yourself :)");
            textFieldUserName.setText("");
            return;
        }
        MsgRequestFriend send = new MsgRequestFriend(connectedClient.getId(), connectedClient.getClient().getUsername(), user);
        connectedClient.getOutputStream().writeObject(send);
        Msg msg = (Msg) connectedClient.getObjectInputStream().readObject();
        if (msg.getText().equals("UsernameNotFound")) {
            failvBox.setVisible(true);
            doing = false;
            warnText.setFill(Color.RED);
            warnText.setText("Username Not Found");
            textFieldUserName.setStyle("-fx-border-color: red; -fx-background-color:  #202225;-fx-text-fill: white");
        } else if(msg.getText().equals("AddedRequest")) {
            warnText.setFill(Color.RED);
            warnText.setText("This User Added to Requests Before");
        }
        else if (msg.getText().equals("AddBefore")) {
            warnText.setFill(Color.RED);
            warnText.setText("This User Added to Friend Before");
        } else if (msg.getText().equals("BlockUser")) {
            warnText.setFill(Color.RED);
            warnText.setText("This User Block You!");
        } else if (msg.getText().equals("RequestSend")) {
            warnText.setFill(Color.GREEN);
            warnText.setText("Succeed your request Send to " + user);
            textFieldUserName.setStyle("-fx-border-color: green; -fx-background-color:  #202225;-fx-text-fill: white");
        } else {
            warnText.setFill(Color.RED);
            warnText.setText("ServerError");
        }
        textFieldUserName.setText("");
    }

    @FXML
    private Button addFriend;

    @FXML
    private Button all;

    @FXML
    private Button pending;

    @FXML
    private Button online;

    @FXML
    private Button block;

    @FXML
    void openFXML(MouseEvent event) throws IOException {
        String stg = "";
        if((Button)event.getSource() == addFriend) {
            stg = "addFriend.fxml";
        }
        else if((Button)event.getSource() == all) {
            stg = "ShowAllFriend.fxml";
        }
        else if((Button)event.getSource() == pending) {
            stg = "PendingListMenu.fxml";
        }
        else if((Button)event.getSource() == online) {
            stg = "ShowOnlineFriend.fxml";
        }
        else if((Button)event.getSource() == block) {
            stg = "ShowBlockList.fxml";
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(stg));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    private boolean doing;
    @FXML
    private VBox failvBox;
    @FXML
    public void handleErrorMassage(MouseEvent event) {
        failvBox.setVisible(false);
        doing = true;
    }
}
