package com.scs.worldcrafter.game;

import ssmith.android.framework.AbstractActivity;
import ssmith.android.framework.modules.AbstractSingleScreenModule;
import ssmith.android.lib2d.gui.Button;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;

import com.scs.worldcrafter.Statics;
import com.scs.worldcrafter.graphics.blocks.Block;

public final class MapScreenModule extends AbstractSingleScreenModule {
	
	private static Paint paint_grass = new Paint();
	private static Paint paint_soil = new Paint();
	private static Paint paint_water = new Paint();
	private static Paint paint_lava = new Paint();
	private static Paint paint_nothing = new Paint();
	private static Paint paint_darkness = new Paint();
	private static Paint paint_items = new Paint();

	static {
		paint_grass.setARGB(255, 0, 200, 0);
		paint_grass.setAntiAlias(true);
		paint_grass.setStyle(Style.FILL);

		paint_soil.setARGB(255, 78, 47, 32);
		paint_soil.setAntiAlias(true);
		paint_soil.setStyle(Style.FILL);

		paint_darkness.setARGB(255, 0, 0, 0);
		paint_darkness.setAntiAlias(true);
		paint_darkness.setStyle(Style.FILL);

		paint_water.setARGB(255, 0, 0, 255);
		paint_water.setAntiAlias(true);
		paint_water.setStyle(Style.FILL);

		paint_nothing.setARGB(255, 150, 150, 255);
		paint_nothing.setAntiAlias(true);
		paint_nothing.setStyle(Style.FILL);

		paint_lava.setARGB(255, 255, 0, 0);
		paint_lava.setAntiAlias(true);
		paint_lava.setStyle(Style.FILL);

		paint_items.setARGB(255, 255, 0, 255);
		paint_items.setAntiAlias(true);
		paint_items.setStyle(Style.FILL);

	}

	
	public MapScreenModule(AbstractActivity act, GameModule game) {
		super(act, game);
		
		Bitmap bmp = Bitmap.createBitmap((int)Statics.SCREEN_WIDTH, (int)Statics.SCREEN_HEIGHT, Bitmap.Config.RGB_565);
		
		float sq_size_x = Statics.SCREEN_WIDTH /  game.original_level_data.getGridWidth();
		float sq_size_y = Statics.SCREEN_HEIGHT /  game.original_level_data.getGridHeight();
		
		Canvas c = new Canvas(bmp);
		RectF r = new RectF();
		for (int y=0 ; y<game.original_level_data.getGridHeight() ; y++) {
			for (int x=0 ; x<game.original_level_data.getGridWidth() ; x++) {
				byte b = game.original_level_data.getGridDataAt(x, y).type;
				r.set(x * sq_size_x, y * sq_size_y, (x+1) * sq_size_x, (y+1)*sq_size_y);
				if (b == Block.NOTHING_DAYLIGHT) {
					c.drawRect(r, paint_nothing);
				} else if (b == Block.DARKNESS || b == Block.DARKNESS2) {
					c.drawRect(r, paint_darkness);
				} else if (b == Block.WATER) {
					c.drawRect(r, paint_water);
				} else if (b == Block.LAVA) {
					c.drawRect(r, paint_lava);
				} else if (b == Block.GRASS || b == Block.SNOW || b == Block.TANGLEWEED || b == Block.SLIME) {
					c.drawRect(r, paint_grass);
				} else if (b == Block.MONSTER_GENERATOR || b == Block.CHEST || b == Block.APPLE) {
					c.drawRect(r, paint_items);
				} else {
					c.drawRect(r, paint_soil);
				}
			}
		}
		
		Button b = new Button("Button", "", 0f, 0f, paint_nothing, paint_nothing, bmp);
		this.stat_node_front.attachChild(b);
		
		this.stat_node_front.updateGeometricState();
		

	}

}
