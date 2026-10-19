package game.units;

import game.Point;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/** Paints small character sprites using three-pixel squares. */
public final class PixelArt {
    private PixelArt() { }

    /** Returns a fresh collision outline matching a 16 by 20 sprite. */
    public static Point[] outline() {
        return new Point[] {new Point(0, 0), new Point(48, 0),
            new Point(48, 60), new Point(0, 60)};
    }

    /** Paints a sprite at its unit position, respecting polygon rotation. */
    public static void paint(Graphics brush, Point position, double rotation,
            String[] pixels) {
        Graphics2D spriteBrush = (Graphics2D) brush.create();
        spriteBrush.translate(position.x, position.y);
        spriteBrush.rotate(Math.toRadians(rotation), 24, 30);
        for (int row = 0; row < pixels.length; row++) {
            for (int col = 0; col < pixels[row].length(); col++) {
                char pixel = pixels[row].charAt(col);
                if (pixel != '.') {
                    spriteBrush.setColor(color(pixel));
                    spriteBrush.fillRect(col * 3, row * 3, 3, 3);
                }
            }
        }
        spriteBrush.dispose();
    }

    private static Color color(char pixel) {
        switch (pixel) {
            case 'g': return new Color(116, 158, 91);
            case 'G': return new Color(64, 111, 62);
            case 'l': return new Color(151, 198, 80);
            case 'b': return new Color(119, 83, 59);
            case 'B': return new Color(77, 57, 45);
            case 't': return new Color(183, 139, 78);
            case 'T': return new Color(220, 178, 106);
            case 'r': return new Color(181, 53, 48);
            case 'R': return new Color(119, 37, 40);
            case 'y': return new Color(249, 209, 71);
            case 'w': return new Color(233, 230, 204);
            case 'p': return new Color(71, 88, 113);
            case 's': return new Color(155, 169, 175);
            case 'S': return new Color(100, 116, 126);
            default: return new Color(35, 43, 39);
        }
    }
}
