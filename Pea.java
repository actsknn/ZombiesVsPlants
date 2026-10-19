package game.units;

import game.Point;
import game.Polygon;
import java.awt.Color;
import java.awt.Graphics;

/** A small green projectile shared by Peashooter and RapidPlant. */
public class Pea extends Polygon {
    public static final int SIZE = 12;

    /** Creates a pea at the supplied top-left position. */
    public Pea(Point position) {
        super(new Point[] {
            new Point(3, 0), new Point(9, 0),
            new Point(12, 3), new Point(12, 9),
            new Point(9, 12), new Point(3, 12),
            new Point(0, 9), new Point(0, 3)
        }, position, 0);
    }

    /** Draws a crisp circle with a dark edge and a square pixel highlight. */
    public void paint(Graphics brush) {
        int x = (int) position.x;
        int y = (int) position.y;
        brush.setColor(new Color(35, 43, 39));
        brush.fillOval(x, y, SIZE, SIZE);
        brush.setColor(new Color(64, 111, 62));
        brush.fillOval(x + 2, y + 2, 8, 8);
        brush.setColor(new Color(151, 198, 80));
        brush.fillOval(x + 2, y + 2, 7, 7);
        brush.setColor(new Color(233, 230, 204));
        brush.fillRect(x + 3, y + 3, 2, 2);
    }
}
