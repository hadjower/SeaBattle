package com.dudar.seabattle.model;

import com.dudar.seabattle.controller.GameWindowController;
import javafx.scene.paint.Color;

import static com.dudar.seabattle.model.CellState.emptyAlive;
import static com.dudar.seabattle.model.CellState.shipAlive;

/**
 * Created by HadJower5 on 04.04.2016.
 */
public class Cell {
    private Coordinates coordinates;
    private Ship ship;
    private CellState state;

    Cell(int x, int y) {
        coordinates = new Coordinates(x, y);
        setState(emptyAlive);
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
        setState(shipAlive);
    }

    public CellState getState() {
        return state;
    }

    public void setState(CellState state) {
        this.state = state;
        GameWindowController.pointer.drawCell(coordinates.getX(), coordinates.getY(), state);
    }
}