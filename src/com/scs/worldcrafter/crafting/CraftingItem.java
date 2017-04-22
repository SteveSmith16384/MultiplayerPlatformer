package com.scs.worldcrafter.crafting;

public class CraftingItem {
	
	public byte block1, block2, makes_block;
	public boolean req_table, sq_above;

	public CraftingItem(byte b1, byte b2, byte makes, boolean r_table) {
		this(b1, b2, makes, r_table, false);
	}
	
	public CraftingItem(byte b1, byte b2, byte makes, boolean r_table, boolean above) {
		block1 = b1;
		block2 = b2;
		makes_block = makes;
		req_table = r_table;
		sq_above = above;
	}
	

}
