package edu.siren.game.players;

import java.io.IOException;

import edu.siren.core.sprite.Animation;
import edu.siren.core.sprite.SpriteSheet;
import edu.siren.game.Player;

public class Chesus extends Player {

	private static final long serialVersionUID = 1L;

	public Chesus() throws IOException {
        this.speed = 25;
        this.name = "Chesus";
        SpriteSheet linksheet;
            linksheet = SpriteSheet.fromCSS(
                    "res/game/sprites/characters/jesus.png",
                    "res/game/sprites/characters/jesus.css");
        
        String prefix = "jesus";
        int start = 1;
        int end = 13;
        int msec = 100;
        int idleframe = 1;
        
        this.sprite = linksheet.createSprite(
            new Animation("idle-forward", prefix + "-right-" + idleframe),
            new Animation("idle-backward", prefix + "-left-" + idleframe),
            new Animation("idle-right", prefix + "-right-" + idleframe),
            new Animation("idle-left", prefix + "-left-" + idleframe),
            new Animation("move-right", prefix + "-right-", start, end, msec),
            new Animation("move-left", prefix + "-left-", start, end, msec),
            new Animation("move-forward", prefix + "-right-", start, end, msec),
            new Animation("move-backward", prefix +"-left-", start, end, msec));

    }
}
