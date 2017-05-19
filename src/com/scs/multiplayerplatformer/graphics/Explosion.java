package com.scs.multiplayerplatformer.graphics;

import java.awt.image.BufferedImage;

import ssmith.android.compatibility.Canvas;
import ssmith.android.lib2d.Camera;
import ssmith.android.lib2d.MyPointF;
import ssmith.android.lib2d.Node;
import ssmith.lang.Functions;

import com.scs.multiplayerplatformer.Statics;
import com.scs.multiplayerplatformer.game.GameModule;
import com.scs.multiplayerplatformer.game.PhysicsEngine;

public final class Explosion extends GameObject {
	
	private PhysicsEngine phys;
	private BufferedImage bmp[] = new BufferedImage[Statics.MAX_BMP_WIDTH];
	private MyPointF pxl_origin;
	private String filename;
	
	public static void CreateExplosion(GameModule game, int pieces, float pxl_x, float pxl_y, String _r) {
		//game.act.sound_manager.playSound(R.raw.explosion1);
		
		Node parent_node = new Node("ExplosionParent");
		game.root_node.attachChild(parent_node);
		for (int i=0 ; i<pieces ; i++) {
			MyPointF dir = new MyPointF(Functions.rndFloat(-1, 1), Functions.rndFloat(0, -1));
			dir.normalizeLocal();
			new Explosion(game, new MyPointF(pxl_x, pxl_y), dir, parent_node, _r);
		}
		parent_node.updateGeometricState();
	}
	
	
	private Explosion(GameModule _game, MyPointF _pxl_origin, MyPointF _dir, Node parent, String _r) {
		super(_game, "Explosion", false, _pxl_origin.x, _pxl_origin.y, Statics.ROCK_SIZE, Statics.ROCK_SIZE);
		
		filename = _r;
		phys = new PhysicsEngine(_dir, Statics.ROCK_SPEED, Statics.ROCK_GRAVITY);
		//bmp = Statics.img_cache.getImage(r, Statics.ROCK_SIZE, Statics.ROCK_SIZE);
		pxl_origin = _pxl_origin;
		
		parent.attachChild(this);
		this.game.addToProcess(this);
		
		//this.collides = false;
	}

	
	@Override
	public void doDraw(Canvas g, Camera cam, long interpol) {
		// Do nothing
	}

	
	@Override
	public void process(long interpol) {
		float len = this.pxl_origin.subtract(this.getWorldCentre_CreatesNew()).length(); // todo - check physical pos
		if (len > Statics.SCREEN_WIDTH) { // Are we off the screen?
			this.remove();
		} else {
			phys.process();
			this.adjustLocation(phys.offset.x, phys.offset.y);
			this.updateGeometricState();
		}
		
	}


	@Override
	public void doDraw(Canvas g, Camera cam, long interpol, float scale) {
		if (this.visible) {
			int width = (int)(this.getWidth() * scale);
			if (bmp[width] == null) {
				bmp[width] = Statics.img_cache.getImage(filename, Statics.ROCK_SIZE * scale, Statics.ROCK_SIZE * scale); // Notice height and width are reversed since the dying enemy is on it's side
			}
			g.drawBitmap(bmp[width], (this.world_bounds.left) * scale - cam.left, (this.world_bounds.top) * scale - cam.top, paint);
		}
	}

}
