package com.scs.multiplayerplatformer.game;

import com.scs.multiplayerplatformer.Statics;
import com.scs.multiplayerplatformer.graphics.blocks.Block;

import ssmith.android.lib2d.layouts.EfficientGridLayout;

public class MyEfficientGridLayout extends EfficientGridLayout {

	private GameModule game;

	public MyEfficientGridLayout(GameModule _game, int w, int h, float _tile_size) {
		super(w, h, _tile_size);

		game = _game;
	}


	/**
	 * 
	 * @param map_x
	 * @param map_y
	 * @param completely Must be completely empty, otherwise only what blocks movement
	 * @return
	 */
	public boolean isSquareEmpty(int map_x, int map_y) {
		return isSquareEmpty(map_x, map_y, true);
	}
	
	
	public boolean isSquareEmpty(int map_x, int map_y, boolean check_mobs) {
		// Check area is clear of mobs
		if (check_mobs == false || this.game.isAreaClear(map_x*Statics.SQ_SIZE, map_y*Statics.SQ_SIZE, Statics.SQ_SIZE, Statics.SQ_SIZE, false)) {
			try {
				if (blocks[map_x][map_y] == null) {
					return true;
				} else {
					Block b = (Block)blocks[map_x][map_y];
					//return !Block.BlocksMovement(b.getType());  NO!  As apples fall into each other!
					//return Block.CanBeBuiltOver(b.getType()); // NO! As it passes through water and creates air
					return b.getType() == Block.NOTHING_DAYLIGHT;// || b.getType() == Block.DARKNESS || b.getType() == Block.DARKNESS2;
				}
			} catch (java.lang.ArrayIndexOutOfBoundsException ex) {
				return true;
			} 
		} else {
			return false;
		}
	}


	public boolean isSquareSolid(int map_x, int map_y) {
		try {
			if (blocks[map_x][map_y] == null) {
				return false;
			} else {
				Block b = (Block)blocks[map_x][map_y];
				return Block.BlocksAllMovement(b.getType());
			}
		} catch (java.lang.ArrayIndexOutOfBoundsException ex) {
			return true;
		}
	}



}
