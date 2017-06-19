package com.scs.multiplayerplatformer;

import ssmith.android.lib2d.shapes.Geometry;

import com.scs.multiplayerplatformer.Statics.GameMode;
import com.scs.multiplayerplatformer.graphics.ThrownItem;
import com.scs.multiplayerplatformer.graphics.mobs.AbstractMob;
import com.scs.multiplayerplatformer.graphics.mobs.AbstractWalkingMob;
import com.scs.multiplayerplatformer.graphics.mobs.PlayersAvatar;

public final class Collision {

	private Collision() {
	}


	// Returns FALSE if the colliders should move back
	public static boolean Collided(Geometry a, Geometry b) {
		if (a instanceof PlayersAvatar) {
			if (b instanceof PlayersAvatar) {
				return true;
			} else if (b instanceof AbstractMob) {
				return Player_EnemyMob((PlayersAvatar)a, (AbstractMob)b);
			} else if (b instanceof ThrownItem) {
				return ThrownItem_AbstractMob((ThrownItem)b, (AbstractWalkingMob)a);
			}
		} else if (b instanceof PlayersAvatar) {
			if (a instanceof PlayersAvatar) {
				return true;
			} else if (a instanceof AbstractMob) {
				return Player_EnemyMob((PlayersAvatar)b, (AbstractMob)a);
			} else if (a instanceof ThrownItem) {
				return ThrownItem_AbstractMob((ThrownItem)a, (AbstractWalkingMob)b);
			}
		}
		if (a instanceof ThrownItem) {
			if (b instanceof AbstractMob) {
				return ThrownItem_AbstractMob((ThrownItem)a, (AbstractMob)b);
			} else if (b instanceof ThrownItem) {
				return ThrownItem_ThrownItem((ThrownItem)a, (ThrownItem)b);
			}
		} else if (b instanceof ThrownItem) {
			if (a instanceof AbstractMob) {
				return ThrownItem_AbstractMob((ThrownItem)b, (AbstractMob)a);
			} else if (a instanceof ThrownItem) {
				return ThrownItem_ThrownItem((ThrownItem)a, (ThrownItem)b);
			}
		}
		return true;
	}


	private static boolean Player_EnemyMob(PlayersAvatar player, AbstractMob enemy) {
		player.died();
		return false;
	}


	private static boolean ThrownItem_ThrownItem(ThrownItem ti1, ThrownItem ti2) {
		if (ti1.collidesWithOthers && ti2.collidesWithOthers) {
			ti1.remove();
			ti2.remove();
			return true;
		}
		return false;
	}


	private static boolean ThrownItem_AbstractMob(ThrownItem thrown, AbstractMob mob) {
		byte side = -1;
		if (thrown.thrower != null) {
			side = thrown.thrower.side;
		}
		if (side != mob.side) {
			thrown.remove();
			mob.died();
			//return true; // Don't move back!
		}
		return true;
	}

}
