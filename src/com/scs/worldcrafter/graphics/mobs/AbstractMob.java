package com.scs.worldcrafter.graphics.mobs;

import java.util.ArrayList;

import ssmith.android.compatibility.RectF;
import ssmith.android.lib2d.MyPointF;
import ssmith.android.lib2d.shapes.AbstractRectangle;
import ssmith.android.lib2d.shapes.Geometry;
import ssmith.lang.Functions;
import ssmith.util.Interval;

import com.scs.worldcrafter.Statics;
import com.scs.worldcrafter.game.GameModule;
import com.scs.worldcrafter.graphics.AirBubble;
import com.scs.worldcrafter.graphics.GameObject;
import com.scs.worldcrafter.graphics.blocks.Block;
import com.scs.worldcrafter.mapgen.SimpleMobData;

public abstract class AbstractMob extends GameObject {

	private static final int MAX_HOLD_BREATH_TIME = 100;

	// Types
	public static final byte PLAYER = 0;
	public static final byte ZOMBIE = 1;
	public static final byte WASP = 2;
	public static final byte SKELETON = 3;
	public static final byte SHEEP = 4;
	public static final byte PIG = 5;
	public static final byte CHICKEN = 6;
	public static final byte COW = 7;
	public static final byte ENEMY_NINJA_EASY = 8;
	public static final byte HELICOPTER = 9;
	public static final byte ALIEN = 10;
	public static final byte PLATFORM1 = 11;

	protected int health, max_health;
	protected byte current_item = GameModule.HAND;
	private boolean remove_if_far_away;
	private boolean destroy_blocks;
	protected RectF tmp_rect = new RectF();
	public byte side;
	private int hold_breath_time = MAX_HOLD_BREATH_TIME;
	public boolean is_on_ice = false;
	private Interval bubble_int = new Interval(1000, false);

	public AbstractMob(GameModule _game, String name, float x, float y, float w, float h, byte _health, boolean _remove_if_far_away, boolean _destroy_blocks, byte _side) {
		super(_game, name, true, x, y, w, h);

		health = _health;
		max_health = _health;
		remove_if_far_away = _remove_if_far_away;
		destroy_blocks = _destroy_blocks;
		side = _side;

		game.addToProcess_Instant(this);
		game.root_node.attachChild(this);

		this.updateGeometricState();
	}


	public static void CreateMob(GameModule game, SimpleMobData sm) {
		/*if (Statics.DEBUGGING) {
			return;
		}*/
		switch (sm.getType()) {
		case AbstractMob.ENEMY_NINJA_EASY:
			EnemyNinjaEasy.Subfactory(game, sm.pixel_x, sm.pixel_y);
			break;
		case AbstractMob.PLATFORM1:
			//new PlatformMob(game, sm.pixel_x, sm.pixel_y, Statics.SQ_SIZE, Statics.SQ_SIZE, R.drawable.grass, 1, 0, Statics.SQ_SIZE * 4); // todo - change params
			new PlatformMob(game, sm.pixel_x, sm.pixel_y, Statics.SQ_SIZE*2, Statics.SQ_SIZE, "grass", 0, -1, Statics.SQ_SIZE * 4); // todo - change params
			break;
		default:
			if (Statics.RELEASE_MODE == false) {
				throw new RuntimeException("Unknown mob type: " + sm.getType());
			} else {
				// Do nothing
			}
		}
	}


	public byte getType() {
		if (this instanceof PlayersAvatar) {
			return PLAYER;
		} else {
			if (Statics.RELEASE_MODE == false) {
				throw new RuntimeException("Unknown type: " + this.toString());
			}
			return -1;
		}
	}


	/**
	 * Returns if the move was successful.
	 * 
	 */
	protected boolean move(float off_x, float off_y) {
		if (remove_if_far_away) {
			if (checkIfTooFarAway()) { 
				return false; 
			}
		}

		if (off_x != 0 && off_y != 0) {
			throw new RuntimeException("Movement must only be along one axis at a time");
		}

		float prev_x = this.getWorldX();
		float prev_y = this.getWorldY();

		this.adjustLocation(off_x, off_y);
		this.updateGeometricState();

		is_on_ice = false;

		ArrayList<AbstractRectangle> colls = game.new_grid.getColliders(this.getWorldBounds());

		for (AbstractRectangle ar : colls) {
			if (ar instanceof Block) {
				Block b = (Block)ar;
				if (this instanceof PlayersAvatar) {
					b.touched();
				}
				if (Block.BlocksAllMovement(b.getType())) {
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

					this.collidedWithBlock();

					// Damage the block?
					if (this.destroy_blocks && off_y == 0) { // Only destroy left.right
						if (Functions.rnd(1, 10) == 1) {
							b.damage(1, false);
						}
					}

					return false;
				}
			}
		}

		// Check for collisons with sprites
		ArrayList<Geometry> colls2 = this.getColliders(this.game.root_node);
		for (Geometry g : colls2) {
			/*if (this instanceof PlatformMob || g instanceof PlatformMob) {
			if (g instanceof PlatformMob) {
				PlatformMob p = (PlatformMob)g;
				this.move(p.move_x, p.move_y);
			}
			} else if (g instanceof AbstractMob) {
				AbstractMob am = (AbstractMob)g;
				if (am.side >= -1 && this.side >= 0 && am.side != this.side) { // Only the players side gets damaged, and only mobs with side >= 0 do damage
					if (this.side == Statics.SD_PLAYERS_SIDE) {
						this.damage(2);
					} else {
						am.damage(2);
					}
				}
				return false;
			}*/
			if (this.collidedWith(g, prev_x, prev_y)) {
				return false;
			}
			//return false;
		}
		return true;
	}


	protected void collidedWithBlock() {
		// Override if required.
	}
	

	private boolean collidedWith(Geometry g, float prev_x, float prev_y) {
		if (hasCollidedWith(g)) {
			this.setLocation(prev_x, prev_y);
			this.updateGeometricState();
			return true;
		}
		return false;
	}


	/**
	 * Returns whether the sprite should move back
	 * @param g
	 * @return
	 */
	protected abstract boolean hasCollidedWith(Geometry g);


	protected boolean canSee(AbstractMob mob) {
		MyPointF dir = this.game.player.getWorldCentre_CreatesNew().subtract(this.getWorldCentre_CreatesNew());//new MyPointF(mob.getWorldCentreX(), mob.getWorldCentreY());
		float len = dir.length();
		int num = (int)(len / Statics.SQ_SIZE) * 3;
		dir.divideLocal(num);
		for (int i=0 ; i<num ; i++) {
			float x = this.getWorldCentreX() + (dir.x * i);
			float y = this.getWorldCentreY() + (dir.y * i);
			int map_x = (int)(x / Statics.SQ_SIZE);
			int map_y = (int)(y / Statics.SQ_SIZE);
			Block b = (Block)game.new_grid.getBlockAtMap_MaybeNull(map_x, map_y);
			if (b != null) {
				if (Block.BlocksLight(b.getType())) {
					return false;
				}
			}
		}

		return true;
	}


	public int getHealth() {
		return this.health;
	}


	public void damage(int amt) {
		this.health -= amt;
		if (this.health <= 0) {
			died();
		}
	}


	public void incHealth(int amt) {
		this.health += amt;
		if (this.health > this.max_health) {
			this.health = max_health;
		}
	}


	public void incHealthToMax() {
		this.health = max_health;
	}


	protected void remove() {
		this.removeFromParent();
		this.game.removeFromProcess(this);
	}


	protected abstract void died();


	protected void checkForSuffocation() { 
		Block b = (Block)game.new_grid.getBlockAtPixel_MaybeNull(this.getWorldCentreX(), this.getWorldY());
		if (b != null) {
			if (b.getType() == Block.WATER || b.getType() == Block.LAVA) {
				if (bubble_int.hitInterval()) {
					new AirBubble(game, this);
				}
				// Suffocation
				this.hold_breath_time--;
				if (this.hold_breath_time < 0) {
					this.damage(1);
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


	protected void checkForHarmingBlocks() {
		this.tmp_rect.set(this.getWorldBounds());
		this.tmp_rect.bottom += 2; // In case we're walking on an object on fire.  Must be 2?
		ArrayList<AbstractRectangle> colls = game.new_grid.getColliders(tmp_rect);
		if (colls.size() > 0) {
			for (AbstractRectangle r : colls) {
				Block b = (Block)r;
				if (b != null) {
					if (b.on_fire) {
						this.damage(1);
					}
					if (Block.GetHarm(b.getType()) > 0) {
						this.damage(Block.GetHarm(b.getType()));
					}
				}
			}
		}
		/*Block b = (Block)game.new_grid.getBlockAtPixel_MaybeNull(this.getWorldCentreX(), this.getWorldBounds().bottom);
		if (b != null) {
			if (b.harm > 0) {
				this.damage(b.harm);
			}
		}*/
	}


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
