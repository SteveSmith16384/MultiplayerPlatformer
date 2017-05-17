package com.scs.multiplayerplatformer;

import ssmith.android.lib2d.shapes.Geometry;

import com.scs.multiplayerplatformer.graphics.ThrownItem;
import com.scs.multiplayerplatformer.graphics.mobs.AbstractWalkingMob;
import com.scs.multiplayerplatformer.graphics.mobs.EnemyNinjaEasy;
import com.scs.multiplayerplatformer.graphics.mobs.PlayersAvatar;

public final class Collision {

	private Collision() {
	}


	// Returns FALSE if the colliders should move back
	public static boolean Collided(Geometry a, Geometry b) {
		if (a instanceof PlayersAvatar) {
			if (b instanceof EnemyNinjaEasy) {
				return Player_EnemyMob((PlayersAvatar)a, (EnemyNinjaEasy)b);
			} else if (b instanceof PlayersAvatar) {
				return true;
			} else if (b instanceof ThrownItem) {
				return ThrownItem_AbstractWalkingMob((ThrownItem)b, (AbstractWalkingMob)a);
			}
		} else if (b instanceof PlayersAvatar) {
			if (a instanceof EnemyNinjaEasy) {
				return Player_EnemyMob((PlayersAvatar)b, (EnemyNinjaEasy)a);
			} else if (a instanceof PlayersAvatar) {
				return true;
			} else if (a instanceof ThrownItem) {
				return ThrownItem_AbstractWalkingMob((ThrownItem)a, (AbstractWalkingMob)b);
			}
		}
		if (a instanceof ThrownItem) {
			if (b instanceof AbstractWalkingMob) {
				return ThrownItem_AbstractWalkingMob((ThrownItem)a, (AbstractWalkingMob)b);
			} else if (b instanceof ThrownItem) {
				return ThrownItem_ThrownItem((ThrownItem)a, (ThrownItem)b);
			}
		} else if (b instanceof ThrownItem) {
			if (a instanceof AbstractWalkingMob) {
				return ThrownItem_AbstractWalkingMob((ThrownItem)b, (AbstractWalkingMob)a);
			} else if (a instanceof ThrownItem) {
				return ThrownItem_ThrownItem((ThrownItem)a, (ThrownItem)b);
			}
		}
		return true;
	}


	private static boolean Player_EnemyMob(PlayersAvatar player, EnemyNinjaEasy enemy) {
		player.died();
		return false;
	}


	private static boolean ThrownItem_ThrownItem(ThrownItem ti1, ThrownItem ti2) {
		ti1.remove();
		ti2.remove();
		return true;
	}


	private static boolean ThrownItem_AbstractWalkingMob(ThrownItem thrown, AbstractWalkingMob mob) {
		byte side = -1;
		if (thrown.thrower != null) {
			side = thrown.thrower.side;
		}
		if (side != mob.side) {
			thrown.remove();
			mob.died();
			return true; // Don't move back!
		}
		return true;
	}

}
