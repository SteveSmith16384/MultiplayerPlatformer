package com.scs.multiplayerplatformer.graphics;

import java.awt.image.BufferedImage;

import ssmith.android.compatibility.Canvas;
import ssmith.android.lib2d.Camera;

import com.scs.multiplayerplatformer.Statics;
import com.scs.multiplayerplatformer.game.GameModule;
import com.scs.multiplayerplatformer.graphics.blocks.Block;
import com.scs.multiplayerplatformer.graphics.mobs.AbstractMob;

public class AirBubble extends GameObject {

	private BufferedImage bmp;
	
	public AirBubble(GameModule _game, AbstractMob _thrower) {
		super(_game, "AirBubble", true, _thrower.getWorldCentreX(), _thrower.getWorldCentreY(), Statics.ROCK_SIZE, Statics.ROCK_SIZE);

		bmp = Statics.img_cache.getImage("bubble", Statics.ROCK_SIZE, Statics.ROCK_SIZE);
		
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
		this.adjustLocation(0, -1 * Statics.BUBBLE_SPEED);
		this.parent.updateGeometricState();

		// Has it hit the ground
		Block b = (Block)game.new_grid.getBlockAtPixel_MaybeNull(this.getWorldCentreX(), this.getWorldCentreY());
		if (b != null) {
			if (b.getType() != Block.WATER) {
				this.remove();
			}
		} else {
			this.remove();
		}
	}


}
