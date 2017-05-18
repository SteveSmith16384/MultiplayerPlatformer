package com.scs.multiplayerplatformer.start;

import java.io.File;

import ssmith.android.compatibility.Paint;
import ssmith.android.framework.AbstractActivity;
import ssmith.android.framework.modules.AbstractModule;
import ssmith.android.framework.modules.AbstractOptionsModule2;
import ssmith.android.lib2d.gui.GUIFunctions;

import com.scs.multiplayerplatformer.Statics;
import com.scs.multiplayerplatformer.game.GameModule;

public class SelectLevelModule extends AbstractOptionsModule2 {

	private static final float ICON_WIDTH = Statics.SCREEN_WIDTH * 0.4f;

	private static Paint paint_text = new Paint();

	private String maps[];

	static {
		paint_text.setARGB(255, 255, 255, 255);
		paint_text.setAntiAlias(true);
		paint_text.setTextSize(GUIFunctions.GetTextSizeToFit("CONS TRU TION XXXX", ICON_WIDTH));

	}


	public SelectLevelModule(AbstractActivity act, AbstractModule _return_to) {
		super(act, _return_to, 2, paint_text, Statics.img_cache.getImage("button_blue", ICON_WIDTH, Statics.SCREEN_WIDTH/10), 0, false, "", true);

		this.setBackground(Statics.BACKGROUND_IMAGE);

		maps = new File(Statics.MAP_DIR).list();
	}


	@Override
	public void getOptions() {
		this.addOption("Random");
		for (int i=1 ; i<maps.length ; i++) {
			this.addOption(maps[i]);
		}

	}


	@Override
	public void optionSelected(int idx) {
		GameModule game = null;
		if (idx <= 0) {
			game = new GameModule(Statics.act, null);
		} else {
			game = new GameModule(Statics.act, maps[idx]);
		}
		this.getThread().setNextModule(game);
	}

}
