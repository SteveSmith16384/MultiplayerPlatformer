package com.scs.multiplayerplatformer.start;

import ssmith.android.compatibility.Paint;
import ssmith.android.framework.AbstractActivity;
import ssmith.android.framework.modules.AbstractModule;
import ssmith.android.framework.modules.AbstractOptionsModule2;
import ssmith.android.lib2d.gui.GUIFunctions;

import com.scs.multiplayerplatformer.Statics;
import com.scs.multiplayerplatformer.game.GameModule;
import com.scs.multiplayerplatformer.mapgen.AbstractLevelData;
import com.scs.multiplayerplatformer.mapgen.LoadMap;

public class SelectLevelModule extends AbstractOptionsModule2 {

	private static final float ICON_WIDTH = Statics.SCREEN_WIDTH * 0.4f;

	private static Paint paint_text = new Paint();

	static {
		paint_text.setARGB(255, 255, 255, 255);
		paint_text.setAntiAlias(true);
		paint_text.setTextSize(GUIFunctions.GetTextSizeToFit("CONS TRU TION XXXX", ICON_WIDTH));

	}


	public SelectLevelModule(AbstractActivity act, AbstractModule _return_to) {
		super(act, _return_to, 2, paint_text, Statics.img_cache.getImage("button_blue", ICON_WIDTH, Statics.SCREEN_WIDTH/10), 0, false, "", true);

		this.setBackground("ninja_background2");
	}


	@Override
	public void getOptions() {
		int max = 3; //todo Statics.cfg.getMaxLevel();
		for (int i=1 ; i<=max ; i++) {
			this.addOption("Level " + i);
		}

	}
	

	@Override
	public void optionSelected(int idx) {
		int level = idx+1;
		AbstractLevelData original_level_data = new LoadMap(Statics.GetMapFilename(level));
		GameModule game = new GameModule(Statics.act, original_level_data, level);
		this.getThread().setNextModule(game);
	}

}
