package game.units;

import java.awt.Graphics;
import game.Point;
import game.Polygon;

public class Plant extends Polygon implements Combatant {
    private int maxHP;
    private int currentHP;
    private int damage;
    private double attackRange;
    private int attackCooldown;
    private boolean dying;
    private double scale;

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
    }

    public void attack(Zombie z){

    }

    public void takeDamage(int damage) {
        currentHP -= damage;
        if (currentHP <= 0) {
            dying = true;
        }
    }

    public boolean isDead() {
        return dying;
    }

    public void die(){

    }

    public void updateDeathAnimation(){

    }

    public void paint(Graphics brush) {

    }

}
