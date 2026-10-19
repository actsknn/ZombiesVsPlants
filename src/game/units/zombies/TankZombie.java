package game.units.zombies;

import game.Point;
import game.units.Zombie;
import game.units.PixelArt;
import java.awt.Graphics;

/** A chunky pixel-style TankZombie. */
public class TankZombie extends Zombie {
    private static final String[] SPRITE = {
        "................",
        ".....kkkkkkk....",
        "....kssssssSk...",
        "....ksssssSSk...",
        "....ksssssSSk...",
        "...kkkkkkkkkkk..",
        "....kggwkwggk...",
        "....kgggggggk...",
        ".....kggkwgk....",
        "..kkkBBBBBBkkk..",
        ".kBBBBwwBBBBBBk.",
        ".kBBBBBBBBBgggk.",
        "..kBBBBBBBBBBk..",
        "...kkppppppkk...",
        "....kppkpppk....",
        "....kppkpppk....",
        "....kppkpppk....",
        "...kBBBkkBBBk...",
        "...kkkkkkkkkk...",
        "................"
    };

    /** Creates a unit at the supplied top-left position with starter stats. */
    public TankZombie(Point position) {
        super(PixelArt.outline(), position, 220, 20, 1, 8, 80, 1.0, 1, 5);
    }

    /** Draws this unit at its current position and rotation. */
    @Override
    public void paint(Graphics brush) {
        PixelArt.paint(brush, position, rotation, SPRITE);
    }
}
