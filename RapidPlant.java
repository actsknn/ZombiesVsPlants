package game.units.plants;

import game.Point;
import game.units.Plant;
import game.units.PixelArt;
import java.awt.Graphics;

/** A chunky pixel-style RapidPlant. */
public class RapidPlant extends Plant {
    private static final String[] SPRITE = {
        "................",
        ".......kkkkkk...",
        ".....kkGGlllGk..",
        ".kkkkGGGGwkwGGk.",
        "kGGGkGGGGwkwGGk.",
        "kGkkkGGGGGGGGGk.",
        "kGGGkGGGGGGGGGk.",
        ".kkkkGGGGGGGGGk.",
        "kGGGkGGGGGGGGGk.",
        "kGkkkGGGGGGGGk..",
        "kGGGkGGGGGGGk...",
        ".kkkkkkkkkkk....",
        "........kGk.....",
        "........kGk.....",
        "...kkk..kGk.kk..",
        "..klllkkkGkkllk.",
        "...klllGGGlllk..",
        "....kkkkkkkkk...",
        "................",
        "................"
    };

    /** Creates a unit at the supplied top-left position with starter stats. */
    public RapidPlant(Point position) {
        super(PixelArt.outline(), position, 100, 10, 500, 30, 1.0);
    }

    /** Draws this unit at its current position and rotation. */
    @Override
    public void paint(Graphics brush) {
        PixelArt.paint(brush, position, rotation, SPRITE);
    }
}
