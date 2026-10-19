package game.units.plants;

import game.Point;
import game.units.Plant;
import game.units.PixelArt;
import java.awt.Graphics;

/** A chunky pixel-style Peashooter. */
public class Peashooter extends Plant {
    private static final String[] SPRITE = {
        "................",
        "................",
        ".......kkkkk....",
        ".....kklllllk...",
        "....kllllllllk..",
        ".kkkklllwklllk..",
        "kGGGklllwklllk..",
        "kGkkkllllllllk..",
        "kGkkklllllllGk..",
        "kGGGkllllllGk...",
        ".kkkkkllllkk....",
        "......kkkkk.....",
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
    public Peashooter(Point position) {
        super(PixelArt.outline(), position, 90, 15, 500, 65, 1.0);
    }

    /** Draws this unit at its current position and rotation. */
    @Override
    public void paint(Graphics brush) {
        PixelArt.paint(brush, position, rotation, SPRITE);
    }
}
