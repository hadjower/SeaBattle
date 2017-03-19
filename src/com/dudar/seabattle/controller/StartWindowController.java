package com.dudar.seabattle.controller;

import com.dudar.seabattle.model.Game;
import com.dudar.seabattle.model.GamePvP;
import com.dudar.seabattle.view.MenuContainer;
import com.dudar.seabattle.view.MenuPoint;
import com.dudar.seabattle.view.SubMenu;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Asus X5 on 30.04.2016.
 */
public class StartWindowController {
    public static Stage gameWindowStage;
    public static Stage startWindowstage;


    public void startPvEGame(ActionEvent actionEvent) {
        showShipsSetModeWindow(actionEvent);
        if (ShipsSetModeController.isStart) {
            startWindowstage.hide();
            if (!ShipsSetModeController.isAuto()) {
                try {
                    ShipsSetModeController.isStart = false;
                    Stage primaryStage = new Stage();
                    Parent root = FXMLLoader.load(getClass().getResource("/com/dudar/seabattle/view/fxmls/UserSetShipModeWindow.fxml"));
                    primaryStage.setTitle("Sea Battle");
                    primaryStage.setMinHeight(550);
                    primaryStage.setMinWidth(900);
                    primaryStage.setResizable(false);
                    primaryStage.setScene(new Scene(root));
                    primaryStage.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (ShipsSetModeController.isStart) {
                showGameWindow(actionEvent);
            }
        }
    }

    private void showGameWindow(ActionEvent actionEvent) {
        try {
            gameWindowStage = new Stage();
            Pane root = FXMLLoader.load(getClass().getResource("/com/dudar/seabattle/view/fxmls/GameWindow.fxml"));
            gameWindowStage.setTitle("Морской бой");
            gameWindowStage.setMinHeight(650);
            gameWindowStage.setMinWidth(960);
            gameWindowStage.setResizable(false);
            Scene scene = new Scene(root);
            MenuContainer menu = initMenu();
            root.getChildren().add(menu);
            gameWindowStage.setScene(scene);
            gameWindowStage.show();

            scene.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ESCAPE) {
                    GameWindowController.pointer.turnMenuOffOn(menu);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MenuContainer initMenu() {
        MenuPoint back = new MenuPoint("ПРОДОЛЖИТЬ");
        MenuPoint newGame = new MenuPoint("НОВАЯ ИГРА");
        MenuPoint mainmenu = new MenuPoint("ГЛАВНОЕ МЕНЮ");
        MenuPoint exit = new MenuPoint("ВЫХОД");
        SubMenu pauseMenu = new SubMenu(back, newGame, mainmenu, exit);
        MenuContainer menu = new MenuContainer(pauseMenu);

        back.setOnMouseClicked(event -> GameWindowController.pointer.turnMenuOffOn(menu));
        newGame.setOnMouseClicked(event -> GameWindowController.pointer.startNewGame(event));
        mainmenu.setOnMouseClicked(event -> GameWindowController.pointer.exitToMainMenu(event));
        exit.setOnMouseClicked(event -> System.exit(0));
        return menu;
    }

    private void showShipsSetModeWindow(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            startWindowstage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/com/dudar/seabattle/view/fxmls/ShipsSetMode.fxml"));
            stage.setTitle("Режим расстановки кораблей");
            stage.setMinHeight(153);
            stage.setMinWidth(401);
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startPvPGame(ActionEvent actionEvent) {
        Game game = new GamePvP();
        game.start();
    }
}