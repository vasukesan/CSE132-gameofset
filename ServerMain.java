package lab5;

import java.io.EOFException;
import java.io.IOException;
import java.util.Random;
import java.util.Set;

import lab5.model.Player;
import lab5.model.SetGame;
import lab5.view.Logger;
import lab5.view.NetworkView;

public class ServerMain implements Runnable {

	final private SetGame   model;
	final private Random    rand;
	public static final int DELAY = 1000; // nominal sleep delay (in ms)

	public ServerMain() {
		rand = new Random();
		model = new SetGame();
	}

	public void run() {
		
		//
		// simulate random activity
		//
		//model.addPlayer("Fred");
		
		//
		// logger will produce a printout of all things that happen
		//   to the model.
		//
		Logger logger = new Logger(model);

//		for (int i=0; i < 100; ++i) {
//			
//			sleep();
//
//			//
//			// Pick a random player and score
//			//
//			int p = rand.nextInt(model.getPlayers().size());
//			Player player = model.getPlayers().toArray(new Player[0])[p];
//			int score  = rand.nextInt(100);
//
//			//
//			// and tell the model the person's new score
//			//
//			player.setScore(score);
//	
//		}
		
	}

	/**
	 * The sleep that is observed nominally.
	 * Change DELAY to speed things up or slow things down.
	 */
	public static void sleep() {
		sleep(DELAY);
	}
	
	/**
	 * Delay for an arbitrary amount of time.
	 * @param ms delay amount (in ms)
	 */
	public static void sleep(int ms) {
		try {
			Thread.sleep(ms);
		}
		catch (Exception e) {
			throw new Error("ServerMain.sleep: should not happen " + e);
		}
	}
	
	/**
	 * Accessor for SetGame model
	 * @return SetGame model
	 */
	public SetGame getSetGame() {
		return model;
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		ServerMain m = new ServerMain();
		Runnable v = new NetworkView(m.getSetGame());
		Thread t = new Thread(v);
		t.start();
		m.run();
	}

}
