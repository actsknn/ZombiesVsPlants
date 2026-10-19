package game;

/*
CLASS: ZombieVsPlantsGame
DESCRIPTION: Main game class extending Game.
NOTE: This class is the metaphorical "main method" of your program,
      it is your control center.

*/
import game.units.Plant;
import game.units.Zombie;
import game.units.zombies.ZombieType;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class ZombiesVsPlantsGame extends Game {
    private static final int STARTING_MOONLIGHT = 250;
	private int level;
	private int wave;
	private Phase phase;
	private ArrayList<Zombie> zombies;
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

    this.level = 1;
    this.wave = 1;
    this.phase = Phase.PLANNING;

    this.zombies = new ArrayList<Zombie>();
    this.plants = new ArrayList<Plant>();

    this.battlefield = new Battlefield();
    this.deploymentMenu = new DeploymentMenu();
    this.layoutSelector = new PlantLayoutSelector();

    this.cursor = new DeploymentCursor(
        new Point[] {
            new Point(0, 0),
            new Point(20, 0),
            new Point(20, 20),
            new Point(0, 20)
        },
        new Point(150, 150),
        0
    );

    this.inputHandler = new InputHandler();
    this.waveTimer = new WaveTimer();

    this.addKeyListener(inputHandler);
}
  
	public void paint(Graphics brush) {
		battlefield.paint(brush);
		deploymentMenu.paint(brush, moonlight, selectedZombie);
  }

  
	public void updateGame() {
	}

	public void startWave() {
	}

	public void nextWave() {
	}

	public boolean checkWin() {
		return false; // TODO: Implement later.
	}

	public boolean checkLose() {
		return false; // TODO: Implement later.
	}

	private class InputHandler implements KeyListener {
		@Override
		public void keyPressed(KeyEvent e) {
			if (phase != Phase.PLANNING) {
				return;
			}
			switch (e.getKeyCode()) {
				case KeyEvent.VK_1: selectedZombie = ZombieType.BASIC; break;
				case KeyEvent.VK_2: selectedZombie = ZombieType.FAST; break;
				case KeyEvent.VK_3: selectedZombie = ZombieType.TANK; break;
				default: return;
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

	private class WaveTimer {
		private int remainingTime;

		public void update() {
		}

		public void reset() {
		}
	}

	public static void main (String[] args) {
  		ZombiesVsPlantsGame a = new ZombiesVsPlantsGame();
		a.repaint();
  }
}
