package game;

/*
CLASS: ZombieVsPlantsGame

DESCRIPTION: Main game class extending Game.

NOTE: This class is the metaphorical "main method" of your program,
      it is your control center.
*/

import game.units.Plant;
import game.units.Zombie;
import game.units.zombies.BasicZombie;
import game.units.zombies.FastZombie;
import game.units.zombies.TankZombie;
import game.units.zombies.ZombieType;
import game.units.plants.LayoutType;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class ZombiesVsPlantsGame extends Game {

    private static final int STARTING_MOONLIGHT = 250;

    private int level;
    private int wave;
    private Phase phase;
    private boolean gameComplete;
    private long lastPlantStrategyTime;

    private ArrayList<Zombie> zombies;
    private ArrayList<Zombie> prepZombies;
    private ArrayList<Plant> plants;

    private DeploymentCursor cursor;
    private PlantLayoutSelector layoutSelector;
    private InputHandler inputHandler;
    private WaveTimer waveTimer;
    private Battlefield battlefield;
    private DeploymentMenu deploymentMenu;

    private int moonlight = STARTING_MOONLIGHT;
    private ZombieType selectedZombie = ZombieType.BASIC;

    static int counter = 0;

    public ZombiesVsPlantsGame() {

        super("ZombiesVsPlants!", 1200, 750);

        this.setFocusable(true);
        this.requestFocus();

        level = 1;
        wave = 1;

        // The level starts in the Free phase.
        phase = Phase.FREE;
        gameComplete = false;
        lastPlantStrategyTime = System.currentTimeMillis();

        zombies = new ArrayList<Zombie>();
        prepZombies = new ArrayList<Zombie>();
        plants = new ArrayList<Plant>();

        battlefield = new Battlefield();
        deploymentMenu = new DeploymentMenu();
        layoutSelector = new PlantLayoutSelector();

        cursor = new DeploymentCursor(
            new Point[] {
                new Point(0, 0),
                new Point(20, 0),
                new Point(20, 20),
                new Point(0, 20)
            },
            new Point(85, 180),
            0
        );

        inputHandler = new InputHandler();
        waveTimer = new WaveTimer();

        this.addKeyListener(inputHandler);

        createInitialPlantLayout();
        waveTimer.start(60);
    }

    /**
     * Draws the battlefield, deployment menu, cursor,
     * plants, zombies, and win screen.
     */
    public void paint(Graphics brush) {

        updateGame();

        battlefield.paint(brush);

        deploymentMenu.paint(
            brush,
            moonlight,
            selectedZombie
        );

        // Show current level and wave.
        brush.setFont(new Font("Arial", Font.BOLD, 25));
        brush.drawString(
            "Level " + level + "  Wave " + wave,
            950,
            50
        );

        // Draw plants.
        for (Plant plant : plants) {

            if (!plant.isDead()) {
                plant.paint(brush);
            }
        }

        // Draw zombies.
        for (Zombie zombie : zombies) {

            if (!zombie.isDead()) {
                zombie.paint(brush);
            }
        }
        
        // Draw zombies waiting in Prep.
        for (Zombie zombie : prepZombies) {

            if (!zombie.isDead()) {
                zombie.paint(brush);
            }
        }

        // Draw deployment cursor during Free and Prep.
        if (phase == Phase.FREE || phase == Phase.PREP) {
            cursor.paint(brush);
        }

        // Draw win screen.
        if (phase == Phase.LEVEL_END) {

            brush.setFont(
                new Font("Arial", Font.BOLD, 50)
            );

            brush.drawString(
                "YOU WIN!",
                500,
                350
            );

            brush.setFont(
                new Font("Arial", Font.PLAIN, 25)
            );

            brush.drawString(
                "Zombie reached the Brain!",
                440,
                400
            );

            if (level < 3) {

                brush.drawString(
                    "Press ENTER for the next level",
                    425,
                    450
                );
            }
        }
        
        if (phase == Phase.FREE ||
        	    phase == Phase.PREP ||
        	    phase == Phase.WAVE) {

        	    String phaseText;

        	    if (phase == Phase.FREE) {
        	        phaseText = "FREE";
        	    } else if (phase == Phase.PREP) {
        	        phaseText = "PREP";
        	    } else {
        	        phaseText = "WAVE";
        	    }

        	    brush.setFont(
        	        new Font("Arial", Font.BOLD, 25)
        	    );

        	    brush.drawString(
        	        phaseText + "  Time: "
        	        + waveTimer.getRemainingSeconds(),
        	        950,
        	        80
        	    );
        }
        
        if (phase == Phase.LOSE) {

            brush.setFont(new Font("Arial", Font.BOLD, 40));

            brush.drawString(
                "YOU LOSE",
                350,
                300
            );

            brush.setFont(new Font("Arial", Font.BOLD, 20));

            brush.drawString(
                "Press ENTER to return to Level 1",
                300,
                350
            );
        }
        
        if (gameComplete) {

            brush.setFont(new Font("Arial", Font.BOLD, 40));

            brush.drawString(
                "GAME COMPLETE!",
                300,
                300
            );

            brush.setFont(new Font("Arial", Font.BOLD, 20));

            brush.drawString(
                "You completed all 3 levels!",
                300,
                350
            );
        }
    }

    /**
     * Updates the game state during the Wave phase.
     */
    public void updateGame() {
    	
    	if (phase == Phase.FREE || phase == Phase.WAVE) {

    	    long currentTime = System.currentTimeMillis();

    	    if (currentTime - lastPlantStrategyTime >= 15000) {

    	        if (plants.size() < 45) {

    	            LayoutType layoutType =
    	                    layoutSelector.analyzeZombies(zombies);

    	            ArrayList<Plant> newPlants =
    	                    layoutSelector.createLayout(
    	                            layoutType,
    	                            wave,
    	                            plants,
    	                            zombies
    	                    );

    	            plants.addAll(newPlants);
    	        }

    	        lastPlantStrategyTime = currentTime;
    	    }
    	}

        // Handle Free and Prep timers.
        if (phase == Phase.FREE) {

            if (waveTimer.isFinished()) {

                phase = Phase.PREP;

                waveTimer.start(30);
            }

        } else if (phase == Phase.PREP) {

            if (waveTimer.isFinished()) {

                startWave();
            }
        }

        // No combat during Prep.
        if (phase != Phase.FREE && phase != Phase.WAVE) {
            return;
        }

        // Update zombie cooldowns and movement.
        for (Zombie zombie : zombies) {

            if (zombie.isDead()) {
                continue;
            }

            zombie.updateCooldown();

            boolean blocked = false;

            for (Plant plant : plants) {

                if (plant.isDead()) {
                    continue;
                }

                double xDistance = Math.abs(
                        zombie.position.x - plant.position.x
                );

                double yDistance = Math.abs(
                        zombie.position.y - plant.position.y
                );

                /*
                 * A zombie is blocked when it reaches
                 * the same lane and gets close enough
                 * to the plant.
                 */
                if (xDistance <= 30 && yDistance <= 50) {

                    blocked = true;

                    boolean wasAlive = !plant.isDead();

                    zombie.attack(plant);

                    if (wasAlive && plant.isDead()) {
                        moonlight += 25;
                    }

                    break;
                }
            }

            if (!blocked) {
                zombie.move();
            }
        }

        // Update plant cooldowns and attacks.
        for (Plant plant : plants) {

            if (plant.isDead()) {
                continue;
            }

            plant.updateCooldown();

            Zombie target = null;
            double closestDistance = Double.MAX_VALUE;

            for (Zombie zombie : zombies) {

                if (zombie.isDead()) {
                    continue;
                }

                // Plants can only attack zombies in the same lane.
                double laneDistance = Math.abs(
                    zombie.position.y - plant.position.y
                );

                if (laneDistance > 50) {
                    continue;
                }

                // Check horizontal attack range.
                double distance = Math.abs(
                    zombie.position.x - plant.position.x
                );

                if (distance <= plant.getAttackRange()
                        && distance < closestDistance) {

                    closestDistance = distance;
                    target = zombie;
                }
            }

            if (target != null) {
                plant.attack(target);
            }
        }

        // Update zombie death animations.
        for (Zombie zombie : zombies) {

            if (zombie.isDead()) {
                zombie.updateDeathAnimation();
            }
        }

        // Update plant death animations.
        for (Plant plant : plants) {

            if (plant.isDead()) {
                plant.updateDeathAnimation();
            }
        }
        
        if (waveTimer.isFinished()) {
            nextWave();
            return;
        }

        // Check whether a zombie reached the Brain.
        if (checkWin()) {

            phase = Phase.LEVEL_END;
        }
    }

    /**
     * Starts the current Wave.
     *
     * The Wave can only start from the Prep phase.
     */
    public void startWave() {

        if (phase != Phase.PREP) {
            return;
        }

        /*
         * Move all zombies from the deployment board
         * to the battlefield.
         */
        for (Zombie zombie : prepZombies) {

            double deploymentX = zombie.position.x;

            // Convert deployment-board position to battlefield position.
            double battlefieldX =
                    Battlefield.BOARD_X
                    + Battlefield.DEPLOYMENT_WIDTH
                    + 20
                    + (deploymentX - 85);

            zombie.position.x = battlefieldX;
        }

        // Release all Prep zombies at the same time.
        zombies.addAll(prepZombies);
        prepZombies.clear();

        phase = Phase.WAVE;

        // Temporary Wave test time.
        waveTimer.start(30);
    }

    /**
     * Moves to the next Wave or next Level.
     */
    public void nextWave() {

        // Wave 1 -> Wave 2
        if (wave == 1) {
            wave = 2;
            phase = Phase.FREE;
            waveTimer.start(60);
            return;
        }

        // Wave 2 -> Wave 3
        if (wave == 2) {
            wave = 3;
            phase = Phase.FREE;
            waveTimer.start(60);
            return;
        }

        // Wave 3 ends without reaching the Brain -> Lose
        if (wave == 3) {
            phase = Phase.LOSE;
            waveTimer.reset();
        }
    }

    /**
     * Checks whether a zombie has reached the Brain.
     */
    private boolean checkWin() {

        for (Zombie zombie : zombies) {

            if (zombie.isDead()) {
                continue;
            }

            // Brain is on the right side of the battlefield.
            if (zombie.position.x >= Battlefield.BOARD_X
                    + Battlefield.BOARD_WIDTH) {

                return true;
            }
        }

        return false;
    }

    /**
     * Checks whether all zombies have died.
     */
    public boolean checkLose() {

        if (zombies.isEmpty()) {
            return false;
        }

        for (Zombie zombie : zombies) {

            if (!zombie.isDead()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Creates the initial plant layout.
     */
    private void createInitialPlantLayout() {

        LayoutType layoutType =
                layoutSelector.analyzeZombies(zombies);

        ArrayList<Plant> newPlants =
                layoutSelector.createLayout(
                        layoutType,
                        wave,
                        plants,
                        zombies
                );

        plants.addAll(newPlants);
    }

    /**
     * Places the currently selected zombie.
     */
    private void placeSelectedZombie() {

        // Zombies can only be summoned during Free or Prep.
        if (phase != Phase.FREE && phase != Phase.PREP) {
            return;
        }

        int cost = selectedZombie.getCost();

        if (moonlight < cost) {
            return;
        }

        double spawnX;
        double spawnY;

        if (phase == Phase.FREE) {

            // Free-time zombies enter from the left side
            // of the actual battlefield.
            spawnX = Battlefield.BOARD_X
                    + Battlefield.DEPLOYMENT_WIDTH
                    + 20;

            spawnY = Battlefield.BOARD_Y
                    + cursor.getRow() * 100
                    + 40;

        } else {

            // Prep zombies are stored on the deployment board.
            spawnX = cursor.position.x;

            spawnY = cursor.position.y;
        }

        Point spawnPosition = new Point(
                spawnX,
                spawnY
        );

        Zombie zombie = createZombie(
                selectedZombie,
                spawnPosition
        );

        if (zombie == null) {
            return;
        }

        if (phase == Phase.FREE) {

            zombies.add(zombie);

        } else if (phase == Phase.PREP) {

            prepZombies.add(zombie);
        }

        moonlight -= cost;
    }

    /**
     * Creates a zombie based on the selected type.
     */
    private Zombie createZombie(
            ZombieType type,
            Point position) {

        if (type == ZombieType.BASIC) {
            return new BasicZombie(position);
        }

        if (type == ZombieType.FAST) {
            return new FastZombie(position);
        }

        if (type == ZombieType.TANK) {
            return new TankZombie(position);
        }

        return null;
    }

    /**
     * Handles keyboard input.
     */
    private class InputHandler implements KeyListener {

        @Override
        public void keyPressed(KeyEvent e) {

            /*
             * If the level is over, Enter starts
             * the next level.
             */
            if (phase == Phase.LEVEL_END) {

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                    // Level 1 or Level 2: start the next level
                    if (level < 3) {

                        level++;
                        wave = 1;

                        zombies.clear();
                        plants.clear();
                        prepZombies.clear();
                        lastPlantStrategyTime = System.currentTimeMillis();

                        moonlight = STARTING_MOONLIGHT;

                        phase = Phase.FREE;

                        createInitialPlantLayout();

                        waveTimer.start(60);

                        repaint();

                    } else {
                    	gameComplete = true;
                        repaint();
                    }
                }

                return;
            }

            /*
             * If the game is lost, Enter returns
             * to Level 1.
             */
            if (phase == Phase.LOSE) {

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                    level = 1;
                    wave = 1;

                    zombies.clear();
                    plants.clear();
                    prepZombies.clear();
                    lastPlantStrategyTime = System.currentTimeMillis();

                    moonlight = STARTING_MOONLIGHT;

                    phase = Phase.FREE;

                    createInitialPlantLayout();

                    waveTimer.start(60);

                    repaint();
                }

                return;
            }

            switch (e.getKeyCode()) {

                case KeyEvent.VK_1:
                    selectedZombie = ZombieType.BASIC;
                    break;

                case KeyEvent.VK_2:
                    selectedZombie = ZombieType.FAST;
                    break;

                case KeyEvent.VK_3:
                    selectedZombie = ZombieType.TANK;
                    break;

                case KeyEvent.VK_UP:
                    cursor.moveUp();
                    break;

                case KeyEvent.VK_DOWN:
                    cursor.moveDown();
                    break;

                case KeyEvent.VK_LEFT:
                    cursor.moveLeft();
                    break;

                case KeyEvent.VK_RIGHT:
                    cursor.moveRight();
                    break;

                case KeyEvent.VK_SPACE:
                    placeSelectedZombie();
                    break;

                case KeyEvent.VK_ENTER:

                    /*
                     * Enter only starts a Wave from Prep.
                     * It cannot skip a Wave.
                     */
                    startWave();
                    break;

                default:
                    return;
            }

            repaint();
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }
    }

    /**
     * Controls Wave timing.
     */
    private class WaveTimer {

        private long endTime;

        /**
         * Starts a timer for the given number of seconds.
         */
        public void start(int seconds) {
            endTime = System.currentTimeMillis()
                    + seconds * 1000L;
        }

        /**
         * Returns whether the timer has finished.
         */
        public boolean isFinished() {
            return System.currentTimeMillis() >= endTime;
        }

        /**
         * Returns the remaining time in seconds.
         */
        public int getRemainingSeconds() {

            long remaining =
                    endTime - System.currentTimeMillis();

            if (remaining <= 0) {
                return 0;
            }

            return (int) Math.ceil(remaining / 1000.0);
        }

        /**
         * Resets the timer.
         */
        public void reset() {
            endTime = 0;
        }
    }

    public static void main(String[] args) {

        ZombiesVsPlantsGame game =
                new ZombiesVsPlantsGame();

        game.repaint();
    }
}