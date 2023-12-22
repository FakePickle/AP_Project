package com.example.stick_hero;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.util.Objects;
import java.util.Scanner;


//Decorator Pattern is being used while Reading the file
public class HelloApplication extends Application {
    @Override
    public void start(Stage start_stage) throws IOException {
        Parent root_fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Home_Page.fxml")));
        Scene scene = new Scene(root_fxml, 600, 720, Color.ORANGE);
        String imagePath = "/com/example/stick_hero/Images/icon.png";
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        start_stage.getIcons().add(icon);
        start_stage.setTitle("Stick Hero");
        int HighScore = 0;
        int cherryCount = 0;

        Scanner scanner = new Scanner(new BufferedReader(new FileReader("score.txt")));
        if (scanner.hasNextInt())
        {
            HighScore = scanner.nextInt();
        }
        if (scanner.hasNextInt())
        {
            cherryCount = scanner.nextInt();
        }
        Game.getInstance().setCherry_counter(cherryCount);
        Game.getInstance().setHigh_score(HighScore);

        start_stage.setScene(scene);
        start_stage.setResizable(false);
        start_stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
