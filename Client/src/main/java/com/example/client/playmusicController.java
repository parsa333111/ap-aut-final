package com.example.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class playmusicController implements Initializable {
    @FXML
    private Text nameOfSong;
    private Media media;
    private MediaPlayer mediaPlayer;
    @FXML
    private ProgressBar progressBarMusic;
    private ArrayList<File>songs;
    private File dir, files[];
    private int songNumber;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(ConnectHandler.mediaPlayer != null) {
            ConnectHandler.mediaPlayer.stop();
        }
        songs = new ArrayList<>();
        dir = new File("musicOfChannelX");
        files = dir.listFiles();
        for (File file : files) {
            songs.add(file);
        }
        if(songs.size() == 0) {
            nameOfSong.setText("Please upload first");
        }
        else {
            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            Platform.runLater(() -> {
                nameOfSong.setText(songs.get(songNumber).getName());
            });
        }
    }
    private Timer timer;
    private TimerTask task;
    @FXML
    public void previous(MouseEvent event) {
        if(songNumber < songs.size() - 1) {
            songNumber++;
            mediaPlayer.stop();
            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            Platform.runLater(() -> {
                nameOfSong.setText(songs.get(songNumber).getName());
            });
        }
        else {
            songNumber=0;
            mediaPlayer.stop();
            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            Platform.runLater(() -> {
                nameOfSong.setText(songs.get(songNumber).getName());
            });
        }
        playMedia();
    }
    @FXML
    public void next(MouseEvent event) {
        if (songNumber > 0) {
            songNumber--;
            mediaPlayer.stop();
            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            Platform.runLater(() -> {
                nameOfSong.setText(songs.get(songNumber).getName());
            });
        }
        else {
            songNumber = songs.size() - 1;
            mediaPlayer.stop();
            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            Platform.runLater(() -> {
                nameOfSong.setText(songs.get(songNumber).getName());
            });
        }
        playMedia();
    }
    @FXML
    public void seeking(MouseEvent event) {
        Platform.runLater(() -> {
            progressBarMusic.setProgress(event.getX() / 1207);
            mediaPlayer.seek(Duration.seconds((event.getX() / 1207) * media.getDuration().toSeconds()));
        });
    }
    @FXML
    void play(MouseEvent event) {
        Platform.runLater(() -> {
            beginTimer();
            mediaPlayer.play();
        });
    }
    void playMedia() {
        beginTimer();
        mediaPlayer.play();
    }
    @FXML
    public void pause(MouseEvent event) {
        cancelTimer();
        mediaPlayer.pause();
    }

    @FXML
    public void stop(MouseEvent event) {
        Platform.runLater(() -> {
            progressBarMusic.setProgress(0);
            mediaPlayer.seek(Duration.seconds(0));
            pauseMedia();
        });
    }
    void pauseMedia() {
        cancelTimer();
        mediaPlayer.pause();
    }
    public void beginTimer() {
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                try {
                    running = true;
                    double current = mediaPlayer.getCurrentTime().toSeconds();
                    double end = media.getDuration().toSeconds();
                    Platform.runLater(() -> {
                        progressBarMusic.setProgress(current / end);
                    });
                    if (current == end) {
                        cancelTimer();
                    }
                }
                catch (Exception ignore) {

                }
            }
        };
        Platform.runLater(() -> {
            timer.scheduleAtFixedRate(task, 0, 1000);
        });
    }
    public void cancelTimer() {
        running = false;
        timer.cancel();
    }
    boolean running;
    @FXML
    public void back(MouseEvent event) {
        ConnectHandler.mediaPlayer = mediaPlayer;
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
    }

}
