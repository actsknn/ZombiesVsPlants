package game.units.zombies;

import game.Point;
import game.units.Zombie;
import game.units.PixelArt;
import java.awt.Graphics;

/** A chunky pixel-style BasicZombie. */
public class BasicZombie extends Zombie {
    private static final String[] SPRITE = {
        "................",
        "......kkkkkk....",
        ".....kggggggk...",
        ".....kggwkwgkk..",
        ".....kgggggggk..",
        "......kggkwkk...",
        "......kggggk....",
        "....kkbwwbkk....",
        "...kbbbwrbbbk...",
        "...kbbbwrbbbggk.",
        "...kbbbwwbkkggk.",
        "....kbbbbbk.....",
        "....kbbBbbk.....",
        ".....kppppk.....",
        ".....kppkpk.....",
        ".....kppkpk.....",
        ".....kppkpk.....",
        "....kBBkkBBk....",
        "....kkkkkkkk....",
        "................"
    };

    /** Creates a unit at the supplied top-left position with starter stats. */
    public BasicZombie(Point position) {
        super(PixelArt.outline(), position, 100, 15, 1, 8, 60, 1.0, 2, 2);
    }

    /** Draws this unit at its current position and rotation. */
    @Override
    public void paint(Graphics brush) {
        PixelArt.paint(brush, position, rotation, SPRITE);
    }
}
