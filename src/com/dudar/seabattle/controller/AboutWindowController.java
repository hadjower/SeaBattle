package com.dudar.seabattle.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Created by Administrator on 04.05.2016.
 */
public class AboutWindowController {
    @FXML Label lblDetails;
    @FXML Button actionButton;

    public void initialize() {
        lblDetails.setText("Курсовой проект, выполненый студентом ХНУРЕ группы КН-15-4 \nДударем В.В. Все права защищены ©.");
        actionButton.setVisible(false);
    }
    public void getMoney(ActionEvent actionEvent) {

    }
    public void closeWindow(ActionEvent actionEvent) {
        Node source = (Node)actionEvent.getSource();
        Stage stage = (Stage)source.getScene().getWindow();
        stage.close();
    }
}
