package game;

import game.units.zombies.ZombieType;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

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
        paint(brush, true);
    }

    /** Highlights the cell in red when the selected unit is too expensive. */
    public void paint(Graphics brush, boolean affordable) {
        int cellWidth = Battlefield.DEPLOYMENT_WIDTH / 2;
        int cellHeight = Battlefield.BOARD_HEIGHT / Battlefield.NUM_LANES;

        int x = Battlefield.BOARD_X + column * cellWidth;
        int y = Battlefield.BOARD_Y + row * cellHeight;

        Graphics2D highlight = (Graphics2D) brush.create();

        // Light tint inside the selected cell.
        highlight.setColor(affordable
                ? new Color(240, 215, 100, 45) : new Color(220, 85, 80, 45));
        highlight.fillRect(x + 2, y + 2, cellWidth - 4, cellHeight - 4);

        // Outline slightly inside the paving seams.
        highlight.setColor(affordable
                ? new Color(240, 215, 100) : new Color(220, 85, 80));
        highlight.setStroke(new BasicStroke(2));
        highlight.drawRect(x + 2, y + 2, cellWidth - 4, cellHeight - 4);

        highlight.dispose();
    }
}
