package com.scs.worldcrafter.graphics;

import ssmith.android.lib2d.Camera;
import ssmith.android.lib2d.MyPointF;
import ssmith.android.lib2d.Node;
import ssmith.lang.Functions;
import android.graphics.Canvas;

import com.scs.worldcrafter.Statics;
import com.scs.worldcrafter.game.GameModule;

public class Smoke extends GameObject { // todo - use this
	
	private MyPointF pxl_origin;
	
	public static void CreateSmoke(GameModule game, int pieces, float pxl_x, float pxl_y) {
		Node parent_node = new Node("ExplosionParent");
		game.root_node.attachChild(parent_node);
		for (int i=0 ; i<pieces ; i++) {
			MyPointF dir = new MyPointF(Functions.rndFloat(-1, 1), Functions.rndFloat(0, -1));
			dir.normalizeLocal();
			new Smoke(game, new MyPointF(pxl_x, pxl_y), dir, parent_node);
		}
		parent_node.updateGeometricState();
	}
	
	
	private Smoke(GameModule _game, MyPointF _pxl_origin, MyPointF _dir, Node parent) {
		super(_game, "Smoke", false, _pxl_origin.x, _pxl_origin.y, Statics.ROCK_SIZE, Statics.ROCK_SIZE);
		
		pxl_origin = _pxl_origin;
		
		parent.attachChild(this);
		this.game.addToProcess_Instant(this);
		
		//this.collides = false;
	}

	
	@Override
	public void doDraw(Canvas g, Camera cam, long interpol) {
		if (this.visible) {
			//g.drawBitmap(bmp, this.world_bounds.left - cam.left, this.world_bounds.top - cam.top, paint);
		}
		
	}

	
	@Override
	public void process(long interpol) {
		/*if (len > Statics.SCREEN_WIDTH) { // Are we off the screen?
			this.remove();
		} else {
			//this.adjustLocation(phys.offset.x, phys.offset.y);
			this.updateGeometricState(true);
		}*/
		
	}

}
