package com.scs.multiplayerplatformer.game;

import ssmith.android.compatibility.Canvas;
import ssmith.android.lib2d.Camera;
import ssmith.android.lib2d.layouts.EfficientGridLayout;

import com.scs.multiplayerplatformer.Statics;
import com.scs.multiplayerplatformer.graphics.blocks.Block;

public final class MyEfficientGridLayout extends EfficientGridLayout implements IProcessable, IDrawable {

	private GameModule game;

	public MyEfficientGridLayout(GameModule _game, int w, int h, float _tile_size) {
		super(w, h, _tile_size);

		game = _game;
	}


	public void doDraw(Canvas g, Camera cam, long interpol) {
		// Do nothing
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


	@Override
	public void doDraw(Canvas g, Camera cam, long interpol, float scale) {
		float tile_size = Statics.SQ_SIZE * scale;
		int draw_width = (int)((cam.right - cam.left) / tile_size);
		int draw_height = (int)((cam.bottom - cam.top) / tile_size);

		int s_x = (int)(cam.left / tile_size);
		int s_y = (int)(cam.top / tile_size);

		objects_being_drawn = 0;

		for (int y=s_y ; y<=s_y + draw_height+1 ; y++) {
			if (y >= 0 && y < blocks[0].length) {
				for (int x=s_x ; x<=s_x + draw_width+1 ; x++) {
					if (x >= 0 && x < blocks.length) {
						try {
							if (blocks[x][y] != null) {
								Block block = (Block)blocks[x][y];
								block.doDraw(g, cam, interpol, scale); // block.bmp.getWidth()
								objects_being_drawn++;
							}
						} catch (ArrayIndexOutOfBoundsException ex) {
							//AbstractActivity.HandleError(null, ex);
						}
					}
				}
			}
		}
		
	}


	@Override
	public void process(long interpol) {
		// Do nothing
		
	}



}
