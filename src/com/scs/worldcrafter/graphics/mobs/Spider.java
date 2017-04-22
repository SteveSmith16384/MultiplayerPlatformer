package com.scs.worldcrafter.graphics.mobs;


public class Spider { // todo - use this  - extends AbstractMob {
/*
	private static final float TURN_DURATION = 4000; // In case can't get to player
	private static final int MAX_FRAMES = 3;
	private static final int HEALTH = 2;

	private int x_offset = -1;
	private boolean tried_jumping = false;
	private float turn_timer = 0;


	public static void Factory(GameModule game, Block gen) { // gen == null for normal appearance
		if (Statics.game_mode != Statics.GM_CONSTRUCTION) {
			if (gen == null) {
				float start = game.root_cam.top;
				while (start <= game.root_cam.bottom) {
					boolean res;
					if (Functions.rnd(1, 2) == 1) {
						res = Spider.Subfactory(game, game.root_cam.left, start);//game.player.getWorldCentreY() - Statics.SCREEN_HEIGHT);
					} else {
						res = Spider.Subfactory(game, game.root_cam.left + Statics.SCREEN_WIDTH, start);//, game.player.getWorldCentreY() - Statics.SCREEN_HEIGHT);
					}
					if (res) {
						break;
					} else {
						start += (Statics.SQ_SIZE*2);
					}
				}
			} else {
				// Use generator
				Spider.Subfactory(game, gen.getWorldX(), gen.getWorldY() - Statics.PLAYER_HEIGHT);
			}
		}
	}


	private static boolean Subfactory(GameModule game, float x, float y) {
		if (game.getNumProcessInstant() < Statics.MAX_INSTANTS) {
			if (game.isAreaClearOfMobs(x, y, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT)) {
				new Spider(game, x, y);
			} else {
				return false;
			}
			//dummy_rect.removeFromParent();
		}
		return true;
	}


	private Spider(GameModule _game, float x, float y) {
		super(_game, "Spider", x, y, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT, HEALTH, MAX_FRAMES, 200, true, true);

		a_bmp_left[0] = game.img_cache.getImage(R.drawable.zombie_l0, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_left[1] = game.img_cache.getImage(R.drawable.zombie_l1, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_left[2] = game.img_cache.getImage(R.drawable.zombie_l2, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_right[0] = game.img_cache.getImage(R.drawable.zombie_r0, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_right[1] = game.img_cache.getImage(R.drawable.zombie_r1, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
		a_bmp_right[2] = game.img_cache.getImage(R.drawable.zombie_r2, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);

		this.game.view.sound_manager.playSound(R.raw.walkandgrunt);
	}


	@Override
	public void process(long interpol) {
		float diff = game.player.getWorldCentreX() - this.getWorldCentreX();
		if (NumberFunctions.mod(diff) < Statics.SCREEN_WIDTH) { // Only process if close
			turn_timer -= interpol;
			if (turn_timer <= 0) {
				turn_timer = TURN_DURATION;
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
		}

	}


	@Override
	protected void died() {
		game.showMsg("Zombie killed");
		Explosion.CreateExplosion(game, 6, this.getWorldCentreX(), this.getWorldCentreY());
		this.remove(); // Must be before we drop an item
		// Drop an item
		Point p = this.game.new_grid.getMapCoordsFromPixels(this.getWorldCentreX(), this.getWorldBounds().bottom-1);
		if (this.game.new_grid.isSquareEmpty(p.x, p.y)) {
			int i = Functions.rnd(1, 3);
			if (i == 1) {
				this.game.addBlock(Block.ACORN, p.x, p.y, true, false);
			} else if (i == 2) {
				this.game.addBlock(Block.APPLE, p.x, p.y, true, false);
			} else {
				this.game.addBlock(Block.FLINT, p.x, p.y, true, false);
			}
		} else {

		}
	}
*/

}
