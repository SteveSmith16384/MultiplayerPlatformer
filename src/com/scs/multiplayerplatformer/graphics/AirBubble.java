package com.scs.multiplayerplatformer.graphics;

import java.awt.image.BufferedImage;

import ssmith.android.compatibility.Canvas;
import ssmith.android.lib2d.Camera;

import com.scs.multiplayerplatformer.Statics;
import com.scs.multiplayerplatformer.game.GameModule;
import com.scs.multiplayerplatformer.graphics.blocks.Block;
import com.scs.multiplayerplatformer.graphics.mobs.AbstractMob;

public final class AirBubble extends GameObject {

	private BufferedImage bmp[] = new BufferedImage[Statics.MAX_BMP_WIDTH];
	
	public AirBubble(GameModule _game, AbstractMob _thrower) {
		super(_game, "AirBubble", true, _thrower.getWorldCentreX(), _thrower.getWorldCentreY(), Statics.ROCK_SIZE/3, Statics.ROCK_SIZE/3);

		this.game.rootNode.attachChild(this);
		this.updateGeometricState();
		this.game.addToProcess(this);
	}


	@Override
	public void doDraw(Canvas g, Camera cam, long interpol) {
	}


	@Override
	public void process(long interpol) {
		this.adjustLocation(0, -1 * Statics.BUBBLE_SPEED);
		this.updateGeometricState();

		// Has it hit the ground
		Block b = (Block)game.blockGrid.getBlockAtPixel_MaybeNull(this.getWorldCentreX(), this.getWorldCentreY());
		if (b != null) {
			if (b.getType() != Block.WATER) {
				this.remove();
			}
		} else {
			this.remove();
		}
	}


	@Override
	public void doDraw(Canvas g, Camera cam, long interpol, float scale) {
		if (this.visible) {
			//g.drawBitmap(bmp, this.world_bounds.left - cam.left, this.world_bounds.top - cam.top, paint);
			int width = (int)(this.getWidth() * scale);
			if (bmp[width] == null) {
				bmp[width] = Statics.img_cache.getImage("bubble",  this.getHeight() * scale, this.getWidth() * scale);//Block.GetBufferedImage(Statics.img_cache, type, this.getHeight() * scale, this.getWidth() * scale);
			}
			g.drawBitmap(bmp[width], (this.worldBounds.left) * scale - cam.left, (this.worldBounds.top) * scale - cam.top, paint);
		}

	}


}
