package com.dudar.seabattle.view;

import javafx.scene.layout.VBox;

/**
 * Created by Administrator on 08.05.2016.
 */
public class SubMenu extends VBox {
    public SubMenu(MenuPoint...items){
        setSpacing(15);
        setTranslateY(230);
        setTranslateX(335);
        for(MenuPoint item : items){
            getChildren().addAll(item);
        }
    }
}
