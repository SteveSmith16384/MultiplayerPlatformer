package com.scs.multiplayerplatformer.game;

import ssmith.android.util.Timer;

import com.scs.multiplayerplatformer.graphics.blocks.Block;
import com.scs.multiplayerplatformer.graphics.mobs.EnemyNinjaEasy;

public class EnemyEventTimer extends Timer implements IProcessable {

	private static final long EVENT_TIMER = 1000 * 7;

	private GameModule game;

	public EnemyEventTimer(GameModule _game) {
		super(EVENT_TIMER);

		game = _game;

		game.addToProcess(this);
	}


	@Override
	public void process(long interpol) {
		/*if (game.getNumProcessInstant() < Statics.MAX_INSTANTS) {
			if (this.hasHit(interpol)) {
				if (game.is_day == false) {
					GenerateRandomMonster(game, null);
				}
			}
		}*/
	}


	public static void generateRandomMonster(GameModule game, Block gen) {
		EnemyNinjaEasy.factory(game, gen);
	}

}
