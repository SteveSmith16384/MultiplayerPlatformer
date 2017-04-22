package com.scs.worldcrafter.miniwiki;

import java.util.ArrayList;

import ssmith.android.framework.AbstractActivity;
import ssmith.android.framework.modules.AbstractModule;
import ssmith.android.framework.modules.AbstractOptionsModule;
import ssmith.android.lib2d.gui.GUIFunctions;
import ssmith.android.lib2d.gui.Label;
import ssmith.android.util.ImageCache;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import com.scs.ninja.main.lite.R;
import com.scs.worldcrafter.Statics;
import com.scs.worldcrafter.graphics.blocks.Block;

/**
 * This is for showing a list of all the blocks in the mini-wiki
 *
 */
public class BlockListModule extends AbstractOptionsModule {

	private static ArrayList<String> options = new ArrayList<String>();

	private static final float ICON_WIDTH = Statics.SCREEN_WIDTH/4;

	private static Paint paint_large_text = new Paint();
	private static Paint paint_normal_text = new Paint();
	public static ImageCache img_cache;

	static {
		paint_large_text.setARGB(255, 0, 0, 0);
		paint_large_text.setAntiAlias(true);
		//paint_large_text.setStyle(Style.STROKE);
		paint_large_text.setTextSize(Statics.GetHeightScaled(0.09f)); // Was 28

		paint_normal_text.setARGB(255, 0, 0, 0);
		paint_normal_text.setAntiAlias(true);
		
		options.add(Block.GetDesc(Block.ACORN));
		options.add(Block.GetDesc(Block.AMULET));
		options.add(Block.GetDesc(Block.APPLE));
		options.add(Block.GetDesc(Block.ADAMANTIUM));
		options.add(Block.GetDesc(Block.BARREL));
		options.add(Block.GetDesc(Block.BED));
		options.add(Block.GetDesc(Block.BREAD));
		options.add(Block.GetDesc(Block.BRICKS));
		options.add(Block.GetDesc(Block.CHEST));
		options.add(Block.GetDesc(Block.CLAY));
		options.add(Block.GetDesc(Block.CRAFTING_TABLE));
		options.add(Block.GetDesc(Block.CRATE));
		options.add(Block.GetDesc(Block.DYNAMITE));
		options.add(Block.GetDesc(Block.FIRE));
		options.add(Block.GetDesc(Block.FLINT));
		options.add(Block.GetDesc(Block.LIT_FURNACE));
		options.add(Block.GetDesc(Block.GOLD));
		options.add(Block.GetDesc(Block.IRON_ORE));
		options.add(Block.GetDesc(Block.MONSTER_GENERATOR));
		options.add(Block.GetDesc(Block.ROCK));
		options.add(Block.GetDesc(Block.SAND));
		options.add(Block.GetDesc(Block.SLIME));
		options.add(Block.GetDesc(Block.SOIL));
		options.add(Block.GetDesc(Block.STICKS));
		options.add(Block.GetDesc(Block.STONE));
		options.add(Block.GetDesc(Block.TANGLEWEED));
		options.add(Block.GetDesc(Block.TREE_ROOT));
		options.add(Block.GetDesc(Block.WATER));
		options.add(Block.GetDesc(Block.WHEAT_HIGH));
		options.add(Block.GetDesc(Block.WHEAT_ROOT));
		options.add(Block.GetDesc(Block.WHEAT_SEED));
		options.add(Block.GetDesc(Block.WOOD));
		options.add(Block.GetDesc(Block.WOOL));
		
		options.add(Statics.act.getString(R.string.return_text));
	}


	public BlockListModule(AbstractActivity act, AbstractModule _return_to) {
		super(act, _return_to, 3, paint_normal_text, Statics.img_cache.getImage(R.drawable.button_blue, ICON_WIDTH, Statics.SCREEN_WIDTH/10), false);

		background = Statics.img_cache.getImage(R.drawable.parchment, Statics.SCREEN_WIDTH, Statics.SCREEN_HEIGHT);

		img_cache = new ImageCache(act.getResources());
		Label l = new Label("Block_types_title", act.getString(R.string.block_types), null, paint_large_text);
		l.setCentre(Statics.SCREEN_WIDTH/2, paint_large_text.getTextSize());
		this.stat_node_front.attachChild(l);

		paint_normal_text.setTextSize(Statics.GetHeightScaled(0.05f));

		this.stat_node_front.updateGeometricState();

		paint_normal_text.setTextSize(GUIFunctions.GetTextSizeToFit("TANGLEWEED", ICON_WIDTH));

	}


	@Override
	public ArrayList<String> getOptions() {
		return options;
	}
	
	
	@Override
	public void stopped() {
		//img_cache.clear();
	}


	@Override
	public void optionSelected(String cmd) {
		AbstractActivity act = Statics.act;
		
		if (cmd.equalsIgnoreCase(Statics.act.getString(R.string.return_text))) {
			super.returnTo();
		} else {
			byte type = -1;
			try {
				for (byte i=2 ; i<=Block.MAX_BLOCK_ID ; i++) {
					String b = Block.GetDesc(i);
					if (cmd.equalsIgnoreCase(b)) {
						type = i;
						break;
					}
				}
				if (type < 0) { // Not found
					throw new RuntimeException("Block " + cmd + " not found.");
				}
			} catch (RuntimeException ex) {
				AbstractActivity.HandleError(act, ex);
				super.returnTo();
				return;
			}

			String text = act.getString(R.string.text_not_found);
			if (cmd.equalsIgnoreCase(Block.GetDesc(Block.ACORN))) {
				text = act.getString(R.string.desc_acorn);
			} else if (cmd.equalsIgnoreCase(Block.GetDesc(Block.TANGLEWEED))) {
				text = act.getString(R.string.desc_tangleweed);
			} else if (cmd.equalsIgnoreCase(Block.GetDesc(Block.SOIL))) {
				text = act.getString(R.string.desc_soil);
			} else if (cmd.equalsIgnoreCase(Block.GetDesc(Block.ROCK))) {
				text = act.getString(R.string.desc_rock);
			} else if (cmd.equalsIgnoreCase(Block.GetDesc(Block.WATER))) {
				text = act.getString(R.string.desc_water);
			} else if (cmd.equalsIgnoreCase(Block.GetDesc(Block.DYNAMITE))) {
				text = act.getString(R.string.desc_dynamite);
			} else if (cmd.equalsIgnoreCase(Block.GetDesc(Block.APPLE))) {
				text = act.getString(R.string.desc_apple);
			} else if (cmd.equalsIgnoreCase(Block.GetDesc(Block.CHEST))) {
				text = act.getString(R.string.desc_chest);
			} else if (cmd.equalsIgnoreCase(Block.GetDesc(Block.MONSTER_GENERATOR))) {
				text = act.getString(R.string.desc_monster_gen);
			} else if (cmd.equalsIgnoreCase(Block.GetDesc(Block.ADAMANTIUM))) {
				text = act.getString(R.string.desc_adamantium);
			} else if (cmd.equalsIgnoreCase(Block.GetDesc(Block.FLINT))) {
				text = act.getString(R.string.desc_flint);
			} else if (cmd.equalsIgnoreCase(Block.GetDesc(Block.IRON_ORE))) {
				text = act.getString(R.string.desc_iron_ore);
			} else if (cmd.equalsIgnoreCase(Block.GetDesc(Block.FIRE))) {
				text = act.getString(R.string.desc_fire);
			} else if (cmd.equalsIgnoreCase(Block.GetDesc(Block.LAVA))) {
				text = act.getString(R.string.desc_lava);
			} else if (cmd.equalsIgnoreCase(Block.GetDesc(Block.AMULET))) {
				text = act.getString(R.string.desc_amulet);
			} else if (cmd.equalsIgnoreCase(Block.GetDesc(Block.SLIME))) {
				text = act.getString(R.string.desc_slime);
			} else if (cmd.equalsIgnoreCase(Block.GetDesc(Block.GOLD))) {
				text = act.getString(R.string.desc_gold);
			} else if (cmd.equalsIgnoreCase(Block.GetDesc(Block.STONE))) {
				text = act.getString(R.string.desc_stone);
			} else if (cmd.equalsIgnoreCase(Block.GetDesc(Block.SAND))) {
				text = act.getString(R.string.desc_sand);
			} else if (cmd.equalsIgnoreCase(Block.GetDesc(Block.WHEAT_HIGH))) {
				text = act.getString(R.string.desc_wheat);
			} else if (cmd.equalsIgnoreCase(Block.GetDesc(Block.WHEAT_ROOT))) {
				text = act.getString(R.string.desc_wheat_root);
			} else if (cmd.equalsIgnoreCase(Block.GetDesc(Block.WHEAT_SEED))) {
				text = act.getString(R.string.desc_wheat_seed);
			} else if (cmd.equalsIgnoreCase(Block.GetDesc(Block.TREE_ROOT))) {
				text = act.getString(R.string.desc_tree_root);
			} else if (cmd.equalsIgnoreCase(Block.GetDesc(Block.BED))) {
				text = act.getString(R.string.desc_bed);
			} else if (cmd.equalsIgnoreCase(Block.GetDesc(Block.WOOL))) {
				text = act.getString(R.string.desc_wool);
			} else if (cmd.equalsIgnoreCase(Block.GetDesc(Block.WOOD))) {
				text = act.getString(R.string.desc_wood);
			} else if (cmd.equalsIgnoreCase(Block.GetDesc(Block.LIT_FURNACE))) {
				text = act.getString(R.string.desc_lit_furnace);
			} else if (cmd.equalsIgnoreCase(Block.GetDesc(Block.BREAD))) {
				text = act.getString(R.string.desc_bread);
			} else if (cmd.equalsIgnoreCase(Block.GetDesc(Block.CRAFTING_TABLE))) {
				text = act.getString(R.string.desc_crafting_table);
			} else if (cmd.equalsIgnoreCase(Block.GetDesc(Block.STICKS))) {
				text = act.getString(R.string.desc_sticks);
			} else if (cmd.equalsIgnoreCase(Block.GetDesc(Block.CLAY))) {
				text = act.getString(R.string.desc_clay);
			} else if (cmd.equalsIgnoreCase(Block.GetDesc(Block.BRICKS))) {
				text = act.getString(R.string.desc_bricks);
			} else if (cmd.equalsIgnoreCase(Block.GetDesc(Block.CRATE))) {
				text = "Crate";
			} else if (cmd.equalsIgnoreCase(Block.GetDesc(Block.BARREL))) {
				text = "Barrel";
			} else if (cmd.equalsIgnoreCase(Block.GetDesc(Block.SLIME_SPURT))) {
				text = "Slime";
			} else if (cmd.equalsIgnoreCase(Block.GetDesc(Block.BLOOD_SPURT))) {
				text = "Blood";
			} else {
				//super.returnToPrevModule();
			}

			Bitmap bmp = Block.GetBitmap(img_cache, type, Statics.SCREEN_WIDTH * .3f, Statics.SCREEN_WIDTH * .3f);
			this.getThread().setNextModule(new DetailsModule(act, this, bmp, cmd, text));
		}
	}

}
