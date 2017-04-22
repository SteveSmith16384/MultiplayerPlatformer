package com.scs.worldcrafter.graphics;

import ssmith.android.lib2d.Camera;
import ssmith.android.lib2d.shapes.AbstractRectangle;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.scs.ninja.main.lite.R;
import com.scs.worldcrafter.Statics;
import com.scs.worldcrafter.game.GameModule;

public class BlockHighlighter extends GameObject {
	
	private static final float DURATION = 300;
	
	private Bitmap bmp;
	private float time = 0;

	public BlockHighlighter(GameModule _game, AbstractRectangle block) {
		super(_game, "BlockHighlighter", false, block.getWorldX(), block.getWorldY(), Statics.SQ_SIZE, Statics.SQ_SIZE);
		
		bmp = Statics.img_cache.getImage(R.drawable.block_highlighter, Statics.SQ_SIZE, Statics.SQ_SIZE);

		game.addToProcess_Instant(this);
		game.root_node.attachChild(this);
	}
	

	@Override
	public void doDraw(Canvas g, Camera cam, long interpol) {
		this.visible = !this.visible; // Flash
		if (this.visible) {
			g.drawBitmap(bmp, this.world_bounds.left - cam.left, this.world_bounds.top - cam.top, paint);
		}

	}

	
	@Override
	public void process(long interpol) {
		time += interpol;
		if (time > DURATION) {
			this.remove();
		}
	}

	
}
