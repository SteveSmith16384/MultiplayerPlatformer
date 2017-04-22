package com.scs.worldcrafter.game;

import ssmith.android.util.Timer;
import ssmith.lang.Functions;

import com.scs.worldcrafter.Statics;
import com.scs.worldcrafter.graphics.blocks.Block;
import com.scs.worldcrafter.graphics.mobs.EnemyNinjaEasy;
import com.scs.worldcrafter.graphics.mobs.Skeleton;
import com.scs.worldcrafter.graphics.mobs.Wasp;
import com.scs.worldcrafter.graphics.mobs.Zombie;

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
		if (Statics.GAME_MODE == Statics.GM_WORLDCRAFTER) {
			int i = Functions.rnd(2, 6);
			switch (i) {
			case 2:
				Wasp.Factory(game, gen);
				break;
			case 3:
				Skeleton.Factory(game, gen);
				break;
			default:
				Zombie.Factory(game, gen);
				break;
			}
		} else {
			EnemyNinjaEasy.Factory(game, gen);
		}

	}

}
