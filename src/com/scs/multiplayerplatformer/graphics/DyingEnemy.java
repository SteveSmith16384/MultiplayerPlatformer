package com.scs.multiplayerplatformer.graphics;

import java.awt.image.BufferedImage;

import ssmith.android.compatibility.Canvas;
import ssmith.android.lib2d.Camera;
import ssmith.android.lib2d.MyPointF;

import com.scs.multiplayerplatformer.Statics;
import com.scs.multiplayerplatformer.game.GameModule;
import com.scs.multiplayerplatformer.game.PhysicsEngine;

public class DyingEnemy extends GameObject {

	private BufferedImage bmp[] = new BufferedImage[Statics.MAX_BMP_WIDTH];
	private PhysicsEngine phys;
	private String filename; 

	public DyingEnemy(GameModule _game, String r, GameObject o) {
		super(_game, "DyingEnemy", false, o.getWorldX(), o.getWorldY(), o.getWidth(), o.getHeight()); // Notice height and width are reversed since the dying enemy is on it's side

		filename = r;
		
		phys = new PhysicsEngine(new MyPointF(0, -1), Statics.ROCK_SPEED*2, Statics.ROCK_GRAVITY*2);
		
		this.game.root_node.attachChild(this);
		this.updateGeometricState();
		this.game.addToProcess(this);
	}


	@Override
	public void doDraw(Canvas g, Camera cam, long interpol) {
		// Do nothing
	}


	@Override
	public void doDraw(Canvas g, Camera cam, long interpol, float scale) {
		if (this.visible) {
			int width = (int)(this.getWidth() * scale);
			if (bmp[width] == null) {
				bmp[width] = Statics.img_cache.getImage(filename, this.getHeight() * scale, this.getWidth() * scale); // Notice height and width are reversed since the dying enemy is on it's side
			}

			g.drawBitmap(bmp[width], (this.world_bounds.left) * scale - cam.left, (this.world_bounds.top) * scale - cam.top, paint);
		}
	
	}


	@Override
	public void process(long interpol) {
		phys.process();
		this.adjustLocation(phys.offset.x, phys.offset.y);
		this.parent.updateGeometricState();

		if (this.getScreenY(game.root_cam) > Statics.SCREEN_HEIGHT) {
			this.remove();
		}
	}


}
