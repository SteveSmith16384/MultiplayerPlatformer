package com.scs.multiplayerplatformer;

import ssmith.android.lib2d.shapes.Geometry;

import com.scs.multiplayerplatformer.graphics.ThrownItem;
import com.scs.multiplayerplatformer.graphics.mobs.AbstractWalkingMob;
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
		if (a instanceof ThrownItem) {
			if (b instanceof AbstractWalkingMob) {
				return ThrownItem_AbstractWalkingMob((ThrownItem)a, (AbstractWalkingMob)b);
			}
		} else if (b instanceof ThrownItem) {
			if (a instanceof AbstractWalkingMob) {
				return ThrownItem_AbstractWalkingMob((ThrownItem)b, (AbstractWalkingMob)a);
			}
		}
		return true;
	}


	private static boolean Player_EnemyMob(PlayersAvatar player, EnemyNinjaEasy enemy) {
		player.died();
		return true;
	}


	private static boolean ThrownItem_AbstractWalkingMob(ThrownItem thrown, AbstractWalkingMob mob) {
		thrown.remove();
		mob.died();
		return true;
	}

}
