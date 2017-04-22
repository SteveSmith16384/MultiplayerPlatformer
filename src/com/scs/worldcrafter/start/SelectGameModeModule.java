package com.scs.worldcrafter.start;

import ssmith.android.framework.AbstractActivity;
import ssmith.android.framework.modules.AbstractModule;
import ssmith.android.framework.modules.AbstractOptionsModule2;
import ssmith.android.lib2d.gui.GUIFunctions;
import android.graphics.Paint;

import com.scs.ninja.main.lite.R;
import com.scs.worldcrafter.Statics;

public final class SelectGameModeModule extends AbstractOptionsModule2 {
	
	private static final float ICON_WIDTH = Statics.SCREEN_WIDTH * 0.4f;

	public static Paint paint_text = new Paint();

	static {
		paint_text.setARGB(255, 255, 255, 255);
		paint_text.setAntiAlias(true);
		paint_text.setTextSize(GUIFunctions.GetTextSizeToFit("CONSTRUCTION", ICON_WIDTH));

	}

	
	public SelectGameModeModule(AbstractActivity act, AbstractModule _return_to) {
		super(act, _return_to, 2, paint_text, Statics.img_cache.getImage(R.drawable.button_blue, ICON_WIDTH, Statics.SCREEN_WIDTH/10), 0, false, "", true);

		background = Statics.img_cache.getImage(R.drawable.menu_background, Statics.SCREEN_WIDTH, Statics.SCREEN_HEIGHT);
	}
	

	@Override
	public void getOptions() {
		AbstractActivity act = Statics.act;
		
		super.addOption(act.getString(R.string.construction));
		super.addOption(act.getString(R.string.survival));
		super.addOption(act.getString(R.string.farming));
		super.addOption(act.getString(R.string.quest));
		super.addOption(act.getString(R.string.race));
	}

	
	@Override
	public void optionSelected(int idx) {
		AbstractActivity act = Statics.act;
		
		String cmd = this.getActionCommand(idx);
		if (cmd.equalsIgnoreCase(act.getString(R.string.survival))) {
			Statics.los_to_see_monsters = true;
			Statics.monsters = true;
			Statics.player_loses_health = true;
			Statics.amulet = false;
			Statics.has_infinite_blocks = false;
			Statics.has_timer = false;
		} else if (cmd.equalsIgnoreCase(act.getString(R.string.construction))) {
			Statics.los_to_see_monsters = false;
			Statics.monsters = false;
			Statics.player_loses_health = false;
			Statics.amulet = false;
			Statics.has_infinite_blocks = true;
			Statics.has_timer = false;
		} else if (cmd.equalsIgnoreCase(act.getString(R.string.quest))) {
			Statics.los_to_see_monsters = true;
			Statics.monsters = true;
			Statics.player_loses_health = false;
			Statics.amulet = true;
			Statics.has_infinite_blocks = false;
			Statics.has_timer = false;
		} else if (cmd.equalsIgnoreCase(act.getString(R.string.race))) {
			Statics.los_to_see_monsters = false;
			Statics.monsters = false;
			Statics.player_loses_health = false;
			Statics.amulet = true;
			Statics.has_infinite_blocks = false;
			Statics.has_timer = true;
		} else if (cmd.equalsIgnoreCase(act.getString(R.string.farming))) {
			Statics.los_to_see_monsters = false;
			Statics.monsters = false;
			Statics.player_loses_health = true;
			Statics.amulet = false;
			Statics.has_infinite_blocks = false;
			Statics.has_timer = false;
		} else {
			throw new RuntimeException("Unknown game mode: " + cmd);
		}
		SelectSavedMapModule select_map = new SelectSavedMapModule(act, this);
		this.getThread().setNextModule(select_map);

	}


}
