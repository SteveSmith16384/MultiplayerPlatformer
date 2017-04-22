package com.scs.worldcrafter.crafting;

import ssmith.android.framework.AbstractActivity;
import ssmith.android.framework.modules.AbstractComplexModule;
import ssmith.android.framework.modules.AbstractModule;
import ssmith.android.lib2d.gui.Button;
import ssmith.android.lib2d.gui.GUIFunctions;
import ssmith.android.lib2d.gui.Label;
import ssmith.android.lib2d.layouts.FlowGridLayout;
import ssmith.android.lib2d.shapes.Geometry;
import ssmith.android.util.ImageCache;
import ssmith.lang.NumberFunctions;
import android.graphics.Paint;

import com.scs.ninja.main.lite.R;
import com.scs.worldcrafter.Statics;
import com.scs.worldcrafter.graphics.blocks.Block;

public final class MiniCraftingGuide extends AbstractComplexModule {

	private static Paint paint_normal_text = new Paint();

	static {
		paint_normal_text.setARGB(255, 0, 0, 0);
		paint_normal_text.setAntiAlias(true);
		paint_normal_text.setTextSize(GUIFunctions.GetTextSizeToFit("Crafting Table Required", Statics.SCREEN_WIDTH*.75f));//Statics..GetHeightScaled(0.12f));

	}

	private ImageCache img_cache;

	public MiniCraftingGuide(AbstractActivity _act, AbstractModule _return_to) {
		super(_act, _return_to);

		background = Statics.img_cache.getImage(R.drawable.parchment, Statics.SCREEN_WIDTH, Statics.SCREEN_HEIGHT);


	}


	@Override
	public void started() {
		AbstractActivity act = Statics.act;
		
		img_cache = new ImageCache(act.getResources());
		super.scroll_lr = false;

		FlowGridLayout grid = new FlowGridLayout("Layout", Statics.SCREEN_WIDTH, NewCraftingGuideItem.HEIGHT, 5, 1);

		Label l = new Label("Label1", act.getString(R.string.simple_crafting_items), null, paint_normal_text, false);
		grid.attachChild(l);

		for (CraftingItem ci : CraftingData.crafting_data) {
			if (ci.req_table == false) {
				NewCraftingGuideItem cgi = new NewCraftingGuideItem(img_cache, ci.block1, ci.block2, ci.makes_block);
				grid.attachChild(cgi);
			}
		}

		l = new Label("Label1", act.getString(R.string.crafting_table_required), null, paint_normal_text, false);
		grid.attachChild(l);

		for (CraftingItem ci : CraftingData.crafting_data) {
			if (ci.req_table) {
				NewCraftingGuideItem cgi = new NewCraftingGuideItem(img_cache, ci.block1, ci.block2, ci.makes_block);
				grid.attachChild(cgi);
			}
		}

		l = new Label("Label2", act.getString(R.string.furnace_required), null, paint_normal_text, false);
		grid.attachChild(l);

		for (FurnaceData ci : CraftingData.furnace_data) {
			NewCraftingGuideItem cgi = new NewCraftingGuideItem(img_cache, ci.from, (byte)-1, ci.to);
			grid.attachChild(cgi);
		}
		this.root_node.attachChild(grid);
		this.root_node.updateGeometricState();

		//this.root_cam.lookAt(grid.getWorldX() + (grid.getWidth()/2), grid.getWorldY() + (Statics.SCREEN_HEIGHT/2), true);
		this.root_cam.lookAt(grid.getWorldX() + (grid.getWidth()/3), grid.getWorldY() + (Statics.SCREEN_HEIGHT/2), true);
		
	}


	@Override
	public void stopped() {
		img_cache.clear();
	}


	@Override
	public boolean componentClicked(Geometry c) {
		AbstractActivity act = Statics.act;
		
		try {
			if (c instanceof Button) {
				Button b = (Button) c;
				this.showToast(Block.GetDesc(NumberFunctions.ParseByte(b.getActionCommand())));
				return true;
			}
		} catch (Exception ex) {
			AbstractActivity.HandleError(act, ex);
		}
		return false;
	}


	@Override
	public void updateGame(long interpol) {
		// Do nothing
	}


}
