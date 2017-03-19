package com.dudar.seabattle.controller;

import com.dudar.seabattle.model.*;
import com.dudar.seabattle.view.MenuContainer;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Asus X5 on 30.04.2016.
 */
public class GameWindowController {
    GamePvE game;
    public static volatile boolean isPlaySounds;
    private GraphicsContext gcP1;
    private GraphicsContext gcP2;
    private final int CELL_SIZE = 40;   //длина и ширина ячейки в пикселях
    private Image emptyShotIcon;    //иконка выстрела по воде
    private Thread gameThread;
    private Coordinates shotCoordinates;
    public static GameWindowController pointer; // указатель на текущий обьект этого класса
    public Thread winnerMessageThread;
    private String log;
    private Map<String, Media> sounds = new HashMap<>(3);

    @FXML private Canvas canvasPlayerField;
    @FXML private Canvas canvasEnemyField;
    @FXML private RadioMenuItem radioBtnViewMode;
    @FXML private TextArea txtAreaPlayer1Field;
    @FXML private TextArea txtAreaPlayer2Field;
    @FXML private GridPane gridPane;

    //Инициализация контролера
    public void initialize() {
        emptyShotIcon = new Image(getClass().getResourceAsStream("/com/dudar/seabattle/pictures/bomb.png"));
        pointer = this;
        gcP1 = canvasPlayerField.getGraphicsContext2D();
        gcP2 = canvasEnemyField.getGraphicsContext2D();
        gcP2.clearRect(0, 0, 401, 401);
        gcP1.clearRect(0, 0, 401, 401);
        drawField(gcP1);
        drawField(gcP2);
        initSounds();
        log = "";

        startGameThreads();
    }

    //Управление отображением меню паузы
    public void turnMenuOffOn(MenuContainer menu) {
        FadeTransition ft = new FadeTransition(Duration.seconds(0.5),menu);
        if (!menu.isVisible()) {
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();
            menu.setVisible(true);
        }
        else{
            ft.setFromValue(1);
            ft.setToValue(0);
            ft.setOnFinished(evt ->   menu.setVisible(false));
            ft.play();

        }
    }
    //Инициализация и старт игровых потоков
    private void startGameThreads() {
        new MediaPlayer(sounds.get("battle")).play();
        //Создание потока игры
        gameThread = new Thread(() -> {
            game = new GamePvE();
            game.start();
        }, "gameThread");
        gameThread.setDaemon(true);
        gameThread.start();
        winnerMessageThread = new Thread(() -> {
            winnerMessageThread.suspend();
            Platform.runLater(this::createWinInformWindow);
        }, "wimMsgThread");
        winnerMessageThread.setDaemon(true);
        winnerMessageThread.start();
    }
    //Инициализация звуков
    private void initSounds() {
        sounds.put("battle", new Media(getClass().getResource("/com/dudar/seabattle/sounds/start_battle.wav").toString()));
        sounds.put("miss", new Media(getClass().getResource("/com/dudar/seabattle/sounds/shot_in_water.wav").toString())); //miss
        sounds.put("hit", new Media(getClass().getResource("/com/dudar/seabattle/sounds/ranen.wav").toString()));    //hit
        sounds.put("kill", new Media(getClass().getResource("/com/dudar/seabattle/sounds/ubit.wav").toString()));    //kill
    }
    //Отрисовывает поле
    public void drawField(GraphicsContext gc) {
        int startX = 0;
        int startY = 0;
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        int lineLength = CELL_SIZE * 10;
        for (int i = 0; i <= 10; i++) {
            gc.strokeLine(i * CELL_SIZE + startX, startY, i * CELL_SIZE + startX, lineLength + startY);
            gc.strokeLine(startX, i * CELL_SIZE + startY, lineLength + startX, i * CELL_SIZE + startY);
            if (i != 10) {
                for (int j = 0; j < 10; j++) {
                    gc.clearRect(j * CELL_SIZE + startX + 3, i * CELL_SIZE + startY + 3, CELL_SIZE - 5, CELL_SIZE - 5);
                }
            }
        }
    }
    //Возвращает координаты выстрела игрока
    public Coordinates getShotCoordinates() {
        return shotCoordinates;
    }
    //Получает выстрел игрока по полю
    public void doShoot(MouseEvent mouseEvent) {
        int x = (int) mouseEvent.getX();
        int y = (int) mouseEvent.getY();

        if (x < 400 && y < 400) {
            x /= 40;
            y /= 40;
            shotCoordinates = new Coordinates(y, x);
            gameThread.resume();
        }
    }
    //отрисовывает контур корабля Игрока
    public void drawShip(Ship ship) {
            gcP1.drawImage(ship.getIcon() ,ship.getLeftUp().getY() * CELL_SIZE, ship.getLeftUp().getX() * CELL_SIZE);
    }
    //отрисовывает контур корабля ИИ
    public void drawShipComputer(Ship ship) {
        if (!game.isBlind()) {
            gcP2.drawImage(ship.getIcon() ,ship.getLeftUp().getY() * CELL_SIZE, ship.getLeftUp().getX() * CELL_SIZE);
        }
    }
    //отрисовывает клетки и проигрывает звуки
    public void drawCell(int x, int y, CellState state) {
        GraphicsContext gc;
        Color color;
        if (GamePvE.isPlayer1Turn) {
            gc = gcP2;
        } else {
            gc = gcP1;
        }
        switch (state) {
            case emptyAlive:
                gc.clearRect(x * CELL_SIZE + 3, y * CELL_SIZE + 3, CELL_SIZE - 5, CELL_SIZE - 5);
                break;
            case shipAlive:
                if(gc == gcP2 && game.isBlind()) {
                    gc.clearRect(x * CELL_SIZE + 3, y * CELL_SIZE + 3, CELL_SIZE - 5, CELL_SIZE - 5);
                } else {
                    color = Color.GREEN;
                    gc.setFill(color);
                    gc.fillOval(x * CELL_SIZE + 8, y * CELL_SIZE + 8, CELL_SIZE - 15, CELL_SIZE - 15);
                }
                break;
            case emptyShot:
                if (isPlaySounds) {
                    new MediaPlayer(sounds.get("miss")).play();
                }
                gc.drawImage(emptyShotIcon, x * CELL_SIZE, y * CELL_SIZE , 40, 40);
                break;
            case shipKilled:
                color = Color.BLACK;
                gc.setFill(color);
                gc.setStroke(Color.RED);
                gc.setLineWidth(3);
                gc.clearRect(x * CELL_SIZE + 3, y * CELL_SIZE + 3, CELL_SIZE - 5, CELL_SIZE - 5);
                gc.strokeLine(x * CELL_SIZE + 5, y * CELL_SIZE + 5, (x + 1) * CELL_SIZE - 5, (y + 1) * CELL_SIZE - 5);
                gc.strokeLine(x * CELL_SIZE + 5, (y + 1) * CELL_SIZE - 5, (x + 1) * CELL_SIZE - 5, y * CELL_SIZE + 5);
                if (isPlaySounds && GamePvE.isPlayer1Turn) {
                    new MediaPlayer(sounds.get("kill")).play();
                }
                break;
            case shipWounded:
                color = Color.RED;
                gc.setFill(color);
                gc.fillOval(x * CELL_SIZE + 8, y * CELL_SIZE + 8, CELL_SIZE - 15, CELL_SIZE - 15);
                if (isPlaySounds) {
                    new MediaPlayer(sounds.get("hit")).play();
                }
                break;
            default:
                break;
        }
        isPlaySounds = false;
    }

    public void renewField(boolean isAIField) {
        if(isAIField) {
            gcP2.clearRect(0, 0, 401, 401);
            drawField(gcP2);
        }
    }

    public void addToLog(String string) {
        log+=string;
    }

    public void writeLog() {
        if(game.isPlayer1Turn) {
            txtAreaPlayer2Field.appendText(log);
        } else {
            txtAreaPlayer1Field.appendText(log);
        }
        log = "";
    }

    /**
    *  Кнопки Меню
     */

    public void closeApplication(Event event) {
        Object source = event.getSource();
        gameThread.stop();
        if (winnerMessageThread.isAlive()) {
            winnerMessageThread.stop();
            Platform.exit();
        }
        if(source instanceof MenuItem) {
            System.exit(0);
        }
        if(source instanceof Button) {
            StartWindowController.startWindowstage.show();
        }
        StartWindowController.gameWindowStage.close();
    }
    // Кнопка Поле противника/Видимо или нет
    public void setEnemyFieldView(ActionEvent actionEvent) {
        game.setIsBlind(!radioBtnViewMode.isSelected());
        game.getPlayerAI().getField().draw(game.isBlind());
    }
    //Новая Игра
    public void startNewGame(Event actionEvent) {
            gameThread.stop();
        if (winnerMessageThread.isAlive()) {
            winnerMessageThread.stop();
        }

        try {
            Stage stage = new Stage();
            Parent root1 = FXMLLoader.load(getClass().getResource("/com/dudar/seabattle/view/fxmls/ShipsSetMode.fxml"));
            stage.setTitle("Режим расстановки кораблей");
            stage.setMinHeight(153);
            stage.setMinWidth(401);
            stage.setResizable(false);
            stage.setScene(new Scene(root1));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(StartWindowController.gameWindowStage);

            stage.showAndWait();

            if (ShipsSetModeController.isStart) {
                gcP2.clearRect(0, 0, 401, 401);
                gcP1.clearRect(0, 0, 401, 401);
                drawField(gcP1);
                drawField(gcP2);
                txtAreaPlayer1Field.clear();
                txtAreaPlayer2Field.clear();
                StartWindowController.gameWindowStage.hide();
                if(!ShipsSetModeController.isAuto()) {
                    Stage stageSetShips = new Stage();
                    Parent root = FXMLLoader.load(getClass().getResource("/com/dudar/seabattle/view/fxmls/UserSetShipModeWindow.fxml"));
                    stageSetShips.setTitle("Sea Battle");
                    stageSetShips.setMinHeight(550);
                    stageSetShips.setMinWidth(900);
                    stageSetShips.setResizable(false);
                    stageSetShips.setScene(new Scene(root));
                    stageSetShips.showAndWait();
                }

                if (ShipsSetModeController.isStart) {
                    StartWindowController.gameWindowStage.show();
                    startGameThreads();
                }
            } else {
                StartWindowController.gameWindowStage.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Показать правила игры
    public void showGameRules(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/com/dudar/seabattle/view/fxmls/GameRulesWindow.fxml"));
            stage.setTitle("Правила игры");
            stage.setMinHeight(400);
            stage.setMinWidth(600);
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(StartWindowController.gameWindowStage);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Показать информацию о программе
    public void showAbout(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/com/dudar/seabattle/view/fxmls/AboutWindow.fxml"));
            stage.setTitle("О программе");
            stage.setMinHeight(133);
            stage.setMinWidth(502);
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(StartWindowController.gameWindowStage);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //Выход в главное меню
    public void exitToMainMenu(Event actionEvent) {
        gameThread.stop();
        if (winnerMessageThread.isAlive()) {
            winnerMessageThread.stop();
//            Platform.exit();
        }
        StartWindowController.gameWindowStage.close();
        StartWindowController.startWindowstage.show();
    }
    //Окно окончания игры
    public void createWinInformWindow() {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/com/dudar/seabattle/view/fxmls/WinnerWindow.fxml"));
            stage.setTitle("Конец игры");
            stage.setMinHeight(369);
            stage.setMinWidth(682);
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(StartWindowController.gameWindowStage);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}