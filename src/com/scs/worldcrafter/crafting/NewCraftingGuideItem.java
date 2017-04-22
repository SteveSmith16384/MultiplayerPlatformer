package com.scs.worldcrafter.crafting;

import ssmith.android.lib2d.gui.Button;
import ssmith.android.lib2d.layouts.HorizontalFlowGridLayout;
import ssmith.android.util.ImageCache;
import android.graphics.Bitmap;

import com.scs.ninja.main.lite.R;
import com.scs.worldcrafter.Statics;
import com.scs.worldcrafter.graphics.blocks.Block;

public class NewCraftingGuideItem extends HorizontalFlowGridLayout {

	private static final float ICON_SIZE = Statics.SCREEN_WIDTH/10;
	public static final float HEIGHT = ICON_SIZE * 1.1f;

	private Bitmap bmp1, bmp2, bmp_new;
	private Button cmd1, cmd2, cmd_new;
	private Bitmap bmp_plus, bmp_equals;

	private ImageCache img_cache;

	public NewCraftingGuideItem(ImageCache _img_cache, byte b1, byte b2, byte becomes) {
		super("NewCraftingGuideItem", ICON_SIZE * 0.1f);

		img_cache = _img_cache;

		bmp_plus = img_cache.getImage(R.drawable.plus, ICON_SIZE, ICON_SIZE);
		bmp_equals = img_cache.getImage(R.drawable.equals, ICON_SIZE, ICON_SIZE);

		bmp1 = Block.GetBitmap(img_cache, b1, ICON_SIZE, ICON_SIZE);
		cmd1 = new Button("" + b1, "", null, null, bmp1);
		this.attachChild(cmd1);
		if (b2 > 0) {
			Button plus = new Button("", "", null, null, bmp_plus);
			this.attachChild(plus);

			bmp2 = Block.GetBitmap(img_cache, b2, ICON_SIZE, ICON_SIZE);
			cmd2 = new Button("" + b2, "", null, null, bmp2);
			this.attachChild(cmd2);
		}
		Button eq = new Button("", "", null, null, bmp_equals);
		this.attachChild(eq);

		bmp_new = Block.GetBitmap(img_cache, becomes, ICON_SIZE, ICON_SIZE);
		cmd_new = new Button("" + becomes, "", null, null, bmp_new);
		this.attachChild(cmd_new);

	}


	/*@Override
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

	}*/


}
