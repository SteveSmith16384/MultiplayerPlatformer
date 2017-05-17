package com.scs.multiplayerplatformer.graphics.mobs;

import java.awt.image.BufferedImage;

import ssmith.android.compatibility.Canvas;
import ssmith.android.lib2d.Camera;
import ssmith.android.lib2d.shapes.Geometry;
import ssmith.lang.NumberFunctions;

import com.scs.multiplayerplatformer.Statics;
import com.scs.multiplayerplatformer.game.GameModule;

public class PlatformMob extends AbstractMob {

	public float move_x, move_y, max_dist, dist_moved;
	private BufferedImage bmp;
	private long start;


	public PlatformMob(GameModule _game, float x, float y, float w, float h, String _r, float off_x, float off_y, float _dist) {
		super(_game, "Platform", x, y, w, h, false, false, (byte)-1);

		move_x = off_x * Statics.PLATFORM_SPEED;
		move_y = off_y * Statics.PLATFORM_SPEED;
		max_dist = _dist;
		dist_moved = 0;
		bmp = Statics.img_cache.getImage(_r, w, h);
		
		start = System.currentTimeMillis();
	}


	@Override
	public void process(long interpol) {
		if (this.move(move_x, move_y, false) == false) {
			//this.move_x = this.move_x * -1;  Don't reverse, just pause!
			//this.move_y = this.move_y * -1;
		} else {
			dist_moved += NumberFunctions.mod(move_x);
			dist_moved += NumberFunctions.mod(move_y);
			if (dist_moved >= max_dist && this.max_dist > 0) {
				dist_moved = 0;
				this.reverseDir();
			}
		}
	}


	@Override
	protected void collidedWithBlock() {
		this.reverseDir();
	}


	private void reverseDir() {
		long end = System.currentTimeMillis();
		//long diff = end - start;
		start = System.currentTimeMillis(); // down = 5, up = 8, down = 2.5, up = 5
		this.move_x = this.move_x * -1;
		this.move_y = this.move_y * -1;
	}

	
	@Override
	public void died() {
		// Do nothing

	}

	
	@Override
	public void doDraw(Canvas g, Camera cam, long interpol) {
		// Do nothing
	}


	/*@Override
	protected boolean hasCollidedWith(Geometry g) {
		if (this.move_y < 0) {
			// Move the mob up with us
			if (g instanceof AbstractMob) {
				AbstractMob m = (AbstractMob)g;
				m.move(0, this.move_y, false);
				return false; // So it doesn't stop us moving up
			}
		}
		return true;
	}*/


	@Override
	public void doDraw(Canvas g, Camera cam, long interpol, float scale) {
		g.drawBitmap(bmp, (this.getWorldX()) * scale - cam.left, (this.getWorldY()) * scale - cam.top, paint);
		
	}

}
