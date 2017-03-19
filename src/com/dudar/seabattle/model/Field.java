package com.dudar.seabattle.model;

import com.dudar.seabattle.controller.GameWindowController;

import java.util.ArrayList;
import java.util.List;

import static com.dudar.seabattle.model.CellState.*;

/**
 * Created by HadJower5 on 02.04.2016.
 */

public class Field {
    private final int SIZE = 10;

    private Cell[][] cells = new Cell[SIZE][SIZE];
    private List<Ship> ships = new ArrayList<>();
    private int amountOfShips;
    private Player player;


    public Field() {
        amountOfShips = 0;
    }

    public Field(Player player) {
        amountOfShips = 0;
        this.player = player;
    }

    void init() {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < SIZE; j++) {
                cells[i][j] = new Cell(i, j);
            }
        }
    }

    //Установка случайных кораблей
    public void setRandomShips() {
        Ship ship;
        int t = 4;
        while (t > 0) {
            for (int i = 0; i < 5 - t; i++) {
                ship = new Ship(t);
                do {
                    //Установить t-палубный корабль
                    ship.setCharacteristics();
                } while (!isItPossibleToPlaceShip(ship));
                amountOfShips++;
                ships.add(ship);
                setShipToCells(ship);
            }
            t--;
        }
    }
    //Устанавливает корабли на игровое поле после ручной расстановки
    public void setShips(List<Ship> ships) {
        for (int i = 0; i < 10; i++) {
            this.ships.add(ships.get(i));
            setShipToCells(ships.get(i));
            amountOfShips++;
        }
    }

    private boolean isItPossibleToPlaceShip(Ship ship) {
        //Пробежаться по всем уже поставленным кораблям и проверить на пересечение
        for (int i = 0; i < amountOfShips; i++) {
            if (ship.isIntersect(ships.get(i)))
                return false;
        }
        return true;
    }

    //Присваивает клеткам наличие в них корабля и добавляет кораблю эти клетки в его массив клеток, в которых он лежит
    private void setShipToCells(Ship ship) {
        for (int i = 0; i < ship.getSize(); i++) {
            if (ship.getDirection().name() == "toDown") {
                cells[ship.getLeftUp().getY()][ship.getLeftUp().getX() + i].setShip(ship);
                ship.getCells().add(cells[ship.getLeftUp().getY()][ship.getLeftUp().getX() + i]);
            } else {
                cells[ship.getLeftUp().getY() + i][ship.getLeftUp().getX()].setShip(ship);
                ship.getCells().add(cells[ship.getLeftUp().getY() + i][ship.getLeftUp().getX()]);
            }
        }
    }

    public boolean getShot(Coordinates shoot) {
        CellState state = empty;
        String string = "";
        try {
            switch (cells[shoot.getY()][shoot.getX()].getState()) {
                case emptyAlive:
                    string = "Ха-ха, промах, ну ты и нубас!!\n";
                    state = emptyShot;
                    return false;
                case shipAlive:
                    Ship woundedShip = cells[shoot.getY()][shoot.getX()].getShip();
                    woundedShip.reduceHealth();
                    if (woundedShip.getHealth() == 0) {
                        string = "Супер! Корабль потоплен\n";
                        amountOfShips--; //Уменьшить счетчик кораблей на 1(отображает оставшееся количество кораблей врага)
                        //пометить клетки вокруг убитого корабля
                        GameWindowController.isPlaySounds = false;
                        markCellsAroundKilledShip(shoot);
                        GameWindowController.isPlaySounds = true;
                        woundedShip.updateCells();
                        state = shipKilled;
                    } else {
                        string = "Йоу, ты его ранил!!!\n";
                        state = shipWounded;
                    }
                    return true;
                case emptyShot:
                    state = cells[shoot.getY()][shoot.getX()].getState();
                    string = "Ты тупой что-ли? Уже ж стрелял сюда!\n";
                    return false;
                case shipKilled:
                case shipWounded:
                    GameWindowController.isPlaySounds = false;
                    state = cells[shoot.getY()][shoot.getX()].getState();
                    string = "Ты тупой что-ли? Уже ж стрелял сюда!\n";
                    return false;
                default:
                    string = "ERROR\n";
                    return false;
            }
        } finally {
            cells[shoot.getY()][shoot.getX()].setState(state);
            GameWindowController.pointer.addToLog(string);
            GameWindowController.pointer.writeLog();
        }
    }

    private void markCellsAroundKilledShip(Coordinates shoot) {
        Ship tempShip = cells[shoot.getY()][shoot.getX()].getShip();
        boolean up = false, down = false, left = false, right = false;
        int lY = tempShip.getLeftUp().getY();
        int lX = tempShip.getLeftUp().getX();
        int rX = tempShip.getRightDown().getX();
        int rY = tempShip.getRightDown().getY();
        if (tempShip.getDirection() == ShipDirection.toDown) {
            for (int i = 0; i < tempShip.getSize(); i++) {
                //над
                if (lY != 0) {
                    cells[lY - 1][lX + i].setState(emptyShot);
                    if(!(player instanceof PlayerAI))
                        PlayerAI.getCurrentPlayer().addCoordinatesToAIList(lY - 1, lX + i);
                    up = true;
                }
                //снизу
                if (lY != 9) {
                    cells[lY + 1][lX + i].setState(emptyShot);
                    if(!(player instanceof PlayerAI))
                        PlayerAI.getCurrentPlayer().addCoordinatesToAIList(lY + 1, lX + i);
                    down = true;
                }
                //слева
                if (lX != 0) {
                    cells[lY][lX - 1].setState(emptyShot);
                    if(!(player instanceof PlayerAI))
                        PlayerAI.getCurrentPlayer().addCoordinatesToAIList(lY, lX - 1);
                    left = true;
                }
                //справа
                if (rX != 9) {
                    cells[lY][rX + 1].setState(emptyShot);
                    if(!(player instanceof PlayerAI))
                        PlayerAI.getCurrentPlayer().addCoordinatesToAIList(lY, rX + 1);
                    right = true;
                }
            }
        } else {
            for (int i = 0; i < tempShip.getSize(); i++) {
                //над
                if (lY != 0) {
                    cells[lY - 1][lX].setState(emptyShot);
                    if(!(player instanceof PlayerAI))
                        PlayerAI.getCurrentPlayer().addCoordinatesToAIList(lY - 1, lX);
                    up = true;
                }
                //снизу
                if (rY != 9) {
                    cells[rY + 1][lX].setState(emptyShot);
                    if(!(player instanceof PlayerAI))
                        PlayerAI.getCurrentPlayer().addCoordinatesToAIList(rY + 1, lX);
                    down = true;
                }
                //слева
                if (lX != 0) {
                    cells[lY + i][lX - 1].setState(emptyShot);
                    if(!(player instanceof PlayerAI))
                        PlayerAI.getCurrentPlayer().addCoordinatesToAIList(lY + i, lX - 1);
                    left = true;

                }
                //справа
                if (lX != 9) {
                    cells[lY + i][lX + 1].setState(emptyShot);
                    if(!(player instanceof PlayerAI))
                        PlayerAI.getCurrentPlayer().addCoordinatesToAIList(lY + i, lX + 1);
                    right = true;
                }
            }
        }
        if (up && left) {
            cells[lY - 1][lX - 1].setState(emptyShot);
            if(!(player instanceof PlayerAI))
                PlayerAI.getCurrentPlayer().addCoordinatesToAIList(lY - 1, lX - 1);
        }
        if (up && right) {
            cells[lY - 1][rX + 1].setState(emptyShot);
            if(!(player instanceof PlayerAI))
                PlayerAI.getCurrentPlayer().addCoordinatesToAIList(lY - 1, rX + 1);
        }
        if (down && right) {
            cells[rY + 1][rX + 1].setState(emptyShot);
            if(!(player instanceof PlayerAI))
                PlayerAI.getCurrentPlayer().addCoordinatesToAIList(rY + 1, rX + 1);
        }
        if (down && left) {
            cells[rY + 1][lX - 1].setState(emptyShot);
            if(!(player instanceof PlayerAI))
                PlayerAI.getCurrentPlayer().addCoordinatesToAIList(rY + 1, lX - 1);
        }
    }

    public int getAmountOfShips() {
        return amountOfShips;
    }

    public void draw(boolean blind) {
        GameWindowController.pointer.renewField(true);
        for (int i = 0; i < cells.length; i++) {
            if (!blind) {
                GamePvE.isPlayer1Turn = true;
                GameWindowController.pointer.drawShipComputer(ships.get(i));
            }
            for (int j = 0; j < SIZE; j++) {
                cells[i][j].setState(cells[i][j].getState());
            }
        }
    }

    public List<Ship> getShips() {
        return ships;
    }
}