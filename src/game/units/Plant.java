package game.units;

import game.Point;
import game.Polygon;
import java.awt.Graphics;

public abstract class Plant extends Polygon implements Combatant {

    private int maxHP;
    private int currentHP;
    private int damage;
    private double attackRange;
    private int attackCooldown;
    private boolean dying;
    private int deathFrames;
    private double scale;

    private int currentCooldown;

    public Plant(Point[] shape, Point position, int maxHP, int damage,
            double attackRange, int attackCooldown, double scale) {

        super(shape, position, 0);

        this.maxHP = maxHP;
        this.currentHP = maxHP;
        this.damage = damage;
        this.attackRange = attackRange;
        this.attackCooldown = attackCooldown;

        this.dying = false;
        this.scale = scale;

        this.currentCooldown = 0;
    }

    public Pea shoot(){
        if(dying || damage <= 0 || currentCooldown > 0){
            return null;
        }

        currentCooldown = attackCooldown;

        Point muzz = new Point(position.x - Pea.SIZE, position.y + 21);

        return new Pea(muzz, damage);
    }

    /**
     * Reduces the plant's HP.
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
     * Returns whether the plant is dead.
     */
    @Override
    public boolean isDead() {
        return dying;
    }

    /**
     * Handles the plant's death.
     */
    public void die() {
        dying = true;
    }

    /**
     * Updates the plant's death animation.
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
     * Updates the plant's attack cooldown.
     */
    public void updateCooldown() {
        if (currentCooldown > 0) {
            currentCooldown--;
        }
    }

    /** Draws the specific plant sprite supplied by each subclass. */
    public abstract void paint(Graphics brush);

    public int getDamage() {
        return damage;
    }

    public double getAttackRange() {
        return attackRange;
    }

    public int getAttackCooldown() {
        return attackCooldown;
    }

    public double getScale() {
        return scale;
    }

    public int getCurrentCooldown() {
        return currentCooldown;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public int getMaxHP() {
        return maxHP;
    }
}
