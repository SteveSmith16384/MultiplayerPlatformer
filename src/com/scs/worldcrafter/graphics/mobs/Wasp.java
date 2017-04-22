package com.scs.worldcrafter.graphics.mobs;

import ssmith.android.lib2d.Camera;
import ssmith.android.lib2d.shapes.Geometry;
import ssmith.lang.Functions;
import ssmith.lang.NumberFunctions;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.scs.ninja.main.lite.R;
import com.scs.worldcrafter.Statics;
import com.scs.worldcrafter.game.GameModule;
import com.scs.worldcrafter.graphics.blocks.Block;
import com.scs.worldcrafter.mapgen.SimpleMobData;

public class Wasp extends AbstractMob {

	private static final int TURN_DURATION = 500; // In case can't get to player

	private Bitmap[] bmp_left = new Bitmap[2];
	private Bitmap[] bmp_right = new Bitmap[2];
	private int off_x = 1, off_y;
	private boolean facing_left = true;
	private int left_right_timer, up_down_timer;


	public static void Factory(GameModule game, Block gen) { // gen == null for normal appearance
		if (game.getNumProcessInstant() < Statics.MAX_INSTANTS) {
			if (Statics.monsters) {
				if (gen == null) {
					float start = game.root_cam.bottom + (Statics.WASP_HEIGHT);
					float left = game.root_cam.left - Statics.WASP_WIDTH;
					if (Functions.rnd(1, 2) == 1) {
						left = game.root_cam.right + Statics.PLAYER_WIDTH;
					}
					while (start >= game.root_cam.top - (Statics.WASP_HEIGHT)) {
						boolean res = Wasp.Subfactory(game, left, start);
						if (res) {
							break;
						} else {
							start -= (Statics.SQ_SIZE);
						}
					}
				} else {
					// Use generator
					Wasp.Subfactory(game, gen.getWorldX(), gen.getWorldY() - (Statics.WASP_HEIGHT));
				}
			}
		}
	}


	public static boolean Subfactory(GameModule game, float x, float y) {
		if (Statics.monsters) {
			if (game.isAreaClear(x, y, Statics.WASP_WIDTH, Statics.WASP_HEIGHT, true)) {
				new Wasp(game, x, y);
				return true;
			}
		}
		return false;
	}


	private Wasp(GameModule _game, float x, float y) {
		super(_game, Statics.act.getString(R.string.wasp), x, y, Statics.WASP_WIDTH, Statics.WASP_HEIGHT, (byte)1, false, false, Statics.SD_ENEMY_SIDE);

		bmp_left[0] = Statics.img_cache.getImage(R.drawable.wasp_l1, Statics.WASP_WIDTH, Statics.WASP_HEIGHT);
		bmp_left[1] = Statics.img_cache.getImage(R.drawable.wasp_l2, Statics.WASP_WIDTH, Statics.WASP_HEIGHT);
		bmp_right[0] = Statics.img_cache.getImage(R.drawable.wasp_r1, Statics.WASP_WIDTH, Statics.WASP_HEIGHT);
		bmp_right[1] = Statics.img_cache.getImage(R.drawable.wasp_r2, Statics.WASP_WIDTH, Statics.WASP_HEIGHT);

	}


	@Override
	public void doDraw(Canvas g, Camera cam, long interpol) {
		if (this.visible) {
			if (this.facing_left) {
				g.drawBitmap(bmp_left[Functions.rnd(0, 1)], this.getWorldX() - cam.left, this.getWorldY() - cam.top, paint);
			} else {
				g.drawBitmap(bmp_right[Functions.rnd(0, 1)], this.getWorldX() - cam.left, this.getWorldY() - cam.top, paint);
			}
		}
	}


	protected void died() {
		game.showToast("Wasp killed");
		this.remove();
	}


	@Override
	public void process(long interpol) {
		float dist = super.getDistanceTo(game.player); 
		if (dist < Statics.ACTIVATE_DIST) { // Only process if close
			if (Functions.rnd(1, 10) == 1) {
				// move randomly
				this.move(Functions.rnd(-1, 1) * Statics.WASP_SPEED, 0);
				this.move(0, Functions.rnd(-1, 1) * Statics.WASP_SPEED);
			} else {
				// Move left/right
				if (this.left_right_timer > 0) {
					this.left_right_timer -= interpol;
				} else {
					float diff = game.player.getWorldCentreX() - this.getWorldCentreX();
					off_x = NumberFunctions.sign(diff);
				}
				if (this.move(off_x * Statics.WASP_SPEED, 0) == false) {
					off_x = off_x * -1;
					this.left_right_timer = TURN_DURATION;
				}

				// Move up/down
				if (this.up_down_timer > 0) {
					this.up_down_timer -= interpol;
				} else {
					float diff = game.player.getWorldCentreY() - this.getWorldCentreY();
					off_y = NumberFunctions.sign(diff);
				}
				if (this.move(0, off_y * Statics.WASP_SPEED) == false) {
					off_y = off_y * -1;
					this.up_down_timer = TURN_DURATION;
				}
			}
		} else if (dist > Statics.DEACTIVATE_DIST) {
			this.remove();
			// Re-add them to list to create
			this.game.original_level_data.mobs.add(new SimpleMobData(AbstractMob.WASP, this.getWorldX(), this.getWorldY()));
		}
	}


	protected void remove() {
		//Explosion.CreateExplosion(game, 6, this.getWorldCentreX(), this.getWorldCentreY());
		this.removeFromParent();
		this.game.removeFromProcess(this);
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
