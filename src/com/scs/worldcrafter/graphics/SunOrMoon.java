package com.scs.worldcrafter.graphics;

import ssmith.android.framework.AbstractActivity;
import ssmith.android.lib2d.Camera;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.scs.ninja.main.lite.R;
import com.scs.worldcrafter.Statics;
import com.scs.worldcrafter.game.GameModule;

public class SunOrMoon extends GameObject {

	// Stages
	private static final int GOING_UP = 0;
	private static final int COMING_DOWN = 1;

	private boolean sun;
	private Bitmap bmp;
	private float y_off = Statics.SCREEN_HEIGHT;
	private int stage = GOING_UP;

	public SunOrMoon(GameModule _game, boolean _sun) {
		super(_game, "SunMoon", false, Statics.SCREEN_WIDTH/2, Statics.SCREEN_HEIGHT, Statics.SUN_SIZE, Statics.SUN_SIZE);

		AbstractActivity act = Statics.act;
		
		sun = _sun;
		if (sun) {
			bmp = Statics.img_cache.getImage(R.drawable.sun, Statics.SUN_SIZE, Statics.SUN_SIZE);
			act.sound_manager.playSound(R.raw.outside_ambience);
		} else {
			bmp = Statics.img_cache.getImage(R.drawable.moon, Statics.SUN_SIZE, Statics.SUN_SIZE);
			act.sound_manager.playSound(R.raw.horrorambience);
		}
		game.is_day = sun;
		
		/*if (game.is_day == false) {
			this.game.showToast("Night time!");
		}*/

		game.addToProcess_Instant(this);
		game.stat_node_back.attachChild(0, this);
	}


	@Override
	public void doDraw(Canvas g, Camera cam, long interpol) {
		if (this.visible) {
			g.drawBitmap(bmp, this.world_bounds.left - cam.left, this.world_bounds.top - cam.top, paint);
		}

	}


	@Override
	public void process(long interpol) {
		if (stage == GOING_UP) {
			y_off -= Statics.SUN_SPEED;
		} else {
			y_off += Statics.SUN_SPEED;
		}
		this.setLocation(Statics.SCREEN_WIDTH/2, y_off);
		this.updateGeometricState();
		if (stage == GOING_UP) {
			if (y_off < 0) {
				stage = COMING_DOWN;
			}
		} else { // Coming down
			if (y_off > Statics.SCREEN_HEIGHT) {
				this.remove();
				new SunOrMoon(game, !sun);
			}
		}
	}


}


