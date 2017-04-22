package com.scs.worldcrafter.graphics.mobs;

import ssmith.android.lib2d.Camera;
import ssmith.android.lib2d.MyPointF;
import ssmith.android.lib2d.shapes.Geometry;
import ssmith.android.util.Timer;
import ssmith.lang.Functions;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

import com.scs.ninja.main.lite.R;
import com.scs.worldcrafter.Statics;
import com.scs.worldcrafter.game.GameModule;
import com.scs.worldcrafter.graphics.Bullet;
import com.scs.worldcrafter.graphics.Explosion;

public class Helicopter extends AbstractMob {

	private Bitmap[] bmp = new Bitmap[2];
	private Timer anim_timer = new Timer(500);
	private boolean curr_bmp1 = true;
	private int off_x = 1;
	private float down_dist = 0;

	public static void Factory(GameModule game, float x, float y) {
		if (Statics.monsters) {
			if (game.getNumProcessInstant() < Statics.MAX_INSTANTS) {
				if (x < 0 || y < 0) {
				x = game.root_cam.left + (Functions.rnd(0, 1) * Statics.SCREEN_WIDTH);
				y = game.root_cam.top;// - (Statics.SCREEN_HEIGHT/2);
				}
				if (game.getNumProcessInstant() < Statics.MAX_INSTANTS) {
					RectF dummy_rect = new RectF(x, y, x+Statics.INVADER_WIDTH, y+Statics.INVADER_HEIGHT);
					if (game.new_grid.getColliders(dummy_rect).size() == 0) {
						new Helicopter(game, x, y);
					}
				}
			}
		}
	}


	private Helicopter(GameModule _game, float x, float y) {
		super(_game, "Helicopter", x, y, Statics.INVADER_WIDTH, Statics.INVADER_HEIGHT, (byte)1, false, true, Statics.SD_ENEMY_SIDE);

		bmp[0] = Statics.img_cache.getImage(R.drawable.helicopter_left, Statics.INVADER_WIDTH, Statics.INVADER_HEIGHT);
		bmp[1] = Statics.img_cache.getImage(R.drawable.helicopter_right, Statics.INVADER_WIDTH, Statics.INVADER_HEIGHT);

	}


	@Override
	public void doDraw(Canvas g, Camera cam, long interpol) {
		if (this.visible) {
			if (curr_bmp1) {
				g.drawBitmap(bmp[1], this.getWorldX() - cam.left, this.getWorldY() - cam.top, paint);
			} else {
				g.drawBitmap(bmp[0], this.getWorldX() - cam.left, this.getWorldY() - cam.top, paint);
			}
		}
	}


	protected void died() {
		this.remove();
	}


	@Override
	public void process(long interpol) {
		if (anim_timer.hasHit(interpol)) {
			curr_bmp1 = !curr_bmp1;
		}
		if (Functions.rnd(1, 30) == 1) {
			new Bullet(game, this, new MyPointF(0, 1), R.drawable.laser_bolt, 10);
		}
		if (down_dist <= 0) {
			if (this.off_x > 0) {
				if (this.getWorldCentreX() > game.player.getWorldCentreX() + (Statics.SCREEN_WIDTH/2)) {
					this.down_dist = getDropDist();
					this.off_x = this.off_x * -1;
				}
			} else if (this.off_x < 0) {
				if (this.getWorldCentreX() < game.player.getWorldCentreX() - (Statics.SCREEN_WIDTH/2)) {
					this.down_dist = getDropDist();
					this.off_x = this.off_x * -1;
				}
			}
			if (this.move(off_x * Statics.INVADER_SPEED, 0) == false) {
				this.remove();
			}
		} else { // Moving down
			down_dist -= Statics.INVADER_SPEED;
			if (this.move(0, Statics.INVADER_SPEED) == false) {
				this.remove();
			}

		}

	}


	private float getDropDist() {
		return Statics.SQ_SIZE * 1.2f;
	}


	protected void remove() {
		Explosion.CreateExplosion(game, 6, this.getWorldCentreX(), this.getWorldCentreY(), R.drawable.thrown_rock);
		super.remove();
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
