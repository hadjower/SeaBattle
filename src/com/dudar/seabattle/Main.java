package com.dudar.seabattle;/**
 * Created by Asus X5 on 30.04.2016.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/dudar/seabattle/view/fxmls/StartWindow.fxml"));
        primaryStage.setTitle("Sea Battle");
        primaryStage.setWidth(1024);
        primaryStage.setHeight(610);
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
