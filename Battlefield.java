package game;

import java.awt.Color;
import java.awt.Graphics;

/** Draws the lawn and deployment paving for the five-lane battlefield. */
public class Battlefield {

    public static final int BOARD_X = 50;
    public static final int BOARD_Y = 140;
    public static final int BOARD_WIDTH = 850;
    public static final int BOARD_HEIGHT = 500;
    public static final int NUM_LANES = 5;
    public static final int DEPLOYMENT_WIDTH = 180;

    /** Paints the board without changing any game state. */
    public void paint(Graphics brush) {
        int laneHeight = BOARD_HEIGHT / NUM_LANES;
        int battlefieldX = BOARD_X + DEPLOYMENT_WIDTH + 20;
        int battlefieldWidth = BOARD_WIDTH - DEPLOYMENT_WIDTH - 20;

        // A quiet dusk background and a small shadow beneath the board.
        brush.setColor(new Color(57, 67, 64));
        brush.fillRect(0, 0, 1600, 850);
        brush.setColor(new Color(43, 51, 46));
        brush.fillRect(BOARD_X - 8, BOARD_Y + 8,
                BOARD_WIDTH + 24, BOARD_HEIGHT + 8);

        // Soil frames the lawn and fills the gap beside the paving.
        brush.setColor(new Color(100, 79, 57));
        brush.fillRect(BOARD_X - 8, BOARD_Y - 8,
                BOARD_WIDTH + 16, BOARD_HEIGHT + 16);

        // Concrete slabs in the zombie deployment area.
        brush.setColor(new Color(151, 151, 139));
        brush.fillRect(BOARD_X, BOARD_Y, DEPLOYMENT_WIDTH, BOARD_HEIGHT);
        for (int lane = 0; lane < NUM_LANES; lane++) {
            int y = BOARD_Y + lane * laneHeight;
            brush.setColor(new Color(166, 165, 152));
            brush.drawLine(BOARD_X + 2, y + 2,
                    BOARD_X + DEPLOYMENT_WIDTH - 2, y + 2);
            brush.setColor(new Color(116, 119, 109));
            brush.drawLine(BOARD_X, y, BOARD_X + DEPLOYMENT_WIDTH, y);
            brush.drawLine(BOARD_X + DEPLOYMENT_WIDTH / 2, y + 3,
                    BOARD_X + DEPLOYMENT_WIDTH / 2, y + laneHeight - 2);
        }

        // Alternating mowing strips keep the five lanes easy to read.
        for (int lane = 0; lane < NUM_LANES; lane++) {
            int y = BOARD_Y + lane * laneHeight;
            brush.setColor(lane % 2 == 0
                    ? new Color(99, 139, 76) : new Color(89, 128, 68));
            brush.fillRect(battlefieldX, y, battlefieldWidth, laneHeight);

            // Fixed positions prevent the grass details from flickering.
            for (int tuft = 0; tuft < (battlefieldWidth - 28) / 73; tuft++) {
                int x = battlefieldX + 28 + tuft * 73;
                int grassY = y + 25 + (tuft * 19 + lane * 13) % 65;
                brush.setColor(new Color(115, 151, 86));
                brush.drawLine(x, grassY, x - 2, grassY - 5);
                brush.drawLine(x, grassY, x + 3, grassY - 4);
            }

            brush.setColor(new Color(73, 108, 58));
            brush.drawLine(battlefieldX, y,
                    battlefieldX + battlefieldWidth, y);
        }

        // A narrow stone path marks the end of the lawn.
        int pathX = battlefieldX + battlefieldWidth + 10;
        brush.setColor(new Color(174, 160, 133));
        brush.fillRect(pathX, BOARD_Y - 8, 28, BOARD_HEIGHT + 16);
        brush.setColor(new Color(125, 116, 98));
        for (int y = BOARD_Y + 35; y < BOARD_Y + BOARD_HEIGHT; y += 55) {
            brush.drawLine(pathX, y, pathX + 27, y);
        }

        paintBrain(brush, pathX + 44);

        brush.setColor(new Color(79, 85, 73));
        brush.drawRect(BOARD_X, BOARD_Y, DEPLOYMENT_WIDTH, BOARD_HEIGHT);
        brush.setColor(new Color(60, 89, 49));
        brush.drawRect(battlefieldX, BOARD_Y, battlefieldWidth, BOARD_HEIGHT);

        brush.setColor(new Color(224, 222, 201));
        brush.drawString("Zombie Deployment", BOARD_X + 20, BOARD_Y - 22);
        brush.drawString("Dave's Brain",
                battlefieldX + battlefieldWidth - 90, BOARD_Y - 22);
    }

    /** Draws the goal: a big brain wearing Dave's frying pan. */
    private void paintBrain(Graphics brush, int x) {
        int y = BOARD_Y + BOARD_HEIGHT / 2 - 60;

        brush.setColor(new Color(43, 51, 46));
        brush.fillOval(x - 5, y + 128, 198, 26);

        // Overlapping lobes give the brain a soft, irregular outline.
        brush.setColor(new Color(162, 91, 109));
        brush.fillOval(x, y + 10, 100, 126);
        brush.fillOval(x + 77, y + 8, 108, 128);
        brush.fillOval(x + 25, y + 57, 137, 91);
        brush.setColor(new Color(219, 145, 159));
        brush.fillOval(x + 5, y + 5, 94, 119);
        brush.fillOval(x + 79, y + 3, 99, 121);
        brush.fillOval(x + 29, y + 53, 128, 86);

        // A center crease and a few curved folds keep the shape readable.
        brush.setColor(new Color(159, 86, 106));
        brush.drawArc(x + 76, y + 15, 25, 113, 90, 180);
        brush.drawArc(x + 17, y + 28, 48, 37, 20, 240);
        brush.drawArc(x + 32, y + 61, 46, 43, 110, 230);
        brush.drawArc(x + 16, y + 85, 36, 32, 80, 220);
        brush.drawArc(x + 108, y + 25, 48, 40, -60, 250);
        brush.drawArc(x + 108, y + 66, 48, 39, -130, 240);
        brush.drawArc(x + 69, y + 105, 54, 25, 180, 180);

        // Upside-down pan: rounded metal bowl, broad rim, and long handle.
        brush.setColor(new Color(45, 49, 50));
        brush.fillRoundRect(x + 133, y - 14, 76, 14, 8, 8);
        brush.setColor(new Color(113, 122, 125));
        brush.fillArc(x + 17, y - 46, 139, 93, 0, 180);
        brush.fillRect(x + 17, y, 139, 12);
        brush.setColor(new Color(158, 166, 166));
        brush.drawArc(x + 28, y - 38, 115, 76, 35, 110);
        brush.setColor(new Color(64, 71, 73));
        brush.fillOval(x + 8, y + 4, 158, 14);
    }
}
