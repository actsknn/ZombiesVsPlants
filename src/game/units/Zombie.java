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
    }

    public void move(){

    }

    public void attack(Plant p){

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

    public void paint(Graphics brush){

    }
}
