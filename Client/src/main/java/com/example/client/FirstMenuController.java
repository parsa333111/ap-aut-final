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

public class FirstMenuController implements Initializable {

    private Client connectedClient;

    @FXML
    private ListView<StackPane> listOfServers;

    @FXML
    private ListView<HBox> listOfDirectMessage;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        findFriendVBox.setVisible(false);
        doing = true;
        try {
            ConnectHandler.client.UpdClient();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
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
            stack.setOnMouseClicked(mouseEvent -> {
                MsgGetServer msg;
                try {
                    connectedClient.getOutputStream().writeObject(new MsgGetServer(connectedClient.getId(), connectedClient.getClient().getUsername(), server, null));
                    msg = (MsgGetServer) connectedClient.getObjectInputStream().readObject();
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                ConnectHandler.setServer(msg.getServer());
                Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
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
            });
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
    private ImageView setting;
    @FXML
    void openFXML(MouseEvent event) throws IOException {
        if(!doing) return;
        String stg = "";
        if(event.getSource() instanceof Button) {
            if ((Button) event.getSource() == addFriend) {
                stg = "addFriend.fxml";
            } else if ((Button) event.getSource() == all) {
                stg = "ShowAllFriend.fxml";
            } else if ((Button) event.getSource() == pending) {
                stg = "PendingListMenu.fxml";
            } else if ((Button) event.getSource() == online) {
                stg = "ShowOnlineFriend.fxml";
            } else if ((Button) event.getSource() == block) {
                stg = "ShowBlockList.fxml";
            }
        }
        else if((ImageView)event.getSource() == setting) {
            stg = "Setting.fxml";
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(stg));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private VBox findFriendVBox;

    @FXML
    private TextField inputForFindFriend;
    private boolean doing;
    @FXML
    public void findFriend(MouseEvent event) {
        warnForFindFriend.setText("");
        inputForFindFriend.setText("");
        findFriendVBox.setVisible(true);
        doing = false;
    }
    @FXML
    private Text warnForFindFriend;
    @FXML
    public void findFriendAndGo(MouseEvent event) {
        String username = inputForFindFriend.getText();
        if(connectedClient.getClient().getFriends().contains(username)) {
            MsgGetChannel msg;
            try {
                connectedClient.getOutputStream().writeObject(new MsgGetPrivateChat(connectedClient.getId(), connectedClient.getClient().getUsername(), username));
                msg = (MsgGetChannel)connectedClient.getObjectInputStream().readObject();
                connectedClient.getOutputStream().writeObject(new MsgCurrentChat(connectedClient.getId(), connectedClient.getClient().getUsername(), msg.getChatChannel().getId(), msg.getChatChannel()));
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            ConnectHandler.setCurrentChannel(msg.getChatChannel());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
            return;
        }
        else {
            warnForFindFriend.setText("You aren't friend with this username");
        }
    }
    @FXML
    public void back(MouseEvent event) {
        doing = true;
        findFriendVBox.setVisible(false);
    }
}
