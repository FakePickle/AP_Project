package com.example.stick_hero;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class GameController implements Initializable
{
    @FXML
    private Text textBox;

    @FXML
    private Text Cherry_counter;
    // Objects used in the scene builder for one instance
    private Rectangle Stick1;
    @FXML
    private Rectangle Pillar1;
    private Rectangle Pillar2;
    @FXML
    private ImageView Character;
    @FXML
    private AnchorPane gamePane;
    @FXML
    private Scene scene;

    private ImageView cherry = new ImageView();
    private Rectangle bonus_point;

    // Boolean variables required for the instance to function
    private boolean mouseButtonPressed = false;
    private boolean stickFalling = false;
    private boolean stickExtending = false;
    private boolean characterMoving = false;
    private boolean dead = false;
    private boolean flipped = false;
    Game game = Game.getInstance();
    private boolean instanceRunning = false;

    // GameLoops
    private Timeline GameLoop1;

    private MediaPlayer mediaPlayer;
    private double angle = 0;

    // Sprite Animation
    private int currentFrame = 0;


    // Initializing the Controller
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

        Media media;
        try {
            media = new Media(Objects.requireNonNull(getClass().getResource("/com/example/stick_hero/Sound/Background_Normal.mp3")).toURI().toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();

        Timeline stickExtendedTimeline = getStickExtendedTimeline();
        stickExtendedTimeline.play();

        Timeline characterMovingTimeline = getCharacterMovingTimeline();
        characterMovingTimeline.play();

        afterDeath();

        Cherry_counter.setText(String.valueOf(game.getCherry_counter()));
        GameLoop1 = new Timeline(new KeyFrame(Duration.millis(0.25), event -> {
            textBox.setText(String.valueOf(game.getScore()));
            Pillar2 = new Pillars().pillar_generator();


            if (gamePane != null)
            {
                scene = gamePane.getScene();
            }
            Pillar2.setOpacity(1);
            bonus_point = new Rectangle(Pillar2.getX() + Pillar2.getWidth()/2 - 3.5, 510, 7, 7);
            bonus_point.setFill(Color.RED);
            Stick1 = new Rectangle(Pillar1.getX() + Pillar1.getWidth() - 5, 506, 5, 5);
            Stick1.setOpacity(0);
            if(game.getScore()%3 ==0) {
                cherry = add_cherry();
            }

            FadeTransition Pillar2Fade = new FadeTransition(Duration.millis(1000), Pillar2);
            Pillar2Fade.setCycleCount(1);
            Pillar2Fade.setFromValue(0);
            Pillar2Fade.setToValue(1);
            Pillar2Fade.play();

            FadeTransition BonusPointFade = new FadeTransition(Duration.millis(1000), bonus_point);
            BonusPointFade.setCycleCount(1);
            BonusPointFade.setFromValue(0);
            BonusPointFade.setToValue(1);
            BonusPointFade.play();

            gamePane.getChildren().add(Pillar2);
            gamePane.getChildren().add(Stick1);
            gamePane.getChildren().add(cherry);
            gamePane.getChildren().add(bonus_point);
            scene.setOnMousePressed(event1 ->
            {
                if (MouseButton.PRIMARY == event1.getButton() && !characterMoving && !dead && !stickExtending && !stickFalling && !instanceRunning)
                {
                    Stick1.setOpacity(1);
                    mouseButtonPressed = true;
                    stickExtending = true;
                }
            });

            scene.setOnMouseReleased(event1 ->
            {
                if (MouseButton.PRIMARY == event1.getButton() && !characterMoving && !dead && !stickFalling && !instanceRunning)
                {
                    mouseButtonPressed = false;
                    stickExtending = false;
                    stickFalling = true;
                }
            });

            scene.setOnMouseClicked(event1 -> {
                if (MouseButton.PRIMARY == event1.getButton() && !dead && !stickExtending && !stickFalling && Character.getLayoutX() + Character.getFitWidth()/2 <= Pillar2.getX())
                {
                    flipped = !flipped;
                    flipCharacter();
                }
            });


        }));

        GameLoop1.setCycleCount(1);
        GameLoop1.play();
    }

    private void flipCharacter()
    {
        Timeline flipTimeline = new Timeline(new KeyFrame(Duration.millis(0.25), event -> {
            if (flipped && Character.getLayoutX() + Character.getFitWidth()/2 <= Pillar2.getX())
            {
                Character.setScaleY(-1);
                Character.setY(Character.getY() + Character.getFitHeight()/2);
            }
            else if (!flipped && Character.getLayoutX() + Character.getFitWidth()/2 <= Pillar2.getX())
            {
                Character.setScaleY(1);
                Character.setY(Character.getY() - Character.getFitHeight()/2);
            }
        }));
        flipTimeline.setCycleCount(1);
        flipTimeline.play();
    }

    private void afterDeath()
    {
        AnimationTimer timer = new AnimationTimer() {
            private int deathAngle = 90;
            @Override
            public void handle(long now)  {
                if ((dead) && Character.getLayoutY() <= 700)
                {
                    Character.setLayoutY(Character.getLayoutY() + 6);
                    double pivotX = Stick1.getX() + Stick1.getWidth()/2;
                    double pivotY = Stick1.getY() + Stick1.getHeight();

                    deathAngle += 2;
                    Stick1.getTransforms().clear();
                    Stick1.getTransforms().add(new Rotate(deathAngle, pivotX, pivotY));
                    if (deathAngle >= 180)
                    {
                        mediaPlayer.stop();
                        String file = "/com/example/stick_hero/Sound/Death.mp3";
                        Media media;
                        try {
                            media = new Media(Objects.requireNonNull(getClass().getResource(file)).toURI().toString());
                        } catch (URISyntaxException e) {
                            throw new RuntimeException(e);
                        }
                        MediaPlayer mediaPlayer = new MediaPlayer(media);
                        mediaPlayer.setCycleCount(1);
                        mediaPlayer.play();
//                        System.out.println("You died");
                        stop();
                        dead = false;
                        try {
                            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("End_Scene.fxml")));
                            Stage stage = (Stage) gamePane.getScene().getWindow();
                            Scene scene = new Scene(root);
                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }

            }
        };

        Timeline afterDeathLoop = new Timeline(new KeyFrame(Duration.millis(0.25), event -> {
            if (dead) {
                timer.start();
            }
        }));
        afterDeathLoop.setCycleCount(Timeline.INDEFINITE);
        afterDeathLoop.play();
    }

    int counter = 0;

    private Timeline getStickExtendedTimeline()
    {
        AnimationTimer stickExtendedAnimation = new AnimationTimer()
        {
            @Override
            public void handle(long now)
            {
            if (stickExtending && !characterMoving && !stickFalling && !dead && !instanceRunning)
            {
                Stick1.setHeight(Stick1.getHeight() + 2);
                Stick1.setY(Stick1.getY() - 2);
            }
            else if (stickFalling)
            {
                double pivotX = Stick1.getX() + Stick1.getWidth()/2;
                double pivotY = Stick1.getY() + Stick1.getHeight();

                angle += 2;
                Stick1.getTransforms().clear();
                Stick1.getTransforms().add(new Rotate(angle, pivotX, pivotY));
                if (angle >= 90)
                {
                    stop();
                    angle = 0;
                    characterMoving = true;
                    stickFalling = false;
                }
            }
            }
        };

        Timeline stickExtendedTimeline = new Timeline(new KeyFrame(Duration.millis(0.25), event -> {
            if (!characterMoving && mouseButtonPressed && !stickFalling)
            {
                stickExtendedAnimation.start();
            }
        }));
        stickExtendedTimeline.setCycleCount(Timeline.INDEFINITE);
        return stickExtendedTimeline;
    }

    private Timeline getCharacterMovingTimeline()
    {
        AnimationTimer characterMovingAnimation = new AnimationTimer()
        {
            boolean cherry_check_bool = false;
            @Override
            public void handle(long now)
            {
                if (characterMoving && !stickFalling && !dead && !stickExtending && !mouseButtonPressed && !collisionCheck())
                {
                    Character.setLayoutX(Character.getLayoutX() + 2);
                    if (counter % 5 == 0){
                        updateSprite();
                    }                    if(cherry_check()){
                        cherry_check_bool = true;
                        cherry_remove();
                    }
                    counter++;
                }
                else
                {
                    if(cherry_check_bool){
                        cherry_check_bool = false;
                        game.setCherry_counter(game.getCherry_counter()+1);
                    }
                    dead = true;
                    stop();
                }
                if (Character.getLayoutX() >= Stick1.getX() + Stick1.getHeight() - Character.getFitWidth()/2 && !dead)
                {
                    if(Stick1.getX() + Stick1.getHeight() >= Pillar2.getX() && Stick1.getX() + Stick1.getHeight() <= Pillar2.getX() + Pillar2.getWidth())
                    {
                        if ((Character.getLayoutX() + Character.getFitWidth()/2 >= Pillar2.getX() + Pillar2.getWidth() - 25))
                        {
                            if (Stick1.getX() + Stick1.getHeight() >= bonus_point.getX() && Stick1.getX() + Stick1.getHeight() <= bonus_point.getX() + bonus_point.getWidth())
                            {
                                game.setScore(game.getScore()+2);
                                textBox.setText(String.valueOf(game.getScore()));
                            }
                            else
                            {
                                game.setScore(game.getScore()+1);
                                textBox.setText(String.valueOf(game.getScore()));
                            }
                            if(cherry_check_bool){
                                game.setCherry_counter(game.getCherry_counter()+1);
                                Cherry_counter.setText(String.valueOf(game.getCherry_counter()));
                                cherry_check_bool = false;
                            }
                            Character.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/stick_hero/Images/Stick_Hero_Sprite_Walking 1.png"))));
                            characterMoving = false;
                            nodeTransition();
                            stop();
                        }
                    }
                    else
                    {
                        if(cherry_check_bool){
                            cherry_check_bool = false;
                            game.setCherry_counter(game.getCherry_counter()+1);
                        }
                        dead = true;
                        stop();
                    }
                }
            }
        };

        Timeline characterMovingTimeline = new Timeline(new KeyFrame(Duration.millis(0.25), event -> {
            if (characterMoving)
            {
                instanceRunning = true;
                characterMovingAnimation.start();
            }
        }));
        characterMovingTimeline.setCycleCount(Timeline.INDEFINITE);
        return characterMovingTimeline;
    }

    private void updateSprite()
    {
        if (characterMoving) {
            if (currentFrame >= 0 && currentFrame < 5) {
                String imagePath = String.format("/com/example/stick_hero/Images/Stick_Hero_Sprite_Walking %d.png", currentFrame + 1);
                Character.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath))));
                currentFrame = (currentFrame + 1) % 5;
            }
        }
    }
    public ImageView add_cherry()
    {
        Cherry cherry = new Cherry();
        return cherry.gen_cherry(Pillar1,Pillar2);
    }

    private Boolean cherry_check(){
        return Character.getLayoutX() + Character.getFitWidth() / 2 >= cherry.getX() && Character.getLayoutX() + Character.getFitWidth()/2 <= cherry.getX()+cherry.getFitWidth() && flipped;
    }

    private void cherry_remove(){
        gamePane.getChildren().remove(cherry);
    }

    private void nodeTransition() {
        Thread transitionThread = new Thread(new Runnable() {
            @Override
            public void run() {
                AnimationTimer timer = new AnimationTimer() {
                    @Override
                    public void handle(long now) {
                        if (Pillar2.getX() > 0) {
                            Pillar2.setX(Pillar2.getX() - 2);
                            Pillar1.setX(Pillar1.getX() - 2);
                            Character.setLayoutX(Character.getLayoutX() - 2);
                            Stick1.setY(Stick1.getY() + 2);
                            cherry.setX(cherry.getX() - 2);
                            bonus_point.setX(bonus_point.getX() - 2);
                        } else {
                            instanceRunning = false;
                            this.stop();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    javafx.application.Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            gamePane.getChildren().remove(Pillar1);
                                            gamePane.getChildren().remove(Stick1);
                                            gamePane.getChildren().remove(cherry);
                                            gamePane.getChildren().remove(bonus_point);
                                            GameLoop1.play();
                                            Pillar1 = Pillar2;
                                        }
                                    });
                                }
                            }).start();
                        }
                    }
                };
                timer.start();
            }
        });
        transitionThread.start();
    }

    private boolean collisionCheck()
    {
        return flipped && Pillar2.getX() <= Character.getLayoutX() + Character.getFitWidth() / 2;
    }

}
