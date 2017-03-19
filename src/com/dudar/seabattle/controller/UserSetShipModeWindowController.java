package com.dudar.seabattle.controller;

import com.dudar.seabattle.model.Ship;
import com.dudar.seabattle.model.ShipDirection;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 04.05.2016.
 */
public class UserSetShipModeWindowController {
    private static final int CELL_SIZE = 40;
    private GraphicsContext graphicsContext;
    private GraphicsContext gcHorizontal;
    private GraphicsContext gcVertical;

    @FXML private Canvas canvasField;
    @FXML private Canvas canvasHorizontalShips;
    @FXML private Canvas canvasVerticalShips;
    @FXML private Button btnPlay;
    @FXML private ImageView shipImgView;

    @FXML private Label lblInstructions;

    @FXML private Text lblVertical;
    @FXML private Text lblH4;
    @FXML private Text lblH3;
    @FXML private Text lblH2;
    @FXML private Text lblH1;

    private boolean isShipTook;
    private ShipDirection selectedShipDirection;
    private int selectedShipSize;
    private Image selectedShipImage;
    private static List<Ship> ships = new ArrayList<>(10);
    private Map<String, Image> images = new HashMap<>(7);
    private int[] shipsCounts = {4, 3, 2, 1};
    private boolean isFieldFull;

    public void initialize() {
        isFieldFull = false;
        shipImgView.setVisible(false);
        graphicsContext = canvasField.getGraphicsContext2D();
        gcHorizontal = canvasHorizontalShips.getGraphicsContext2D();
        gcVertical = canvasVerticalShips.getGraphicsContext2D();
        initEvents();
        initImages();
        lblInstructions.setText("Разместите корабли \nна своем игровом поле.\nЧтобы начать игру \nнажмите кнопку \"В БОЙ!\".\nЧтобы очистить поле \nнажмите кнопку \"СБРОС\".");

        drawBigField();
        drawSmallField(gcHorizontal);
        drawSmallField(gcVertical);
        fillShips(gcHorizontal);
        fillShips(gcVertical);
        updateLabelCounts();
    }

    public static List<Ship> getShips() {
        return ships;
    }

    private void initImages() {
        images.put("1Desk", new Image("/com/dudar/seabattle/pictures/ships/1DShip.png"));
        images.put("2DHorizontal", new Image("/com/dudar/seabattle/pictures/ships/2DShipH.png"));
        images.put("2DVertical", new Image("/com/dudar/seabattle/pictures/ships/2DShipV.png"));
        images.put("3DHorizontal", new Image("/com/dudar/seabattle/pictures/ships/3DShipH.png"));
        images.put("3DVertical", new Image("/com/dudar/seabattle/pictures/ships/3DShipV.png"));
        images.put("4DHorizontal", new Image("/com/dudar/seabattle/pictures/ships/4DShipH.png"));
        images.put("4DVertical", new Image("/com/dudar/seabattle/pictures/ships/4DShipV.png"));
    }


    private void initEvents() {
        canvasVerticalShips.setOnMouseClicked(event -> {
            if (!isFieldFull) {
                if (takeVerticalShip(event)) {
                    isShipTook = true;
                }
            }
        });

        canvasHorizontalShips.setOnMouseClicked(event -> {
            if (!isFieldFull) {
                if (takeHorizontalShip(event)) {
                    isShipTook = true;
                }
            }
        });

        canvasField.setOnMouseClicked(event -> {
            if (isShipTook && !isFieldFull) {
                if(!drawShip(event)) {
                    showError();
                }
            }
            shipImgView.setVisible(false);
            isShipTook = false;
        });
    }

    private void showError() {
        //todo вывести окно об ошибке
    }

    private void fillShips(GraphicsContext gc) {
        for (int i = 0; i < 4; i++) {
            if (gc == gcHorizontal) {
                String[] keys = {"4DHorizontal", "3DHorizontal", "2DHorizontal", "1Desk"};
                gc.drawImage(images.get(keys[i]), 5 , 5 + CELL_SIZE * i, CELL_SIZE * (4 - i) - 6, CELL_SIZE - 6 );
            } else {
                String[] keys = {"4DVertical", "3DVertical", "2DVertical", "1Desk"};
                gc.drawImage(images.get(keys[i]), 5 + CELL_SIZE * i, 5, CELL_SIZE - 6, CELL_SIZE * (4 - i) - 6);
            }
        }
    }

    private void drawSmallField(GraphicsContext gc) {
        int startX = 2;
        int startY = 2;
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        for (int i = 0; i < 5; i++) {
            int lineLength = CELL_SIZE * 4;
            gc.strokeLine(i * CELL_SIZE + startX, startY, i * CELL_SIZE + startX, lineLength + startY);
            gc.strokeLine(startX, i * CELL_SIZE + startY, lineLength + startX, i * CELL_SIZE + startY);
        }
    }

    public void drawBigField() {
        int startX = 2;
        int startY = 2;
        graphicsContext.clearRect(0, 0, 400 + startX, 400 + startY);
        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.setLineWidth(1);
        int lineLength = CELL_SIZE * 10;
        for (int i = 0; i <= 10; i++) {
            graphicsContext.strokeLine(i * CELL_SIZE + startX, startY, i * CELL_SIZE + startX, lineLength + startY);
            graphicsContext.strokeLine(startX, i * CELL_SIZE + startY, lineLength + startX, i * CELL_SIZE + startY);
        }
    }


    public boolean takeVerticalShip(MouseEvent mouseEvent) {
        String[] keys = {"4DVertical", "3DVertical", "2DVertical", "1Desk"};
        int x = (int) mouseEvent.getX();
        int y = (int) mouseEvent.getY();

        for (int i = 0; i < 4; i++) {
            if((x > 5 + CELL_SIZE * i && x < 5 + CELL_SIZE * i + CELL_SIZE - 6) && (y > 5 && y <  CELL_SIZE * (4 - i) - 6) && shipsCounts[4 - i - 1] > 0) {
                selectedShipImage = images.get(keys[i]);
                selectedShipDirection = ShipDirection.toDown;
                selectedShipSize = 4 - i;
                return true;
            }
        }
        return false;
    }

    //todo установка корабля на главное поле
    private boolean takeHorizontalShip(MouseEvent mouseEvent) {
        String[] keys = {"4DHorizontal", "3DHorizontal", "2DHorizontal", "1Desk"};
        int x = (int) mouseEvent.getX();
        int y = (int) mouseEvent.getY();

        for (int i = 0; i < 4; i++) {
            if((x > 5 && x < CELL_SIZE * (4 - i) - 6) && (y > 5 + CELL_SIZE * i && y <  5 + CELL_SIZE * i + CELL_SIZE - 6) && shipsCounts[4 - i - 1] > 0) {
                selectedShipImage = images.get(keys[i]);
                selectedShipDirection = ShipDirection.toRight;
                selectedShipSize = 4 - i;
                return true;
            }
        }
        return false;
    }

    public boolean drawShip(MouseEvent mouseEvent) {
        if(!isShipTook) {
            return false;
        }
        int x = (int) mouseEvent.getX();
        int y = (int) mouseEvent.getY();

        if (x < 400 && y < 400) {
            x /= 40;
            y /= 40;
        }

        try {
            Ship selectedShip = new Ship(y, x, selectedShipSize, selectedShipDirection);
            for (int i = 0; i < ships.size(); i++) {
                if(selectedShip.isIntersect(ships.get(i)) ) {
                    return false;
                }
            }
            graphicsContext.drawImage(selectedShipImage, x * CELL_SIZE, y * CELL_SIZE);
            ships.add(selectedShip);
            shipsCounts[selectedShipSize - 1]--;
            updateLabelCounts();
            if(ships.size() == 10) {
                btnPlay.setVisible(true);
                isFieldFull = true;
            }
                return true;
            } catch (IOException e) {
                return false;
            }
    }

    private void updateLabelCounts() {
            lblH4.setText("x" + shipsCounts[3]);
            lblH3.setText("x" + shipsCounts[2]);
            lblH2.setText("x" + shipsCounts[1]);
            lblH1.setText("x" + shipsCounts[0]);
        lblVertical.setText("x" + shipsCounts[3] + "   x" + shipsCounts[2] + "   x" + shipsCounts[1] + "   x" + shipsCounts[0]);
    }

    public void clearField(ActionEvent actionEvent) {
        drawBigField();
        ships.clear();
        selectedShipSize = 0;
        selectedShipDirection = null;
        selectedShipImage = null;
        isFieldFull = false;
        btnPlay.setVisible(false);
        initShipsCount();
        updateLabelCounts();
        new MediaPlayer(new Media(getClass().getResource("/com/dudar/seabattle/sounds/crumpled.wav").toString())).play();
    }

    private void initShipsCount() {
        for (int i = 0; i < 4; i++) {
            shipsCounts[i] = 4 - i;
        }
    }

    public void startGame(ActionEvent actionEvent) {
        ShipsSetModeController.isStart = true;
        Node source = (Node)actionEvent.getSource();
        Stage stage = (Stage)source.getScene().getWindow();
        stage.close();
    }

    public void moveImg(MouseEvent event) {
        if(isShipTook){
            shipImgView.setFitHeight(selectedShipImage.getHeight());
            shipImgView.setFitWidth(selectedShipImage.getWidth());
            shipImgView.setImage(selectedShipImage);
            shipImgView.setVisible(true);
            shipImgView.setTranslateX(event.getSceneX() - 20);
            shipImgView.setTranslateY(event.getSceneY() - 20);
        }
    }

    public void cancelWindow(ActionEvent actionEvent) {
        ShipsSetModeController.isStart = false;
        Node source = (Node)actionEvent.getSource();
        Stage stage = (Stage)source.getScene().getWindow();
        stage.close();
        if (StartWindowController.gameWindowStage != null) {
            StartWindowController.gameWindowStage.close();
        }
        StartWindowController.startWindowstage.show();
    }
}