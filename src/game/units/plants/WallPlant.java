package game.units.plants;

import game.Point;
import game.units.Plant;
import game.units.PixelArt;
import java.awt.Graphics;

/** A chunky pixel-style WallPlant. */
public class WallPlant extends Plant {
    private static final String[] SPRITE = {
        "................",
        ".....kkkkkk.....",
        "...kkTTTTttkk...",
        "..kTTTTTTTtttk..",
        "..kTTTTTTTtttk..",
        ".kTTTTTTTTttttk.",
        ".kTTTTTTTTttttk.",
        ".kTTkkTTTkktttk.",
        ".kTTwkTTTwktttk.",
        ".kTTwkTTTwktttk.",
        ".kTTTTTTTTttttk.",
        ".kTTTTkkkTttttk.",
        ".kTTTkTTTkttttk.",
        ".kTTTTTTTTttttk.",
        "..kTTTTTTttttk..",
        "..kTTTTTtttttk..",
        "...kkttttttkk...",
        ".....kkkkkk.....",
        "................",
        "................"
    };

    /** Creates a unit at the supplied top-left position with starter stats. */
    public WallPlant(Point position) {
        super(PixelArt.outline(), position, 150, 0, 0, 90, 1.0);
    }

    /** Draws this unit at its current position and rotation. */
    @Override
    public void paint(Graphics brush) {
        PixelArt.paint(brush, position, rotation, SPRITE);
    }
}
