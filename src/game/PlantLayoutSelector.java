package game;

import game.units.Plant;
import game.units.Zombie;
import game.units.plants.LayoutType;
import java.util.ArrayList;
import game.units.plants.Peashooter;
import game.units.plants.RapidPlant;
import game.units.plants.WallPlant;

/** Chooses the plant strategy that counters the player's zombie composition. */
public class PlantLayoutSelector {

    /** Returns the counter-strategy based on living zombies' offense and defense. */
    public LayoutType analyzeZombies(ArrayList<Zombie> zombies) {
        int offenseTotal = 0;
        int defenseTotal = 0;

        if (zombies != null) {
            for (Zombie zombie : zombies) {
                if (zombie != null && !zombie.isDead()) {
                    offenseTotal += zombie.getOffenseScore();
                    defenseTotal += zombie.getDefenseScore();
                }
            }
        }

        if (offenseTotal > defenseTotal) {
            return LayoutType.DEFENSIVE;
        }
        if (defenseTotal > offenseTotal) {
            return LayoutType.OFFENSIVE;
        }
        return LayoutType.NEUTRAL;
    }

    /** Builds a fixed layout for the selected strategy, level, and wave. */
    public ArrayList<Plant> createLayout(LayoutType type, int level, int wave) {
       ArrayList<Plant> plants = new ArrayList<Plant>();
        if(level == 1){
            switch (wave){
                case 1:
                    addWaveOne(plants, type);
                    break;
                case 2:
                    addWaveTwo(plants, type);
                    break;
                case 3:
                    addWaveThree(plants, type);
                    break;
            }
        }
        else if (level == 2) {
            addLevelTwo(plants, type, wave);
        } else if (level == 3) {
            addLevelThree(plants, type, wave);
        }
        return plants;
    }

        private void addWaveOne(ArrayList<Plant> plants, LayoutType type) {
        plants.add(new Peashooter(plantPosition(1, 7)));

        if (type == LayoutType.DEFENSIVE) {
            plants.add(new WallPlant(plantPosition(1, 5)));
        }
        else {
            plants.add(new Peashooter(plantPosition(3, 7)));

            if (type == LayoutType.OFFENSIVE) {
                plants.add(new Peashooter(plantPosition(2, 6)));
            }
        }
    }

        private void addWaveTwo(ArrayList<Plant> plants, LayoutType type) {
        plants.add(new Peashooter(plantPosition(0, 7)));
        plants.add(new Peashooter(plantPosition(4, 7)));

        if (type == LayoutType.DEFENSIVE) {
            plants.add(new WallPlant(plantPosition(0, 4)));
            plants.add(new WallPlant(plantPosition(4, 4)));
        } else {
            plants.add(new Peashooter(plantPosition(2, 7)));

            if (type == LayoutType.OFFENSIVE) {
                plants.add(new RapidPlant(plantPosition(1, 6)));
            } else {
                plants.add(new WallPlant(plantPosition(2, 4)));
            }
        }
    }

        private void addWaveThree(ArrayList<Plant> plants, LayoutType type) {
        for (int lane = 0; lane < Battlefield.NUM_LANES; lane++) {
            if (type == LayoutType.OFFENSIVE && (lane == 1 || lane == 3)) {
                plants.add(new RapidPlant(plantPosition(lane, 7)));
            } else {
                plants.add(new Peashooter(plantPosition(lane, 7)));
            }
        }

        if (type == LayoutType.NEUTRAL) {
            plants.add(new WallPlant(plantPosition(1, 4)));
            plants.add(new WallPlant(plantPosition(3, 4)));
        } else if (type == LayoutType.DEFENSIVE) {
            plants.add(new WallPlant(plantPosition(0, 4)));
            plants.add(new WallPlant(plantPosition(2, 4)));
            plants.add(new WallPlant(plantPosition(4, 4)));
        }
    }

    /** Covers every lane, adding stronger shooters and more walls each wave. */
    private void addLevelTwo(ArrayList<Plant> plants, LayoutType type, int wave) {
        int rapidCount;
        int wallCount;

        if (type == LayoutType.OFFENSIVE) {
            rapidCount = wave + 1;
            wallCount = wave - 1;
        } else if (type == LayoutType.DEFENSIVE) {
            rapidCount = 0;
            wallCount = wave + 2;
        } else {
            rapidCount = wave - 1;
            wallCount = wave + 1;
        }

        // This fixed order spreads support across the middle and outside lanes.
        int[] lanes = {1, 3, 2, 0, 4};
        for (int i = 0; i < lanes.length; i++) {
            int lane = lanes[i];
            if (i < rapidCount) {
                plants.add(new RapidPlant(plantPosition(lane, 7)));
            } else {
                plants.add(new Peashooter(plantPosition(lane, 7)));
            }
            if (i < wallCount) {
                plants.add(new WallPlant(plantPosition(lane, 4)));
            }
        }
    }

    /** Adds deeper formations, with extra rear shooters in waves two and three. */
    private void addLevelThree(ArrayList<Plant> plants, LayoutType type, int wave) {
        int rapidCount;
        int wallCount;

        if (type == LayoutType.OFFENSIVE) {
            rapidCount = 5;
            wallCount = wave;
        } else if (type == LayoutType.DEFENSIVE) {
            rapidCount = 1;
            wallCount = 5;
        } else {
            rapidCount = wave + 1;
            wallCount = wave + 2;
        }

        int[] lanes = {2, 0, 4, 1, 3};
        for (int i = 0; i < lanes.length; i++) {
            int lane = lanes[i];
            if (i < rapidCount) {
                plants.add(new RapidPlant(plantPosition(lane, 7)));
            } else {
                plants.add(new Peashooter(plantPosition(lane, 7)));
            }
            if (i < wallCount) {
                plants.add(new WallPlant(plantPosition(lane, 4)));
            }
            if (i < wave - 1) {
                plants.add(new Peashooter(plantPosition(lane, 8)));
            }
        }
    }

    /** Converts a lane (0-4) and lawn column (0-8) to a sprite position. */
    private Point plantPosition(int lane, int column) {
    int lawnX = Battlefield.BOARD_X + Battlefield.DEPLOYMENT_WIDTH + 20;
    int lawnWidth = Battlefield.BOARD_WIDTH - Battlefield.DEPLOYMENT_WIDTH - 20;
    int laneHeight = Battlefield.BOARD_HEIGHT / Battlefield.NUM_LANES;

    int x = lawnX + 15 + column * (lawnWidth - 70) / 8;
    int y = Battlefield.BOARD_Y + lane * laneHeight + (laneHeight - 60) / 2;

    return new Point(x, y);
    }
}
