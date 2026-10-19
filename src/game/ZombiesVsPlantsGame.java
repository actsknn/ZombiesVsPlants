package game;

/*
CLASS: ZombieVsPlantsGame

        DESCRIPTION: Main game class extending Game.

        NOTE: This class is the metaphorical "main method" of your program,
        it is your control center.
         */

import game.units.Plant;
import game.units.Pea;
import game.units.Zombie;
import game.units.plants.LayoutType;
import game.units.zombies.BasicZombie;
import game.units.zombies.FastZombie;
import game.units.zombies.TankZombie;
import game.units.zombies.ZombieType;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class ZombiesVsPlantsGame extends Game {
    private static final int STARTING_MOONLIGHT = 250;
    private static final int WAVES_PER_LEVEL = 3;
    private static final int TOTAL_LEVELS = 3;
    private static final int PLANNING_SECONDS = 30;

    private int level;
    private int wave;
    private Phase phase;
    private boolean gameComplete;
    private boolean initialized;

    private ArrayList<Zombie> zombies;
    private ArrayList<Zombie> prepZombies;
    private ArrayList<Plant> plants;
    private ArrayList<Pea> peas;

    private DeploymentCursor cursor;
    private PlantLayoutSelector layoutSelector;
    private InputHandler inputHandler;
    private WaveTimer waveTimer;
    private Battlefield battlefield;
    private DeploymentMenu deploymentMenu;

    private int moonlight = STARTING_MOONLIGHT;
    private ZombieType selectedZombie = ZombieType.BASIC;
    private Zombie previewZombie;

    static int counter = 0;

    public ZombiesVsPlantsGame() {
        super("ZombiesVsPlants!", 1200, 750);

        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);

        level = 1;
        wave = 1;

        // The level starts in the Planning phase.
        phase = Phase.PLANNING;
        gameComplete = false;

        zombies = new ArrayList<Zombie>();
        prepZombies = new ArrayList<Zombie>();
        plants = new ArrayList<Plant>();
        peas = new ArrayList<Pea>();

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
                new Point(85, 160),
                0);

        inputHandler = new InputHandler();
        waveTimer = new WaveTimer();
        previewZombie = createZombie(selectedZombie, new Point(0, 0));

        this.addKeyListener(inputHandler);
        beginPlanning();
        initialized = true;

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                requestFocusInWindow();
            }
        });
        Window window = getGameWindow();
        if (window != null) {
            window.addWindowFocusListener(new WindowAdapter() {
                @Override
                public void windowGainedFocus(WindowEvent e) {
                    requestFocusInWindow();
                }
            });
        }
        EventQueue.invokeLater(() -> requestFocusInWindow());
    }

    /** Finds the window containing this canvas. */
    private Window getGameWindow() {
        Container parent = getParent();
        while (parent != null && !(parent instanceof Window)) {
            parent = parent.getParent();
        }
        return (Window) parent;
    }

    /**
     * Draws the battlefield, deployment menu, cursor,
     * plants, zombies, and win screen.
     */
    public void paint(Graphics brush) {
        // AWT may request a paint while the constructor is still running.
        if (!initialized) {
            return;
        }
        brush.setFont(new Font("Dialog", Font.PLAIN, 12));

        updateGame();

        battlefield.paint(brush);

        deploymentMenu.paint(brush, moonlight, selectedZombie);

        // Show current level and wave.
        brush.setFont(new Font("Arial", Font.BOLD, 25));
        brush.drawString("Level " + level + "  Wave " + wave, 950, 50);

        // Draw plants.
        for (Plant plant : plants) {
            if (!plant.isDead()) {
                plant.paint(brush);
            }
        }

        // Draw peas.
        for (Pea pea : peas) {
            pea.paint(brush);
        }

        // Draw zombies.
        for (Zombie zombie : zombies) {
            if (!zombie.isDead()) {
                zombie.paint(brush);
            }
        }

        // Draw zombies waiting in Planning.
        for (Zombie zombie : prepZombies) {
            if (!zombie.isDead()) {
                zombie.paint(brush);
            }
        }

        // Draw deployment cursor during Planning.
        if (phase == Phase.PLANNING) {
            cursor.paint(brush, moonlight >= selectedZombie.getCost());
            paintZombiePreview(brush);
            brush.setFont(new Font("Dialog", Font.PLAIN, 14));
            brush.drawString("WASD / arrows: move   Enter: place   Backspace: refund   Space: start wave",
                    Battlefield.BOARD_X, Battlefield.BOARD_Y + Battlefield.BOARD_HEIGHT + 35);
        }

        if (phase == Phase.WAVE_END) {
            brush.setFont(new Font("Arial", Font.BOLD, 40));
            brush.drawString("WAVE " + wave + " COMPLETE!", 375, 350);
            brush.setFont(new Font("Arial", Font.PLAIN, 25));
            brush.drawString("Press ENTER to plan wave " + (wave + 1), 390, 400);
        }

        // Draw level results after all three waves have succeeded.
        if (phase == Phase.LEVEL_END && !gameComplete) {
            brush.setFont(new Font("Arial", Font.BOLD, 50));

            brush.drawString("LEVEL COMPLETE!", 375, 350);

            brush.setFont(new Font("Arial", Font.PLAIN, 25));

            brush.drawString("All " + WAVES_PER_LEVEL + " waves completed!", 425, 400);

            if (level < TOTAL_LEVELS) {
                brush.drawString("Press ENTER for the next level", 425, 450);
            } else {
                brush.drawString("Press ENTER to finish the game", 425, 450);
            }

        }

        if (phase == Phase.PLANNING || phase == Phase.WAVE) {
            String phaseText = phase == Phase.PLANNING
                    ? "Planning: " + waveTimer.getRemainingSeconds() : "Wave in progress";
            brush.setFont(new Font("Arial", Font.BOLD, 25));
            brush.drawString(phaseText, 950, 80);
        }
        if (phase == Phase.LOSE) {
            brush.setFont(new Font("Arial", Font.BOLD, 40));

            brush.drawString("YOU LOSE", 350, 300);

            brush.setFont(new Font("Arial", Font.BOLD, 20));

            brush.drawString("Press ENTER to return to Level 1", 300, 350);
        }

        if (gameComplete) {
            brush.setFont(new Font("Arial", Font.BOLD, 40));

            brush.drawString("GAME COMPLETE!", 300, 300);

            brush.setFont(new Font("Arial", Font.BOLD, 20));

            brush.drawString("You completed all 3 levels!", 300, 350);
        }
    }

    /**
     * Updates the game state during the Wave phase.
     */
    public void updateGame() {
        if (phase == Phase.PLANNING) {
            if (waveTimer.isFinished()) {
                if (prepZombies.isEmpty()) {
                    waveTimer.start(PLANNING_SECONDS);
                } else {
                    startWave();
                }
            }
            return;
        }
        if (phase != Phase.WAVE) {
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

                double xDistance = Math.abs(zombie.position.x - plant.position.x);

                double yDistance = Math.abs(zombie.position.y - plant.position.y);

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
                double laneDistance = Math.abs(zombie.position.y - plant.position.y);

                if (laneDistance > 50) {
                    continue;
                }

                // Check horizontal attack range.
                double distance = Math.abs(zombie.position.x - plant.position.x);

                if (distance <= plant.getAttackRange()
                && distance < closestDistance) {
                    closestDistance = distance;
                    target = zombie;
                }
            }

            if (target != null) {
                Pea pea = plant.shoot();
                if (pea != null) {
                    peas.add(pea);
                }
            }
        }

        // Work backward so removing a pea does not skip the next one.
        for (int i = peas.size() - 1; i >= 0; i--) {
            Pea pea = peas.get(i);
            pea.move();

            boolean hit = false;
            for (Zombie zombie : zombies) {
                if (!zombie.isDead() && pea.collides(zombie)) {
                    zombie.takeDamage(pea.getDamage());
                    hit = true;
                    break;
                }
            }

            if (hit || pea.position.x + Pea.SIZE < Battlefield.BOARD_X) {
                peas.remove(i);
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

        // Check whether a zombie reached the Brain.
        if (checkWin()) {
            phase = wave < WAVES_PER_LEVEL ? Phase.WAVE_END : Phase.LEVEL_END;
        } else if (checkLose()) {
            phase = Phase.LOSE;
        }
    }

    /**
     * Starts the current Wave.
     *
     * The Wave can only start from the Planning phase.
     */
    public void startWave() {
        if (phase != Phase.PLANNING || prepZombies.isEmpty()) {
            return;
        }

        zombies.addAll(prepZombies);
        prepZombies.clear();
        createInitialPlantLayout();

        phase = Phase.WAVE;
    }

    /** Advances after a successful wave and resets the board for planning. */
    public void nextWave() {
        if (phase != Phase.WAVE_END || wave >= WAVES_PER_LEVEL) {
            return;
        }
        wave++;
        beginPlanning();
    }

    /** Starts a fresh planning phase with a cleared board and a new budget. */
    private void beginPlanning() {
        zombies.clear();
        prepZombies.clear();
        plants.clear();
        peas.clear();
        if(wave == 1){
			moonlight = STARTING_MOONLIGHT;
		}
		else moonlight += STARTING_MOONLIGHT;
        gameComplete = false;
        phase = Phase.PLANNING;
        waveTimer.start(PLANNING_SECONDS);
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
        LayoutType layoutType = layoutSelector.analyzeZombies(zombies);

        ArrayList<Plant> newPlants = 
			layoutSelector.createLayout(layoutType, level, wave);

        plants.addAll(newPlants);
    }

    /**
     * Places the currently selected zombie.
     */
    private void placeSelectedZombie() {
        if (phase != Phase.PLANNING || moonlight < selectedZombie.getCost()) {
            return;
        }
        if (getZombieInSelectedCell() != null) {
            return;
        }
        Zombie zombie = createZombie(selectedZombie, getDeploymentPosition());
        if (zombie != null) {
            if (prepZombies.isEmpty() && waveTimer.isFinished()) {
                waveTimer.start(PLANNING_SECONDS);
            }
            prepZombies.add(zombie);
            moonlight -= selectedZombie.getCost();
        }
    }

    /** Returns the sprite position centered in the selected deployment cell. */
    private Point getDeploymentPosition() {
        int cellWidth = Battlefield.DEPLOYMENT_WIDTH / 2;
        int cellHeight = Battlefield.BOARD_HEIGHT / Battlefield.NUM_LANES;
        return new Point(
                Battlefield.BOARD_X + cursor.getColumn() * cellWidth + (cellWidth - 48) / 2,
                Battlefield.BOARD_Y + cursor.getRow() * cellHeight + (cellHeight - 60) / 2);
    }

    /** Finds a queued zombie in the highlighted cell. */
    private Zombie getZombieInSelectedCell() {
        Point position = getDeploymentPosition();
        for (Zombie zombie : prepZombies) {
            if (zombie.position.x == position.x && zombie.position.y == position.y) {
                return zombie;
            }
        }
        return null;
    }

    /** Paints a separate ghost sprite without placing or activating it. */
    private void paintZombiePreview(Graphics brush) {
        if (getZombieInSelectedCell() != null) {
            return;
        }
        previewZombie.position = getDeploymentPosition();
        Graphics2D ghostBrush = (Graphics2D) brush.create();
        ghostBrush.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.45f));
        previewZombie.paint(ghostBrush);
        ghostBrush.dispose();
    }

    /** Removes a queued zombie and refunds its own type's cost. */
    private void removeSelectedZombie() {
        if (phase != Phase.PLANNING) {
            return;
        }
        Zombie zombie = getZombieInSelectedCell();
        if (zombie == null) {
            return;
        }
        if (zombie instanceof BasicZombie) {
            moonlight += ZombieType.BASIC.getCost();
        } else if (zombie instanceof FastZombie) {
            moonlight += ZombieType.FAST.getCost();
        } else if (zombie instanceof TankZombie) {
            moonlight += ZombieType.TANK.getCost();
        }
        prepZombies.remove(zombie);
    }

    /** Changes the selected zombie and refreshes the preview. */
    private void selectZombie(ZombieType type) {
        selectedZombie = type;
        previewZombie = createZombie(type, getDeploymentPosition());
    }
    /**
     * Creates a zombie based on the selected type.
     */
    private Zombie createZombie(ZombieType type, Point position) {
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
            if (gameComplete) {
                return;
            }
            if (phase == Phase.WAVE_END) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    nextWave();
                    repaint();
                }
                return;
            }
            /*
             * If the level is over, Enter starts
             * the next level.
             */
            if (phase == Phase.LEVEL_END) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Level 1 or Level 2: start the next level
                    if (level < TOTAL_LEVELS) {
                        level++;
                        wave = 1;
                        beginPlanning();

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
                    beginPlanning();

                    repaint();
                }

                return;
            }

            switch (e.getKeyCode()) {
                case KeyEvent.VK_1:
                    selectZombie(ZombieType.BASIC);
                    break;

                case KeyEvent.VK_2:
                    selectZombie(ZombieType.FAST);
                    break;

                case KeyEvent.VK_3:
                    selectZombie(ZombieType.TANK);
                    break;

                case KeyEvent.VK_W:
                case KeyEvent.VK_UP:
                    cursor.moveUp();
                    break;

                case KeyEvent.VK_S:
                case KeyEvent.VK_DOWN:
                    cursor.moveDown();
                    break;

                case KeyEvent.VK_A:
                case KeyEvent.VK_LEFT:
                    cursor.moveLeft();
                    break;

                case KeyEvent.VK_D:
                case KeyEvent.VK_RIGHT:
                    cursor.moveRight();
                    break;

                case KeyEvent.VK_SPACE:
                    startWave();
                    break;
                case KeyEvent.VK_ENTER:
                    placeSelectedZombie();
                    break;
                case KeyEvent.VK_BACK_SPACE:
                    removeSelectedZombie();
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
            long remaining = endTime - System.currentTimeMillis();

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
        EventQueue.invokeLater(() -> {
            ZombiesVsPlantsGame game = new ZombiesVsPlantsGame();
            game.repaint();
        });
    }
}
