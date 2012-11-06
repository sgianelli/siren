package edu.siren.game.ai;

import java.util.Random;

import org.lwjgl.Sys;

import edu.siren.game.entity.Entity;

public class RandomWalker implements AI {
    private double dt = getTime();
    private Entity entity;

    private Random random = new Random();

    private double getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    @Override
    public void attach(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void think() {
        if ((getTime() - dt) > 10000) {
            entity.moveTo(random.nextInt(1024), random.nextInt(1024));
            dt = getTime();
        }
    }

}
