package com.dudar.seabattle.model;


/**
 * Created by HadJower5 on 02.04.2016.
 */
public abstract class Game {
    protected PlayerHuman player1;
    protected  boolean isBlind = true;

    public  void setIsBlind(boolean isBlind) {
        this.isBlind = isBlind;
    }


    public abstract void start();

    public  boolean isBlind() {
        return isBlind;
    }
}
