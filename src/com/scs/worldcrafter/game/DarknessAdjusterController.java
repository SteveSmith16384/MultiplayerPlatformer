package com.scs.worldcrafter.game;

import java.util.ArrayList;

public class DarknessAdjusterController extends ArrayList<DarknessAdjuster> {

	private static final long serialVersionUID = 1L;
	
	private GameModule game;

	public DarknessAdjusterController(GameModule _game) {
		super();

		game = _game;
	}


	public void add(int map_x) {
		// See if there's one running in the same col, and stop it if so
		for (DarknessAdjuster da : this) {
			if (da.current_coords.x == map_x) {
				da.remove(); // Stop this one
				break;
			}
		}
		DarknessAdjuster da = new DarknessAdjuster(game, this, map_x);//, _block_added);
		this.add(da);
		game.addToProcess_Slow(da, true);
		//startAnotherIfNoneRunning();
	}


	public void signalFinished(DarknessAdjuster ad) {
		this.remove(ad);
	}


}
