
package com.scs.worldcrafter.graphics.blocks;

import ssmith.android.framework.ErrorReporter;

import ssmith.android.framework.AbstractActivity;
import ssmith.android.lib2d.Camera;
import ssmith.android.lib2d.MyPointF;
import ssmith.android.util.ImageCache;
import ssmith.lang.Functions;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.scs.ninja.main.lite.R;
import com.scs.worldcrafter.Statics;
import com.scs.worldcrafter.crafting.CraftingData;
import com.scs.worldcrafter.game.EnemyEventTimer;
import com.scs.worldcrafter.game.GameCompletedModule;
import com.scs.worldcrafter.game.GameModule;
import com.scs.worldcrafter.graphics.Explosion;
import com.scs.worldcrafter.graphics.GameObject;
import com.scs.worldcrafter.graphics.ThrownItem;
import com.scs.worldcrafter.mapgen.AbstractLevelData;
import com.scs.worldcrafter.mapgen.LoadSavedMap;

public class Block extends GameObject {

	// THERE MUST BE NO GAPS IN THE SEQUENCE OF NUMBERS
	public static final byte NOTHING_DAYLIGHT = 0;
	public static final byte DARKNESS = 1;
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
	public static final byte COAL = 14; // Todo - add to wiki
	public static final byte GOLD = 15;
	public static final byte LAVA = 16;
	public static final byte MONSTER_GENERATOR = 17;
	public static final byte SNOW = 18;// Todo - add to wiki
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
	public static final byte DARKNESS2 = 39;
	public static final byte STICKS = 40;
	public static final byte CRAFTING_TABLE = 41;
	public static final byte BONES = 42;  // Add to wiki
	public static final byte COBWEB = 43;
	public static final byte LAMB_CHOP = 44;  // Add to wiki
	public static final byte UNLIT_FURNACE = 45;
	public static final byte LIT_FURNACE = 46;
	public static final byte BREAD = 47;
	public static final byte GLASS = 48;  // Add to wiki
	public static final byte CLAY = 49; 
	public static final byte BRICKS = 50;
	public static final byte BED = 51;
	public static final byte RAW_BEEF = 52;  // Add to wiki
	public static final byte RAW_DEAD_CHICKEN = 53;  // Add to wiki
	public static final byte RAW_PORK = 54;  // Add to wiki
	public static final byte EGG = 55;  // Add to wiki
	public static final byte SHURIKEN = 56;  // Add to wiki
	public static final byte CRATE = 57;  // Add to wiki
	public static final byte MEDIKIT = 58;
	public static final byte SAVEPOINT = 59;
	public static final byte END_OF_LEVEL = 60;
	public static final byte BARREL = 61;
	public static final byte ROPE = 62;
	public static final byte SLIME_SPURT = 63;
	public static final byte BLOOD_SPURT = 64;
	public static final byte MAX_BLOCK_ID = 64;
	// If any are added, add to the Wiki as well


	// Misc
	private static final int MOB_GEN_DURATION = 5000;
	private static final int GERMINATION_DURATION = 15000;
	private static final int FIRE_DURATION = 200;
	private static final int SLIME_DURATION = 300;
	private static final int FURNACE_DURATION = 10 * 1000;

	private static Bitmap fire_bmp1, fire_bmp2;

	private byte type;
	public Bitmap bmp, bmp2;
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
			fire_bmp2 = Statics.img_cache.getImage(R.drawable.fire2, Statics.SQ_SIZE, Statics.SQ_SIZE);//GetBitmap(_game.img_cache, Block.FIRE, Statics.SQ_SIZE, Statics.SQ_SIZE);
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
			bmp2 = Statics.img_cache.getImage(R.drawable.water2, Statics.SQ_SIZE_INT, Statics.SQ_SIZE_INT);
			break;
		case FIRE:
			health = (byte)100;
			bmp2 = Statics.img_cache.getImage(R.drawable.fire2, Statics.SQ_SIZE, Statics.SQ_SIZE);
			on_fire = true;
			break;
		case LAVA:
			health = (byte)100;
			bmp2 = Statics.img_cache.getImage(R.drawable.lava2, Statics.SQ_SIZE_INT, Statics.SQ_SIZE_INT);
			break;
		case SLIME:
			health = (byte)100;
			bmp2 = Statics.img_cache.getImage(R.drawable.slime2, Statics.SQ_SIZE_INT, Statics.SQ_SIZE_INT);
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


	public static boolean AddInConstructionMode(byte type) {
		switch (type) {
		case DARKNESS:
		case DARKNESS2:
		case WHEAT_NONE:
		case WHEAT_LOW:
		case WHEAT_MED:
		case WHEAT_HIGH:
			//case TANGLEWEED:
		case APPLE:
		case LAVA:
		case MONSTER_GENERATOR:
		case ADAMANTIUM:
		case CHEST:
		case FIRE:
			//case WATER:
		case SLIME:
		case AMULET:
		case TREE_BRANCH_LEFT:
		case TREE_BRANCH_RIGHT:
			//case GRASS:
		case TREE_ROOT:
		case WHEAT_ROOT:
			//case WEEDS:
		case COBWEB:
		case LAMB_CHOP:
		case BREAD:
		case LIT_FURNACE:
			//case UNLIT_FURNACE:
			//case CRAFTING_TABLE:
		case ACORN:
			//case WHEAT_SEED:
			//case STICKS:
		case BONES:
			//case WOOL:
			//case SNOW:
		case BED:
		case RAW_BEEF:
		case RAW_DEAD_CHICKEN:
		case RAW_PORK:
		case EGG:
		case SHURIKEN:
		case MEDIKIT:
		case SAVEPOINT:
		case END_OF_LEVEL:
		case SLIME_SPURT:
		case BLOOD_SPURT:
			return false;
		default:
			return true;
		}
	}


	public static boolean AddToInv(byte type) {
		switch (type) {
		case DARKNESS:
		case DARKNESS2:
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
		case CRAFTING_TABLE:
		case COBWEB:
		case LAMB_CHOP:
		case BREAD:
		case BED:
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
		case CRAFTING_TABLE:
		case BONES:
		case LAMB_CHOP:
		case UNLIT_FURNACE:
		case LIT_FURNACE:
		case BREAD:
		case GLASS:
		case BED:
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
		case DARKNESS:
		case DARKNESS2:
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
		case LAMB_CHOP:
		case BREAD:
		case BED:
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
		case DARKNESS:
		case DARKNESS2:
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


	public void touched() {
		AbstractActivity act = Statics.act;

		if (Statics.GAME_MODE == Statics.GM_NINJA) {
			switch (type) {
			case Block.SHURIKEN:
				game.inv.addBlock(this.getType(), Statics.SHURIKENS_FROM_BLOCK);
				destroy(0, false);
				break;
			case Block.MEDIKIT:
				game.player.incHealthToMax();
				destroy(0, false);
				break;
			case Block.SAVEPOINT:
				game.showToast("Position Saved!");
				game.original_level_data.setStartPos(this.map_x, this.map_y);
				destroy(0, false);
				break;
			case Block.END_OF_LEVEL:
				game.level++;
				int map_r = Statics.GetNinjaFilename(game.level);
				if (map_r >= 0) {
					AbstractLevelData original_level_data = new LoadSavedMap(map_r);
					GameModule mod = new GameModule(act, original_level_data, game.level, null);
					game.getThread().setNextModule(mod);
				} else {
					GameCompletedModule mod = new GameCompletedModule(act, game);
					game.getThread().setNextModule(mod);
				}
				destroy(0, false);
				break;
			case Block.SAND:
				destroy(2, false);
			}
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
		case DARKNESS:
		case DARKNESS2:
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
		case BREAD:
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
		case DARKNESS:
		case DARKNESS2:
		case WATER:
		case WEEDS:
			return true;
		default:
			return false;
		}
	}


	public static Bitmap GetBitmap(ImageCache img_cache, byte type, float w, float h) {
		switch (type) {
		case DARKNESS:
			return img_cache.getImage(R.drawable.darkness, w, h);
		case DARKNESS2:
			return img_cache.getImage(R.drawable.darkness2, w, h);
		case GRASS:
			return img_cache.getImage(R.drawable.grass, w, h);
		case SOIL:
			return img_cache.getImage(R.drawable.mud, w, h);
		case ROCK:
			return img_cache.getImage(R.drawable.rock, w, h);
		case WATER:
			return img_cache.getImage(R.drawable.water, w, h);
		case FIRE:
			return img_cache.getImage(R.drawable.fire, w, h);
		case LADDER:
			return img_cache.getImage(R.drawable.ladder, w, h);
		case WOOD:
			return img_cache.getImage(R.drawable.wood, w, h);
		case DYNAMITE:
			return img_cache.getImage(R.drawable.dynamite, w, h);
		case TANGLEWEED:
			return img_cache.getImage(R.drawable.tangleweed, w, h);
		case APPLE:
			return img_cache.getImage(R.drawable.apple, w, h);
		case CHEST:
			return img_cache.getImage(R.drawable.chest, w, h);
		case COAL:
			return img_cache.getImage(R.drawable.coal, w, h);
		case GOLD:
			return img_cache.getImage(R.drawable.gold, w, h);
		case LAVA:
			return img_cache.getImage(R.drawable.lava, w, h);
		case MONSTER_GENERATOR:
			return img_cache.getImage(R.drawable.spawner, w, h);
		case SNOW:
			return img_cache.getImage(R.drawable.snow, w, h);
		case TREE_BARK:
			return img_cache.getImage(R.drawable.tree_bark, w, h);
		case ACORN:
			return img_cache.getImage(R.drawable.acorn, w, h);
		case TREE_BRANCH_LEFT:
			return img_cache.getImage(R.drawable.tree_branch_left, w, h);
		case TREE_BRANCH_RIGHT:
			return img_cache.getImage(R.drawable.tree_branch_right, w, h);
		case FLINT:
			return img_cache.getImage(R.drawable.flint, w, h);
		case ADAMANTIUM:
			return img_cache.getImage(R.drawable.adamantium, w, h);
		case IRON_ORE:
			return img_cache.getImage(R.drawable.iron_ore, w, h);
		case SAND:
			return img_cache.getImage(R.drawable.sand, w, h);
		case WHEAT_NONE:
			return img_cache.getImage(R.drawable.blank, w, h);
		case WHEAT_LOW:
			return img_cache.getImage(R.drawable.wheat_low, w, h);
		case WHEAT_MED:
			return img_cache.getImage(R.drawable.wheat_med, w, h);
		case WHEAT_HIGH:
			return img_cache.getImage(R.drawable.wheat_high, w, h);
		case AMULET:
			return img_cache.getImage(R.drawable.amulet, w, h);
		case SLIME:
			return img_cache.getImage(R.drawable.slime1, w, h);
		case STONE:
			return img_cache.getImage(R.drawable.stone, w, h);
		case WHEAT_SEED:
			return img_cache.getImage(R.drawable.wheat_seed, w, h);
		case GIRDER:
			return img_cache.getImage(R.drawable.girder, w, h);
		case TREE_ROOT:
			return img_cache.getImage(R.drawable.tree_roots, w, h);
		case WHEAT_ROOT:
			return img_cache.getImage(R.drawable.wheat_roots, w, h);
		case WOOL:
			return img_cache.getImage(R.drawable.wool, w, h);
		case WEEDS:
			return img_cache.getImage(R.drawable.weeds, w, h);
		case STICKS:
			return img_cache.getImage(R.drawable.sticks, w, h);
		case CRAFTING_TABLE:
			return img_cache.getImage(R.drawable.table, w, h);
		case BONES:
			return img_cache.getImage(R.drawable.bones, w, h);
		case COBWEB:
			return img_cache.getImage(R.drawable.cobweb, w, h);
		case LAMB_CHOP:
			return img_cache.getImage(R.drawable.raw_lamb_chop, w, h);
		case UNLIT_FURNACE:
			return img_cache.getImage(R.drawable.furnace_unlit, w, h);
		case LIT_FURNACE:
			return img_cache.getImage(R.drawable.furnace_lit, w, h);
		case BREAD:
			return img_cache.getImage(R.drawable.bread, w, h);
		case GLASS:
			return img_cache.getImage(R.drawable.glass, w, h);
		case CLAY:
			return img_cache.getImage(R.drawable.clay, w, h);
		case BRICKS:
			return img_cache.getImage(R.drawable.bricks, w, h);
		case BED:
			if (Statics.GAME_MODE == Statics.GM_NINJA) {
				return img_cache.getImage(R.drawable.adamantium, w, h);
			} else {
				return img_cache.getImage(R.drawable.bed, w, h);
			}
		case RAW_BEEF:
			return img_cache.getImage(R.drawable.raw_beef, w, h);
		case RAW_DEAD_CHICKEN:
			return img_cache.getImage(R.drawable.raw_dead_chicken, w, h);
		case RAW_PORK:
			return img_cache.getImage(R.drawable.raw_pork, w, h);
		case EGG:
			return img_cache.getImage(R.drawable.egg, w, h);
		case SHURIKEN:
			return img_cache.getImage(R.drawable.shuriken, w, h);
		case CRATE:
			return img_cache.getImage(R.drawable.crate, w, h);
		case MEDIKIT:
			return img_cache.getImage(R.drawable.bread, w, h);
		case SAVEPOINT:
			return img_cache.getImage(R.drawable.gold_star, w, h);
		case END_OF_LEVEL:
			return img_cache.getImage(R.drawable.gold_star, w, h);
		case BARREL:
			return img_cache.getImage(R.drawable.wooden_barrel, w, h);
		case ROPE:
			return img_cache.getImage(R.drawable.rope, w, h);
		case SLIME_SPURT:
			return img_cache.getImage(R.drawable.blob, w, h);
		case BLOOD_SPURT:
			return img_cache.getImage(R.drawable.blood_spurt, w, h);
		default:
			try {
				throw new RuntimeException("Unknown block type:" + type);
			} catch (Exception ex) {
				ErrorReporter.getInstance().handleSilentException(ex);

			}
			return img_cache.getImage(R.drawable.wood, w, h);
		}
	}


	public static String GetDesc(byte type) {
		switch (type) {
		case DARKNESS:
		case DARKNESS2:
		case NOTHING_DAYLIGHT:
			return Statics.act.getString(R.string.nothing);// "Nothing";
		case GRASS:
			return Statics.act.getString(R.string.grass);// "Grass";
		case SOIL:
			return Statics.act.getString(R.string.soil);// "Soil";
		case ROCK:
			return Statics.act.getString(R.string.rock);// "Rock";
		case WATER:
			return Statics.act.getString(R.string.water);// "Water";
		case FIRE:
			return Statics.act.getString(R.string.fire);// "Fire";
		case LADDER:
			return Statics.act.getString(R.string.ladder);// "Ladder";
		case WOOD:
			return Statics.act.getString(R.string.wood);// "Wood";
		case DYNAMITE:
			return Statics.act.getString(R.string.dynamite);// "Dynamite";
		case TANGLEWEED:
			return Statics.act.getString(R.string.tangleweed);// "Tangleweed";
		case APPLE:
			return Statics.act.getString(R.string.apple);// "Apple";
		case CHEST:
			return Statics.act.getString(R.string.chest);// "Chest";
		case COAL:
			return Statics.act.getString(R.string.coal);// "Coal";
		case GOLD:
			return Statics.act.getString(R.string.gold);// "Gold";
		case LAVA:
			return Statics.act.getString(R.string.lava);// "Lava";
		case MONSTER_GENERATOR:
			return Statics.act.getString(R.string.monster_gen);// "Monster Gen"; // Keep it short
		case SNOW:
			return Statics.act.getString(R.string.snow);// "Snow";
		case TREE_BARK:
			return Statics.act.getString(R.string.tree);// "Tree";
		case ACORN:
			return Statics.act.getString(R.string.acorn);// "Acorn Seed";
		case TREE_BRANCH_LEFT:
		case TREE_BRANCH_RIGHT:
			return Statics.act.getString(R.string.tree_branch);// "Tree Branch";
		case FLINT:
			return Statics.act.getString(R.string.flint);// "Flint";
		case ADAMANTIUM:
			return Statics.act.getString(R.string.adamantium);// "Adamantium";
		case IRON_ORE:
			return Statics.act.getString(R.string.iron_ore);// "Iron Ore";
		case SAND:
			return Statics.act.getString(R.string.sand);// "Sand";
		case WHEAT_NONE:
		case WHEAT_LOW:
		case WHEAT_MED:
		case WHEAT_HIGH:
			return Statics.act.getString(R.string.wheat);// "Wheat";
		case AMULET:
			return Statics.act.getString(R.string.amulet);// "Amulet";
		case SLIME:
			return Statics.act.getString(R.string.slime);// "Slime";
		case STONE:
			return Statics.act.getString(R.string.stone);// "Stone";
		case WHEAT_SEED:
			return Statics.act.getString(R.string.wheat_seed);// "Wheat Seed";
		case GIRDER:
			return Statics.act.getString(R.string.metal_girder);// "Metal Girder";
		case TREE_ROOT:
			return Statics.act.getString(R.string.tree_roots);// "Tree Roots";
		case WHEAT_ROOT:
			return Statics.act.getString(R.string.wheat_roots);// "Wheat Roots";
		case WOOL:
			return Statics.act.getString(R.string.wool);// Wool";
		case WEEDS:
			return Statics.act.getString(R.string.weeds);//"Weeds";
		case STICKS:
			return Statics.act.getString(R.string.sticks);//"Sticks";
		case CRAFTING_TABLE:
			return Statics.act.getString(R.string.crafting_table);//"Table";
		case BONES:
			return Statics.act.getString(R.string.bones);//"Bones";
		case COBWEB:
			return Statics.act.getString(R.string.cobweb);// "Cobweb";
		case LAMB_CHOP:
			return Statics.act.getString(R.string.lamb_chop);// "Lamb Chop";
		case UNLIT_FURNACE:
			return Statics.act.getString(R.string.unlit_furnace);// "Unlit Furnace";
		case LIT_FURNACE:
			return Statics.act.getString(R.string.lit_furnace);// "Lit Furnace";
		case BREAD:
			return Statics.act.getString(R.string.bread);// "Bread";
		case GLASS:
			return Statics.act.getString(R.string.glass);// "Glass";
		case CLAY:
			return Statics.act.getString(R.string.clay);// "Clay";
		case BRICKS:
			return Statics.act.getString(R.string.bricks);// "Bricks";
		case BED:
			return Statics.act.getString(R.string.bed);// "Bed";
		case RAW_BEEF:
			return Statics.act.getString(R.string.raw_beef);// "Beef";
		case RAW_DEAD_CHICKEN:
			return Statics.act.getString(R.string.chicken);// "Dead chicken";
		case RAW_PORK:
			return Statics.act.getString(R.string.pork);// "Pork";
		case EGG:
			return Statics.act.getString(R.string.egg);// "Egg";
		case SHURIKEN:
			return "Shuriken";
		case CRATE:
			return "Crate";
		case SAVEPOINT:
			return "Savepoint";
		case END_OF_LEVEL:
			return "End of Level";
		case BARREL:
			return "Barrel";
		case ROPE:
			return "Rope";
		case SLIME_SPURT:
			return "Slime";
		case BLOOD_SPURT:
			return "Blood";
		case MEDIKIT:
			return "Medikit";
		default:
			try {
				throw new RuntimeException("Unknown block type:" + type);
			} catch (Exception ex) {
				ErrorReporter.getInstance().handleSilentException(ex);
			}
			return "Unknown";
		}

	}



	public static int GetConsumeValue(byte type) {
		switch (type) {
		case APPLE:
			return 10;
		case BREAD:
			return 30;
		case LAMB_CHOP:
			return 40;
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


	public String getDesc() {
		return GetDesc(this.getType());
	}


	public void damage(int amt, boolean give_to_player) {
		AbstractActivity act = Statics.act;

		this.health -= amt;
		act.sound_manager.playSound(R.raw.crumbling);
		Explosion.CreateExplosion(game, 1, this.getWorldCentreX(), this.getWorldCentreY(), R.drawable.thrown_rock);

		if (this.health <= 0) {
			this.destroy(1, give_to_player);
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


	public void destroy(int explode_pieces, boolean give_to_player) {
		if (explode_pieces > 0) {
			Explosion.CreateExplosion(game, explode_pieces, this.getWorldCentreX(), this.getWorldCentreY(), R.drawable.thrown_rock);
		}
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
			Block above = (Block)game.new_grid.getBlockAtMap_MaybeNull(this.map_x, this.map_y-1);
			if (above != null && (above.getType() == Block.DARKNESS || above.getType() == Block.DARKNESS2 || Block.BlocksLight(above.getType()))) {
				if (Functions.rnd(1, 2) == 1) {
					this.game.addBlock(Block.DARKNESS, this.map_x, this.map_y, true);
				} else {
					this.game.addBlock(Block.DARKNESS2, this.map_x, this.map_y, true);
				}
			} else {
				this.game.addBlock(Block.NOTHING_DAYLIGHT, this.map_x, this.map_y, true);
			}
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
			if (Statics.GAME_MODE == Statics.GM_WORLDCRAFTER) {
				processWater();
			}
			break;
		case LAVA:
			if (Statics.GAME_MODE == Statics.GM_WORLDCRAFTER) {
				if (this.getDistanceTo(game.player) <= Statics.ACTIVATE_DIST) {
					checkLavaSquares();
				} else {
					remove_from_process = false; // Try again later
				}
			}
			break;
		case TANGLEWEED:
			remove_from_process = false;
			int i = Functions.rnd(1, 10); 
			if (i <= 3) {
				if (this.on_fire == false) {
					byte[] types2 = {NOTHING_DAYLIGHT, DARKNESS, DARKNESS2};
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
			if (Statics.GAME_MODE == Statics.GM_WORLDCRAFTER) {
				remove_from_process = false;
				if (this.event_time < System.currentTimeMillis()) {
					this.event_time = System.currentTimeMillis() + GERMINATION_DURATION;
					// Check conditions are right
					Block b = (Block)this.game.new_grid.getBlockAtMap_MaybeNull(this.map_x-1, this.map_y);
					if (b != null && Block.CanGrowPlants(b.getType())) {
						b = (Block)this.game.new_grid.getBlockAtMap_MaybeNull(this.map_x+1, this.map_y);
						if (b != null && Block.CanGrowPlants(b.getType())) {
							b = (Block)this.game.new_grid.getBlockAtMap_MaybeNull(this.map_x, this.map_y+1);
							if (b != null && Block.CanGrowPlants(b.getType())) {
								growTree();
							}
						}
					}
				}
			}
			break;
		case WHEAT_ROOT:
			if (Statics.GAME_MODE == Statics.GM_WORLDCRAFTER) {
				remove_from_process = false;
				if (this.event_time < System.currentTimeMillis()) {
					this.event_time = System.currentTimeMillis() + (GERMINATION_DURATION*3);
					// Check conditions are right
					Block b = (Block)this.game.new_grid.getBlockAtMap_MaybeNull(this.map_x-1, this.map_y);
					if (b != null && Block.CanGrowPlants(b.getType())) {
						b = (Block)this.game.new_grid.getBlockAtMap_MaybeNull(this.map_x+1, this.map_y);
						if (b != null && Block.CanGrowPlants(b.getType())) {
							b = (Block)this.game.new_grid.getBlockAtMap_MaybeNull(this.map_x, this.map_y+1);
							if (b != null && Block.CanGrowPlants(b.getType())) {
								remove_from_process = growWheat();
							}
						}
					}
				}
			}
			break;
		case SLIME:
			remove_from_process = false;
			if (this.event_time < System.currentTimeMillis()) {
				this.event_time = System.currentTimeMillis() + SLIME_DURATION;
				if (Functions.rnd(1, 2) == 1) {
					if (this.getDistanceTo(game.player) <= Statics.ACTIVATE_DIST) {
						act.sound_manager.playSound(R.raw.slime);
						new ThrownItem(game, Block.SLIME_SPURT, new MyPointF(this.getWorldCentreX(), this.getWorldY()), new MyPointF(Functions.rndFloat(-.5f, .5f), -1), null, 10, Statics.ROCK_SPEED, Statics.ROCK_GRAVITY, Statics.SLIME_SIZE);
					}
				}
			}
			break;
		case Block.LIT_FURNACE:
			remove_from_process = processFurnace();
			break;
		}

		if (this.getType() == Block.FIRE || this.on_fire) {
			if (Statics.GAME_MODE == Statics.GM_WORLDCRAFTER) {
				if (checkFire()) {
					remove_from_process = false;
				}
			}
		}

		if (Falls(this.getType())) {
			if (game.new_grid.isSquareEmpty(this.map_x, this.map_y+1)) {
				game.addBlock(this.getType(), this.map_x, this.map_y+1, true);
				this.remove();
			}
		} else { // Always falls if not attached to anything UNLESS DARKNESS!
			if (this.getType() != Block.DARKNESS && this.getType() != Block.DARKNESS2) {
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
			}
		}

		if (remove_from_process) {
			this.game.removeFromProcess(this);
		}
	}


	/**
	 * Returns whether it's gone out (and should be removed from process).
	 * @return
	 */
	private boolean processFurnace() {
		if (this.event_time < System.currentTimeMillis()) {
			event_time = System.currentTimeMillis() + FURNACE_DURATION;
			// Does it go out?
			if (Functions.rnd(1, 10) == 1) {
				game.addBlock(Block.UNLIT_FURNACE, this.map_x, this.map_y, true);
				return true;
			} else {
				// Crafting?
				Block above = (Block)game.new_grid.getBlockAtMap_MaybeNull(this.map_x, this.map_y-1);
				/*if (above.getType() == Block.SAND) {
					game.addBlock(Block.GLASS, this.map_x, this.map_y-1, true, true);
				} else if (above.getType() == Block.WHEAT_HIGH) {
					game.addBlock(Block.BREAD, this.map_x, this.map_y-1, true, true);
				} else if (above.getType() == Block.CLAY) {
					game.addBlock(Block.BRICKS, this.map_x, this.map_y-1, true, true);
				}*/
				CraftingData.DoFurnace(game, above);
			}
		}
		return false;
	}


	private void processWater() {
		byte[] types = {NOTHING_DAYLIGHT, DARKNESS, DARKNESS2, FIRE, LADDER, ROPE};
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


	private void growTree() {
		boolean found = false;
		byte[] types = {NOTHING_DAYLIGHT, DARKNESS, DARKNESS2};
		for (int y_off=1 ; y_off<=4 ; y_off++) {
			if (this.game.new_grid.isSquareEmpty(this.map_x, this.map_y-y_off)) {
				if (y_off == 1) {
					// Acorn must be in light
					byte[] types2 = {NOTHING_DAYLIGHT};
					checkAndChangeAdjacentSquares(0, -y_off, types2, Block.TREE_BARK, true);
				} else {
					checkAndChangeAdjacentSquares(0, -y_off, types, Block.TREE_BARK, true);
				}
				found = true;
				break;
			}
		} 
		if (found == false) {
			// Grow branches / apples/acorns
			byte type_to_grow = Block.APPLE;
			if (Functions.rnd(1, 5) == 1) {
				type_to_grow = Block.ACORN;
			}
			if (this.game.new_grid.isSquareEmpty(this.map_x-1, this.map_y-2)) {
				checkAndChangeAdjacentSquares(-1, -2, types, Block.TREE_BRANCH_LEFT, true);
			} else if (this.game.new_grid.isSquareEmpty(this.map_x+1, this.map_y-2)) {
				checkAndChangeAdjacentSquares(1, -2, types, Block.TREE_BRANCH_RIGHT, true);
			} else if (this.game.new_grid.isSquareEmpty(this.map_x-1, this.map_y-4)) {
				checkAndChangeAdjacentSquares(-1, -4, types, Block.TREE_BRANCH_LEFT, true);
			} else if (this.game.new_grid.isSquareEmpty(this.map_x+1, this.map_y-4)) {
				checkAndChangeAdjacentSquares(1, -4, types, Block.TREE_BRANCH_RIGHT, true);
				/*} else if (this.game.new_grid.isSquareEmpty(this.map_x, this.map_y-5)) {
				checkAndChangeAdjacentSquares(0, -5, types, Block.TREE_BRANCH);*/
			} else if (this.game.new_grid.isSquareEmpty(this.map_x-1, this.map_y-1)) {
				checkAndChangeAdjacentSquares(-1, -1, types, type_to_grow, true);
			} else if (this.game.new_grid.isSquareEmpty(this.map_x+1, this.map_y-1)) {
				checkAndChangeAdjacentSquares(1, -1, types, type_to_grow, true);
			} else if (this.game.new_grid.isSquareEmpty(this.map_x-1, this.map_y-3)) {
				checkAndChangeAdjacentSquares(-1, -3, types, type_to_grow, true);
			} else if (this.game.new_grid.isSquareEmpty(this.map_x+1, this.map_y-3)) {
				checkAndChangeAdjacentSquares(1, -3, types, type_to_grow, true);
			} else {
				// Remove acorn
				game.addBlock(Block.SOIL, map_x, map_y, false);
				this.game.removeFromProcess(this);
				this.addAdjacentBlocksToProcess();
			}
		}
	}


	/**
	 * Returns whether to remove from processing
	 */
	private boolean growWheat() {
		for (int x_off=-1 ; x_off<=1 ; x_off++) {
			//if (this.game.new_grid.isSquareEmpty(this.map_x+x_off, this.map_y-1)) {
			// Acorn must be in light
			byte[] types4 = {NOTHING_DAYLIGHT};
			if (checkAndChangeAdjacentSquares(x_off, -1, types4, Block.WHEAT_NONE, true)) {
				continue;
			}
			byte[] types = {Block.WHEAT_NONE};
			if (checkAndChangeAdjacentSquares(x_off, -1, types, Block.WHEAT_LOW, true)) {
				continue;
			}
			byte[] types2 = {WHEAT_LOW};
			if (checkAndChangeAdjacentSquares(x_off, -1, types2, Block.WHEAT_MED, true)) {
				continue;
			}
			byte[] types3 = {WHEAT_MED};
			if (checkAndChangeAdjacentSquares(x_off, -1, types3, Block.WHEAT_HIGH, true)) {
				continue;
			}
			// Goes to seed
			if (Functions.rnd(1, 3) == 1) {
				byte[] types5 = {WHEAT_HIGH};
				if (checkAndChangeAdjacentSquares(x_off, -1, types5, Block.WHEAT_SEED, true)) {
					return true;
				}
			}
		} 
		return false;
	}


	private void checkLavaSquares() {
		this.checkSpecificLavaSquare(-1, 0);
		this.checkSpecificLavaSquare(1, 0);
		this.checkSpecificLavaSquare(0, -1);
		this.checkSpecificLavaSquare(0, 1);
	}


	private void checkSpecificLavaSquare(int off_x, int off_y) {
		if (off_y >= 0) { // Lava only flows across and down
			byte[] types_lava = {NOTHING_DAYLIGHT, DARKNESS, DARKNESS2, FIRE, APPLE, CHEST, GOLD, MONSTER_GENERATOR, DYNAMITE, ACORN, LADDER, COAL, TREE_BRANCH_LEFT, TREE_BRANCH_RIGHT, ROPE};
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
