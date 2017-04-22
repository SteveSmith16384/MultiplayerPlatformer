package com.scs.worldcrafter.game;

import ssmith.android.framework.AbstractActivity;
import ssmith.android.framework.modules.SimpleAbstractModule;
import ssmith.android.lib2d.gui.AbstractComponent;
import ssmith.android.lib2d.gui.Button;
import ssmith.android.lib2d.gui.GUIFunctions;
import ssmith.android.lib2d.gui.Label;
import ssmith.android.lib2d.gui.MultiLineLabel;
import ssmith.android.lib2d.layouts.GridLayout;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import com.scs.ninja.main.lite.R;
import com.scs.worldcrafter.Statics;

public class GameCompletedModule extends SimpleAbstractModule {
	
	private static String RESTART;// = "Replay Map";
	
	private static Paint paint_large_text = new Paint();
	private static Paint paint_ink = new Paint();
	
	private GameModule game;

	static {
		paint_large_text.setARGB(255, 255, 255, 255);
		paint_large_text.setAntiAlias(true);
		//paint_large_text.setStyle(Style.STROKE);
		paint_large_text.setTextSize(Statics.GetHeightScaled(0.1f));

		paint_ink.setARGB(255, 255, 255, 255);
		paint_ink.setAntiAlias(true);
		//paint_ink.setStyle(Style.STROKE);
		paint_ink.setTextSize(GUIFunctions.GetTextSizeToFit("Replay MapXXXX", Statics.SCREEN_WIDTH/3));
	}
	
	
	public GameCompletedModule(AbstractActivity _act, GameModule _game) {
		super(_act, _game);
		
		game = _game;
		
		RESTART = "Return";//act.getString(R.string.replay_map);
		
		if (Statics.GAME_MODE == Statics.GM_NINJA) {
			this.setBackground(R.drawable.ninja_background2);
		} else if (Statics.GAME_MODE == Statics.GM_POLICECOP) {
			this.setBackground(R.drawable.policecop_background);
		}
		
		Label l = new Label("Game_Over_title", "Game Completed!", null, paint_large_text);
		l.updateGeometricState();
		l.setCentre(Statics.SCREEN_WIDTH/2, paint_large_text.getTextSize());
		this.stat_node_front.attachChild(l);

		String str = "Congratulations!  You have completed the game!\n\n";
		if (Statics.FULL_VERSION) {
			str = str + "Look out for an update with more levels coming soon!";
		} else {
			str = str + "Please get the full version for more levels!  Thanks for supporting us!";
		}
		MultiLineLabel ml = new MultiLineLabel("Game_Over_Comment", str, null, paint_ink, true, Statics.SCREEN_WIDTH*0.9f);
		ml.setCentre(Statics.SCREEN_WIDTH/2, Statics.SCREEN_HEIGHT * .35f);
		this.stat_node_front.attachChild(ml);

		GridLayout menu_node = new GridLayout("Menu", Statics.SCREEN_WIDTH/3, Statics.SCREEN_WIDTH/10, 10);
		Bitmap bmp = Statics.img_cache.getImage(R.drawable.button_blue, Statics.SCREEN_WIDTH/3, Statics.SCREEN_WIDTH/10);
		menu_node.attachChild(new Button(RESTART, RESTART, null, paint_ink, bmp), 0, 0);
		//menu_node.attachChild(new Button(RETURN, RETURN, null, paint_ink, bmp), 1, 0);
		menu_node.updateGeometricState();
		menu_node.setCentre(Statics.SCREEN_WIDTH/2, Statics.SCREEN_HEIGHT * 0.75f);
		this.stat_node_front.attachChild(menu_node);

		stat_node_front.updateGeometricState();

		this.stat_cam.lookAt(stat_node_front, true);

	}


	@Override
	public void handleClick(AbstractComponent c) throws Exception {
		if (c.getActionCommand().equalsIgnoreCase(RESTART)) {
			this.getThread().setNextModule(Statics.GetStartupModule(Statics.act));
		} else {
			game.loadPlayer();
			this.getThread().setNextModule(game);
		}
	}


}

