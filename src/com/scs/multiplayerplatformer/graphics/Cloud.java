package com.scs.multiplayerplatformer.graphics;

import java.awt.image.BufferedImage;

import ssmith.android.compatibility.Canvas;
import ssmith.android.lib2d.Camera;
import ssmith.lang.Functions;

import com.scs.multiplayerplatformer.Statics;
import com.scs.multiplayerplatformer.game.GameModule;

public final class Cloud extends GameObject {

	private BufferedImage bmp_day;//, bmp_night;
	private float speed;
	private float x_off = -Statics.CLOUD_WIDTH;
	private float y_off = Functions.rndFloat(0, Statics.SCREEN_HEIGHT/2);

	public Cloud(GameModule _game) {
		super(_game, "Cloud", false, 0, 0, Statics.CLOUD_WIDTH, Statics.CLOUD_HEIGHT);

		this.setLocation(x_off, y_off);

		bmp_day = Statics.img_cache.getImage("white_cloud", Statics.CLOUD_WIDTH, Statics.CLOUD_HEIGHT);
		//bmp_night = Statics.img_cache.getImage("red_cloud", Statics.CLOUD_WIDTH, Statics.CLOUD_HEIGHT);
		speed = Functions.rndFloat(Statics.CLOUD_SPEED * .05f, Statics.CLOUD_SPEED);

		game.addToProcess_Instant(this);//, true);
		game.stat_node_back.attachChild(0, this); // Must be in front of sun/moon
	}


	@Override
	public void doDraw(Canvas g, Camera cam, long interpol) {
		// Do nothing
	}


	@Override
	public void process(long interpol) {
		x_off += speed; 
		this.setLocation(x_off, y_off);
		this.updateGeometricState();
		if (x_off > Statics.SCREEN_WIDTH) {
			this.remove();
			new Cloud(game);

		}
	}


	@Override
	public void doDraw(Canvas g, Camera cam, long interpol, float scale) {
		if (this.visible) {
			g.drawBitmap(bmp_day, this.world_bounds.left - cam.left, this.world_bounds.top - cam.top, paint);
		}
		
	}


}


