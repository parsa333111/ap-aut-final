package com.example.client;

import javafx.scene.image.ImageView;

import java.io.File;
import java.net.MalformedURLException;

public class ShowPictureHandler {
    public static String getPath(String fileName) {
       // System.out.println((System.getProperty("user.dir").toString() + "/src/main/resources/com/example/client/picturre/" + fileName));
        return (System.getProperty("user.dir").toString() + "/src/main/resources/com/example/client/picturre/" + fileName);
    }
    public static String getPathFile(String fileName) {
        // System.out.println((System.getProperty("user.dir").toString() + "/src/main/resources/com/example/client/picturre/" + fileName));
        return (System.getProperty("user.dir").toString() + "/src/main/resources/com/example/client/saveFile/" + fileName);
    }

    public static String makeStandardPath(String input) {
        String path = getPath(input);
        File file = new File(path);
        String localUrl;
        try {
            localUrl = file.toURI().toURL().toString();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return localUrl;
    }

    public static String avatarMakeStandardPath(String input) {
        String path = input;
        File file = new File(path);
        String localUrl;
        try {
            localUrl = file.toURI().toURL().toString();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return localUrl;
    }

    public static String FileMakeStandardPath(String input) {
        String path = getPathFile(input);
        File file = new File(path);
        String localUrl;
        try {
            localUrl = file.toURI().toURL().toString();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return localUrl;
    }
}
