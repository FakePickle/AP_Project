package com.example.stick_hero;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.util.Objects;

public class EndScene_Controller implements Initializable
{
    @FXML
    private Text cherry_count;
    @FXML
    private Text bestScore;
    @FXML
    private Text score;
    @FXML
    private Button revive;

    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        score.setText("Score " + Game.getInstance().getScore());
        bestScore.setText("Best " + Game.getHigh_score());
        cherry_count.setText(String.valueOf(Game.getInstance().getCherry_counter()));

        try {
            writeScoresToFile(); // Call the method to write scores to file
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Decorator Pattern is being used while Writing to file
    private void writeScoresToFile() throws IOException {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter("score.txt")));
            out.println( Game.getHigh_score());
            out.println( Game.getInstance().getCherry_counter());
        } finally{
            if(out !=null){
                out.close();
            }
        }
    }

    public void restart_game(ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Game_Page.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Game.getInstance().setScore(0);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void revive_button(ActionEvent event) throws IOException {
        if(Game.getInstance().getCherry_counter()>=10) {
            Game.getInstance().setGameStatus(true);
            Game.getInstance().setGamePaused(false);
            Game.getInstance().setCherry_counter(Game.getInstance().getCherry_counter() - 10);
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Game_Page.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        else{
            System.out.println("Not enough cherries");
            Text text = new Text("Not enough cherries");
            text.setStyle("-fx-font-size: 20px; -fx-fill : #FF0000");
            text.setX(200);
            text.setY(480);
            ((Pane)revive.getParent()).getChildren().add(text);
        }
    }
    public void home_page(ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Home_Page.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Game.getInstance().setScore(0);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
