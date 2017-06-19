package com.scs.multiplayerplatformer.start;

import java.util.List;

import ssmith.android.compatibility.Paint;
import ssmith.android.framework.modules.AbstractOptionsModule2;
import ssmith.android.lib2d.gui.GUIFunctions;

import com.scs.multiplayerplatformer.Statics;
import com.scs.multiplayerplatformer.game.GameModule;
import com.scs.multiplayerplatformer.mapgen.MapLoader;

public class SelectLevelModule extends AbstractOptionsModule2 {

	private static final float ICON_WIDTH = Statics.SCREEN_WIDTH * 0.4f;

	private static Paint paint_text = new Paint();

	private List<String> maps;

	static {
		paint_text.setARGB(255, 255, 255, 255);
		paint_text.setAntiAlias(true);
		paint_text.setTextSize(GUIFunctions.getTextSizeToFit("CONS TRU TION XXXX", ICON_WIDTH));

	}


	public SelectLevelModule() {
		super(2, paint_text, Statics.img_cache.getImage("button_blue", ICON_WIDTH, Statics.SCREEN_WIDTH/20), 0, false, "", true);

		this.setBackground(Statics.BACKGROUND_IMAGE);

		maps = MapLoader.GetMaps();//new File(Statics.MAP_DIR).list();
		
		//ImageCache.Save();
	}


	@Override
	public void getOptions() {
		this.addOption("Random");
		for (int i=0 ; i<maps.size() ; i++) {
			this.addOption(maps.get(i));
		}

	}


	@Override
	public void optionSelected(int idx) {
		GameModule game = null;
		if (idx <= 0) {
			game = new GameModule(null);
		} else {
			game = new GameModule(maps.get(idx-1));
		}
		this.getThread().setNextModule(game);
	}


	@Override
	public boolean onBackPressed() {
		Statics.act.thread.setNextModule(new SelectGameModeModule());
		return true;
	}


}
