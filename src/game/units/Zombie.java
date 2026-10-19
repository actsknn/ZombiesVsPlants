package game.units;

import java.awt.Graphics;

import game.Point;
import game.Polygon;

public abstract class Zombie extends Polygon implements Combatant {

    private int maxHP;
    private int currentHP;
    private int damage;
    private int speed;
    private double attackRange;
    private int attackCooldown;
    private int offenseScore;
    private int defenseScore;
    private boolean dying;
    private int deathFrames;
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
        if (dying && deathFrames <= 30) {
            if (deathFrames < 30) {
                rotate(-3);
            }
            deathFrames++;
        }
    }

    /** Returns true after the quarter-turn has been displayed. */
    public boolean isDeathAnimationFinished() {
        return dying && deathFrames > 30;
    }

    /**
     * Updates the zombie's attack cooldown.
     */
    public void updateCooldown() {
        if (currentCooldown > 0) {
            currentCooldown--;
        }
    }

    /** Draws the specific zombie sprite supplied by each subclass. */
    public abstract void paint(Graphics brush);

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
