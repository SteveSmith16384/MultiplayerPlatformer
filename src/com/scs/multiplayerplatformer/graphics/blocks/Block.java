
package com.scs.multiplayerplatformer.graphics.blocks;

import java.awt.image.BufferedImage;

import ssmith.android.compatibility.Canvas;
import ssmith.android.framework.AbstractActivity;
import ssmith.android.framework.ErrorReporter;
import ssmith.android.lib2d.Camera;
import ssmith.android.lib2d.MyPointF;
import ssmith.awt.ImageCache;
import ssmith.lang.Functions;

import com.scs.multiplayerplatformer.Statics;
import com.scs.multiplayerplatformer.game.EnemyEventTimer;
import com.scs.multiplayerplatformer.game.GameModule;
import com.scs.multiplayerplatformer.graphics.Explosion;
import com.scs.multiplayerplatformer.graphics.GameObject;
import com.scs.multiplayerplatformer.graphics.ThrownItem;
import com.scs.multiplayerplatformer.graphics.mobs.PlayersAvatar;
import com.scs.multiplayerplatformer.mapgen.AbstractLevelData;
import com.scs.multiplayerplatformer.mapgen.LoadMap;

public class Block extends GameObject {

	// THERE MUST BE NO GAPS IN THE SEQUENCE OF NUMBERS
	public static final byte NOTHING_DAYLIGHT = 0;
	public static final byte GRASS = 2;
	public static final byte SOIL = 3;
	public static final byte ROCK = 4;
	public static final byte WATER = 5;
	public static final byte FIRE = 6;
	public static final byte LADDER = 7;
	public static final byte WOOD = 8;
	public static final byte DYNAMITE = 9;
	public static final byte TANGLEWEED = 10;
	public static final byte APPLE = 11;
	public static final byte CHEST = 12;
	public static final byte TREE_BRANCH_RIGHT = 13;
	public static final byte COAL = 14;
	public static final byte GOLD = 15;
	public static final byte LAVA = 16;
	public static final byte MONSTER_GENERATOR = 17;
	public static final byte SNOW = 18;
	public static final byte TREE_BARK = 19;
	public static final byte ACORN = 20;
	public static final byte TREE_BRANCH_LEFT = 21;
	public static final byte FLINT = 22;
	public static final byte ADAMANTIUM = 23;
	public static final byte IRON_ORE = 24;
	public static final byte SAND = 25;
	public static final byte WHEAT_LOW = 26;
	public static final byte WHEAT_MED = 27;
	public static final byte WHEAT_HIGH = 28;
	public static final byte AMULET = 29;
	public static final byte SLIME = 30;
	public static final byte STONE = 31;
	public static final byte WHEAT_SEED = 32;
	public static final byte GIRDER = 33; // Add to wiki
	public static final byte WHEAT_NONE = 34; 
	public static final byte TREE_ROOT = 35;
	public static final byte WHEAT_ROOT = 36;
	public static final byte WOOL = 37;
	public static final byte WEEDS = 38;
	public static final byte STICKS = 40;
	public static final byte BONES = 42;  // Add to wiki
	public static final byte COBWEB = 43;
	//public static final byte LAMB_CHOP = 44;  // Add to wiki
	public static final byte UNLIT_FURNACE = 45;
	public static final byte LIT_FURNACE = 46;
	//public static final byte BREAD = 47;
	public static final byte GLASS = 48;  // Add to wiki
	public static final byte CLAY = 49; 
	public static final byte BRICKS = 50;
	//public static final byte BED = 51;
	public static final byte RAW_BEEF = 52;  // Add to wiki
	public static final byte RAW_DEAD_CHICKEN = 53;  // Add to wiki
	public static final byte RAW_PORK = 54;  // Add to wiki
	public static final byte EGG = 55;  // Add to wiki
	public static final byte SHURIKEN = 56;  // Add to wiki
	public static final byte CRATE = 57;  // Add to wiki
	public static final byte MEDIKIT = 58;
	//public static final byte SAVEPOINT = 59;
	public static final byte END_OF_LEVEL = 60;
	public static final byte BARREL = 61;
	public static final byte ROPE = 62;
	public static final byte SLIME_SPURT = 63;
	public static final byte BLOOD_SPURT = 64;
	public static final byte MAX_BLOCK_ID = 64;

	// Misc
	private static final int MOB_GEN_DURATION = 5000;
	private static final int FIRE_DURATION = 200;
	private static final int SLIME_DURATION = 300;

	private static BufferedImage fire_bmp1, fire_bmp2;

	private byte type;
	public BufferedImage bmp, bmp2;
	private byte health = 1;
	private int map_x, map_y;
	private long event_time = System.currentTimeMillis() + 10000; // So they don't start straight away
	public boolean on_fire = false;

	public Block(GameModule _game, byte _type, int _map_x, int _map_y) {
		super(_game, "Block", true, 0, 0, Statics.SQ_SIZE, Statics.SQ_SIZE);

		if (fire_bmp1 == null) {
			fire_bmp1 = GetBitmap(Statics.img_cache, Block.FIRE, Statics.SQ_SIZE, Statics.SQ_SIZE);
		}
		if (fire_bmp2 == null) {
			fire_bmp2 = Statics.img_cache.getImage("fire2", Statics.SQ_SIZE, Statics.SQ_SIZE);//GetBitmap(_game.img_cache, Block.FIRE, Statics.SQ_SIZE, Statics.SQ_SIZE);
		}

		type =_type;
		map_x = _map_x;
		map_y = _map_y;
		bmp = GetBitmap(Statics.img_cache, type, Statics.SQ_SIZE_INT, Statics.SQ_SIZE_INT);

		switch (type) {
		case ROCK:
			break;
		case STONE:
			break;
		case WATER:
			health = (byte)100;
			bmp2 = Statics.img_cache.getImage("water2", Statics.SQ_SIZE_INT, Statics.SQ_SIZE_INT);
			break;
		case FIRE:
			health = (byte)100;
			bmp2 = Statics.img_cache.getImage("fire2", Statics.SQ_SIZE, Statics.SQ_SIZE);
			on_fire = true;
			break;
		case LAVA:
			health = (byte)100;
			bmp2 = Statics.img_cache.getImage("lava2", Statics.SQ_SIZE_INT, Statics.SQ_SIZE_INT);
			break;
		case SLIME:
			health = (byte)100;
			bmp2 = Statics.img_cache.getImage("slime2", Statics.SQ_SIZE_INT, Statics.SQ_SIZE_INT);
			break;
		case MONSTER_GENERATOR:
			health = (byte)(health * 2);
			break;
		case ADAMANTIUM:
			health = 127;
			break;
		case IRON_ORE:
			health = (byte)(health * 2);
			break;
		case WHEAT_MED:
			break;
		case WHEAT_HIGH:
			health = (byte)(health * 2);
			break;
		case AMULET:
			health = (byte)(health * 2);
			break;
		case GIRDER:
			health = (byte)(health * 2);
			break;
		case WOOD:
			health = (byte)(health * 2);
			break;
		case CLAY:
			health = (byte)(health * 2);
			break;
		case BRICKS:
			health = (byte)(health * 3);
			break;
		case UNLIT_FURNACE:
		case LIT_FURNACE:
			health = (byte)(health * 3);
			break;
		default:
			//throw new RuntimeException("Unknown block type:" + type);

		}

	}


	public static byte GetInvType(byte type) {
		switch (type) {
		case GRASS:
		case SNOW:
		case TREE_ROOT:
		case WHEAT_ROOT:
			return SOIL;
		case TREE_BRANCH_RIGHT:
		case TREE_BRANCH_LEFT:
			return TREE_BARK;
		default:
			return type;
		}
	}


	public static boolean AddToInv(byte type) {
		switch (type) {
		case TANGLEWEED:
			//case APPLE:
			/*case WHEAT_LOW:
		case WHEAT_MED:
		case WHEAT_HIGH:*/
		case LAVA:
		case MONSTER_GENERATOR:
		case CHEST:
		case FIRE:
		case WATER:
		case SLIME:
		case WEEDS:
		case COBWEB:
			return false;
		default:
			return true;
		}
	}


	public static boolean Flammable(byte type) {
		switch (type) {
		case WOOD:
		case TREE_BARK:
		case DYNAMITE:
		case TANGLEWEED:
		case LADDER:
		case WHEAT_LOW:
		case WHEAT_MED:
		case WHEAT_HIGH:
		case ACORN:
		case FLINT:
		case COAL:
		case CHEST:
		case TREE_BRANCH_LEFT:
		case TREE_BRANCH_RIGHT:
		case WHEAT_SEED:
		case WOOL:
		case COBWEB:
		case RAW_BEEF:
		case RAW_DEAD_CHICKEN:
		case RAW_PORK:
		case BARREL:
		case CRATE:
		case ROPE:
			return true;
		default:
			return false;
		}
	}


	public static boolean DamagedByThrownObject(byte type) {
		switch (type) {
		case MONSTER_GENERATOR:
		case CHEST:
		case DYNAMITE:
		case COBWEB:
			return true;
		default:
			return false;
		}
	}


	public static boolean Falls(byte type) {
		switch (type) {
		case MONSTER_GENERATOR:
		case FLINT:
		case LAVA:
		case IRON_ORE:
		case CHEST:
		case ACORN:
		case SAND:
		case APPLE:
		case WHEAT_LOW:
		case WHEAT_MED:
		case WHEAT_HIGH:
		case WHEAT_SEED:
		case GOLD:
		case AMULET:
		case SLIME:
		case WOOL:
		case WEEDS:
		case STICKS:
		case BONES:
		case UNLIT_FURNACE:
		case LIT_FURNACE:
		case GLASS:
		case RAW_BEEF:
		case RAW_DEAD_CHICKEN:
		case RAW_PORK:
		case EGG:
		case CRATE:
		case BARREL:
			return true;
		default:
			return false;
		}
	}


	/*public static boolean InfiniteSupplyToThrow(byte type) {
		switch (type) {
		//case ROCK:
		case SHURIKEN:
			return true;
		default:
			return false;
		}
	}*/


	public static boolean CanBeThrown(byte type) {
		switch (type) {
		case FLINT:
		case DYNAMITE:
		case ROCK:
		case STONE:
		case WHEAT_SEED:
		case ACORN:
		case WOOL:
		case STICKS:
		case TREE_BARK:
		case WOOD:
		case SHURIKEN:
			return true;
		default:
			return false;
		}
	}


	public static boolean BlocksAllMovement(byte type) {
		switch (type) {
		case APPLE:
		case CHEST:
		case LAVA:
		case WATER:
		case FIRE:
		case LADDER:
		case ACORN:
		case WHEAT_SEED:
		case AMULET:
		case SLIME: // Need this so splurts don't disappear straight away.
		case TREE_BARK:
		case TREE_BRANCH_RIGHT:
		case TREE_BRANCH_LEFT:
		case WHEAT_NONE:
		case WHEAT_LOW:
		case WHEAT_MED:
		case WHEAT_HIGH:
		case WOOL:
		case WEEDS:
		case COBWEB:
		case RAW_BEEF:
		case RAW_DEAD_CHICKEN:
		case RAW_PORK:
		case EGG:
		case ROPE:
			return false;
		default:
			return true;
		}
	}


	public static boolean CanBeHitByThrownObject(byte type) {
		switch (type) {
		case APPLE:
		case FIRE:
		case LADDER:
		case ROPE:
		case SLIME: // Need this so spurts don't get removed straight away
		case WATER:
			return false;
		default:
			return true;
		}
	}


	public void touched(PlayersAvatar player) {
		AbstractActivity act = Statics.act;

			switch (type) {
			case Block.SHURIKEN:
				player.inv.addBlock(this.getType(), Statics.SHURIKENS_FROM_BLOCK);
				destroy(0, false, null);
				break;
			case Block.MEDIKIT:
				player.incHealthToMax();
				destroy(0, false, null);
				break;
			case Block.END_OF_LEVEL:
				game.level++;
				String map_r = Statics.GetMapFilename(game.level);
					AbstractLevelData original_level_data = new LoadMap(map_r);
					GameModule mod = new GameModule(act, original_level_data, game.level);
					game.getThread().setNextModule(mod);
				destroy(0, false, null);
				break;
			case Block.SAND:
				destroy(2, false, null);
			}
	}


	public static boolean BlocksDownMovement(byte type) {
		switch (type) {
		case TREE_BRANCH_RIGHT:
		case TREE_BRANCH_LEFT:
		case FLINT:
		case LADDER:
		case ROPE:
			return true;
		default:
			return false;
		}
	}


	public static boolean CanMoveDownThrough(byte type) {
		switch (type) {
		case LADDER:
		case ROPE:
			return true;
		default:
			return false;
		}
	}


	public static boolean RequireProcessing(byte type) {
		switch (type) {
		case MONSTER_GENERATOR:
		case FIRE:
		case LAVA:
		case WATER:
		case SLIME:
		case TREE_ROOT:
		case WHEAT_ROOT:
		case LIT_FURNACE:
			return true;
		default:
			return false;
		}
	}


	public static int GetHarm(byte type) {
		switch (type) {
		case FIRE:
		case LAVA:
		case SLIME:
			return 1;
		default:
			return 0;
		}
	}


	public static boolean BlocksLight(byte type) {
		switch (type) {
		case APPLE:
		case LADDER:
		case WHEAT_LOW:
		case WHEAT_MED:
		case WHEAT_HIGH:
		case WHEAT_SEED:
		case TREE_BRANCH_LEFT:
		case TREE_BRANCH_RIGHT:
		case FIRE:
		case ACORN:
		case AMULET:
		case FLINT:
		case WOOL:
		case WEEDS:
		case COBWEB:
		case GLASS:
		case RAW_BEEF:
		case RAW_DEAD_CHICKEN:
		case RAW_PORK:
		case EGG:
		case ROPE:
			return false;
		default:
			return true;
		}
	}


	public static boolean CanBeBuiltOver(byte type) {
		switch (type) {
		case NOTHING_DAYLIGHT:
		case WATER:
		case WEEDS:
			return true;
		default:
			return false;
		}
	}


	public static BufferedImage GetBitmap(ImageCache img_cache, byte type, float w, float h) {
		switch (type) {
		case SOIL:
			return img_cache.getImage("mud", w, h);
		case GRASS:
			return img_cache.getImage("grass", w, h);
		case ROCK:
			return img_cache.getImage("rock", w, h);
		case WATER:
			return img_cache.getImage("water", w, h);
		case FIRE:
			return img_cache.getImage("fire", w, h);
		case LADDER:
			return img_cache.getImage("ladder", w, h);
		case WOOD:
			return img_cache.getImage("wood", w, h);
		case DYNAMITE:
			return img_cache.getImage("dynamite", w, h);
		case TANGLEWEED:
			return img_cache.getImage("tangleweed", w, h);
		case APPLE:
			return img_cache.getImage("apple", w, h);
		case CHEST:
			return img_cache.getImage("chest", w, h);
		case COAL:
			return img_cache.getImage("coal", w, h);
		case GOLD:
			return img_cache.getImage("gold", w, h);
		case LAVA:
			return img_cache.getImage("lava", w, h);
		case MONSTER_GENERATOR:
			return img_cache.getImage("spawner", w, h);
		case SNOW:
			return img_cache.getImage("snow", w, h);
		case TREE_BARK:
			return img_cache.getImage("tree_bark", w, h);
		case ACORN:
			return img_cache.getImage("acorn", w, h);
		case TREE_BRANCH_LEFT:
			return img_cache.getImage("tree_branch_left", w, h);
		case TREE_BRANCH_RIGHT:
			return img_cache.getImage("tree_branch_right", w, h);
		case FLINT:
			return img_cache.getImage("flint", w, h);
		case ADAMANTIUM:
			return img_cache.getImage("adamantium", w, h);
		case IRON_ORE:
			return img_cache.getImage("iron_ore", w, h);
		case SAND:
			return img_cache.getImage("sand", w, h);
		case WHEAT_NONE:
			return img_cache.getImage("blank", w, h);
		case WHEAT_LOW:
			return img_cache.getImage("wheat_low", w, h);
		case WHEAT_MED:
			return img_cache.getImage("wheat_med", w, h);
		case WHEAT_HIGH:
			return img_cache.getImage("wheat_high", w, h);
		case AMULET:
			return img_cache.getImage("amulet", w, h);
		case SLIME:
			return img_cache.getImage("slime1", w, h);
		case STONE:
			return img_cache.getImage("stone", w, h);
		case WHEAT_SEED:
			return img_cache.getImage("wheat_seed", w, h);
		case GIRDER:
			return img_cache.getImage("girder", w, h);
		case TREE_ROOT:
			return img_cache.getImage("tree_roots", w, h);
		case WHEAT_ROOT:
			return img_cache.getImage("wheat_roots", w, h);
		case WOOL:
			return img_cache.getImage("wool", w, h);
		case WEEDS:
			return img_cache.getImage("weeds", w, h);
		case STICKS:
			return img_cache.getImage("sticks", w, h);
		case BONES:
			return img_cache.getImage("bones", w, h);
		case COBWEB:
			return img_cache.getImage("cobweb", w, h);
		case UNLIT_FURNACE:
			return img_cache.getImage("furnace_unlit", w, h);
		case LIT_FURNACE:
			return img_cache.getImage("furnace_lit", w, h);
		case GLASS:
			return img_cache.getImage("glass", w, h);
		case CLAY:
			return img_cache.getImage("clay", w, h);
		case BRICKS:
			return img_cache.getImage("bricks", w, h);
		case RAW_BEEF:
			return img_cache.getImage("raw_beef", w, h);
		case RAW_DEAD_CHICKEN:
			return img_cache.getImage("raw_dead_chicken", w, h);
		case RAW_PORK:
			return img_cache.getImage("raw_pork", w, h);
		case EGG:
			return img_cache.getImage("egg", w, h);
		case SHURIKEN:
			return img_cache.getImage("shuriken", w, h);
		case CRATE:
			return img_cache.getImage("crate.gif", w, h);
		case MEDIKIT:
			return img_cache.getImage("medikit", w, h);
		case END_OF_LEVEL:
			return img_cache.getImage("gold_star", w, h);
		case BARREL:
			return img_cache.getImage("wooden_barrel", w, h);
		case ROPE:
			return img_cache.getImage("rope", w, h);
		case SLIME_SPURT:
			return img_cache.getImage("blob", w, h);
		case BLOOD_SPURT:
			return img_cache.getImage("blood_spurt", w, h);
		default:
			/*try {
				throw new RuntimeException("Unknown block type:" + type);
			} catch (Exception ex) {
				ErrorReporter.getInstance().handleSilentException(ex);

			}*/
			return img_cache.getImage("wood", w, h); // todo - show dummy image
		}
	}



	public static int GetConsumeValue(byte type) {
		switch (type) {
		case APPLE:
			return 10;
		case RAW_BEEF:
			return 40;
		case RAW_DEAD_CHICKEN:
			return 40;
		case RAW_PORK:
			return 40;
		case EGG:
			return 30;
		default:
			return 0;
		}
	}


	/**
	 * Returns true if a block should never be moved to the very_slow list.
	 * @return
	 */
	public boolean alwaysProcess() {
		switch (this.getType()) {
		case WHEAT_ROOT:
		case TREE_ROOT:
			return true;
		default:
			return false;
		}
	}


	public static boolean IsSolidGround(byte type) {
		switch (type) {
		case SOIL:
		case ROCK:
		case SNOW:
		case GRASS:
		case STONE:
		case CRATE:
		case BARREL:
			return true;
		default:
			return false;
		}
	}


	public static boolean CanGrowPlants(byte type) {
		switch (type) {
		case SOIL:
		case TREE_ROOT:
		case WHEAT_ROOT:
			return true;
		default:
			return false;
		}
	}


	public String toString() {
		return super.toString() + " " + type;
	}


	public void damage(int amt, boolean give_to_player, PlayersAvatar player) {
		AbstractActivity act = Statics.act;

		this.health -= amt;
		act.sound_manager.playSound("crumbling");
		Explosion.CreateExplosion(game, 1, this.getWorldCentreX(), this.getWorldCentreY(), "thrown_rock");

		if (this.health <= 0) {
			this.destroy(1, give_to_player, player);
			/*Explosion.CreateExplosion(game, 1, this.getWorldCentreX(), this.getWorldCentreY());
			if (give_to_player) {
				if (Block.AddToInv(this.getType())) {
					this.game.inv.addBlock(Block.GetInvType(this.getType()), 1);
					if (getType() == Block.AMULET) {
						game.newAmulet();
					}
				}
			} else {
				if (getType() == Block.DYNAMITE) {
					// Remove us so we don't get caught in a loop of explosions
					this.remove();
					game.explosion(3, Statics.DYNAMITE_DAMAGE, 6, this.getWorldCentreX(), this.getWorldCentreY());
				}
			}
			this.remove();*/
		}
	}


	public void destroy(int explode_pieces, boolean give_to_player, PlayersAvatar player) {
		if (explode_pieces > 0) {
			Explosion.CreateExplosion(game, explode_pieces, this.getWorldCentreX(), this.getWorldCentreY(), "thrown_rock");
		}
		if (give_to_player) {
			if (Block.AddToInv(this.getType())) {
				player.inv.addBlock(Block.GetInvType(this.getType()), 1);
				if (getType() == Block.AMULET) {
					game.newAmulet();
				}
			}
		} else {
			if (getType() == Block.DYNAMITE) {
				// Remove us so we don't get caught in a loop of explosions
				this.remove();
				game.explosionWithDamage(3, Statics.DYNAMITE_DAMAGE, 6, this.getWorldCentreX(), this.getWorldCentreY());
			}
		}
		this.remove();

	}


	/**
	 * Don't call this directly, call damage();
	 */
	protected void remove() {
		if (getType() == Block.CHEST) {
			int i = Functions.rnd(1, 4);
			switch (i) {
			case 1:
				this.game.addBlock(Block.ACORN, this.map_x, this.map_y, true);
				break;
			case 2:
				this.game.addBlock(Block.FLINT, this.map_x, this.map_y, true);
				break;
			case 3:
				this.game.addBlock(Block.GOLD, this.map_x, this.map_y, true);
				break;
			case 4:
				this.game.addBlock(Block.WHEAT_SEED, this.map_x, this.map_y, true);
				break;
			}
		} else {
			/*Block above = (Block)game.new_grid.getBlockAtMap_MaybeNull(this.map_x, this.map_y-1);
			if (above != null && (above.getType() == Block.DARKNESS || above.getType() == Block.DARKNESS2 || Block.BlocksLight(above.getType()))) {
				if (Functions.rnd(1, 2) == 1) {
					this.game.addBlock(Block.DARKNESS, this.map_x, this.map_y, true);
				} else {
					this.game.addBlock(Block.DARKNESS2, this.map_x, this.map_y, true);
				}
			} else {*/
				this.game.addBlock(Block.NOTHING_DAYLIGHT, this.map_x, this.map_y, true);
			//}
		}
		this.game.removeFromProcess(this);
		if (this.getType() != Block.NOTHING_DAYLIGHT) {
			this.addAdjacentBlocksToProcess();
		}
	}


	private void addAdjacentBlocksToProcess() {
		addBlockToProcess(this.map_x+1, this.map_y);
		addBlockToProcess(this.map_x-1, this.map_y);
		addBlockToProcess(this.map_x, this.map_y+1);
		addBlockToProcess(this.map_x, this.map_y-1);
	}


	private void addBlockToProcess(int map_x, int map_y) {
		Block b = (Block)this.game.new_grid.getBlockAtMap_MaybeNull(map_x, map_y);
		if (b != null) {
			game.addToProcess_Slow(b, false);
		}
	}


	@Override
	public void doDraw(Canvas g, Camera cam, long interpol) {
		if (this.visible) {
			if (bmp2 != null) {
				if (Functions.rnd(1, 2) == 2) {
					g.drawBitmap(bmp2, this.world_bounds.left - cam.left, this.world_bounds.top - cam.top, paint);
					return;
				}
			}
			g.drawBitmap(bmp, this.world_bounds.left - cam.left, this.world_bounds.top - cam.top, paint); // bmp.getWidth()
			if (this.on_fire) {
				if (Functions.rnd(1, 2) == 2) {
					g.drawBitmap(fire_bmp1, this.world_bounds.left - cam.left, this.world_bounds.top - cam.top, paint);
				} else {
					g.drawBitmap(fire_bmp2, this.world_bounds.left - cam.left, this.world_bounds.top - cam.top, paint);
				}
			}
		}
	}


	@Override
	public void process(long interpol) {
		AbstractActivity act = Statics.act;
		
		boolean remove_from_process = true; // Default
		switch (type) {
		case WATER:
				processWater();
			break;
		case LAVA:
				if (this.getDistanceTo(game.player) <= Statics.ACTIVATE_DIST) {
					checkLavaSquares();
				} else {
					remove_from_process = false; // Try again later
				}
			break;
		case TANGLEWEED:
			remove_from_process = false;
			int i = Functions.rnd(1, 10); 
			if (i <= 3) {
				if (this.on_fire == false) {
					byte[] types2 = {NOTHING_DAYLIGHT};
					if (checkAndChangeAdjacentSquares(-1, 0, types2, TANGLEWEED, true)) {
						break;
					}
					if (checkAndChangeAdjacentSquares(1, 0, types2, TANGLEWEED, true)) {
						break;
					}
					if (checkAndChangeAdjacentSquares(0, 1, types2, TANGLEWEED, true)) {
						break;
					}
					if (checkAndChangeAdjacentSquares(0, -1, types2, TANGLEWEED, true)) {
						break;
					}
				}
			} else if (i == 10){
				remove_from_process = true;
			}
			break;
		case MONSTER_GENERATOR:
			remove_from_process = false;
			if (this.event_time < System.currentTimeMillis()) {
				this.event_time = System.currentTimeMillis() + MOB_GEN_DURATION;
				if (this.getDistanceTo(game.player) <= Statics.ACTIVATE_DIST) {
					EnemyEventTimer.GenerateRandomMonster(game, this);
				}
			}
			break;
		case TREE_ROOT:
			break;
		case WHEAT_ROOT:
			break;
		case SLIME:
			remove_from_process = false;
			if (this.event_time < System.currentTimeMillis()) {
				this.event_time = System.currentTimeMillis() + SLIME_DURATION;
				if (Functions.rnd(1, 2) == 1) {
					if (this.getDistanceTo(game.player) <= Statics.ACTIVATE_DIST) {
						act.sound_manager.playSound("slime");
						new ThrownItem(game, Block.SLIME_SPURT, new MyPointF(this.getWorldCentreX(), this.getWorldY()), new MyPointF(Functions.rndFloat(-.5f, .5f), -1), null, 10, Statics.ROCK_SPEED, Statics.ROCK_GRAVITY, Statics.SLIME_SIZE);
					}
				}
			}
			break;
		}

		if (this.getType() == Block.FIRE || this.on_fire) {
				if (checkFire()) {
					remove_from_process = false;
				}
		}

		if (Falls(this.getType())) {
			if (game.new_grid.isSquareEmpty(this.map_x, this.map_y+1)) {
				game.addBlock(this.getType(), this.map_x, this.map_y+1, true);
				this.remove();
			}
		} else { // Always falls if not attached to anything UNLESS DARKNESS!
			//if (this.getType() != Block.DARKNESS && this.getType() != Block.DARKNESS2) {
				if (game.new_grid.isSquareEmpty(this.map_x, this.map_y+1)) {
					if (game.new_grid.isSquareEmpty(this.map_x, this.map_y-1)) {
						if (game.new_grid.isSquareEmpty(this.map_x+1, this.map_y)) {
							if (game.new_grid.isSquareEmpty(this.map_x-1, this.map_y)) {
								game.addBlock(this.getType(), this.map_x, this.map_y+1, true);
								this.remove();
							}
						}
					}
				}
			//}
		}

		if (remove_from_process) {
			this.game.removeFromProcess(this);
		}
	}


	/**
	 * Returns whether it's gone out (and should be removed from process).
	 * @return
	 */
	private void processWater() {
		byte[] types = {NOTHING_DAYLIGHT, FIRE, LADDER, ROPE};
		checkAndChangeAdjacentSquares(0, 1, types, WATER, false);
		// Only check left/right if square below is solid
		//if (game.new_grid.isSquareSolid(this.map_x, this.map_y+1)) {
		checkAndChangeAdjacentSquares(-1, 0, types, WATER, false);
		checkAndChangeAdjacentSquares(1, 0, types, WATER, false);
		//}

	}


	/**
	 * Returns false if fire is out.
	 * @return
	 */
	private boolean checkFire() {
		if (this.event_time < System.currentTimeMillis()) {
			event_time = System.currentTimeMillis() + FIRE_DURATION;
			int i = Functions.rnd(1, 8);
			if (i == 1) {
				this.remove();
				return false;
			} else { // Spread!
				Block b = (Block)this.game.new_grid.getBlockAtMap_MaybeNull(this.map_x+1, this.map_y);
				if (b != null) {
					if (Flammable(b.getType()) && b.on_fire == false) {
						b.on_fire = true;
						game.addToProcess_Slow(b, true);
					} else if (b.getType() == Block.UNLIT_FURNACE) {
						game.addBlock(Block.LIT_FURNACE, this.map_x+1, this.map_y, false);
					}
				}
				b = (Block)this.game.new_grid.getBlockAtMap_MaybeNull(this.map_x-1, this.map_y);
				if (b != null) {
					if (Flammable(b.getType()) && b.on_fire == false) {
						b.on_fire = true;
						game.addToProcess_Slow(b, true);
					} else if (b.getType() == Block.UNLIT_FURNACE) {
						game.addBlock(Block.LIT_FURNACE, this.map_x-1, this.map_y, false);
					}
				}
				b = (Block)this.game.new_grid.getBlockAtMap_MaybeNull(this.map_x, this.map_y+1);
				if (b != null) {
					if (Flammable(b.getType()) && b.on_fire == false) {
						b.on_fire = true;
						game.addToProcess_Slow(b, true);
					} else if (b.getType() == Block.UNLIT_FURNACE) {
						game.addBlock(Block.LIT_FURNACE, this.map_x, this.map_y+1, false);
					}
				}
				b = (Block)this.game.new_grid.getBlockAtMap_MaybeNull(this.map_x, this.map_y-1);
				if (b != null) {
					if (Flammable(b.getType()) && b.on_fire == false) {
						b.on_fire = true;
						game.addToProcess_Slow(b, true);
					} else if (b.getType() == Block.UNLIT_FURNACE) {
						game.addBlock(Block.LIT_FURNACE, this.map_x, this.map_y-1, false);
					}
				}
			}
		}
		return true;
	}


	private void checkLavaSquares() {
		this.checkSpecificLavaSquare(-1, 0);
		this.checkSpecificLavaSquare(1, 0);
		this.checkSpecificLavaSquare(0, -1);
		this.checkSpecificLavaSquare(0, 1);
	}


	private void checkSpecificLavaSquare(int off_x, int off_y) {
		if (off_y >= 0) { // Lava only flows across and down
			byte[] types_lava = {NOTHING_DAYLIGHT, FIRE, APPLE, CHEST, GOLD, MONSTER_GENERATOR, DYNAMITE, ACORN, LADDER, COAL, TREE_BRANCH_LEFT, TREE_BRANCH_RIGHT, ROPE};
			checkAndChangeAdjacentSquares(off_x, off_y, types_lava, LAVA, false);
			byte[] types_lava2 = {WATER};
			checkAndChangeAdjacentSquares(off_x, off_y, types_lava2, ROCK, true);
		}
		Block b = (Block)this.game.new_grid.getBlockAtMap_MaybeNull(this.map_x+off_x, this.map_y+off_y);
		if (b != null) {
			if (Flammable(b.getType())) {
				b.on_fire = true;
				game.addToProcess_Slow(b, true);
				//this.game.addBlock(Block.FIRE, this.map_x+off_x, this.map_y+off_y, true, false);
			}
		}
	}


	private boolean checkAndChangeAdjacentSquares(int off_x, int off_y, byte[] from, byte to, boolean check_for_sprites) {
		for(int i=0 ;i<from.length ; i++) {
			if (this.map_x+off_x <= game.map_loaded_up_to_col) {
				Block b = (Block)this.game.new_grid.getBlockAtMap_MaybeNull(this.map_x+off_x, this.map_y+off_y);
				if (b != null) {
					if (b.getType() == from[i]) {
						// Check area is clear
						if (check_for_sprites == false || game.isAreaClear((map_x+off_x)*Statics.SQ_SIZE, (map_y+off_y)*Statics.SQ_SIZE, Statics.SQ_SIZE, Statics.SQ_SIZE, false)) {
							game.addBlock(to, map_x+off_x, map_y+off_y, true);
							return true;
						}
						break;
					}
				} else if (from[i] == NOTHING_DAYLIGHT) {
					// Check area is clear
					if (check_for_sprites == false || game.isAreaClear((map_x+off_x)*Statics.SQ_SIZE, (map_y+off_y)*Statics.SQ_SIZE, Statics.SQ_SIZE, Statics.SQ_SIZE, false)) {
						game.addBlock(to, map_x+off_x, map_y+off_y, true);
						return true;
					}
					break;
				}
			}
		}
		return false;
	}


	public byte getType() {
		return this.type;
	}


	public int getMapX() {
		return this.map_x;
	}


	public int getMapY() {
		return this.map_y;
	}

}
