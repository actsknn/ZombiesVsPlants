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
                                                                      
    /**                                                               
     * Attacks a zombie.                                              
     */                                                               
    public void attack(Zombie z) {                                    
        if (dying || z == null || z.isDead()) {                       
            return;                                                   
        }                                                             
                                                                      
        if (currentCooldown > 0) {                                    
            return;                                                   
        }                                                             
                                                                      
        if (damage > 0) {                                             
            z.takeDamage(damage);                                     
        }                                                             
                                                                      
        currentCooldown = attackCooldown;                             
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
            dying = true;                                             
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
        // Death animation will be added later.                       
    }                                                                 
                                                                      
    /**                                                               
     * Updates the plant's attack cooldown.                           
     */                                                               
    public void updateCooldown() {                                    
        if (currentCooldown > 0) {                                    
            currentCooldown--;                                        
        }                                                             
    }                                                                 
                                                                      
    /**                                                               
     * Draws the plant using its Polygon shape.                       
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
        brush.drawString(
        	    "HP: " + currentHP,
        	    (int) position.x,
        	    (int) position.y - 5
        	);
   }
                                                               
                                                                     
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