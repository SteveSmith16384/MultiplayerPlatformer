package com.scs.worldcrafter.start;

import java.io.File;

import ssmith.android.framework.AbstractActivity;
import ssmith.android.framework.modules.AbstractModule;
import ssmith.android.framework.modules.AbstractOptionsModule2;
import ssmith.android.lib2d.gui.GUIFunctions;
import android.graphics.Paint;

import com.scs.worldcrafter.Statics;
import com.scs.worldcrafter.game.GameModule;
import com.scs.ninja.main.lite.R;
import com.scs.worldcrafter.mapgen.AbstractLevelData;
import com.scs.worldcrafter.mapgen.LoadSavedMap;
import com.scs.worldcrafter.mapgen.MinecraftLevelData;

public final class SelectSavedMapModule extends AbstractOptionsModule2 {

	public static Paint paint_text = new Paint();

	static {
		paint_text.setARGB(255, 255, 255, 255);
		paint_text.setAntiAlias(true);
		paint_text.setTextSize(GUIFunctions.GetTextSizeToFit("Cannot access SD card", Statics.SCREEN_WIDTH/2));

	}


	public SelectSavedMapModule(AbstractActivity act, AbstractModule _return_to) {
		super(act, _return_to, 1, paint_text, Statics.img_cache.getImage(R.drawable.button_blue, Statics.SCREEN_WIDTH/2, Statics.SCREEN_WIDTH/10), 0, false, "", true);

		background = Statics.img_cache.getImage(R.drawable.menu_background, Statics.SCREEN_WIDTH, Statics.SCREEN_HEIGHT);

	}


	@Override
	public void getOptions() {
		AbstractActivity act = Statics.act;
		
		File dir = Statics.GetExtStorage();
		File files[] = dir.listFiles();

		if (files != null) {
			//ArrayList<String> s = new ArrayList<String>();
			super.addOption(act.getString(R.string.new_map));
			for (int i=0 ; i<files.length ; i++) {
				if (files[i].isDirectory() == false) {
					super.addOption(files[i].getName());
				}
			}

			//return s;
		} else {
			//ArrayList<String> s = new ArrayList<String>();
			super.addOption(act.getString(R.string.new_map));
			super.addOption(act.getString(R.string.cannot_access_sd_card));
			//return s;
		}
	}


	@Override
	public void optionSelected(int idx) {
		AbstractActivity act = Statics.act;
		
		String cmd = this.getActionCommand(idx);
		boolean new_map = cmd.equalsIgnoreCase(act.getString(R.string.new_map));
		String filename = "";
		AbstractLevelData original_level_data = null;
		if (new_map) {
			filename = "";
			original_level_data = new MinecraftLevelData();
		} else {
			original_level_data = new LoadSavedMap(cmd);
		}
		GameModule game = new GameModule(act, original_level_data, -1, filename);
		this.getThread().setNextModule(game);

	}



}
