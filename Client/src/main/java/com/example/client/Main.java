package com.example.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Scanner;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException, InterruptedException {
        Client client = new Client("127.0.0.1", 2000, RandomToken.randomString(20));
        client.start(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}