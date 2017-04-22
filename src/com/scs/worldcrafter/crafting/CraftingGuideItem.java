package com.scs.worldcrafter.crafting;

import ssmith.android.lib2d.Camera;
import ssmith.android.lib2d.shapes.AbstractRectangle;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.scs.worldcrafter.Statics;
import com.scs.worldcrafter.graphics.blocks.Block;
import com.scs.ninja.main.lite.R;

public class CraftingGuideItem extends AbstractRectangle { // todo - delete
	
	private static final float ICON_SIZE = Statics.SCREEN_WIDTH/10;
	public static final float HEIGHT = ICON_SIZE * 1.1f;//HEIGHT = Statics.SCREEN_HEIGHT/7;
	
	private Bitmap bmp1, bmp2, bmp_new;
	//private Bitmap cmd_1, cmd_2, cmd_new;
	private static Bitmap plus, equals;
	
	public CraftingGuideItem(byte b1, byte b2, byte becomes) {
		super("CraftingGuideItem", null, 0, 0, HEIGHT * 5, HEIGHT);

		if (plus == null) {
			plus = Statics.img_cache.getImage(R.drawable.plus, ICON_SIZE, ICON_SIZE);
			equals = Statics.img_cache.getImage(R.drawable.equals, ICON_SIZE, ICON_SIZE);
		}
		
		bmp1 = Block.GetBitmap(Statics.img_cache, b1, ICON_SIZE, ICON_SIZE);
		if (b2 > 0) {
			bmp2 = Block.GetBitmap(Statics.img_cache, b2, ICON_SIZE, ICON_SIZE);
		}
		bmp_new = Block.GetBitmap(Statics.img_cache, becomes, ICON_SIZE, ICON_SIZE);
		
	}

	@Override
	public void doDraw(Canvas g, Camera cam, long interpol) {
		g.drawBitmap(bmp1, this.world_bounds.left - cam.left, this.world_bounds.top - cam.top, paint);
		if (bmp2 != null) {
			g.drawBitmap(plus, this.world_bounds.left - cam.left + (HEIGHT*1), this.world_bounds.top - cam.top, paint);
			g.drawBitmap(bmp2, this.world_bounds.left - cam.left + (HEIGHT*2), this.world_bounds.top - cam.top, paint);
			g.drawBitmap(equals, this.world_bounds.left - cam.left + (HEIGHT*3), this.world_bounds.top - cam.top, paint);
			g.drawBitmap(bmp_new, this.world_bounds.left - cam.left + (HEIGHT*4), this.world_bounds.top - cam.top, paint);
		} else {
			g.drawBitmap(equals, this.world_bounds.left - cam.left + (HEIGHT*1), this.world_bounds.top - cam.top, paint);
			g.drawBitmap(bmp_new, this.world_bounds.left - cam.left + (HEIGHT*2), this.world_bounds.top - cam.top, paint);
		}

	}

	
}
