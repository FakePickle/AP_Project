package com.example.stick_hero;

import org.junit.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import static org.junit.Assert.*;

public class JunitTest
{
    private int HighScore = 0;
    private int cherryCount = 0;

    @Test
    public void PillarTest1()
    {
        Pillars pillars = new Pillars();
        pillars.pillar_generator();
        assertEquals(510, (int)pillars.getStart_rect().getY());
        assertEquals(210, (int)pillars.getStart_rect().getHeight());
        assertEquals(0, (int)pillars.getStart_rect().getOpacity());
        assert pillars.getStart_rect() != null;
    }

    @Test
    public void PillarTest2()
    {
        Pillars pillars = new Pillars();
        pillars.pillar_generator();
        assertNotEquals(-120, (int)pillars.getStart_rect().getX());
        assertNotEquals(-120, (int)pillars.getStart_rect().getWidth());
        assertNotEquals(1, (int)pillars.getStart_rect().getOpacity());
        assert pillars.getStart_rect() != null;
    }

    @Test
    public void testHighScore() throws FileNotFoundException
    {
        Scanner scanner = new Scanner(new BufferedReader(new FileReader("score.txt")));
        if (scanner.hasNextInt())
        {
            HighScore = scanner.nextInt();
        }
        assertEquals(HighScore, Game.getHigh_score());
    }

    @Test
    public void testCherryCount() throws FileNotFoundException
    {
        Scanner scanner = new Scanner(new BufferedReader(new FileReader("score.txt")));
        scanner.nextInt();
        if (scanner.hasNextInt())
        {
            cherryCount = scanner.nextInt();
        }
        assertEquals(cherryCount, Game.getInstance().getCherry_counter());
    }
}
