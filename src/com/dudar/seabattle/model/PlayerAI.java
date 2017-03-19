package com.dudar.seabattle.model;

import com.dudar.seabattle.controller.GameWindowController;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by HadJower on 12.04.2016.
 */
public class PlayerAI extends Player {
    private ArrayList<Integer> alreadyShotCoordinatesX;
    private ArrayList<Integer> alreadyShotCoordinatesY;
    private static PlayerAI currentPlayer;

    public PlayerAI() {
        alreadyShotCoordinatesX = new ArrayList<>(100);
        alreadyShotCoordinatesY = new ArrayList<>(100);
        field = new Field(this);
        currentPlayer = this;
    }

    public static PlayerAI getCurrentPlayer() {
        return currentPlayer;
    }

    public void addCoordinatesToAIList(int x, int y) {
        alreadyShotCoordinatesY.add(y);
        alreadyShotCoordinatesX.add(x);
    }

    public Coordinates getRandomShot() {
        Random random = new Random();
        int x;
        int y;
        boolean check;
        do {
            check = false;
            x = random.nextInt(10);
            y = random.nextInt(10);
            for (int i = 0; i < alreadyShotCoordinatesX.size(); i++) {
                if((alreadyShotCoordinatesX.get(i) == x) && (alreadyShotCoordinatesY.get(i) == y))
                    check = true;
            }
        } while (check);
        alreadyShotCoordinatesX.add(x);
        alreadyShotCoordinatesY.add(y);
        char c = super.letters[x];
        GameWindowController.pointer.addToLog("Computer's shot is [" + (y+1) + " " + c + "]\n");
        return new Coordinates(y, x);
    }
}
