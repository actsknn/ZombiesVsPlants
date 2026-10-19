package game.units.zombies;

import game.Point;
import game.units.Zombie;
import game.units.PixelArt;
import java.awt.Graphics;

/** A chunky pixel-style FastZombie. */
public class FastZombie extends Zombie {
    private static final String[] SPRITE = {
        "................",
        ".......kkkkk....",
        "......kgggggk...",
        "......kgwkwgkk..",
        "......kggggggk..",
        ".......kgkwkk...",
        ".......kgggk....",
        ".....kkrrrrkk...",
        "....krrryyrrrk..",
        "....krryyrrgggk.",
        "....krrryyrgggk.",
        ".....kryyRk.....",
        ".....krrRRk.....",
        ".....kppppk.....",
        "....kppkkppk....",
        "...kppk..kppk...",
        "..kppk....kpk...",
        ".kBBk.....kBBk..",
        ".kkkk.....kkkk..",
        "................"
    };

    /** Creates a unit at the supplied top-left position with starter stats. */
    public FastZombie(Point position) {
        super(PixelArt.outline(), position, 65, 12, 2, 8, 40, 1.0, 4, 1);
    }

    /** Draws this unit at its current position and rotation. */
    @Override
    public void paint(Graphics brush) {
        PixelArt.paint(brush, position, rotation, SPRITE);
    }
}
