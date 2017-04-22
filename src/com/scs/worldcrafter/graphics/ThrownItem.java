package com.scs.worldcrafter.graphics;

import java.util.ArrayList;

import ssmith.android.lib2d.Camera;
import ssmith.android.lib2d.MyPointF;
import ssmith.android.lib2d.shapes.Geometry;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.scs.worldcrafter.Statics;
import com.scs.worldcrafter.crafting.CraftingData;
import com.scs.worldcrafter.game.GameModule;
import com.scs.worldcrafter.game.PhysicsEngine;
import com.scs.worldcrafter.graphics.blocks.Block;
import com.scs.worldcrafter.graphics.mobs.AbstractMob;

public class ThrownItem extends GameObject {

	private PhysicsEngine phys;
	private Bitmap bmp;
	private AbstractMob thrower;
	private byte type;
	private int damage;
	
	
	public static void ThrowRock(GameModule _game, byte _type, AbstractMob _thrower, MyPointF _dir) {
		new ThrownItem(_game, _type, _thrower, _dir, 1, Statics.ROCK_SPEED, Statics.ROCK_GRAVITY, Statics.ROCK_SIZE);
	}

	
	public static void ThrowShuriken(GameModule _game, AbstractMob _thrower, MyPointF _dir) {
		new ThrownItem(_game, Block.SHURIKEN, _thrower, _dir, 1, Statics.ROCK_SPEED*2, Statics.ROCK_GRAVITY/2, Statics.ROCK_SIZE);
	}

	
	private ThrownItem(GameModule _game, byte _type, AbstractMob _thrower, MyPointF _dir, int _damage, float speed, float grav, float size) {
		this(_game, _type, new MyPointF(_thrower.getWorldCentreX(), _thrower.getWorldBounds().top + (_thrower.getHeight()*.25f)), _dir, _thrower, _damage, speed, grav, size);
	}


	public ThrownItem(GameModule _game, byte _type, MyPointF _start, MyPointF _dir, AbstractMob _thrower, int _damage, float speed, float grav, float size) {
		super(_game, "ThrownItem", true, _start.x, _start.y, size, size);

		type = _type;
		phys = new PhysicsEngine(_dir, speed, grav);
		bmp = Block.GetBitmap(Statics.img_cache, _type, size, size);
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
		phys.process();

		this.adjustLocation(phys.offset.x, phys.offset.y);
		this.updateGeometricState();

		// Has it hit anything
		ArrayList<Geometry> colls = this.getColliders(this.game.root_node);
		if (colls.size() > 0) {
			for (Geometry c : colls) { // this
				if (c == thrower) {
					// Do nothing
				} else {
					if (c instanceof ThrownItem) { // c == this
						// Do nothing
					} else if (c instanceof AbstractMob) {
						AbstractMob m = (AbstractMob)c;
						m.damage(damage);
						this.remove();
					}
				}
			}
		}

		// Has it hit the ground
		Block block = (Block)game.new_grid.getBlockAtPixel_MaybeNull(this.getWorldCentreX(), this.getWorldCentreY());
		if (block != null) {
			if (Block.CanBeHitByThrownObject(block.getType())) {
				this.remove();
				if (Block.DamagedByThrownObject(block.getType())) {
					block.damage(1, false);
				} else {
					boolean used = checkCrafting(block, this.type, block.getType());
					if (used == false && thrower == game.player && Block.AddToInv(this.type)) {
						// Return it to the players inventory
						this.game.inv.addBlock(type, 1);
					}
				}
			}
		}
	}


	private boolean checkCrafting(Block block, byte t1, byte t2) {
		boolean used = false;
		used = CraftingData.DoCrafting(game, block, this);
		return used;
	}

	
	public byte getType() {
		return this.type;
	}

}
