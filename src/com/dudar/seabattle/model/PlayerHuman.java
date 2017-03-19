package com.dudar.seabattle.model;

import com.dudar.seabattle.controller.GameWindowController;
import com.dudar.seabattle.controller.UserSetShipModeWindowController;

/**
 * Created by HadJower on 12.04.2016.
 */
public class PlayerHuman extends Player {
    private String name;


    public PlayerHuman() {
        field = new Field(this);
    }

    public PlayerHuman(String name) {
        this.name = name;
        field = new Field(this);
    }

    public Coordinates getShot(Coordinates shot) {
        char c = super.letters[shot.getY()];
        GameWindowController.pointer.addToLog(name + "'s shot is [" + (shot.getX()+1) + " " + c + "]\n");
        return shot;
    }

    public void setShips() {
        field.setShips(UserSetShipModeWindowController.getShips());
    }


    public String getName() {
        return name;
    }
}
