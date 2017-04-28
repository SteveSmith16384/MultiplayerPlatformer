package com.scs.multiplayerplatformer.graphics.mobs;

import ssmith.android.lib2d.shapes.Geometry;
import ssmith.android.util.Timer;
import ssmith.lang.DateFunctions;

import com.scs.multiplayerplatformer.Statics;
import com.scs.multiplayerplatformer.game.BlockInventory;
import com.scs.multiplayerplatformer.game.GameModule;
import com.scs.multiplayerplatformer.input.IInputDevice;

public class PlayersAvatar extends AbstractLandMob {

	private static final byte MAX_HEALTH = 100;

	public int move_x_offset = 0;
	private Timer dec_health_timer = new Timer(DateFunctions.MINUTE/4);
	public BlockInventory inv;
	private int controllerID;
	private IInputDevice input;
	
	public PlayersAvatar(GameModule _game, float x, float y, IInputDevice _input, int _controllerID) {
		super(_game, Statics.act.getString("player"), x, y, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT, MAX_HEALTH, 3, 100, false, false, Statics.SD_PLAYERS_SIDE, false);

		input = _input;
		controllerID = _controllerID;
		
		this.setNumFrames(8);
		a_bmp_left[0] = Statics.img_cache.getImage("ninja_l0", Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_left[1] = Statics.img_cache.getImage("ninja_l1", Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_left[2] = Statics.img_cache.getImage("ninja_l2", Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_left[3] = Statics.img_cache.getImage("ninja_l3", Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_left[4] = Statics.img_cache.getImage("ninja_l4", Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_left[5] = Statics.img_cache.getImage("ninja_l5", Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_left[6] = Statics.img_cache.getImage("ninja_l6", Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_left[7] = Statics.img_cache.getImage("ninja_l7", Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);

		a_bmp_right[0] = Statics.img_cache.getImage("ninja_r0", Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_right[1] = Statics.img_cache.getImage("ninja_r1", Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_right[2] = Statics.img_cache.getImage("ninja_r2", Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_right[3] = Statics.img_cache.getImage("ninja_r3", Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_right[4] = Statics.img_cache.getImage("ninja_r4", Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_right[5] = Statics.img_cache.getImage("ninja_r5", Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_right[6] = Statics.img_cache.getImage("ninja_r6", Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_right[7] = Statics.img_cache.getImage("ninja_r7", Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
	}


	@Override
	public void process(long interpol) {
		//IInputDevice input = game.getInputDevice(controllerID);
		if (is_on_ice == false) {
			move_x_offset = 0;
		}
		if (input.isLeftPressed()) {
			move_x_offset = -1;
		} else if (input.isRightPressed()) {
			move_x_offset = 1;
		}
		
		moving_down = false;
		if (input.isJumpPressed()) {
			startJumping();
		} else if (input.isDownPressed()) {
			moving_down = true;
		}
		
		if (input.isThrowPressed()) {
			game.throwItem(this, input.getAngle(), input.getThrowDuration());
		}
		
		if (Statics.player_loses_health) {
			if (dec_health_timer.hasHit(interpol)) {
				this.damage(1);
			}	
		}

		if (move_x_offset != 0) {
			this.move(move_x_offset * Statics.PLAYER_SPEED, 0, false);
			game.checkIfMapNeedsLoading();
		}

		performJumpingOrGravity();
		checkForSuffocation();
		checkForHarmingBlocks();

	}


	@Override
	public void damage(int amt) {
		super.damage(amt);
		this.startJumping();
	}


	@Override
	protected void died() {
		//todo game.gameOver("You have been killed!");
	}


	@Override
	protected boolean hasCollidedWith(Geometry g) {
		if (g instanceof PlatformMob) { // try and get this to work
			PlatformMob pm = (PlatformMob)g;
			// Move us in the same direction as the platform.
			this.move(pm.move_x, pm.move_y, false);
		}
		//return true;
		return true;
	}


}
