package edu.siren.game.players;

import java.io.IOException;

import edu.siren.core.sprite.Animation;
import edu.siren.core.sprite.SpriteSheet;
import edu.siren.game.Player;

public class Red extends Player {
    public Red() throws IOException {
        this.speed = 25;
        this.maxMoves = 6;
        this.moves = 6;
        this.name = "Red";
        SpriteSheet redsheet;
            redsheet = SpriteSheet.fromCSS(
                    "res/game/sprites/characters/red.png",
                    "res/game/sprites/characters/red.css");
        
        this.sprite = redsheet.createSprite(
            new Animation("idle-forward", "red-forward-1"),
            new Animation("idle-backward", "red-backward-1"),
            new Animation("idle-right", "red-left-1"),
            new Animation("idle-left", "red-right-1"),
            new Animation("move-right", "red-left-", 1, 2, 100),
            new Animation("move-left", "red-right-", 1, 2, 100),
            new Animation("move-forward", "red-forward-", 1, 2, 100),
            new Animation("move-backward", "red-backward-", 1, 2, 100));
    }
}
