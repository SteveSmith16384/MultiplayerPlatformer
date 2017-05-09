package com.scs.multiplayerplatformer.graphics.mobs;

import ssmith.android.compatibility.PointF;
import ssmith.android.framework.AbstractActivity;
import ssmith.android.lib2d.MyPointF;
import ssmith.lang.GeometryFuncs;

import com.scs.multiplayerplatformer.Statics;
import com.scs.multiplayerplatformer.game.BlockInventory;
import com.scs.multiplayerplatformer.game.GameModule;
import com.scs.multiplayerplatformer.graphics.ThrownItem;
import com.scs.multiplayerplatformer.graphics.blocks.Block;
import com.scs.multiplayerplatformer.input.IInputDevice;

public class PlayersAvatar extends AbstractLandMob {

	public float move_x_offset = 0;
	public int playerNum;
	public BlockInventory inv;
	public boolean completedLevel = false;

	private IInputDevice input;
	private long firePressedTime;
	private boolean prevThrowPressed = false;

	public PlayersAvatar(GameModule _game, int playernum, float x, float y, IInputDevice _input) {
		super(_game, Statics.act.getString("player"), x, y, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT, 3, 100, false, false, Statics.SD_PLAYERS_SIDE, false);

		playerNum = playernum;
		input = _input;
		inv = new BlockInventory(game, this);

		this.setNumFrames(8);
		a_bmp_left[0] = Statics.img_cache.getImage("ninja" + playernum + "_l0", Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_left[1] = Statics.img_cache.getImage("ninja" + playernum + "_l1", Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_left[2] = Statics.img_cache.getImage("ninja" + playernum + "_l2", Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_left[3] = Statics.img_cache.getImage("ninja" + playernum + "_l3", Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_left[4] = Statics.img_cache.getImage("ninja" + playernum + "_l4", Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_left[5] = Statics.img_cache.getImage("ninja" + playernum + "_l5", Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_left[6] = Statics.img_cache.getImage("ninja" + playernum + "_l6", Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_left[7] = Statics.img_cache.getImage("ninja" + playernum + "_l7", Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);

		a_bmp_right[0] = Statics.img_cache.getImage("ninja" + playernum + "_r0", Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_right[1] = Statics.img_cache.getImage("ninja" + playernum + "_r1", Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_right[2] = Statics.img_cache.getImage("ninja" + playernum + "_r2", Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_right[3] = Statics.img_cache.getImage("ninja" + playernum + "_r3", Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_right[4] = Statics.img_cache.getImage("ninja" + playernum + "_r4", Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_right[5] = Statics.img_cache.getImage("ninja" + playernum + "_r5", Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_right[6] = Statics.img_cache.getImage("ninja" + playernum + "_r6", Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_right[7] = Statics.img_cache.getImage("ninja" + playernum + "_r7", Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
	}


	@Override
	public void process(long interpol) {
		if (frozenUntil < System.currentTimeMillis()) {
			if (is_on_ice == false) {
				move_x_offset = 0;
			}
			if (input.isLeftPressed()) {
				move_x_offset = -1 * input.getStickDistance();
			} else if (input.isRightPressed()) {
				move_x_offset = 1 * input.getStickDistance();
			}

			moving_up = false;
			moving_down = false;
			if (input.isJumpPressed()) {
				startJumping();
			} else if (input.isUpPressed()) {
				moving_up = true;
			} else if (input.isDownPressed()) {
				moving_down = true;
			}

			boolean throwPressed = input.isThrowPressed(); 
			if (throwPressed) {
				if (!prevThrowPressed) {
					this.firePressedTime = System.currentTimeMillis();
				}
			} else {
				if (prevThrowPressed) {
					throwItem(input.getAngle(), System.currentTimeMillis() - this.firePressedTime);
				}
			}
			this.prevThrowPressed = throwPressed;

			if (move_x_offset != 0) {
				this.move(move_x_offset * Statics.PLAYER_SPEED, 0, false);
			}

		}

		performJumpingOrGravity();
		checkForSuffocation();
		//checkForHarmingBlocks();  already does this

	}


	private void throwItem(int angle, float power) {
		power = power / 1000;
		if (power > 1) {
			power = 1;
		} 
		/*if (Statics.DEBUG) {
			Statics.p("Duration: " + power);
			Statics.p("Ang: " + angle);
		}*/
		AbstractActivity act = Statics.act;

		byte type = getCurrentItemType();
		byte fallback_type = Block.SHURIKEN; // Default

		if (Block.CanBeThrown(type) == false || inv.hasBlock(type) == false) {
			type = fallback_type;
		}

		boolean thrown = false;
		if (inv.hasBlock(type) || Statics.DEBUG) {
			thrown = true;
		} else {
			game.showToast("You have nothing to throw!  Try a shuriken");
			return;
		}

		if (thrown) {
			act.sound_manager.playSound("throwitem");
			PointF p = GeometryFuncs.GetPointFromAngle(angle, power*2);
			MyPointF dir = new MyPointF(p.x, p.y);
			//Statics.p("Dir:" + dir);
			if (type == Block.SHURIKEN) {
				ThrownItem.ThrowShuriken(game, this, dir);
			} else {
				ThrownItem.ThrowRock(game, type, this, dir);
			}
			inv.addBlock(type, -1);
		}

	}


	/*@Override
	public void damage(int amt) {
		super.damage(amt);
		this.startJumping();
	}*/


	@Override
	public void died() {
		game.restartPlayer(this);
	}
	

	/*
	@Override
	protected boolean hasCollidedWith(Geometry g) {
		if (g instanceof PlatformMob) { // try and get this to work
			PlatformMob pm = (PlatformMob)g;
			// Move us in the same direction as the platform.
			this.move(pm.move_x, pm.move_y, false);
			return false;
		} else if (g instanceof PlayersAvatar) {
			return false;
		} else if (g instanceof ThrownItem) {
			return false; // The ThrownItem class handles collisions
		}
		return true; // Move us back
	}
	 */

}
