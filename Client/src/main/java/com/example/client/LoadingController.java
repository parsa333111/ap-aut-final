package com.example.client;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoadingController implements Initializable {
    @FXML
    private Button go;
    @FXML
    private ImageView discord;
    @FXML
    private ProgressBar prog;
    @FXML
    private Text text;
    private TranslateTransition transition;
    private double point = 0, mande = 40;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        go.setVisible(false);
//        transition = new TranslateTransition();
//        transition.setNode(discord);
//        transition.setToX(500);
//        transition.setToY(300);
//        transition.setDuration(Duration.millis(2000));
//        transition.setAutoReverse(true);
//        transition.setCycleCount(100);
//        transition.play();
//        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(200),e ->{
//            text.setText(String.valueOf(Math.round(point*100.0) + "%"));
//            point += 0.025;
//            prog.setProgress(point);
//            mande--;
//            if (mande == 0) text.setText("100%");
//        }));
//        timeline.setCycleCount(40);
//        timeline.play();
//        Thread thread = new Thread(new ThreadForProgressBar());
//        thread.start();
    }

    public class ThreadForProgressBar extends Thread {
        @Override
        public void run() {
            while(mande > 0) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            go.setVisible(true);
        }
    }


    @FXML
    void goApp(MouseEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FirstMenu.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
