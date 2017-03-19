package com.dudar.seabattle.view;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by Administrator on 08.05.2016.
 */
public class MenuContainer extends Pane {
    static SubMenu subMenu;
    public MenuContainer(SubMenu subMenu){
        MenuContainer.subMenu = subMenu;

        setVisible(false);
        Rectangle bg = new Rectangle(980,670, Color.LIGHTBLUE);
        bg.setOpacity(0.4);
        getChildren().addAll(bg, subMenu);
    }
    public void setSubMenu(SubMenu subMenu){
        getChildren().remove(MenuContainer.subMenu);
        MenuContainer.subMenu = subMenu;
        getChildren().add(MenuContainer.subMenu);
    }
}
