package game;

import game.units.zombies.ZombieType;
import java.awt.Graphics;

public class DeploymentCursor extends Polygon {
    private int row;
    private int column;
    private ZombieType selectedZombieType;

    public DeploymentCursor(Point[] shape, Point position, double rotation) {
        super(shape, position, rotation);
    }

    public void moveUp() {
    }

    public void moveDown() {
    }

    public void moveLeft() {
    }

    public void moveRight() {
    }

    public void select() {
    }

    public void paint(Graphics brush) {
    }
}
