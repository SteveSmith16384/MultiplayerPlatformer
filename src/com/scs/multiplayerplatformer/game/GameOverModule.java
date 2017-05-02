package com.scs.multiplayerplatformer.game;

import java.awt.image.BufferedImage;

import ssmith.android.compatibility.Paint;
import ssmith.android.framework.AbstractActivity;
import ssmith.android.framework.modules.SimpleAbstractModule;
import ssmith.android.lib2d.gui.AbstractComponent;
import ssmith.android.lib2d.gui.Button;
import ssmith.android.lib2d.gui.GUIFunctions;
import ssmith.android.lib2d.gui.Label;
import ssmith.android.lib2d.gui.MultiLineLabel;
import ssmith.android.lib2d.layouts.GridLayout;

import com.scs.multiplayerplatformer.Statics;


/**
 * This is the main game loop
 *
 */
public final class GameOverModule extends SimpleAbstractModule {

	private static String RESTART;// = "Replay Map";
	private static String RETURN;// = "Return";

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
		paint_ink.setTextSize(GUIFunctions.GetTextSizeToFit("Replay Map", Statics.SCREEN_WIDTH/3));
	}


	public GameOverModule(AbstractActivity act, GameModule _game, String reason) {
		super(act, _game);

		game = _game;

		this.setBackground(Statics.BACKGROUND_R);

		RESTART = act.getString("replay_map");
		RETURN = act.getString("main_menu");

		Label l = new Label("Game_Over_title", act.getString("game_over"), null, paint_large_text);
		l.updateGeometricState();
		l.setCentre(Statics.SCREEN_WIDTH/2, paint_large_text.getTextSize());
		this.stat_node_front.attachChild(l);

		MultiLineLabel ml = new MultiLineLabel("Game_Over_Comment", reason, null, paint_ink, true, Statics.SCREEN_WIDTH*0.9f);
		ml.setCentre(Statics.SCREEN_WIDTH/2, Statics.SCREEN_HEIGHT/2);
		this.stat_node_front.attachChild(ml);

		GridLayout menu_node = new GridLayout("Menu", Statics.SCREEN_WIDTH/3, Statics.SCREEN_WIDTH/10, 10);
		BufferedImage bmp = Statics.img_cache.getImage("button_blue", Statics.SCREEN_WIDTH/3, Statics.SCREEN_WIDTH/10);
		menu_node.attachChild(new Button(RESTART, RESTART, null, paint_ink, bmp), 0, 0);
		menu_node.attachChild(new Button(RETURN, RETURN, null, paint_ink, bmp), 1, 0);
		menu_node.updateGeometricState();
		menu_node.setCentre(Statics.SCREEN_WIDTH/2, Statics.SCREEN_HEIGHT * 0.75f);
		this.stat_node_front.attachChild(menu_node);

		stat_node_front.updateGeometricState();

		this.stat_cam.lookAt(stat_node_front, true);

	}


	@Override
	public void handleClick(AbstractComponent c) throws Exception {
		AbstractActivity act = Statics.act;

		if (c.getActionCommand().equalsIgnoreCase(RETURN)) {
			this.getThread().setNextModule(Statics.GetStartupModule(act));
		} else {
			//game.loadPlayer();
			this.getThread().setNextModule(new GameModule(act, game.level));
		}
	}


}

