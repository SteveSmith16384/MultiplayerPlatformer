package com.scs.multiplayerplatformer.graphics.mobs;

import java.util.ArrayList;

import ssmith.android.compatibility.RectF;
import ssmith.android.lib2d.shapes.AbstractRectangle;
import ssmith.android.lib2d.shapes.Geometry;
import ssmith.lang.Functions;
import ssmith.util.Interval;

import com.scs.multiplayerplatformer.Collision;
import com.scs.multiplayerplatformer.Statics;
import com.scs.multiplayerplatformer.game.GameModule;
import com.scs.multiplayerplatformer.graphics.AirBubble;
import com.scs.multiplayerplatformer.graphics.GameObject;
import com.scs.multiplayerplatformer.graphics.blocks.Block;
import com.scs.multiplayerplatformer.mapgen.SimpleMobData;

public abstract class AbstractMob extends GameObject {

	private static final int MAX_HOLD_BREATH_TIME = 100;

	// Types
	public static final byte PLAYER = 0;
	public static final byte ENEMY_NINJA_EASY = 8;
	public static final byte WASP = 9;
	public static final byte PLATFORM1 = 11;

	protected byte current_item = GameModule.HAND;
	//private boolean remove_if_far_away;
	private boolean destroy_blocks;
	protected RectF tmp_rect = new RectF();
	public byte side;
	private int hold_breath_time = MAX_HOLD_BREATH_TIME;
	public boolean is_on_ice = false;
	private Interval bubble_int = new Interval(1000, false);
	public long frozenUntil = 0;

	public AbstractMob(GameModule _game, String name, float x, float y, float w, float h, boolean _remove_if_far_away, boolean _destroy_blocks, byte _side) { // todo - remove _remove_if_far_away
		super(_game, name, true, x, y, w, h);

		//remove_if_far_away = _remove_if_far_away;
		destroy_blocks = _destroy_blocks;
		side = _side;

		game.addToProcess(this);
		game.root_node.attachChild(this);

		this.updateGeometricState();
	}


	public static void CreateMob(GameModule game, SimpleMobData sm) {
		switch (sm.getType()) {
		case AbstractMob.ENEMY_NINJA_EASY:
			EnemyNinjaEasy.Subfactory(game, sm.pixel_x, sm.pixel_y);
			break;
		case AbstractMob.WASP:
			Wasp.Subfactory(game, sm.pixel_x, sm.pixel_y);
			break;
		case AbstractMob.PLATFORM1:
			//new PlatformMob(game, sm.pixel_x, sm.pixel_y, Statics.SQ_SIZE, Statics.SQ_SIZE, R.drawable.grass, 1, 0, Statics.SQ_SIZE * 4);
			//new PlatformMob(game, sm.pixel_x, sm.pixel_y, Statics.SQ_SIZE*2, Statics.SQ_SIZE, "grass", 0, -1, Statics.SQ_SIZE * 4);
			break;
		default:
			if (Statics.RELEASE_MODE == false) {
				throw new RuntimeException("Unknown mob type: " + sm.getType());
			} else {
				// Do nothing
			}
		}
	}


	/*public byte getType() {
		if (this instanceof PlayersAvatar) {
			return PLAYER;
		} else {
			if (Statics.RELEASE_MODE == false) {
				throw new RuntimeException("Unknown type: " + this.toString());
			}
			return -1;
		}
	}*/


	// Returns true of move() was successful
	protected boolean move(float off_x, float off_y, boolean ladderBlocks) {
		/*if (remove_if_far_away) {
			if (checkIfTooFarAway()) { 
				return false; 
			}
		}*/

		if (off_x != 0 && off_y != 0) {
			throw new RuntimeException("Movement must only be along one axis at a time");
		}

		float prev_x = this.getWorldX();
		float prev_y = this.getWorldY();

		this.adjustLocation(off_x, off_y);
		this.updateGeometricState();

		is_on_ice = false;

		ArrayList<AbstractRectangle> colls = game.blockGrid.getColliders(this.getWorldBounds());

		for (AbstractRectangle ar : colls) {
			if (ar instanceof Block) {
				Block b = (Block)ar;
				if (this instanceof PlayersAvatar) {
					b.touched((PlayersAvatar)this);
				}
				boolean blocked = false;

				if (Block.GetHarm(b.getType()) > 0) {
					this.died();
					return false;
				}
				if (Block.BlocksAllMovement(b.getType())) {
					blocked = true;
				}
				if (ladderBlocks && Block.BlocksDownMovement(b.getType())) {
					blocked = true;
				}
				if (blocked) {
					this.is_on_ice = (b.getType() == Block.SNOW);

					// Move us up to the object we hit
					if (off_x < 0) {
						prev_x = b.getWorldBounds().right + 1;
					} else if (off_x > 0) {
						prev_x = b.getWorldBounds().left - this.getWidth() - 1;
					} else if (off_y < 0) {
						prev_y = b.getWorldBounds().bottom + 1;
					} else if (off_y > 0) {
						prev_y = b.getWorldBounds().top - this.getHeight() - 1;
					}
					this.setLocation(prev_x, prev_y);
					this.updateGeometricState();

					// Damage the block?
					if (this.destroy_blocks && off_y == 0) { // Only destroy going left/right
						if (Functions.rnd(1, 10) == 1) {
							b.damage(1, false, null);
						}
					}

					return false;
				}
			}
		}

		// Check for collisons with sprites
		ArrayList<Geometry> colls2 = this.getColliders(this.game.root_node);
		for (Geometry g : colls2) {
			if (this.collidedWith(g, prev_x, prev_y) == false) {
				return false;
			}
		}
		return true;
	}


	// Returns false on a collision!
	private boolean collidedWith(Geometry g, float prev_x, float prev_y) {
		if (hasCollidedWith(g) == false) {
			this.setLocation(prev_x, prev_y);
			this.updateGeometricState();
			return false;
		}
		return true;
	}


	/**
	 * Returns whether the sprite should move back
	 * @param g
	 * @return
	 */
	//protected abstract boolean hasCollidedWith(Geometry g);
	protected final boolean hasCollidedWith(Geometry g) {
		return Collision.Collided(this, g);
	}



	protected PlayersAvatar getVisiblePlayer() {
		return game.getVisiblePlayer(this);
	}


	public void remove() {
		this.removeFromParent();
		this.game.removeFromProcess(this);
	}


	public abstract void died();


	protected void checkForSuffocation() { 
		Block b = (Block)game.blockGrid.getBlockAtPixel_MaybeNull(this.getWorldCentreX(), this.getWorldY());
		if (b != null) {
			if (b.getType() == Block.WATER || b.getType() == Block.LAVA) {
				if (bubble_int.hitInterval()) {
					new AirBubble(game, this);
				}
				// Suffocation
				this.hold_breath_time--;
				if (this.hold_breath_time < 0) {
					this.died();
					this.hold_breath_time = 0;
				}
				return;
			}
		}
		this.hold_breath_time++;
		if (this.hold_breath_time > MAX_HOLD_BREATH_TIME) {
			this.hold_breath_time = MAX_HOLD_BREATH_TIME;
		}
	}


	/*protected void checkForHarmingBlocks_() {
		this.tmp_rect.set(this.getWorldBounds());
		this.tmp_rect.bottom += 2; // In case we're walking on an object on fire.  Must be 2?
		ArrayList<AbstractRectangle> colls = game.blockGrid.getColliders(tmp_rect);
		if (colls.size() > 0) {
			for (AbstractRectangle r : colls) {
				Block b = (Block)r;
				if (b != null) {
					if (Block.GetHarm(b.getType()) > 0) {
						this.died();//.damage(Block.GetHarm(b.getType()));
					}
				}
			}
		}
	}*/


	public boolean hasBlockSelected() {
		return this.current_item > 0;
	}


	public byte getCurrentItemType() {
		return this.current_item;
	}


	public void setItemType(byte t) {
		this.current_item = t;
	}


	public void removeItem() {
		this.current_item = 0;
	}

}
