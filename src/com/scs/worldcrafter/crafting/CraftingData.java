package com.scs.worldcrafter.crafting;

import java.util.ArrayList;

import ssmith.android.framework.AbstractActivity;

import com.scs.worldcrafter.Statics;
import com.scs.ninja.main.lite.R;
import com.scs.worldcrafter.game.GameModule;
import com.scs.worldcrafter.graphics.ThrownItem;
import com.scs.worldcrafter.graphics.blocks.Block;

public class CraftingData {

	public static ArrayList<CraftingItem> crafting_data = new ArrayList<CraftingItem>();
	public static ArrayList<FurnaceData> furnace_data = new ArrayList<FurnaceData>();

	static {
		crafting_data.add(new CraftingItem(Block.TREE_BARK, Block.TREE_BARK, Block.WOOD, false));
		crafting_data.add(new CraftingItem(Block.WOOD, Block.WOOD, Block.STICKS, false));
		crafting_data.add(new CraftingItem(Block.IRON_ORE, Block.FLINT, Block.FIRE, false, true));
		crafting_data.add(new CraftingItem(Block.WOOD, Block.STICKS, Block.CRAFTING_TABLE, false));
		crafting_data.add(new CraftingItem(Block.STONE, Block.STONE, Block.UNLIT_FURNACE, true));
		crafting_data.add(new CraftingItem(Block.ACORN, Block.SOIL, Block.TREE_ROOT, false));
		crafting_data.add(new CraftingItem(Block.WHEAT_SEED, Block.SOIL, Block.WHEAT_ROOT, false));
		crafting_data.add(new CraftingItem(Block.STICKS, Block.WOOL, Block.BED, true));

		furnace_data.add(new FurnaceData(Block.SAND, Block.GLASS));
		furnace_data.add(new FurnaceData(Block.WHEAT_LOW, Block.BREAD));
		furnace_data.add(new FurnaceData(Block.WHEAT_MED, Block.BREAD));
		furnace_data.add(new FurnaceData(Block.WHEAT_HIGH, Block.BREAD));
		furnace_data.add(new FurnaceData(Block.CLAY, Block.BRICKS));

	}


	public static void DoFurnace(GameModule game, Block block) {
		if (block != null) {
			for (FurnaceData ci : furnace_data) {
				if (block.getType() == ci.from) {
					game.addBlock(ci.to, block.getMapX(), block.getMapY(), true);
					break;
				}
			}
		}
	}


	public static boolean DoCrafting(GameModule game, Block block, ThrownItem item) {
		AbstractActivity act = Statics.act;
		
		// Check area above is empty
		if (game.new_grid.isSquareEmpty( block.getMapX(), block.getMapY()-1, false) == false) {
			return false;
		} else {
			boolean table_exists = false;
			Block b = (Block)game.new_grid.getBlockAtMap_MaybeNull( block.getMapX(), block.getMapY()+1);
			if (b != null) {
				if (b.getType() == Block.CRAFTING_TABLE) {
					table_exists = true;
				}
			}

			for (CraftingItem ci : crafting_data) {
				if (ci.req_table == false || table_exists) {
					if ((ci.block1 == block.getType() && ci.block2 == item.getType()) || (ci.block2 == block.getType() && ci.block1 == item.getType())) {
						int y_off = 0;
						if (ci.sq_above) {
							y_off = -1;
						}
						Block new_block = game.addBlock(ci.makes_block,  block.getMapX(), block.getMapY()+y_off, true);
						game.showToast(act.getString(R.string.block_created, new_block.getDesc()));//new_block.getDesc() + " created");
						act.sound_manager.playSound(R.raw.construction);
						return true;
					}
				}
			}
		}
		return false;
	}


}

