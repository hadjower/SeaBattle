package com.dudar.seabattle.model;

import com.dudar.seabattle.controller.GameWindowController;
import javafx.scene.image.Image;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

/**
 * Created by HadJower5 on 02.04.2016.
 */

public class Ship {
    private Coordinates leftUp;
    private Coordinates rightDown;
    private int size;
    private int health;
    private ShipDirection direction;
    private List<Cell> cells = new ArrayList<>(size);
    private Image icon;

    public Ship(int x, int y, int size, ShipDirection direction) throws IOException {

        if (direction == ShipDirection.toDown) {
            if (isGoneOutOfField(x + (size - 1), y)) {
                throw new IOException("Gone out of Field");
            } else {
                rightDown = new Coordinates(x + (size - 1), y);
                setVerticalIcon(size);
            }
        } else {
            if (isGoneOutOfField(x, y + (size - 1))) {
                throw new IOException("Gone out of Field");
            } else {
                rightDown = new Coordinates(x, y + (size - 1));
                setHorizontalIcon(size);
            }
        }

        leftUp = new Coordinates(x, y);
        this.direction = direction;
        this.size = size;
        health = size;
    }


    public List<Cell> getCells() {
        return cells;
    }

    public Ship(int size) {
        leftUp = new Coordinates();
        rightDown = new Coordinates();
        this.size = size;
        health = size;
    }

    //Установка координат и направления
    public void setCharacteristics() {
        Random random = new Random();
        boolean b;
        int a, x, y;
        do {
            leftUp.setX(random.nextInt(10));
            leftUp.setY(random.nextInt(10));
            x = leftUp.getX();
            y = leftUp.getY();
            a = random.nextInt(2);
            if (a == 0)
                b = isGoneOutOfField(x + (size - 1), y);
            else
                b = isGoneOutOfField(x, y + (size - 1));
        } while (b);
        switch (a) {
            case 0:
                direction = ShipDirection.toDown;
                rightDown.setCoordinates(x + (size - 1), y);

                setVerticalIcon(size);
                break;
            case 1:
                direction = ShipDirection.toRight;
                rightDown.setCoordinates(x, y + (size - 1));
                setHorizontalIcon(size);
                break;
            default:
                break;
        }

    }

    private void setHorizontalIcon(int size) {
        switch (size) {
            case 1:
                icon = new Image(getClass().getResourceAsStream("/com/dudar/seabattle/pictures/icons/OneDeskShip.png"));
                break;
            case 2:
                icon = new Image(getClass().getResourceAsStream("/com/dudar/seabattle/pictures/icons/TwoDHorizontal.png"));
                break;
            case 3:
                icon = new Image(getClass().getResourceAsStream("/com/dudar/seabattle/pictures/icons/ThreeDHorizontal.png"));
                break;
            case 4:
                icon = new Image(getClass().getResourceAsStream("/com/dudar/seabattle/pictures/icons/FourDHorizontal.png"));
                break;
        }
    }

    private void setVerticalIcon(int size) {
        switch (size) {
            case 1:
                icon = new Image(getClass().getResourceAsStream("/com/dudar/seabattle/pictures/icons/OneDeskShip.png"));
                break;
            case 2:
                icon = new Image(getClass().getResourceAsStream("/com/dudar/seabattle/pictures/icons/TwoDVertical.png"));
                break;
            case 3:
                icon = new Image(getClass().getResourceAsStream("/com/dudar/seabattle/pictures/icons/ThreeDVertical.png"));
                break;
            case 4:
                icon = new Image(getClass().getResourceAsStream("/com/dudar/seabattle/pictures/icons/FourDVertical.png"));
                break;
        }
    }


    public Coordinates getLeftUp() {
        return leftUp;
    }


    public int getHealth() {
        return health;
    }

    public void reduceHealth() {
        health--;
    }

    public int getSize() {
        return size;
    }

    public ShipDirection getDirection() {
        return direction;
    }

    public Coordinates getRightDown() {
        return rightDown;
    }

    //Проверка на пересечение 2х кораблей
    public boolean isIntersect(Ship anotherShip) {
        if (anotherShip.getRightDown().getX() < leftUp.getX() - 1)
            return false;
        if (anotherShip.getLeftUp().getX() > rightDown.getX() + 1)
            return false;
        if (anotherShip.getRightDown().getY() < leftUp.getY() - 1)
            return false;
        if (anotherShip.getLeftUp().getY() > rightDown.getY() + 1)
            return false;
        return true;
    }

    public boolean isGoneOutOfField(int x, int y) {
        if (((x >= 0) && (x < 10)) && ((y >= 0) && (y < 10)))
            return false;
        return true;
    }

    public void updateCells() {
        for (Cell cell : cells) {
            cell.setState(CellState.shipKilled);
        }
    }

    public Image getIcon() {
        return icon;
    }
}
