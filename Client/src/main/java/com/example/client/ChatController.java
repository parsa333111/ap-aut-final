package com.example.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import share.*;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ChatController implements Initializable {
    private  Client connectClient;

    private Listener listener;

    private Stage stage;

    private ChatChannel chatChannel;

    HashMap<String, Image> imageSender;

    @FXML
    private TextField clientWrite;

    @FXML
    private ListView<VBox> massageList;

    @FXML
    private ImageView setting;

    @FXML
    private Button exit;

    @FXML
    private ListView<StackPane> listOfServers;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connectClient = ConnectHandler.client;
        imageSender = new HashMap<>();
        chatChannel = ConnectHandler.currentChannel;
        massageList.setRotate(180);
        massageList.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
//        exit.setStyle("-fx-background-color: rgba(0,0,0,0.1);");
        int cnt = 0;
        for(Massage massage : chatChannel.getMassages()) {
            VBox hBox = null;
            try {
                if(massage.getChat_id().equals("AddServer")) {
                    hBox = vboxAddServer(massage, Integer.toString(cnt));
                }
                else if(massage.getChat_id().equals("SendFile")) {
                    hBox = vboxAddFile(massage, Integer.toString(cnt));
                }
                else {
                    hBox = makeHBoxForMohammad(massage, Integer.toString(cnt));
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            cnt++;
            hBox.setRotate(180);
            if(!massage.getSender().equals(connectClient.getClient().getUsername()))
                hBox.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            massageList.getItems().add(0, hBox);
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
        for (String server : connectClient.getClient().getServers()) {
            final Circle circle = createCircle();
            try {
                connectClient.getOutputStream().writeObject(new MsgGetServer(connectClient.getId(), connectClient.getClient().getUsername(), server, null));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            MsgGetServer msgGetServer = null;
            try {
                msgGetServer = (MsgGetServer) connectClient.getObjectInputStream().readObject();
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
        listener = new Listener(connectClient.getObjectInputStream());
        Thread listenerThread = new Thread(listener);
        listenerThread.start();
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

    public VBox vboxAddServer(Massage massage, String massageId) {
        VBox vBox = new VBox();
        Image image = new Image(ShowPictureHandler.makeStandardPath("invite.png"));
        Circle circle = createCircle();
        circle.setFill(new ImagePattern(image));
        vBox.getChildren().add(circle);
        Text sender = new Text("  " + massage.getSender() + "\n  " + massage.getText());
        sender.setStyle("color: white; -fx-font-size: 16px;");
        sender.setFill(Color.WHITE);
        vBox.getChildren().add(sender);
        vBox.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown()) {
                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    try {
                        connectClient.getOutputStream().writeObject(new MsgJoinServer(connectClient.getId(), connectClient.getClient().getUsername(), massage.getText()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        connectClient.getOutputStream().writeObject(new MsgRemoveMassage(connectClient.getId(), connectClient.getClient().getUsername(), chatChannel.getId(), Integer.parseInt(massageId)));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        exitButton2();
                    } catch (IOException | ClassNotFoundException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        return vBox;
    }

    public VBox vboxAddFile(Massage massage, String massageId) {
        VBox vBox = new VBox();
        Image image = new Image(ShowPictureHandler.makeStandardPath("file.png"));
        Circle circle = createCircle();
        circle.setFill(new ImagePattern(image));
        vBox.getChildren().add(circle);
        Text sender = new Text("  " + massage.getSender() + "\n  " + massage.getText());
        sender.setStyle("color: white; -fx-font-size: 16px;");
        sender.setFill(Color.WHITE);
        vBox.getChildren().add(sender);
        vBox.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown()) {
                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    try {
                        connectClient.fileOutPut.getFile(massage.getText());
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        return vBox;
    }

    public VBox makeHBoxForMohammad(Massage massage, String massageId) throws IOException, ClassNotFoundException {
        VBox vBox = new VBox();
        HBox hBox = new HBox();
        hBox.getChildren().add(GetClientPhoto.GetImage(massage.getSender()));
        Text sender = new Text("  " + massage.getSender() + "\n  " + massage.getText());
        sender.setStyle("color: white; -fx-font-size: 16px;");
        sender.setFill(Color.WHITE);
        hBox.getChildren().add(sender);
        hBox.setId(massageId);
        vBox.getChildren().add(hBox);
        vBox.getChildren().add(new Text(""));
        HBox rect = new HBox();
        final ContextMenu contextMenu = new ContextMenu();
        {
            HBox reaction = new HBox();
            String path_reaction_like = ShowPictureHandler.getPath("like.png");
            File file_reaction_like = new File(path_reaction_like);
            String localUrl_reaction_like;
            try {
                localUrl_reaction_like = file_reaction_like.toURI().toURL().toString();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            Circle cir2_reaction_like = new Circle(30, 30, 15);
            cir2_reaction_like.setStroke(Color.SEAGREEN);
            Image im_reaction_like = new Image(localUrl_reaction_like, false);
            cir2_reaction_like.setFill(new ImagePattern(im_reaction_like));
            MenuItem menuItem_like = new MenuItem("", cir2_reaction_like);
            menuItem_like.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    contextMenu.hide();
                    System.out.println("like");
                    try {
                        System.out.println("like : " + connectClient.getClient().getUsername());
                        connectClient.getOutputStream().writeObject(new MsgNewReaction(connectClient.getId(), connectClient.getClient().getUsername(), chatChannel.getId(), massageId, "like"));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        exitButton2();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            menuItem_like.setStyle("color: white; -fx-font-size: 16px;");
            contextMenu.getItems().add(menuItem_like);

            String path_reaction_dislike = ShowPictureHandler.getPath("dislike.png");
            File file_reaction_dislike = new File(path_reaction_dislike);
            String localUrl_reaction_dislike;
            try {
                localUrl_reaction_dislike = file_reaction_dislike.toURI().toURL().toString();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            Circle cir2_reaction_dislike = new Circle(30, 30, 15);
            cir2_reaction_dislike.setStroke(Color.SEAGREEN);
            Image im_reaction_dislike = new Image(localUrl_reaction_dislike, false);
            cir2_reaction_dislike.setFill(new ImagePattern(im_reaction_dislike));
            MenuItem menuItem_dislike = new MenuItem("", cir2_reaction_dislike);
            menuItem_dislike.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    contextMenu.hide();
                    try {
                        connectClient.getOutputStream().writeObject(new MsgNewReaction(connectClient.getId(), connectClient.getClient().getUsername(), chatChannel.getId(), massageId, "dislike"));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        exitButton2();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            menuItem_dislike.setStyle("color: white; -fx-font-size: 16px;");
            contextMenu.getItems().add(menuItem_dislike);

            String path_reaction_lol = ShowPictureHandler.getPath("lol.png");
            File file_reaction_lol = new File(path_reaction_lol);
            String localUrl_reaction_lol;
            try {
                localUrl_reaction_lol = file_reaction_lol.toURI().toURL().toString();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            Circle cir2_reaction_lol = new Circle(30, 30, 15);
            cir2_reaction_lol.setStroke(Color.SEAGREEN);
            Image im_reaction_lol = new Image(localUrl_reaction_lol, false);
            cir2_reaction_lol.setFill(new ImagePattern(im_reaction_lol));

            MenuItem menuItem_lol = new MenuItem("", cir2_reaction_lol);
            menuItem_lol.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    contextMenu.hide();
                    try {
                        connectClient.getOutputStream().writeObject(new MsgNewReaction(connectClient.getId(), connectClient.getClient().getUsername(), chatChannel.getId(), massageId, "lol"));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        exitButton2();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            menuItem_dislike.setStyle("color: white; -fx-font-size: 16px;");
            contextMenu.getItems().add(menuItem_lol);
        }
        {
            HBox reaction = new HBox();
            String path_reaction_like = ShowPictureHandler.getPath("like.png");
            File file_reaction_like = new File(path_reaction_like);
            String localUrl_reaction_like;
            try {
                localUrl_reaction_like = file_reaction_like.toURI().toURL().toString();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            Circle cir2_reaction_like = new Circle(30, 30, 15);
            cir2_reaction_like.setStroke(Color.SEAGREEN);
            Image im_reaction_like = new Image(localUrl_reaction_like, false);
            cir2_reaction_like.setFill(new ImagePattern(im_reaction_like));

            String path_reaction_dislike = ShowPictureHandler.getPath("dislike.png");
            File file_reaction_dislike = new File(path_reaction_dislike);
            String localUrl_reaction_dislike;
            try {
                localUrl_reaction_dislike = file_reaction_dislike.toURI().toURL().toString();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            Circle cir2_reaction_dislike = new Circle(30, 30, 15);
            cir2_reaction_dislike.setStroke(Color.SEAGREEN);
            Image im_reaction_dislike = new Image(localUrl_reaction_dislike, false);
            cir2_reaction_dislike.setFill(new ImagePattern(im_reaction_dislike));

            String path_reaction_lol = ShowPictureHandler.getPath("lol.png");
            File file_reaction_lol = new File(path_reaction_lol);
            String localUrl_reaction_lol;
            try {
                localUrl_reaction_lol = file_reaction_lol.toURI().toURL().toString();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            Circle cir2_reaction_lol = new Circle(30, 30, 15);
            cir2_reaction_lol.setStroke(Color.SEAGREEN);
            Image im_reaction_lol = new Image(localUrl_reaction_lol, false);
            cir2_reaction_lol.setFill(new ImagePattern(im_reaction_lol));
            Text text1 = new Text(" " + massage.getLike() + " ");
            text1.setFill(Color.WHITE);
            Text text2 = new Text(" " + massage.getDislike() + " ");
            text2.setFill(Color.WHITE);
            Text text3 = new Text(" " + massage.getLol() + " ");
            text3.setFill(Color.WHITE);
            rect.getChildren().addAll(cir2_reaction_like, text1, cir2_reaction_dislike, text2, cir2_reaction_lol, text3);

            vBox.getChildren().add(rect);
        }
        MenuItem menuItemDelete = new MenuItem("Delete");
        menuItemDelete.setStyle("color: white; -fx-font-size: 16px;");
        contextMenu.getItems().add(menuItemDelete);
        menuItemDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                contextMenu.hide();
                try {
                    connectClient.getOutputStream().writeObject(new MsgRemoveMassage(connectClient.getId(), connectClient.getClient().getUsername(), chatChannel.getId(), Integer.parseInt(massageId)));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    exitButton2();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        vBox.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isSecondaryButtonDown()) {
                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    contextMenu.show(massageList, event.getScreenX(), event.getScreenY());
                    System.out.println("vbox added");
                }
            }
        });
        return vBox;
    }

    @FXML
    public void send() throws IOException {
        System.out.println("Hello World");
        String text = clientWrite.getText();
        if(text.charAt(0) == '@') {
            text = text.substring(1);
            ConnectHandler.client.notification.outputStream.writeObject(new MsgNotification(connectClient.getId(), text, "New Notification From : " + connectClient.getClient().getUsername() + " From Chat : " + chatChannel.getName() + " With Id : " + chatChannel.getId()));
        }
        else {
            Msg send = new MsgChatMassage(connectClient.getId(), connectClient.getClient().getUsername(), clientWrite.getText(), chatChannel.getId());
            connectClient.getOutputStream().writeObject(send);
            clientWrite.setText("");
        }
    }
    public void exitButton(MouseEvent event) throws IOException, InterruptedException {
        listener.stop();
        try {
            connectClient.getOutputStream().writeObject(new MsgByeFromChat(connectClient.getId(), connectClient.getClient().getUsername(), "1", chatChannel));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FirstMenu.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void exitButton2() throws IOException, InterruptedException, ClassNotFoundException {
        updateChatChannel(chatChannel.getId());
        Thread.sleep(500);
        ConnectHandler.setCurrentChannel(chatChannel);
        listener.stop();
        try {
            connectClient.getOutputStream().writeObject(new MsgByeFromChat(connectClient.getId(), connectClient.getClient().getUsername(), "1", chatChannel));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Chat.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        connectClient.getOutputStream().writeObject(new MsgCurrentChat(connectClient.getId(), connectClient.getClient().getUsername(), chatChannel.getId(), chatChannel));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private class Listener implements Runnable{
        ObjectInputStream objInputStream;

        public Listener(ObjectInputStream objInputStream) {
            this.objInputStream = objInputStream;
        }

        private volatile boolean shutdown;
        @Override
        public void run() {
            while (!shutdown) {
                try {
                    Msg msg = (Msg) connectClient.getObjectInputStream().readObject();
                    if(msg instanceof MsgGetChannel) {
                        MsgGetChannel msgGetChannel = (MsgGetChannel)msg;
                        chatChannel = msgGetChannel.getChatChannel();
                        continue;
                    }
                    VBox hBox = makeHBoxForMohammad(new Massage(msg.getOwner(), msg.getText(), chatChannel.getId()), Integer.toString(massageList.getItems().size()));
                    Platform.runLater(() -> {
                        hBox.setRotate(180);
                        if(!msg.getOwner().equals(connectClient.getClient().getUsername()))
                            hBox.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
                        massageList.getItems().add(0, hBox);
                    });
                    Thread.sleep(500);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Exit From The Chat");
        }

        /**
         * stop thread
         */
        public void stop() {
            shutdown = true;
        }
    }

    private FileChooser fileChooser = new FileChooser();

    @FXML
    public void addFile(MouseEvent event) throws IOException, InterruptedException, ClassNotFoundException {
        File file = fileChooser.showOpenDialog(new Stage());
        if(file.toPath() != null) {
            if(!file.toPath().toString().equals(""))
                ConnectHandler.client.fileOutPut.sendFile(chatChannel.getId(), file.getName(), file.toPath().toString());
        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        exitButton2();
    }
    /**
     *
     * @param chatChannelId which we need to update it
     * @throws IOException may throw that
     * @throws ClassNotFoundException may throw that
     */
    public void updateChatChannel(String chatChannelId) throws IOException, ClassNotFoundException {
        connectClient.getOutputStream().writeObject(new MsgGetChannel(connectClient.getId(), connectClient.getClient().getUsername(), chatChannelId, null));
    }
}