package com.scs.multiplayerplatformer.graphics;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import ssmith.android.compatibility.Canvas;
import ssmith.android.lib2d.Camera;
import ssmith.android.lib2d.MyPointF;
import ssmith.android.lib2d.shapes.Geometry;

import com.scs.multiplayerplatformer.Statics;
import com.scs.multiplayerplatformer.game.GameModule;
import com.scs.multiplayerplatformer.graphics.blocks.Block;
import com.scs.multiplayerplatformer.graphics.mobs.AbstractMob;

public class Bullet extends GameObject {

	private BufferedImage bmp;
	private AbstractMob thrower;
	private MyPointF dir;
	private int damage;
	
	public Bullet(GameModule _game, AbstractMob _thrower, MyPointF _dir, String bmp_id, int _damage) {
		super(_game, "Bullet", true, _thrower.getWorldCentreX(), _thrower.getWorldCentreY(), Statics.ROCK_SIZE, Statics.ROCK_SIZE);

		bmp = Statics.img_cache.getImage(bmp_id, Statics.ROCK_SIZE, Statics.ROCK_SIZE);
		dir = _dir.normalize();
		thrower = _thrower;
		damage = _damage;
		
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
		this.adjustLocation(this.dir.x * Statics.BULLET_SPEED, this.dir.y * Statics.BULLET_SPEED);
		this.parent.updateGeometricState();

		// Has it hit anything
		ArrayList<Geometry> colls = this.getColliders(this.game.root_node);
		if (colls.size() > 0) {
			for (Geometry c : colls) {
				if (c == thrower) {
					// Do nothing
				} else {
					if (c instanceof AbstractMob) {
						AbstractMob m = (AbstractMob)c;
						m.died();//.damage(damage);
					}
					this.remove();
				}
			}
		}

		// Has it hit the ground
		Block b = (Block)game.new_grid.getBlockAtPixel_MaybeNull(this.getWorldCentreX(), this.getWorldCentreY());
		if (b != null) {
			if (Block.BlocksAllMovement(b.getType())) {
				b.damage(1, false, null);
				this.remove();
			}
		}
	}


}
