package edu.siren.game.players;

import java.io.IOException;

import edu.siren.core.sprite.Animation;
import edu.siren.core.sprite.SpriteSheet;
import edu.siren.game.Player;

public class Peach extends Player {
    private static final long serialVersionUID = 2939527610315198076L;

    public Peach() throws IOException {
    	
        this.speed = 25;
        this.name = "Peach";
        SpriteSheet linksheet;
            linksheet = SpriteSheet.fromCSS(
                    "res/game/sprites/characters/sprites.png",
                    "res/game/sprites/characters/sprites.css");
        
        String prefix = "peach-car";
        int start = 1;
        int end = 2;
        int msec = 100;
        int idleframe = 1;
        
        this.sprite = linksheet.createSprite(
            new Animation("idle-forward", prefix + "-forward-" + idleframe),
            new Animation("idle-backward", prefix + "-backward-" + idleframe),
            new Animation("idle-right", prefix + "-right-" + idleframe),
            new Animation("idle-left", prefix + "-left-" + idleframe),
            new Animation("move-right", prefix + "-right-", start, end, msec),
            new Animation("move-left", prefix + "-left-", start, end, msec),
            new Animation("move-forward", prefix + "-forward-", start, end, msec),
            new Animation("move-backward", prefix +"-backward-", start, end, msec));

    }
}
