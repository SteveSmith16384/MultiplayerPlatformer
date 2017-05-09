package com.scs.multiplayerplatformer.graphics.mobs;

import ssmith.lang.Functions;
import ssmith.lang.NumberFunctions;
import ssmith.util.Interval;
import ssmith.util.ReturnObject;

import com.scs.multiplayerplatformer.Statics;
import com.scs.multiplayerplatformer.game.GameModule;
import com.scs.multiplayerplatformer.graphics.DyingEnemy;
import com.scs.multiplayerplatformer.graphics.Explosion;
import com.scs.multiplayerplatformer.graphics.ThrownItem;
import com.scs.multiplayerplatformer.graphics.blocks.Block;
import com.scs.multiplayerplatformer.mapgen.SimpleMobData;

public class EnemyNinjaEasy extends AbstractWalkingMob {

	private static final int TURN_DURATION = 4000; // In case can't get to player

	private static final int MAX_FRAMES = 8;

	private int x_offset = -1;
	private boolean tried_jumping = false;
	private Interval turn_interval = new Interval(TURN_DURATION);
	private Interval throw_interval = new Interval(5000);

	public static void Factory(GameModule game, Block gen) { // gen == null for normal appearance
		if (game.getNumProcessInstant() < Statics.MAX_INSTANTS) {
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


	public static boolean Subfactory(GameModule game, float x, float y) {
		if (game.isAreaClear(x, y, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT, true)) {
			new EnemyNinjaEasy(game, x, y);
			return true;
		}
		return false;
	}


	private EnemyNinjaEasy(GameModule _game, float x, float y) {
		super(_game, "Enemy Ninja", x, y, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT, MAX_FRAMES, 200, true, false, Statics.SD_ENEMY_SIDE, true);
	}
	

	@Override
	public void process(long interpol) {
		ReturnObject<PlayersAvatar> playerTemp = new ReturnObject<>();
		float dist = this.getDistanceToClosestPlayer(playerTemp); 
		if (dist < Statics.ACTIVATE_DIST) { // Only process if close
			PlayersAvatar player = playerTemp.toReturn;
			if (turn_interval.hitInterval()) {
				if (player != null) {
					float diff = player.getWorldCentreX() - this.getWorldCentreX();
					x_offset = NumberFunctions.sign(diff);
				}
			}

			// Try moving
			if (frozenUntil < System.currentTimeMillis()) {
				if (this.move(x_offset * Statics.ENEMY_NINJA_SPEED, 0, false) == false) {
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
			}

			performJumpingOrGravity();
			checkForHarmingBlocks();

			if (frozenUntil < System.currentTimeMillis()) {
				if (throw_interval.hitInterval()) {
					player = this.getVisiblePlayer(); 
					if (player != null) {
						if (Statics.DEBUG == false) {
							this.throwShuriken(player);
						}
					}
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


	private void throwShuriken(PlayersAvatar player) {
		ThrownItem.ThrowShuriken(this.game, this, player.getWorldCentre_CreatesNew().subtract(this.getWorldCentre_CreatesNew()).normalizeLocal());
	}


	@Override
	public void died() {
		new DyingEnemy(game, "easy_ninja_dying", this);
		Explosion.CreateExplosion(game, 2, this.getWorldCentreX(), this.getWorldCentreY(), "blood_spurt");
		this.remove(); // Must be before we drop an item
	}


	@Override
	protected void generateBitmaps(int size, float scale) {
		a_bmp_left[size][0] = Statics.img_cache.getImage("easy_ninja_l0", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);
		a_bmp_left[size][1] = Statics.img_cache.getImage("easy_ninja_l1", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);
		a_bmp_left[size][2] = Statics.img_cache.getImage("easy_ninja_l2", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);
		a_bmp_left[size][3] = Statics.img_cache.getImage("easy_ninja_l3", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);
		a_bmp_left[size][4] = Statics.img_cache.getImage("easy_ninja_l4", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);
		a_bmp_left[size][5] = Statics.img_cache.getImage("easy_ninja_l5", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);
		a_bmp_left[size][6] = Statics.img_cache.getImage("easy_ninja_l6", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);
		a_bmp_left[size][7] = Statics.img_cache.getImage("easy_ninja_l7", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);

		a_bmp_right[size][0] = Statics.img_cache.getImage("easy_ninja_r0", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);
		a_bmp_right[size][1] = Statics.img_cache.getImage("easy_ninja_r1", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);
		a_bmp_right[size][2] = Statics.img_cache.getImage("easy_ninja_r2", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);
		a_bmp_right[size][3] = Statics.img_cache.getImage("easy_ninja_r3", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);
		a_bmp_right[size][4] = Statics.img_cache.getImage("easy_ninja_r4", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);
		a_bmp_right[size][5] = Statics.img_cache.getImage("easy_ninja_r5", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);
		a_bmp_right[size][6] = Statics.img_cache.getImage("easy_ninja_r6", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);
		a_bmp_right[size][7] = Statics.img_cache.getImage("easy_ninja_r7", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);
	}


}
