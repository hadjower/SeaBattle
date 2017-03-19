package com.dudar.seabattle.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;

/**
 * Created by HadJower on 12.04.2016.
 */
public class ShipsSetModeController {
    private static boolean isAuto;
    public static boolean isStart;

    @FXML RadioButton radioBtnAuto;
    @FXML RadioButton radioBtnUser;
    @FXML Button btnOK;

    public void initialize() {
        radioBtnAuto.setSelected(true);
    }
    public void isAutoMode(ActionEvent actionEvent) {
        isAuto =  radioBtnAuto.isSelected();
        isStart = true;
        Node source = (Node)actionEvent.getSource();
        Stage stage = (Stage)source.getScene().getWindow();
        stage.close();
    }

    public static boolean isAuto() {
        return isAuto;
    }

    public void autoSelected(ActionEvent actionEvent) {
        radioBtnUser.setSelected(false);
        radioBtnAuto.setSelected(true);
    }

    public void userSelected(ActionEvent actionEvent) {
        radioBtnAuto.setSelected(false);
        radioBtnUser.setSelected(true);
    }

    public void cancelWindow(ActionEvent actionEvent) {
        isStart = false;
        Node source = (Node)actionEvent.getSource();
        Stage stage = (Stage)source.getScene().getWindow();
        stage.close();
    }
}
