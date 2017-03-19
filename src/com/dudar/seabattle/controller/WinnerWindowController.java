package com.dudar.seabattle.controller;

import com.dudar.seabattle.model.GamePvE;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

/**
 * Created by Администратор on 03.05.2016.
 */
public class WinnerWindowController {
    @FXML private ImageView imgViewPicture;
    MediaPlayer mediaPlayer;

    public void initialize() {
        Image image;
        Media media;

        if(GamePvE.isPlayer1Winner()) {
            image = new Image(getClass().getResourceAsStream("/com/dudar/seabattle/pictures/winImage.png"));
            media = new Media(getClass().getResource("/com/dudar/seabattle/sounds/win.wav").toString());

        } else {
            image = new Image(getClass().getResourceAsStream("/com/dudar/seabattle/pictures/defeatImage.png"));
            media = new Media(getClass().getResource("/com/dudar/seabattle/sounds/loss.wav").toString());
        }
        imgViewPicture.setImage(image);
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }

    public void cancelApplication(ActionEvent actionEvent) {
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.close();
        GameWindowController.pointer.closeApplication(actionEvent);
        mediaPlayer.stop();
    }

    public void startNewGame(ActionEvent actionEvent) {
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.close();
        GameWindowController.pointer.startNewGame(actionEvent);
        mediaPlayer.stop();
    }
}
