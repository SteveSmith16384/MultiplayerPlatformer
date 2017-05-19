package com.scs.multiplayerplatformer.graphics;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import ssmith.android.compatibility.Canvas;
import ssmith.android.lib2d.Camera;
import ssmith.android.lib2d.MyPointF;
import ssmith.android.lib2d.shapes.AbstractRectangle;

import com.scs.multiplayerplatformer.game.GameModule;
import com.scs.multiplayerplatformer.graphics.blocks.Block;
import com.scs.multiplayerplatformer.graphics.mobs.PlayersAvatar;

public abstract class Collectable extends GameObject {

	private BufferedImage bmp;

	public Collectable(GameModule _game, MyPointF _start, float size) {
		super(_game, "Collectable", true, _start.x, _start.y, size, size);

		//bmp = todo;//Block.GetBitmap(Statics.img_cache, _type, size, size);

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
			g.drawBitmap(bmp, this.world_bounds.left - cam.left, this.world_bounds.top - cam.top, paint);
		}
		
	}


	@Override
	public void process(long interpol) {
		float y = interpol;
		this.adjustLocation(0, y);
		boolean moveBack = false;

		// Check for landing
		ArrayList<AbstractRectangle> colls = game.blockGrid.getColliders(this.getWorldBounds());
		for (AbstractRectangle g : colls) {
			if (g instanceof Block) {
				Block b = (Block)g;
				if (Block.BlocksDownMovement(b.getType())) {
					moveBack = true;
				}
				if (Block.IsLadder(b.getType())) {
					moveBack = true;
				}
			}
			if (g instanceof PlayersAvatar) {
				PlayersAvatar m = (PlayersAvatar)g;
				this.collected(m);
				this.remove();
				return;
			}
		}

		if (moveBack) {
			this.adjustLocation(0, y);
		} else {
			this.updateGeometricState();
		}

	}


	protected abstract void collected(PlayersAvatar player);

}
