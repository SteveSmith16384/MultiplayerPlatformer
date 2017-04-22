package com.scs.worldcrafter.graphics.mobs;

import ssmith.android.lib2d.shapes.Geometry;
import ssmith.lang.Functions;
import ssmith.lang.NumberFunctions;
import ssmith.util.Interval;

import com.scs.ninja.main.lite.R;
import com.scs.worldcrafter.Statics;
import com.scs.worldcrafter.game.GameModule;
import com.scs.worldcrafter.graphics.DyingEnemy;
import com.scs.worldcrafter.graphics.Explosion;
import com.scs.worldcrafter.graphics.ThrownItem;
import com.scs.worldcrafter.graphics.blocks.Block;
import com.scs.worldcrafter.mapgen.SimpleMobData;

public class EnemyNinjaEasy extends AbstractLandMob {

	private static final int TURN_DURATION = 4000; // In case can't get to player

	private static final int MAX_FRAMES = 8;
	private static final byte HEALTH = 1;

	private int x_offset = -1;
	private boolean tried_jumping = false;
	private Interval turn_interval = new Interval(TURN_DURATION);
	private Interval throw_interval = new Interval(5000);

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
						boolean res = EnemyNinjaEasy.Subfactory(game, left, start);
						if (res) {
							break;
						} else {
							start -= (Statics.SQ_SIZE);
						}
					}
				} else {
					// Use generator
					EnemyNinjaEasy.Subfactory(game, gen.getWorldX(), gen.getWorldY() - (Statics.PLAYER_HEIGHT));
				}
			}
		}
	}


	public static boolean Subfactory(GameModule game, float x, float y) {
		if (Statics.monsters) {
			if (game.isAreaClear(x, y, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT, true)) {
				new EnemyNinjaEasy(game, x, y);
				return true;
			}
		}
		return false;
	}


	private EnemyNinjaEasy(GameModule _game, float x, float y) {
		super(_game, "Enemy Ninja", x, y, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT, HEALTH, MAX_FRAMES, 200, true, false, Statics.SD_ENEMY_SIDE, true);

		a_bmp_left[0] = Statics.img_cache.getImage(R.drawable.easy_ninja_l0, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_left[1] = Statics.img_cache.getImage(R.drawable.easy_ninja_l1, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_left[2] = Statics.img_cache.getImage(R.drawable.easy_ninja_l2, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_left[3] = Statics.img_cache.getImage(R.drawable.easy_ninja_l3, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_left[4] = Statics.img_cache.getImage(R.drawable.easy_ninja_l4, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_left[5] = Statics.img_cache.getImage(R.drawable.easy_ninja_l5, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_left[6] = Statics.img_cache.getImage(R.drawable.easy_ninja_l6, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_left[7] = Statics.img_cache.getImage(R.drawable.easy_ninja_l7, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);

		a_bmp_right[0] = Statics.img_cache.getImage(R.drawable.easy_ninja_r0, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_right[1] = Statics.img_cache.getImage(R.drawable.easy_ninja_r1, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_right[2] = Statics.img_cache.getImage(R.drawable.easy_ninja_r2, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_right[3] = Statics.img_cache.getImage(R.drawable.easy_ninja_r3, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_right[4] = Statics.img_cache.getImage(R.drawable.easy_ninja_r4, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_right[5] = Statics.img_cache.getImage(R.drawable.easy_ninja_r5, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_right[6] = Statics.img_cache.getImage(R.drawable.easy_ninja_r6, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_right[7] = Statics.img_cache.getImage(R.drawable.easy_ninja_r7, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
	}


	@Override
	public void process(long interpol) {
		//if (Statics.DEBUGGING == false) {
		float dist = super.getDistanceTo(game.player); 
		if (dist < Statics.ACTIVATE_DIST) { // Only process if close
			//turn_timer -= interpol;
			//if (turn_timer <= 0) {
			if (turn_interval.hitInterval()) {
				//turn_timer = TURN_DURATION;
				float diff = game.player.getWorldCentreX() - this.getWorldCentreX();
				x_offset = NumberFunctions.sign(diff);
			}

			// Try moving
			if (this.move(x_offset * Statics.ENEMY_NINJA_SPEED, 0) == false) {
				// Can't move
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
			
			if (throw_interval.hitInterval()) {
				if (this.canSee(game.player)) {
					this.throwShuriken();
				}
			}

			if (is_on_ground_or_ladder) { // Must be after we've jumped!
				tried_jumping = false;
			}
		} else if (dist > Statics.DEACTIVATE_DIST) {
			this.remove();
			// Re-add them to list to create
			this.game.original_level_data.mobs.add(new SimpleMobData(AbstractMob.ENEMY_NINJA_EASY, this.getWorldX(), this.getWorldY()));
		}
		//}

	}


	private void throwShuriken() {
		ThrownItem.ThrowShuriken(this.game, this, game.player.getWorldCentre_CreatesNew().subtract(this.getWorldCentre_CreatesNew()).normalizeLocal());
	}
	
	
	@Override
	protected void died() {
		new DyingEnemy(game, R.drawable.easy_ninja_dying, this);
		Explosion.CreateExplosion(game, 2, this.getWorldCentreX(), this.getWorldCentreY(), R.drawable.blood_spurt);
		this.remove(); // Must be before we drop an item
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
