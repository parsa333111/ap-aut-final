package com.example.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Stage;
import share.MsgCurrentChat;
import share.MsgGetChannel;
import share.MsgGetPrivateChat;
import share.MsgGetServer;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class showAllFriendController implements Initializable {

    private Client connectedClient;
    @FXML
    private ListView<HBox> listOfPendingFriend;

    @FXML
    private ListView<StackPane> listOfServers;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ConnectHandler.updateClient();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        connectedClient = ConnectHandler.client;
        for(String friend : connectedClient.getClient().getFriends()) {
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
            friendInfo.getChildren().add(userProfile);
            friendInfo.getChildren().add(new Text("  "));
            Text textFriend = new Text(friend);
            textFriend.setStyle("-fx-font: 15 arial;");;
            textFriend.setFill(Color.WHITE);
            friendInfo.getChildren().add(textFriend);
            String path = ShowPictureHandler.getPath("sms.png");
            File file = new File(path);
            String localUrl;
            try {
                localUrl = file.toURI().toURL().toString();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            ImageView sms = new ImageView(localUrl);
            path = ShowPictureHandler.getPath("call.jpg");
            file = new File(path);
            try {
                localUrl = file.toURI().toURL().toString();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            ImageView call = new ImageView(localUrl);
            sms.setId(RandomToken.randomString(21));
            call.setId(RandomToken.randomString(21));
            sms.setOnMouseClicked(mouseEvent -> {
                int index = 0;
                for(HBox hBox1 : listOfPendingFriend.getItems()) {
                    String choiceUserName;
                    for(Node node : hBox1.getChildren()) {
                        if(node instanceof Text) {
                            choiceUserName = (String)((Text) node).getText();
                        }
                        if (node instanceof ImageView) {
                            ImageView clicked = (ImageView) mouseEvent.getSource();
                            if(node.getId() == null) continue;
                            if(node.getId().equals(clicked.getId())) {
                                MsgGetChannel msg;
                                try {
                                    connectedClient.getOutputStream().writeObject(new MsgGetPrivateChat(connectedClient.getId(), connectedClient.getClient().getUsername(), friend));
                                    msg = (MsgGetChannel)connectedClient.getObjectInputStream().readObject();
                                    connectedClient.getOutputStream().writeObject(new MsgCurrentChat(connectedClient.getId(), connectedClient.getClient().getUsername(), msg.getChatChannel().getId(), msg.getChatChannel()));
                                } catch (IOException | ClassNotFoundException e) {
                                    throw new RuntimeException(e);
                                }
                                ConnectHandler.setCurrentChannel(msg.getChatChannel());
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
                                return;
                            }
                        }
                    }
                    index++;
                }
            });

            call.setOnMouseClicked(mouseEvent -> {
                int index = 0;
                for(HBox hBox1 : listOfPendingFriend.getItems()) {
                    String choiceUserName;
                    for(Node node : hBox1.getChildren()) {
                        if(node instanceof Text) {
                            choiceUserName = (String)((Text) node).getText();
                        }
                        if (node instanceof ImageView) {
                            ImageView clicked = (ImageView) mouseEvent.getSource();
                            if(node.getId() == null) continue;
                            if(node.getId().equals(clicked.getId())) {
                                /*
                                    choiceUsername
                                    make call
                                 */
                                return;
                            }
                        }
                    }
                    index++;
                }
            });
            sms.setFitHeight(30);
            sms.setFitWidth(30);
            call.setFitHeight(30);
            call.setFitWidth(30);
            friendInfo.getChildren().add(new Text("                                       "));
            friendInfo.getChildren().add(sms);
            friendInfo.getChildren().add(new Text("    "));
            friendInfo.getChildren().add(call);
            listOfPendingFriend.getItems().add(friendInfo);
        }
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
}
