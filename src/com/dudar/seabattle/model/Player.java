package com.dudar.seabattle.model;

/**
 * Created by HadJower5 on 02.04.2016.
 */

public abstract class Player {
    protected Field field;
    protected char[] letters = {'А','Б', 'В', 'Г', 'Д', 'Е', 'Ж', 'З', 'И', 'К'};

    public Player() {
        field = new Field();
    }

    public void setRandomShips() {
        field.setRandomShips();
    }

    public Field getField() {
        return field;
    }

    public int amountOfShips() {
        return field.getAmountOfShips();
    }
}


