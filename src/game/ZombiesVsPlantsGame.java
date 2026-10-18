package game;

/*
CLASS: ZombieVsPlantsGame
DESCRIPTION: Main game class extending Game.
NOTE: This class is the metaphorical "main method" of your program,
      it is your control center.

*/
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import game.units.Plant;
import game.units.Zombie;

class ZombiesVsPlantsGame extends Game {
	private int level;
	private int wave;
	private Phase phase;
	private ArrayList<Zombie> zombies;
	private ArrayList<Plant> plants;
	private DeploymentCursor cursor;
	private PlantLayoutSelector layoutSelector;
	private InputHandler inputHandler;
	private WaveTimer waveTimer;

	static int counter = 0;

  public ZombiesVsPlantsGame() {
    super("ZombiesVsPlants!",800,600);
    this.setFocusable(true);
	this.requestFocus();
  }
  
	public void paint(Graphics brush) {
    	brush.setColor(Color.black);
    	brush.fillRect(0,0,width,height);
    	
    	// sample code for printing message for debugging
    	// counter is incremented and this message printed
    	// each time the canvas is repainted
    	counter++;
    	brush.setColor(Color.white);
    	brush.drawString("Counter is " + counter, 10, 10);
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
