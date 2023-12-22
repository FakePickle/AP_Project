package com.example.stick_hero;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class Pillars {
    private Rectangle start_rect;

    public Rectangle getStart_rect() {
        return start_rect;
    }

    public void setStart_rect(Rectangle start_rect) {
        this.start_rect = start_rect;
    }

    public Rectangle pillar_generator(){
        Random random = new Random();
        int x = random.nextInt(120,380)+100;
        int width = random.nextInt(150)+20;
        start_rect = new Rectangle(x,510,width,210);
        start_rect.setFill(Color.BLACK);
        start_rect.setOpacity(0);
        return start_rect;
    }

}
