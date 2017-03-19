package com.dudar.seabattle.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;


/**
 * Created by Administrator on 04.05.2016.
 */
public class RulesWindowController {

    @FXML Label lblRules;

    public void initialize() {
        lblRules.setText("\t«Морской бой» — игра для двух участников, в которой \nигроки по очереди называют координаты на неизвестной им \nкарте соперника. Если у соперника по этим координатам име-\neтся корабль (координаты заняты), то корабль или его часть \n«топится», а попавший получает право сделать ещё один ход. \nЦель игрока — первым поразить все корабли противника.");
    }

    public void closeWindow(ActionEvent actionEvent) {
        Node source = (Node)actionEvent.getSource();
        Stage stage = (Stage)source.getScene().getWindow();
        stage.close();
    }
}
