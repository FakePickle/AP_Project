package com.example.stick_hero;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

import java.util.Objects;
import java.util.Random;

public class Cherry {
    protected final int value = 1;

    public ImageView gen_cherry(Rectangle pillar1,Rectangle pillar2) {
        String imagePath = "/com/example/stick_hero/Images/cherry 1.png";
        ImageView cherry = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath))));
        cherry.setX((pillar2.getX())/2 + pillar1.getWidth()/2);
        cherry.setY(520);
        cherry.setFitHeight(40);
        cherry.setFitWidth(40);
        return cherry;
    }


    public int getValue() {
        return value;
    }
}

