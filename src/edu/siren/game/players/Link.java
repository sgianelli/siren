package edu.siren.game.players;

import java.io.IOException;

import edu.siren.core.sprite.Animation;
import edu.siren.core.sprite.AnimationFrame;
import edu.siren.core.sprite.SpriteSheet;
import edu.siren.game.Player;

public class Link extends Player {
    public Link() throws IOException {
        this.speed = 25;
        this.name = "Link";
        SpriteSheet linksheet;
            linksheet = SpriteSheet.fromCSS(
                    "res/game/sprites/characters/link.png",
                    "res/game/sprites/characters/link.css");
        
        this.sprite = linksheet.createSprite(
            new Animation("idle-forward", "link-forward-4"),
            new Animation("idle-backward", "link-backward-4"),
            new Animation("idle-right", "link-right-4"),
            new Animation("idle-left", "link-left-4"),
            new Animation("move-right", "link-right-", 1, 7, 100),
            new Animation("move-left", "link-left-", 1, 7, 100),
            new Animation("move-forward", "link-forward-", 1, 7, 100),
            new Animation("move-backward", "link-backward-", 1, 7, 100));

    }
}
