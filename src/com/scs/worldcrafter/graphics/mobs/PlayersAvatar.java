package com.scs.worldcrafter.graphics.mobs;

import ssmith.android.lib2d.shapes.Geometry;
import ssmith.android.util.Timer;
import ssmith.lang.DateFunctions;

import com.scs.ninja.main.lite.R;
import com.scs.worldcrafter.Statics;
import com.scs.worldcrafter.game.GameModule;

public class PlayersAvatar extends AbstractLandMob {

	private static final byte MAX_HEALTH = 100;

	public int move_x_offset = 0;
	public boolean moving_with_keys = false;
	private Timer dec_health_timer = new Timer(DateFunctions.MINUTE/4);

	public PlayersAvatar(GameModule _game, float x, float y) {
		super(_game, Statics.act.getString(R.string.player), x, y, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT, MAX_HEALTH, 3, 100, false, false, Statics.SD_PLAYERS_SIDE, false);

		if (Statics.GAME_MODE == Statics.GM_WORLDCRAFTER) {
			a_bmp_left[0] = Statics.img_cache.getImage(R.drawable.player_l0, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
			a_bmp_left[1] = Statics.img_cache.getImage(R.drawable.player_l1, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
			a_bmp_left[2] = Statics.img_cache.getImage(R.drawable.player_l2, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
			a_bmp_right[0] = Statics.img_cache.getImage(R.drawable.player_r0, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
			a_bmp_right[1] = Statics.img_cache.getImage(R.drawable.player_r1, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
			a_bmp_right[2] = Statics.img_cache.getImage(R.drawable.player_r2, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		} else if (Statics.GAME_MODE == Statics.GM_NINJA) {
			this.setNumFrames(8);
			a_bmp_left[0] = Statics.img_cache.getImage(R.drawable.ninja_l0, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
			a_bmp_left[1] = Statics.img_cache.getImage(R.drawable.ninja_l1, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
			a_bmp_left[2] = Statics.img_cache.getImage(R.drawable.ninja_l2, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
			a_bmp_left[3] = Statics.img_cache.getImage(R.drawable.ninja_l3, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
			a_bmp_left[4] = Statics.img_cache.getImage(R.drawable.ninja_l4, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
			a_bmp_left[5] = Statics.img_cache.getImage(R.drawable.ninja_l5, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
			a_bmp_left[6] = Statics.img_cache.getImage(R.drawable.ninja_l6, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
			a_bmp_left[7] = Statics.img_cache.getImage(R.drawable.ninja_l7, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);

			a_bmp_right[0] = Statics.img_cache.getImage(R.drawable.ninja_r0, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
			a_bmp_right[1] = Statics.img_cache.getImage(R.drawable.ninja_r1, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
			a_bmp_right[2] = Statics.img_cache.getImage(R.drawable.ninja_r2, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
			a_bmp_right[3] = Statics.img_cache.getImage(R.drawable.ninja_r3, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
			a_bmp_right[4] = Statics.img_cache.getImage(R.drawable.ninja_r4, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
			a_bmp_right[5] = Statics.img_cache.getImage(R.drawable.ninja_r5, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
			a_bmp_right[6] = Statics.img_cache.getImage(R.drawable.ninja_r6, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
			a_bmp_right[7] = Statics.img_cache.getImage(R.drawable.ninja_r7, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		} else if (Statics.GAME_MODE == Statics.GM_POLICECOP) {
			this.setNumFrames(8);
			a_bmp_left[0] = Statics.img_cache.getImage(R.drawable.ninja_l0, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
			a_bmp_left[1] = Statics.img_cache.getImage(R.drawable.ninja_l1, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
			a_bmp_left[2] = Statics.img_cache.getImage(R.drawable.ninja_l2, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
			a_bmp_left[3] = Statics.img_cache.getImage(R.drawable.ninja_l3, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
			a_bmp_left[4] = Statics.img_cache.getImage(R.drawable.ninja_l4, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
			a_bmp_left[5] = Statics.img_cache.getImage(R.drawable.ninja_l5, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
			a_bmp_left[6] = Statics.img_cache.getImage(R.drawable.ninja_l6, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
			a_bmp_left[7] = Statics.img_cache.getImage(R.drawable.ninja_l7, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);

			a_bmp_right[0] = Statics.img_cache.getImage(R.drawable.ninja_r0, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
			a_bmp_right[1] = Statics.img_cache.getImage(R.drawable.ninja_r1, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
			a_bmp_right[2] = Statics.img_cache.getImage(R.drawable.ninja_r2, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
			a_bmp_right[3] = Statics.img_cache.getImage(R.drawable.ninja_r3, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
			a_bmp_right[4] = Statics.img_cache.getImage(R.drawable.ninja_r4, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
			a_bmp_right[5] = Statics.img_cache.getImage(R.drawable.ninja_r5, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
			a_bmp_right[6] = Statics.img_cache.getImage(R.drawable.ninja_r6, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
			a_bmp_right[7] = Statics.img_cache.getImage(R.drawable.ninja_r7, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		} else {
			throw new RuntimeException("Unknown game: " + Statics.GAME_MODE);
		}
	}


	@Override
	public void process(long interpol) {
		if (Statics.player_loses_health) {
			if (dec_health_timer.hasHit(interpol)) {
				this.damage(1);
			}	
		}

		/*if (Statics.cfg.using_tilt) {
			if (this.game.getPitch() > Statics.TILT_THRESH) { // Left
				move_x_offset = -1;
			} else if (this.game.getPitch() < -Statics.TILT_THRESH) {
				move_x_offset = 1;
			} else {
				if (moving_with_keys == false) {
					this.move_x_offset = 0;
				}
			}
		}*/

		if (move_x_offset != 0) {
			this.move(move_x_offset * Statics.PLAYER_SPEED, 0);
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
		game.gameOver("You have been killed!");
	}


	@Override
	protected boolean hasCollidedWith(Geometry g) {
		/*if (g instanceof PlatformMob) { todo - try and get this to work
			PlatformMob pm = (PlatformMob)g;
			// Move us in the same direction as the platform.
			this.move(pm.move_x, pm.move_y);
		}*/
		//return true;
		return true;
	}


}
