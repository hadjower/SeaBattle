package com.dudar.seabattle.model;

/**
 * Created by HadJower5 on 12.03.2016.
 */
public class Coordinates {
    private int x;
    private int y;

    public Coordinates() {
        setCoordinates(0, 0);
    }

    public static boolean checkCoordinates(int x, int y) {
        return ((x >= 0) && (x < 10)) && ((y >= 0) && (y < 10));
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Coordinates(int x, int y) {
        setCoordinates(x, y);
    }

    public void setCoordinates(int x, int y) {
        if(checkCoordinates(x, y)) {
            this.x = x;
            this.y = y;
        } else {

        }
    }
}
