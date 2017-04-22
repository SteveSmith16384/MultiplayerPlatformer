package com.scs.worldcrafter.graphics;

import ssmith.android.lib2d.Camera;
import ssmith.lang.Functions;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.scs.ninja.main.lite.R;
import com.scs.worldcrafter.Statics;
import com.scs.worldcrafter.game.GameModule;

public class BankingSpaceship extends GameObject {
	
	private Bitmap bmp;
	private float speed;
	private float x_pos = -Statics.SPACESHIP_BANKING_WIDTH;
	private float y_pos = Functions.rndFloat(0, Statics.SCREEN_HEIGHT/2);
	private int dir;

	public BankingSpaceship(GameModule _game, int _dir) {
		super(_game, "BankingSpaceship", false, 0, 0, Statics.SPACESHIP_BANKING_WIDTH, Statics.SPACESHIP_BANKING_HEIGHT);
		
		dir = _dir;
		if (dir < 0) {
			x_pos = Statics.SCREEN_WIDTH;
		} else {
			x_pos = Statics.SPACESHIP_BANKING_WIDTH;
		}
		
		this.setLocation(x_pos, y_pos);
		
		bmp = Statics.img_cache.getImage(R.drawable.spaceship_banking, Statics.SPACESHIP_BANKING_WIDTH, Statics.SPACESHIP_BANKING_HEIGHT);

		speed = Functions.rndFloat(Statics.CLOUD_SPEED * .02f, Statics.CLOUD_SPEED);

		game.addToProcess_Slow(this, true);
		game.stat_node_back.attachChild(0, this); // Must be in front of sun/moon
	}
	

	@Override
	public void doDraw(Canvas g, Camera cam, long interpol) {
		if (this.visible) {
			g.drawBitmap(bmp, this.world_bounds.left - cam.left, this.world_bounds.top - cam.top, paint);
		}

	}

	
	@Override
	public void process(long interpol) {
		x_pos += (speed * dir); 
		this.setLocation(x_pos, y_pos);
		this.updateGeometricState();
		if ((dir > 0 && x_pos > Statics.SCREEN_WIDTH) || (dir < 0 && x_pos < 0)) {
			this.remove();
			new BankingSpaceship(game, dir * -1);
			
		}
	}

	
}


