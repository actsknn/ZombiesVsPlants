package game;

import java.util.ArrayList;
import java.util.Random;

import game.units.Plant;
import game.units.Zombie;
import game.units.plants.LayoutType;
import game.units.plants.Peashooter;
import game.units.plants.RapidPlant;
import game.units.plants.WallPlant;

public class PlantLayoutSelector {

    private int offenseTotal;
    private int defenseTotal;

    private static final int NUM_LANES = 5;
    private static final int NUM_POSITIONS = 9;

    private Random random;

    public PlantLayoutSelector() {
        offenseTotal = 0;
        defenseTotal = 0;
        random = new Random();
    }

    /**
     * Analyzes the zombies and chooses a plant layout strategy.
     */
    public LayoutType analyzeZombies(ArrayList<Zombie> zombies) {

        offenseTotal = 0;
        defenseTotal = 0;

        if (zombies == null || zombies.isEmpty()) {
            return LayoutType.NEUTRAL;
        }

        for (Zombie zombie : zombies) {

            if (zombie == null || zombie.isDead()) {
                continue;
            }

            offenseTotal += zombie.getOffenseScore();
            defenseTotal += zombie.getDefenseScore();
        }

        if (offenseTotal > defenseTotal) {
            return LayoutType.DEFENSIVE;
        }

        if (defenseTotal > offenseTotal) {
            return LayoutType.OFFENSIVE;
        }

        return LayoutType.NEUTRAL;
    }

    /**
     * Creates three new plants based on the selected strategy.
     */
    public ArrayList<Plant> createLayout(
            LayoutType layoutType,
            int wave,
            ArrayList<Plant> currentPlants,
            ArrayList<Zombie> zombies) {

        ArrayList<Plant> newPlants = new ArrayList<Plant>();

        boolean[][] occupied = createOccupiedGrid(currentPlants);

        if (layoutType == LayoutType.OFFENSIVE) {

            int[] busiestLanes = getTwoBusiestLanes(zombies);

            // RapidPlant on the two busiest lanes.
            addRapidPlant(
                    newPlants,
                    occupied,
                    busiestLanes[0]
            );

            addRapidPlant(
                    newPlants,
                    occupied,
                    busiestLanes[1]
            );

            // Peashooter follows the normal random placement rule.
            addRandomPeashooter(
                    newPlants,
                    occupied
            );

        } else if (layoutType == LayoutType.DEFENSIVE) {

            int[] quietestLanes = getTwoQuietestLanes(zombies);

            // WallPlant on the two least populated lanes.
            addWallPlant(
                    newPlants,
                    occupied,
                    quietestLanes[0]
            );

            addWallPlant(
                    newPlants,
                    occupied,
                    quietestLanes[1]
            );

            // RapidPlant follows the random placement rule.
            addRandomRapidPlant(
                    newPlants,
                    occupied
            );

        } else {

            // NEUTRAL:
            // Two Peashooters and one WallPlant.
            addRandomPeashooter(
                    newPlants,
                    occupied
            );

            addRandomPeashooter(
                    newPlants,
                    occupied
            );

            addWallPlantRandomLane(
                    newPlants,
                    occupied
            );
        }

        return newPlants;
    }

    /**
     * Creates a grid showing which plant positions are occupied.
     */
    private boolean[][] createOccupiedGrid(
            ArrayList<Plant> currentPlants) {

        boolean[][] occupied =
                new boolean[NUM_LANES][NUM_POSITIONS];

        if (currentPlants == null) {
            return occupied;
        }

        for (Plant plant : currentPlants) {

            if (plant == null || plant.isDead()) {
                continue;
            }

            int lane = getLane(plant.position.y);
            int position = getPosition(plant.position.x);

            if (lane >= 0 && lane < NUM_LANES
                    && position >= 0
                    && position < NUM_POSITIONS) {

                occupied[lane][position] = true;
            }
        }

        return occupied;
    }

    /**
     * Returns the lane containing the most zombies.
     */
    private int[] getTwoBusiestLanes(ArrayList<Zombie> zombies) {

        int[] counts = new int[NUM_LANES];

        for (Zombie zombie : zombies) {
            if (zombie == null || zombie.isDead()) {
                continue;
            }

            int lane = getLane(zombie.position.y);

            if (lane >= 0 && lane < NUM_LANES) {
                counts[lane]++;
            }
        }

        ArrayList<Integer> candidates = new ArrayList<>();

        int highest = 0;

        for (int lane = 0; lane < NUM_LANES; lane++) {
            if (counts[lane] > highest) {
                highest = counts[lane];
            }
        }

        for (int lane = 0; lane < NUM_LANES; lane++) {
            if (counts[lane] == highest) {
                candidates.add(lane);
            }
        }

        int first = candidates.get(
                random.nextInt(candidates.size())
        );

        candidates.remove(Integer.valueOf(first));

        int second;

        if (candidates.isEmpty()) {
            // This should not normally happen because there are 5 lanes.
            second = first;
        } else {
            int secondHighest = 0;

            for (int lane : candidates) {
                if (counts[lane] > secondHighest) {
                    secondHighest = counts[lane];
                }
            }

            ArrayList<Integer> secondCandidates =
                    new ArrayList<>();

            for (int lane : candidates) {
                if (counts[lane] == secondHighest) {
                    secondCandidates.add(lane);
                }
            }

            second = secondCandidates.get(
                    random.nextInt(secondCandidates.size())
            );
        }

        return new int[] { first, second };
    }

    /**
     * Returns the two lanes containing the fewest zombies.
     */
    private int[] getTwoQuietestLanes(ArrayList<Zombie> zombies) {

        int[] counts = new int[NUM_LANES];

        for (Zombie zombie : zombies) {
            if (zombie == null || zombie.isDead()) {
                continue;
            }

            int lane = getLane(zombie.position.y);

            if (lane >= 0 && lane < NUM_LANES) {
                counts[lane]++;
            }
        }

        ArrayList<Integer> candidates = new ArrayList<>();

        int lowest = Integer.MAX_VALUE;

        for (int lane = 0; lane < NUM_LANES; lane++) {
            if (counts[lane] < lowest) {
                lowest = counts[lane];
            }
        }

        for (int lane = 0; lane < NUM_LANES; lane++) {
            if (counts[lane] == lowest) {
                candidates.add(lane);
            }
        }

        int first = candidates.get(
                random.nextInt(candidates.size())
        );

        candidates.remove(Integer.valueOf(first));

        int second;

        if (candidates.isEmpty()) {
            second = first;
        } else {
            int secondLowest = Integer.MAX_VALUE;

            for (int lane : candidates) {
                if (counts[lane] < secondLowest) {
                    secondLowest = counts[lane];
                }
            }

            ArrayList<Integer> secondCandidates =
                    new ArrayList<>();

            for (int lane : candidates) {
                if (counts[lane] == secondLowest) {
                    secondCandidates.add(lane);
                }
            }

            second = secondCandidates.get(
                    random.nextInt(secondCandidates.size())
            );
        }

        return new int[] { first, second };
    }

    /**
     * Counts zombies in each lane.
     */
    private int[] getZombieLaneCounts(
            ArrayList<Zombie> zombies) {

        int[] counts = new int[NUM_LANES];

        if (zombies == null) {
            return counts;
        }

        for (Zombie zombie : zombies) {

            if (zombie == null || zombie.isDead()) {
                continue;
            }

            int lane = getLane(zombie.position.y);

            if (lane >= 0 && lane < NUM_LANES) {
                counts[lane]++;
            }
        }

        return counts;
    }

    /**
     * Adds a RapidPlant on the right side of a lane.
     */
    private void addRapidPlant(
            ArrayList<Plant> plants,
            boolean[][] occupied,
            int lane) {

        int position = findRightmostFreePosition(
                occupied,
                lane
        );

        if (position == -1) {
            return;
        }

        plants.add(createRapidPlant(lane, position));
        occupied[lane][position] = true;
    }

    /**
     * Adds a WallPlant on the left side of a lane.
     */
    private void addWallPlant(
            ArrayList<Plant> plants,
            boolean[][] occupied,
            int lane) {

        int position = findLeftmostFreePosition(
                occupied,
                lane
        );

        if (position == -1) {
            return;
        }

        plants.add(createWallPlant(lane, position));
        occupied[lane][position] = true;
    }

    /**
     * Adds a WallPlant using a random lane.
     */
    private void addWallPlantRandomLane(
            ArrayList<Plant> plants,
            boolean[][] occupied) {

        ArrayList<Integer> availableLanes =
                getAvailableLanes(occupied);

        if (availableLanes.isEmpty()) {
            return;
        }

        int lane = availableLanes.get(
                random.nextInt(availableLanes.size())
        );

        addWallPlant(plants, occupied, lane);
    }

    /**
     * Adds a Peashooter using the right-side random rule.
     */
    private void addRandomPeashooter(
            ArrayList<Plant> plants,
            boolean[][] occupied) {

        ArrayList<Integer> availableLanes =
                getAvailableLanes(occupied);

        if (availableLanes.isEmpty()) {
            return;
        }

        int lane = availableLanes.get(
                random.nextInt(availableLanes.size())
        );

        int position = findPeashooterPosition(
                occupied,
                lane
        );

        if (position == -1) {
            return;
        }

        plants.add(
                createPeashooter(lane, position)
        );

        occupied[lane][position] = true;
    }

    /**
     * Adds a RapidPlant using a random lane.
     */
    private void addRandomRapidPlant(
            ArrayList<Plant> plants,
            boolean[][] occupied) {

        ArrayList<Integer> availableLanes =
                getAvailableLanes(occupied);

        if (availableLanes.isEmpty()) {
            return;
        }

        int lane = availableLanes.get(
                random.nextInt(availableLanes.size())
        );

        addRapidPlant(plants, occupied, lane);
    }

    /**
     * Peashooter starts from the rightmost position.
     * If the rightmost position is unavailable,
     * it continues toward the left.
     */
    private int findPeashooterPosition(boolean[][] occupied, int lane) {

        ArrayList<Integer> availablePositions = new ArrayList<>();

        for (int position = 0; position < NUM_POSITIONS; position++) {
            if (!occupied[lane][position]) {
                availablePositions.add(position);
            }
        }

        if (availablePositions.isEmpty()) {
            return -1;
        }

        return availablePositions.get(
                random.nextInt(availablePositions.size())
        );
    }

    /**
     * Finds the rightmost free position.
     */
    private int findRightmostFreePosition(
            boolean[][] occupied,
            int lane) {

        for (int position = NUM_POSITIONS - 1;
                position >= 0;
                position--) {

            if (!occupied[lane][position]) {
                return position;
            }
        }

        return -1;
    }

    /**
     * Finds the leftmost free position.
     */
    private int findLeftmostFreePosition(
            boolean[][] occupied,
            int lane) {

        for (int position = 0;
                position < NUM_POSITIONS;
                position++) {

            if (!occupied[lane][position]) {
                return position;
            }
        }

        return -1;
    }

    /**
     * Returns lanes that still have an available position.
     */
    private ArrayList<Integer> getAvailableLanes(
            boolean[][] occupied) {

        ArrayList<Integer> lanes =
                new ArrayList<Integer>();

        for (int lane = 0;
                lane < NUM_LANES;
                lane++) {

            if (findLeftmostFreePosition(
                    occupied,
                    lane) != -1) {

                lanes.add(lane);
            }
        }

        return lanes;
    }

    /**
     * Converts a Y coordinate to a lane.
     */
    private int getLane(double y) {

        int lane = (int) ((y - Battlefield.BOARD_Y) / 100);

        if (lane < 0) {
            return 0;
        }

        if (lane >= NUM_LANES) {
            return NUM_LANES - 1;
        }

        return lane;
    }

    /**
     * Converts an X coordinate to one of the nine plant positions.
     */
    private int getPosition(double x) {

        int battlefieldX =
                Battlefield.BOARD_X
                + Battlefield.DEPLOYMENT_WIDTH
                + 20;

        int battlefieldWidth =
                Battlefield.BOARD_WIDTH
                - Battlefield.DEPLOYMENT_WIDTH
                - 20;

        double leftMargin = 15;
        double rightMargin = 55;

        double spacing =
                (battlefieldWidth - leftMargin - rightMargin) / 8.0;

        int position = (int) Math.round(
                (x - (battlefieldX + leftMargin))
                / spacing
        );

        if (position < 0) {
            return 0;
        }

        if (position >= NUM_POSITIONS) {
            return NUM_POSITIONS - 1;
        }

        return position;
    }

    /**
     * Gets the X coordinate of a plant position.
     */
    private int getPositionX(int position) {

        int battlefieldX =
                Battlefield.BOARD_X
                + Battlefield.DEPLOYMENT_WIDTH
                + 20;

        int battlefieldWidth =
                Battlefield.BOARD_WIDTH
                - Battlefield.DEPLOYMENT_WIDTH
                - 20;

        // Leave a small margin on both sides.
        double leftMargin = 15;
        double rightMargin = 55;

        double spacing =
                (battlefieldWidth - leftMargin - rightMargin) / 8.0;

        return (int) (
                battlefieldX
                + leftMargin
                + position * spacing
        );
    }

    /**
     * Gets the Y coordinate of a lane.
     */
    private int getLaneY(int lane) {

        return Battlefield.BOARD_Y
                + lane * 100
                + 20;
    }

    private Peashooter createPeashooter(
            int lane,
            int position) {

        return new Peashooter(
                new Point(
                        getPositionX(position),
                        getLaneY(lane)
                )
        );
    }

    private RapidPlant createRapidPlant(
            int lane,
            int position) {

        return new RapidPlant(
                new Point(
                        getPositionX(position),
                        getLaneY(lane)
                )
        );
    }

    private WallPlant createWallPlant(
            int lane,
            int position) {

        return new WallPlant(
                new Point(
                        getPositionX(position),
                        getLaneY(lane)
                )
        );
    }
}
