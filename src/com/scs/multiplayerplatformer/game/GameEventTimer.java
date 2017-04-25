package com.scs.multiplayerplatformer.game;

import ssmith.android.util.Timer;
import ssmith.lang.DateFunctions;
import ssmith.lang.Functions;

public class GameEventTimer extends Timer implements IProcessable {

	private static final long EVENT_TIMER = DateFunctions.MINUTE;

	private GameModule game;

	public GameEventTimer(GameModule _game) {
		super(EVENT_TIMER);

		game = _game;

		game.addToProcess_Instant(this);
	}


	@Override
	public void process(long interpol) {
		if (this.hasHit(interpol)) {
			//float x_pos = game.root_cam.left + (Functions.rnd(0, 1) * Statics.SCREEN_WIDTH);
			int i = Functions.rnd(0, 0);
			switch (i) {
			case 0:
				//game.addBlock(Block.APPLE, Functions., map_y, add_to_process, check_darkness)
				break;
			}
		}
	}

}
