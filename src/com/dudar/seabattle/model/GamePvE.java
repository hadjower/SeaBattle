package com.dudar.seabattle.model;

import com.dudar.seabattle.controller.GameWindowController;
import com.dudar.seabattle.controller.ShipsSetModeController;

/**
 * Created by HadJower on 12.04.2016.
 */
public class GamePvE extends Game {
    private PlayerAI playerAI;
    public static boolean isPlayer1Turn;
    private static boolean isPlayer1Winner;
    private int shotCountPlayer1 = 0;
    private int shotCountPlayer2 = 0;

    public static boolean isPlayer1Winner() {
        return isPlayer1Winner;
    }

    public PlayerAI getPlayerAI() {
        return playerAI;
    }

    @Override
    public void start() {
        player1 = new PlayerHuman("Vlad");
        playerAI = new PlayerAI();

        //Создание полей для игры
        player1.getField().init();
        playerAI.getField().init();

        //Установка кораблей
        isPlayer1Turn = false;
        if (ShipsSetModeController.isAuto()) {
            player1.setRandomShips();
        } else {
            player1.setShips();
        }
        isPlayer1Turn = true;
        playerAI.setRandomShips();

        //Начало игры
        mainGameCircle();
        getWinner();
    }

    private void mainGameCircle() {
        for (int i = 0; i < 10; i++) {
                GameWindowController.pointer.drawShip(player1.getField().getShips().get(i));
        }
        boolean isHit = false;
        do {
            Coordinates playerShoot;
            do {
                isPlayer1Turn = true;
                GameWindowController.isPlaySounds = true;
                Thread.currentThread().suspend();
                //Ждем пока игрок не кликнет(выстрелит), потом возвращаем координаты выстрела в playerShot
                playerShoot = player1.getShot(GameWindowController.pointer.getShotCoordinates()); // получить координаты из контроллера
                isHit = playerAI.getField().getShot(playerShoot);
                if(isHit)
                    shotCountPlayer1++;
            } while (isHit && isNotGameOver());
            if (!isNotGameOver())
                return;
            if (player1.amountOfShips() != 0) {
                isPlayer1Turn = false;
                Coordinates playerAIShoot;
                do {
                    playerAIShoot = playerAI.getRandomShot();
                    isHit = player1.getField().getShot(playerAIShoot);
                    if(isHit)
                        shotCountPlayer2++;
                } while (isHit && isNotGameOver());
            }
        } while (isNotGameOver());
    }

    private boolean isNotGameOver() {
        return (player1.amountOfShips() > 0) && (playerAI.amountOfShips() > 0);
    }

    public int getShotCountPlayer1() {
        return shotCountPlayer1;
    }

    public int getShotCountPlayer2() {
        return shotCountPlayer2;
    }

    private void getWinner() {
        if (player1.amountOfShips() == 0) {
            isPlayer1Winner = false;
        } else if (playerAI.amountOfShips() == 0) {
            isPlayer1Winner = true;
        } else {
        }
        GameWindowController.pointer.winnerMessageThread.resume();
    }
}
