package com.dudar.seabattle.view;

import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Created by Administrator on 08.05.2016.
 */
public class MenuPoint extends StackPane {
    public  MenuPoint(String name){
        Rectangle bg = new Rectangle(300,40, Color.BLUE);
        bg.setOpacity(0.5);

        Text text = new Text(name);
        text.setFill(Color.WHITE);
        text.setFont(Font.font("Arial", FontWeight.BOLD,14));

        setAlignment(Pos.CENTER);
        getChildren().addAll(bg,text);
        FillTransition st = new FillTransition(Duration.seconds(0.5),bg);
        setOnMouseEntered(event -> {
            st.setFromValue(Color.BLUEVIOLET);
            st.setToValue(Color.DARKBLUE);
            st.setCycleCount(Animation.INDEFINITE);
            st.setAutoReverse(true);
            st.play();
        });
        setOnMouseExited(event -> {
            st.stop();
            bg.setFill(Color.BLUE);
        });
    }
}
