package com.scs.multiplayerplatformer.start;

import ssmith.android.compatibility.Paint;
import ssmith.android.framework.modules.AbstractModule;
import ssmith.android.framework.modules.AbstractOptionsModule2;
import ssmith.android.lib2d.gui.GUIFunctions;

import com.scs.multiplayerplatformer.Statics;
import com.scs.multiplayerplatformer.Statics.GameMode;

public final class SelectGameModeModule extends AbstractOptionsModule2 {
	
	private static final String NORMAL_MODE = "NORMAL";
	private static final String RACE_TO_DEATH_MODE = "RACE to the DEATH";

	private static final float ICON_WIDTH = Statics.SCREEN_WIDTH * 0.4f;

	private static Paint paint_text = new Paint();

	static {
		paint_text.setARGB(255, 255, 255, 255);
		paint_text.setAntiAlias(true);
		paint_text.setTextSize(GUIFunctions.GetTextSizeToFit("CONS TRU TION XXXX", ICON_WIDTH));

	}


	public SelectGameModeModule() {
		super(1, paint_text, Statics.img_cache.getImage("button_blue", ICON_WIDTH, Statics.SCREEN_WIDTH/10), 0, false, "", true);

		this.setBackground(Statics.BACKGROUND_IMAGE);
	}


	@Override
	public void getOptions() {
		this.addOption(NORMAL_MODE);
		this.addOption(RACE_TO_DEATH_MODE);

	}
	

	@Override
	public void optionSelected(int idx) {
		String opt = super.getActionCommand(idx);
		if (opt.equalsIgnoreCase(RACE_TO_DEATH_MODE)) {
			Statics.GAME_MODE = GameMode.RaceToTheDeath;
		} else {
			Statics.GAME_MODE = GameMode.Normal;
		}
		AbstractModule game = new SelectLevelModule();
		this.getThread().setNextModule(game);
	}


	@Override
	public boolean onBackPressed() {
		Statics.act.thread.setNextModule(new StartupModule());
		return true;
	}


}
