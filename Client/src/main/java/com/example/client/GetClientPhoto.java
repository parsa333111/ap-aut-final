package com.example.client;

import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import share.Msg;
import share.MsgGetStatus;
import share.MsgGivePhoto;
import share.MsgIsOnline;

import java.io.IOException;

public class GetClientPhoto {
    public static Circle GetImage(String username) throws IOException, ClassNotFoundException {
        ConnectHandler.client.getOutputStream().writeObject(new MsgGivePhoto(ConnectHandler.client.getId(), username, username));
        Msg msg = (Msg) ConnectHandler.client.getObjectInputStream().readObject();
        Image image;
        if(msg.getText() == null || msg.getText().equals("")) {
            image = new Image(ShowPictureHandler.makeStandardPath("discordpic.png"));
        }
        else {
            try {
                ConnectHandler.client.fileOutPut.getFile(msg.getText());
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            image = new Image(ShowPictureHandler.FileMakeStandardPath(ConnectHandler.client.getClient().getUsername() + "#" + msg.getText()));
        }
        Circle cir2 = new Circle(30,30,15);
        cir2.setStroke(Color.SEAGREEN);
        cir2.setFill(new ImagePattern(image));
        ConnectHandler.client.getOutputStream().writeObject(new MsgIsOnline(ConnectHandler.client.getId(), username, ""));
        msg = (Msg) ConnectHandler.client.getObjectInputStream().readObject();
        String status = new String("");
        if (msg.getText().equals("Online")) {
            ConnectHandler.client.getOutputStream().writeObject(new MsgGetStatus(ConnectHandler.client.getId(), username, ""));
            msg = (Msg) ConnectHandler.client.getObjectInputStream().readObject();
            status = msg.getText();
        } else {
            status = "Offline";
        }
        if(status.equals("Online"))
            cir2.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKSEAGREEN));
        else if(status.equals("Offline"))
            cir2.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKBLUE));
        else if(status.equals("Idle"))
            cir2.setEffect(new DropShadow(+25d, 0d, +2d, Color.YELLOW));
        else
            cir2.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKRED));
        return cir2;
    }
}
