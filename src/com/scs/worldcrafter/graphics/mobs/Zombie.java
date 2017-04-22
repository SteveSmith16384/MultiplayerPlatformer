package com.scs.worldcrafter.graphics.mobs;

import ssmith.android.lib2d.shapes.Geometry;
import ssmith.lang.Functions;
import ssmith.lang.NumberFunctions;

import com.scs.ninja.main.lite.R;
import com.scs.worldcrafter.Statics;
import com.scs.worldcrafter.game.GameModule;
import com.scs.worldcrafter.graphics.Explosion;
import com.scs.worldcrafter.graphics.blocks.Block;
import com.scs.worldcrafter.mapgen.SimpleMobData;

public class Zombie extends AbstractLandMob {

	private static final float TURN_DURATION = 4000; // In case can't get to player
	private static final int MAX_FRAMES = 3;
	private static final byte HEALTH = 2;

	private int x_offset = -1;	
	private boolean tried_jumping = false;
	private float turn_timer = 0;


	public static void Factory(GameModule game, Block gen) { // gen == null for normal appearance
		if (game.getNumProcessInstant() < Statics.MAX_INSTANTS) {
			if (Statics.monsters) {
				if (gen == null) {
					float start = game.root_cam.bottom + (Statics.PLAYER_HEIGHT);
					float left = game.root_cam.left - Statics.PLAYER_WIDTH;
					if (Functions.rnd(1, 2) == 1) {
						left = game.root_cam.right + Statics.PLAYER_WIDTH;
					}
					while (start >= game.root_cam.top - (Statics.PLAYER_HEIGHT)) {
						boolean res = Zombie.Subfactory(game, left, start);
						if (res) {
							break;
						} else {
							start -= (Statics.SQ_SIZE);
						}
					}
				} else {
					// Use generator
					Zombie.Subfactory(game, gen.getWorldX(), gen.getWorldY() - (Statics.PLAYER_HEIGHT));
				}
			}
		}
	}


	public static boolean Subfactory(GameModule game, float x, float y) {
		if (Statics.monsters) {
			if (game.isAreaClear(x, y, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT, true)) {
				new Zombie(game, x, y);
				return true;
			}
		}
		return false;
	}


	private Zombie(GameModule _game, float x, float y) {
		super(_game, Statics.act.getString(R.string.zombie), x, y, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT, HEALTH, MAX_FRAMES, 200, true, true, Statics.SD_ENEMY_SIDE, true);

		a_bmp_left[0] = Statics.img_cache.getImage(R.drawable.zombie_l0, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_left[1] = Statics.img_cache.getImage(R.drawable.zombie_l1, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_left[2] = Statics.img_cache.getImage(R.drawable.zombie_l2, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_right[0] = Statics.img_cache.getImage(R.drawable.zombie_r0, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_right[1] = Statics.img_cache.getImage(R.drawable.zombie_r1, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_right[2] = Statics.img_cache.getImage(R.drawable.zombie_r2, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);

		/*if (Statics.RELEASE_MODE == false) {
			game.showMsg("Zombie created");
		}*/

		Statics.act.sound_manager.playSound(R.raw.walkandgrunt);
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
			if (this.move(x_offset * Statics.ZOMBIE_SPEED, 0) == false) {
				// CANT move
				if (tried_jumping == false) {
					this.startJumping();
					this.tried_jumping = true;
				} else {
					if (is_on_ground_or_ladder) {
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
			this.game.original_level_data.mobs.add(new SimpleMobData(AbstractMob.ZOMBIE, this.getWorldX(), this.getWorldY()));
		}

	}


	@Override
	protected void died() {
		game.showToast("Zombie killed");
		Explosion.CreateExplosion(game, 6, this.getWorldCentreX(), this.getWorldCentreY(), R.drawable.blob);
		this.remove(); // Must be before we drop an item
		// Drop an item
		/*Point p = this.game.new_grid.getMapCoordsFromPixels(this.getWorldCentreX(), this.getWorldBounds().bottom-1);
		if (this.game.new_grid.isSquareEmpty(p.x, p.y)) {
			int i = Functions.rnd(1, 3);
			if (i == 1) {
				this.game.addBlock(Block.ACORN, p.x, p.y, true, false);
			} else if (i == 2) {
				this.game.addBlock(Block.APPLE, p.x, p.y, true, false);
			} else {
				this.game.addBlock(Block.FLINT, p.x, p.y, true, false);
			}
		}*/
	}


	@Override
	protected boolean hasCollidedWith(Geometry g) {
		if (g instanceof PlayersAvatar) {
			AbstractMob am = (AbstractMob)g;
			/*if (am.side >= -1 && this.side >= 0 && am.side != this.side) { // Only the players side gets damaged, and only mobs with side >= 0 do damage
				if (this.side == Statics.SD_PLAYERS_SIDE) {
					this.damage(2);
				} else {*/
					am.damage(2);
				/*}
			}*/
			//return true;
		}
		//return false;
		return true;
	}


}
