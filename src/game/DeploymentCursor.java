package game;

import game.units.zombies.ZombieType;
import java.awt.Graphics;

public class DeploymentCursor extends Polygon {

    private int row;
    private int column;
    private ZombieType selectedZombieType;

    public DeploymentCursor(Point[] shape, Point position, double rotation) {
        super(shape, position, rotation);

        row = 0;
        column = 0;
        selectedZombieType = null;
    }

    public void moveUp() {
        if (row > 0) {
            row--;
            position.y -= 100;
        }
    }

    public void moveDown() {
        if (row < 4) {
            row++;
            position.y += 100;
        }
    }

    public void moveLeft() {
        if (column > 0) {
            column--;
            position.x -= 90;
        }
    }

    public void moveRight() {
        if (column < 1) {
            column++;
            position.x += 90;
        }
    }

    public void select() {
        // Selection will be handled by the game controller.
    }

    public void setSelectedZombieType(ZombieType zombieType) {
        selectedZombieType = zombieType;
    }

    public ZombieType getSelectedZombieType() {
        return selectedZombieType;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public void paint(Graphics brush) {
        Point[] points = getPoints();

        int[] x = new int[points.length];
        int[] y = new int[points.length];

        for (int i = 0; i < points.length; i++) {
            x[i] = (int) points[i].x;
            y[i] = (int) points[i].y;
        }

        brush.drawPolygon(x, y, points.length);
    }
}
