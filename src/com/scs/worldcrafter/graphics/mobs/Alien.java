package com.scs.worldcrafter.graphics.mobs;

import ssmith.android.lib2d.shapes.Geometry;
import ssmith.android.util.Timer;
import ssmith.lang.Functions;

import com.scs.ninja.main.lite.R;
import com.scs.worldcrafter.Statics;
import com.scs.worldcrafter.game.GameModule;
import com.scs.worldcrafter.graphics.Bullet;
import com.scs.worldcrafter.graphics.Explosion;
import com.scs.worldcrafter.graphics.blocks.Block;
import com.scs.worldcrafter.mapgen.SimpleMobData;

public class Alien extends AbstractLandMob {

	private static final int RELOAD_TIME = 5000;

	private int x_offset = -1;
	private boolean tried_jumping = false;
	private Timer timer;

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
						boolean res = Alien.Subfactory(game, left, start);
						if (res) {
							break;
						} else {
							start -= (Statics.SQ_SIZE);
						}
					}
				} else {
					// Use generator
					Alien.Subfactory(game, gen.getWorldX(), gen.getWorldY() - (Statics.PLAYER_HEIGHT));
				}
			}
		}
	}


	public static boolean Subfactory(GameModule game, float x, float y) {
		if (Statics.monsters) {
			if (game.isAreaClear(x, y, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT, true)) {
				new Alien(game, x, y);
				return true;
			}
		}
		return false;
	}


	private Alien(GameModule _game, float x, float y) {
		super(_game, Statics.act.getString(R.string.alien), x, y, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT, (byte)2, 3, 0, true, false, Statics.SD_ENEMY_SIDE, false);

		timer = new Timer(RELOAD_TIME);

		// Todo - use proper sprite, not Zombie
		a_bmp_left[0] = Statics.img_cache.getImage(R.drawable.zombie_l0, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_left[1] = Statics.img_cache.getImage(R.drawable.zombie_l1, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_left[2] = Statics.img_cache.getImage(R.drawable.zombie_l2, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_right[0] = Statics.img_cache.getImage(R.drawable.zombie_r0, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_right[1] = Statics.img_cache.getImage(R.drawable.zombie_r1, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_right[2] = Statics.img_cache.getImage(R.drawable.zombie_r2, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
	}


	@Override
	public void process(long interpol) {
		float dist = super.getDistanceTo(game.player);//.getWorldCentreX() - this.getWorldCentreX();
		if (dist < Statics.ACTIVATE_DIST) { // Only process if close
			// Do we want to turn around?
			if (Functions.rnd(1, 70) == 1) {
				x_offset = x_offset * -1;
			}

			// Try moving
			if (this.move(x_offset * Statics.ALIEN_SPEED, 0) == false) {
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

			if (is_on_ground_or_ladder) { // Must be after we've jumped!
				tried_jumping = false;
			}

			// Check if can be seen
			/*this.visible = false;
			if (this.getDistanceTo(this.game.player) <= Statics.SCREEN_WIDTH/2) {
				this.visible = this.canSee(this.game.player);
			}*/

			// Shoot?
			if (this.visible) {
				if (timer.hasHit(interpol)) {
					new Bullet(game, this, this.game.player.getWorldCentre_CreatesNew().subtract(this.getWorldCentre_CreatesNew()).normalize(), R.drawable.laser_bolt, 10);
				}
			}
		} else if (dist > Statics.DEACTIVATE_DIST) {
			this.remove();
			// Re-add them to list to create
			this.game.original_level_data.mobs.add(new SimpleMobData(AbstractMob.ALIEN, this.getWorldX(), this.getWorldY()));

		}		
	}



	@Override
	protected void died() {
		Explosion.CreateExplosion(game, 6, this.getWorldCentreX(), this.getWorldCentreY(), R.drawable.thrown_rock);
		this.remove();
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
