package com.scs.multiplayerplatformer.graphics;

import java.awt.image.BufferedImage;

import ssmith.android.compatibility.Canvas;
import ssmith.android.lib2d.Camera;
import ssmith.android.lib2d.MyPointF;

import com.scs.multiplayerplatformer.Statics;
import com.scs.multiplayerplatformer.game.GameModule;
import com.scs.multiplayerplatformer.game.PhysicsEngine;

public class DyingEnemy extends GameObject {

	private BufferedImage bmp;
	private PhysicsEngine phys;

	public DyingEnemy(GameModule _game, String r, GameObject o) {
		super(_game, "DyingEnemy", false, o.getWorldX(), o.getWorldY(), o.getHeight(), o.getWidth()); // Notice height and width are reversed since the dying enemy is on it's side

		phys = new PhysicsEngine(new MyPointF(0, -1), Statics.ROCK_SPEED*2, Statics.ROCK_GRAVITY*2);
		bmp = Statics.img_cache.getImage(r, o.getHeight(), o.getWidth()); // Notice height and width are reversed since the dying enemy is on it's side
		
		this.game.root_node.attachChild(this);
		this.updateGeometricState();
		this.game.addToProcess_Instant(this);
	}


	@Override
	public void doDraw(Canvas g, Camera cam, long interpol) {
		if (this.visible) {
			g.drawBitmap(bmp, this.world_bounds.left - cam.left, this.world_bounds.top - cam.top, paint);
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
