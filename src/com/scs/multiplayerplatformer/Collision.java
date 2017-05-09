package com.scs.multiplayerplatformer;

import ssmith.android.lib2d.shapes.Geometry;

import com.scs.multiplayerplatformer.graphics.mobs.EnemyNinjaEasy;
import com.scs.multiplayerplatformer.graphics.mobs.PlayersAvatar;

public class Collision {

	private Collision() {
	}


	// Returns whether a should move back
	public static boolean Collided(Geometry a, Geometry b) {
		// todo - shurikens
		if (a instanceof PlayersAvatar) {
			if (b instanceof EnemyNinjaEasy) {
				return Player_EnemyMob((PlayersAvatar)a, (EnemyNinjaEasy)b);
			}
		} else if (b instanceof PlayersAvatar) {
			if (a instanceof EnemyNinjaEasy) {
				return Player_EnemyMob((PlayersAvatar)b, (EnemyNinjaEasy)a);
			}
		}
		return true;
	}


	private static boolean Player_EnemyMob(PlayersAvatar player, EnemyNinjaEasy enemy) {
		player.died();
		return true;
	}

}
