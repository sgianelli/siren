package edu.siren.game.players;

import java.io.IOException;

import edu.siren.core.sprite.Animation;
import edu.siren.core.sprite.AnimationFrame;
import edu.siren.core.sprite.SpriteSheet;
import edu.siren.game.Player;

public class Pikachu extends Player {
    public Pikachu() throws IOException {
        this.speed = 25;
        this.name = "Link";
        SpriteSheet linksheet;
            linksheet = SpriteSheet.fromCSS(
                    "res/game/sprites/characters/pikachu.png",
                    "res/game/sprites/characters/pikachu.css");
        
        this.sprite = linksheet.createSprite(
            new Animation("idle-forward", "pikachu-forward-1"),
            new Animation("idle-backward", "pikachu-backward-1"),
            new Animation("idle-right", "pikachu-right-1"),
            new Animation("idle-left", "pikachu-left-1"),
            new Animation("move-right", "pikachu-right-", 1, 2, 100),
            new Animation("move-left", "pikachu-left-", 1, 2, 100),
            new Animation("move-forward", "pikachu-forward-", 1, 2, 100),
            new Animation("move-backward", "pikachu-backward-", 1, 2, 100));
    }
}
