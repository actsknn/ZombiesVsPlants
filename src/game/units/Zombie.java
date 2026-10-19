package game.units;

import java.awt.Graphics;

import game.Point;
import game.Polygon;

public class Zombie extends Polygon implements Combatant {

    private int maxHP;
    private int currentHP;
    private int damage;
    private int speed;
    private double attackRange;
    private int attackCooldown;
    private int offenseScore;
    private int defenseScore;
    private boolean dying;
    private double scale;

    private int currentCooldown;

    public Zombie(Point[] shape, Point position, int maxHP, int damage,
            int speed, double attackRange, int attackCooldown, double scale,
            int offensiveScore, int defensiveScore) {

        super(shape, position, 0);

        this.maxHP = maxHP;
        this.currentHP = maxHP;
        this.damage = damage;
        this.speed = speed;
        this.attackRange = attackRange;
        this.attackCooldown = attackCooldown;

        this.offenseScore = offensiveScore;
        this.defenseScore = defensiveScore;

        this.dying = false;
        this.scale = scale;

        this.currentCooldown = 0;
    }

    /**
     * Moves the zombie forward.
     */
    public void move() {
        if (!dying) {
            position.x += speed;
        }
    }

    /**
     * Attacks a plant.
     */
    public void attack(Plant p) {

        if (dying || p == null || p.isDead()) {
            return;
        }

        if (currentCooldown > 0) {
            return;
        }

        p.takeDamage(damage);

        currentCooldown = attackCooldown;
    }

    /**
     * Reduces the zombie's HP.
     */
    @Override
    public void takeDamage(int damage) {
        if (dying) {
            return;
        }

        currentHP -= damage;

        if (currentHP <= 0) {
            currentHP = 0;
            dying = true;
            die();
        }
    }

    /**
     * Returns whether the zombie is dead.
     */
    @Override
    public boolean isDead() {
        return dying;
    }

    /**
     * Handles the zombie's death.
     */
    public void die() {
        dying = true;
    }

    /**
     * Updates the zombie's death animation.
     */
    public void updateDeathAnimation() {
        // Death animation will be added later.
    }

    /**
     * Updates the zombie's attack cooldown.
     */
    public void updateCooldown() {
        if (currentCooldown > 0) {
            currentCooldown--;
        }
    }

    /**
     * Draws the zombie using its Polygon shape.
     */
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

    public int getMaxHP() {
        return maxHP;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public int getDamage() {
        return damage;
    }

    public int getSpeed() {
        return speed;
    }

    public double getAttackRange() {
        return attackRange;
    }

    public int getAttackCooldown() {
        return attackCooldown;
    }

    public int getOffenseScore() {
        return offenseScore;
    }

    public int getDefenseScore() {
        return defenseScore;
    }

    public double getScale() {
        return scale;
    }

    public int getCurrentCooldown() {
        return currentCooldown;
    }
}
