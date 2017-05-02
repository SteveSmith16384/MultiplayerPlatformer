package com.scs.multiplayerplatformer.graphics;

import ssmith.android.compatibility.Paint;
import ssmith.android.lib2d.shapes.AbstractRectangle;
import ssmith.util.ReturnObject;

import com.scs.multiplayerplatformer.Statics;
import com.scs.multiplayerplatformer.game.GameModule;
import com.scs.multiplayerplatformer.game.IProcessable;
import com.scs.multiplayerplatformer.graphics.mobs.PlayersAvatar;

public abstract class GameObject extends AbstractRectangle implements IProcessable {

	protected static Paint paint = new Paint();

	protected GameModule game;

	public GameObject(GameModule _game, String name, boolean _collides, float pxl_x, float pxl_y, float w, float h) {
		super(name, paint, pxl_x, pxl_y, w, h);

		game = _game;
		this.collides = _collides;

	}


	protected void remove() {
		this.removeFromParent();
		this.game.removeFromProcess(this);
	}


	protected boolean checkIfTooFarAway() {
		if (this.getDistanceToClosestPlayer(null) > (Statics.SCREEN_WIDTH * 2)) {
			this.remove();
			return true;
		}
		return false;
	}


	public float getDistanceToClosestPlayer(ReturnObject<PlayersAvatar> returnClosest) {
		float closestDistance = 9999;
		for(PlayersAvatar player : game.players) {
			float dist = this.getDistanceTo(player); 
			if (dist < closestDistance) {
				if (returnClosest != null) {
					returnClosest.toReturn = player;
				}
				closestDistance = dist;
			}
		}
		return closestDistance;
	}


}


