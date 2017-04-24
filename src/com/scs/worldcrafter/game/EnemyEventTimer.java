package com.scs.worldcrafter.game;

import java.rmi.server.Skeleton;

import ssmith.android.util.Timer;
import ssmith.lang.Functions;

import com.scs.worldcrafter.Statics;
import com.scs.worldcrafter.graphics.blocks.Block;
import com.scs.worldcrafter.graphics.mobs.EnemyNinjaEasy;

public class EnemyEventTimer extends Timer implements IProcessable {

	private static final long EVENT_TIMER = 1000 * 7;

	private GameModule game;

	public EnemyEventTimer(GameModule _game) {
		super(EVENT_TIMER);

		game = _game;

		game.addToProcess_Instant(this);
	}


	@Override
	public void process(long interpol) {
		if (game.getNumProcessInstant() < Statics.MAX_INSTANTS) {
			if (this.hasHit(interpol)) {
				if (game.is_day == false) {
					GenerateRandomMonster(game, null);
				}
			}
		}
	}


	public static void GenerateRandomMonster(GameModule game, Block gen) {
			EnemyNinjaEasy.Factory(game, gen);
	}

}
