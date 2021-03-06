package com.scs.multiplayerplatformer.graphics;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import ssmith.android.compatibility.Canvas;
import ssmith.android.lib2d.Camera;
import ssmith.android.lib2d.MyPointF;
import ssmith.android.lib2d.shapes.Geometry;

import com.scs.multiplayerplatformer.Collision;
import com.scs.multiplayerplatformer.Statics;
import com.scs.multiplayerplatformer.game.GameModule;
import com.scs.multiplayerplatformer.game.PhysicsEngine;
import com.scs.multiplayerplatformer.graphics.blocks.Block;
import com.scs.multiplayerplatformer.graphics.mobs.AbstractMob;

public class ThrownItem extends GameObject {

	private PhysicsEngine phys;
	private BufferedImage bmp[] = new BufferedImage[Statics.MAX_BMP_WIDTH];
	public AbstractMob thrower;
	private byte type;
	public boolean collidesWithOthers;

	public static void throwRock(GameModule _game, byte _type, AbstractMob _thrower, MyPointF _dir) {
		new ThrownItem(_game, _type, _thrower, _dir, 1, Statics.ROCK_SPEED, Statics.ROCK_GRAVITY, Statics.ROCK_SIZE);
	}


	public static void throwShuriken(GameModule _game, AbstractMob _thrower, MyPointF _dir) {
		new ThrownItem(_game, Block.SHURIKEN, _thrower, _dir, 1, Statics.ROCK_SPEED*2, Statics.ROCK_GRAVITY/2, Statics.ROCK_SIZE);
	}


	private ThrownItem(GameModule _game, byte _type, AbstractMob _thrower, MyPointF _dir, int _damage, float speed, float grav, float size) {
		this(_game, _type, new MyPointF(_thrower.getWorldCentreX(), _thrower.getWorldBounds().top + (_thrower.getHeight()*.25f)), _dir, _thrower, _damage, speed, grav, size, false);
	}


	public ThrownItem(GameModule _game, byte _type, MyPointF _start, MyPointF _dir, AbstractMob _thrower, int _damage, float speed, float grav, float size, boolean _collidesWithothers) {
		super(_game, "ThrownItem", true, _start.x, _start.y, size, size);

		type = _type;
		phys = new PhysicsEngine(_dir, speed, grav);
		thrower = _thrower;
		collidesWithOthers = _collidesWithothers;

		this.game.rootNode.attachChild(this);
		this.updateGeometricState();
		this.game.addToProcess(this);
	}


	@Override
	public void doDraw(Canvas g, Camera cam, long interpol) {
		// Do nothing
	}


	@Override
	public void process(long interpol) {
		phys.process();

		this.adjustLocation(phys.offset.x, phys.offset.y);
		this.updateGeometricState();

		// Has it hit anything
		ArrayList<Geometry> colls = this.getColliders(this.game.rootNode);
		if (colls.size() > 0) {
			for (Geometry c : colls) { // this
				Collision.Collided(this, c);
			}
		}

		// Has it hit the ground
		Block block = (Block)game.blockGrid.getBlockAtPixel_MaybeNull(this.getWorldCentreX(), this.getWorldCentreY());
		if (block != null) {
			if (Block.CanBeHitByThrownObject(block.getType())) {
				this.remove();
				if (Block.DamagedByThrownObject(block.getType())) {
					block.damage(1, false, null);
				}
				return;
			}
		}

		// See if we're off the map
		if (this.getWorldY() < 0) {
			this.remove();
		}
	}


	public byte getType() {
		return this.type;
	}


	@Override
	public void doDraw(Canvas g, Camera cam, long interpol, float scale) {
		if (this.visible) {
			int width = (int)(this.getWidth() * scale);
			if (bmp[width] == null) {
				bmp[width] = Block.GetBufferedImage(Statics.img_cache, type, this.getHeight() * scale, this.getWidth() * scale);
			}
			g.drawBitmap(bmp[width], (this.worldBounds.left) * scale - cam.left, (this.worldBounds.top) * scale - cam.top, paint);
		}

	}

}
