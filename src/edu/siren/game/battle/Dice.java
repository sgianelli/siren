package edu.siren.game.battle;

import java.util.Random;

public class Dice {
    
    public static int roll(int n) {
        Random random = new Random();
        return random.nextInt(n);
    }

}
