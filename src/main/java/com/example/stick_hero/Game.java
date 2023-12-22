package com.example.stick_hero;

// We are Using SingleTon Design Pattern
public class Game {
    private boolean GameStatus;
    private boolean GamePaused;
    private int score = 0;
    private static int high_score = 0;
    private static Game game = null;
    private static int cherry_counter = 0;

    public static Game getInstance() {
        if (game == null) {
            game = new Game();
        }
        return game;
    }

    public int getCherry_counter() {
        return cherry_counter;
    }

    public void setCherry_counter(int cherry_counter) {
        Game.cherry_counter = cherry_counter;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
        if (high_score < score)
        {
            high_score = score;
        }
    }

    public void setHigh_score(int high_score) {
        Game.high_score = high_score;
    }

    public static int getHigh_score() {
        return high_score;
    }

    private Game() {
        GameStatus = true;
        GamePaused = false;
    }

    public boolean isGameStatus() {
        return GameStatus;
    }

    public void setGameStatus(boolean gameStatus) {
        GameStatus = gameStatus;
    }

    public boolean isGamePaused() {
        return GamePaused;
    }

    public void setGamePaused(boolean gamePaused) {
        GamePaused = gamePaused;
    }
}
