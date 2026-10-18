package game.units;

public interface Combatant {
    void takeDamage(int damage);
    boolean isDead();
}