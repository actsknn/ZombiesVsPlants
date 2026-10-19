package game;

import game.units.Zombie;
import game.units.zombies.BasicZombie;
import game.units.zombies.FastZombie;
import game.units.zombies.TankZombie;
import game.units.zombies.ZombieType;
import java.awt.Color;
import java.awt.Graphics;

/** A small keyboard selection bar above the deployment board. */
public class DeploymentMenu {
    private final Zombie[] previews = {
        new BasicZombie(new Point(231, 28)),
        new FastZombie(new Point(411, 28)),
        new TankZombie(new Point(591, 28))
    };

    /** Draws the current budget and the three available zombie cards. */
    public void paint(Graphics brush, int moonlight, ZombieType selected) {
        Graphics menuBrush = brush.create();
        menuBrush.setColor(new Color(43, 51, 46));
        menuBrush.fillRect(50, 18, 710, 84);

        // A crescent made from two overlapping circles.
        menuBrush.setColor(new Color(225, 226, 188));
        menuBrush.fillOval(65, 32, 27, 27);
        menuBrush.setColor(new Color(43, 51, 46));
        menuBrush.fillOval(75, 27, 25, 25);
        menuBrush.setColor(new Color(233, 230, 204));
        menuBrush.drawString("Moonlight", 105, 44);
        menuBrush.drawString(Integer.toString(moonlight), 105, 66);

        String[] names = {"Basic", "Fast", "Tank"};
        ZombieType[] types = ZombieType.values();
        for (int i = 0; i < previews.length; i++) {
            int x = 220 + i * 180;
            menuBrush.setColor(new Color(67, 77, 65));
            menuBrush.fillRect(x, 24, 170, 72);
            menuBrush.setColor(types[i] == selected
                    ? new Color(225, 208, 117) : new Color(112, 123, 103));
            menuBrush.drawRect(x, 24, 170, 72);
            if (types[i] == selected) {
                menuBrush.drawRect(x + 1, 25, 168, 70);
            }
            previews[i].paint(menuBrush);
            menuBrush.setColor(moonlight >= types[i].getCost()
                    ? new Color(233, 230, 204) : new Color(155, 157, 144));
            menuBrush.drawString((i + 1) + "  " + names[i], x + 66, 48);
            menuBrush.drawString(types[i].getCost() + " moonlight", x + 66, 72);
        }
        menuBrush.setColor(new Color(224, 222, 201));
        menuBrush.drawString("1 / 2 / 3: select zombie", 790, 49);
        menuBrush.drawString("Zombie deployment", 790, 72);
        menuBrush.dispose();
    }
}
