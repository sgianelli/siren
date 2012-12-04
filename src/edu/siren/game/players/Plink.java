package edu.siren.game.players;

import java.io.IOException;

import edu.siren.core.sprite.Animation;
import edu.siren.core.sprite.SpriteSheet;
import edu.siren.game.Player;

public class Plink extends Player {
    public Plink() throws IOException {
        this.speed = 40;
        this.name = "Plink";
        SpriteSheet linksheet;
            linksheet = SpriteSheet.fromCSS(
                    "res/game/sprites/characters/link-pink.png",
                    "res/game/sprites/characters/link-pink.css");
        
        this.sprite = linksheet.createSprite(
            new Animation("idle-forward", "link-pink-forward-4"),
            new Animation("idle-backward", "link-pink-backward-4"),
            new Animation("idle-right", "link-pink-right-4"),
            new Animation("idle-left", "link-pink-left-4"),
            new Animation("move-right", "link-pink-right-", 1, 7, 100),
            new Animation("move-left", "link-pink-left-", 1, 7, 100),
            new Animation("move-forward", "link-pink-forward-", 1, 7, 100),
            new Animation("move-backward", "link-pink-backward-", 1, 7, 100));

    }
}
