package game.units.zombies;

/** Available zombie types and their moonlight placement costs. */
public enum ZombieType {
    BASIC(50),
    FAST(75),
    TANK(125);

    private final int cost;

    ZombieType(int cost) {
        this.cost = cost;
    }

    /** Returns the moonlight required to place this zombie type. */
    public int getCost() {
        return cost;
    }
}

