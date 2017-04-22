package com.scs.worldcrafter.graphics.mobs;

import ssmith.android.lib2d.shapes.Geometry;
import ssmith.lang.Functions;
import ssmith.lang.NumberFunctions;
import android.graphics.Point;

import com.scs.ninja.main.lite.R;
import com.scs.worldcrafter.Statics;
import com.scs.worldcrafter.game.GameModule;
import com.scs.worldcrafter.graphics.Explosion;
import com.scs.worldcrafter.graphics.blocks.Block;
import com.scs.worldcrafter.mapgen.SimpleMobData;

public class Chicken extends AbstractLandMob {

	private static final float TURN_DURATION = 4000; // In case can't get to player
	private static final int MAX_FRAMES = 1;
	private static final byte HEALTH = 1;

	private int x_offset = -1;
	private boolean tried_jumping = false;
	private float turn_timer = 0;


	public static void Factory(GameModule game, Block gen) { // gen == null for normal appearance
		if (game.getNumProcessInstant() < Statics.MAX_INSTANTS) {
			if (gen == null) {
				float start = game.root_cam.bottom + (Statics.SQ_SIZE);
				float left = game.root_cam.left - Statics.SQ_SIZE;
				if (Functions.rnd(1, 2) == 1) {
					left = game.root_cam.right + Statics.SQ_SIZE;
				}
				while (start >= game.root_cam.top - (Statics.SQ_SIZE)) {
					boolean res = Sheep.Subfactory(game, left, start);
					if (res) {
						break;
					} else {
						start -= (Statics.SQ_SIZE);
					}
				}
			} else {
				// Use generator
				Chicken.Subfactory(game, gen.getWorldX(), gen.getWorldY() - (Statics.SQ_SIZE));
			}
		}
	}


	public static boolean Subfactory(GameModule game, float x, float y) {
		if (game.isAreaClear(x, y, Statics.SQ_SIZE, Statics.SQ_SIZE, true)) {
			new Chicken(game, x, y);
			return true;
		} else {
			return false;
		}
	}


	private Chicken(GameModule _game, float x, float y) {
		super(_game, Statics.act.getString(R.string.chicken), x, y, Statics.SQ_SIZE, Statics.SQ_SIZE, HEALTH, MAX_FRAMES, 200, false, false, Statics.SD_PLAYERS_SIDE, true);

		a_bmp_left[0] = Statics.img_cache.getImage(R.drawable.chicken_l, Statics.SQ_SIZE, Statics.SQ_SIZE);
		a_bmp_right[0] = Statics.img_cache.getImage(R.drawable.chicken_r, Statics.SQ_SIZE, Statics.SQ_SIZE);

		//this.game.view.sound_manager.playSound(R.raw.walkandgrunt);
	}


	@Override
	public void process(long interpol) {
		float dist = super.getDistanceTo(game.player); 
		if (dist < Statics.ACTIVATE_DIST) { // Only process if close
			turn_timer -= interpol;
			if (turn_timer <= 0) {
				turn_timer = TURN_DURATION;
				float diff = game.player.getWorldCentreX() - this.getWorldCentreX();
				x_offset = NumberFunctions.sign(diff);
			}

			// Try moving
			if (this.move(x_offset * Statics.CHICKEN_SPEED, 0) == false) {
				// CANT move
				if (tried_jumping == false) {
					this.startJumping();
					this.tried_jumping = true;
				} else {
					if (is_on_ground_or_ladder || getJumpingYOff() <= 0) {
						tried_jumping = false;
						x_offset = x_offset * -1; //Turn around
					}
				}
			}

			performJumpingOrGravity();
			checkForHarmingBlocks();

			if (is_on_ground_or_ladder) { // Must be after we've jumped!
				tried_jumping = false;
			}
		} else if (dist > Statics.DEACTIVATE_DIST) {
			this.remove();
			// Re-add them to list to create
			this.game.original_level_data.mobs.add(new SimpleMobData(AbstractMob.CHICKEN, this.getWorldX(), this.getWorldY()));
		}

	}


	@Override
	protected void died() {
		game.showToast("Chicken killed");
		Explosion.CreateExplosion(game, 2, this.getWorldCentreX(), this.getWorldCentreY(), R.drawable.blood_spurt);
		this.remove(); // Must be before we drop an item
		// Drop an item
		Point p = this.game.new_grid.getMapCoordsFromPixels(this.getWorldCentreX(), this.getWorldBounds().bottom-1);
		if (this.game.new_grid.isSquareEmpty(p.x, p.y)) { // this.game.new_grid.isSquareEmpty(17, 32)
			this.game.addBlock(Block.RAW_DEAD_CHICKEN, p.x, p.y, false);
		}
	}


	@Override
	protected boolean hasCollidedWith(Geometry g) {
		/*if (g instanceof AbstractMob) {
			return true;
		}
		return false;*/
		return true;
	}


}
