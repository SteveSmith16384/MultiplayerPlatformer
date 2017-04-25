package com.scs.multiplayerplatformer.start;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import ssmith.android.compatibility.MotionEvent;
import ssmith.android.compatibility.Paint;
import ssmith.android.compatibility.Style;
import ssmith.android.framework.AbstractActivity;
import ssmith.android.framework.MyEvent;
import ssmith.android.framework.modules.AbstractModule;
import ssmith.android.lib2d.gui.AbstractComponent;
import ssmith.android.lib2d.gui.Button;
import ssmith.android.lib2d.gui.GUIFunctions;
import ssmith.android.lib2d.gui.Label;
import ssmith.android.lib2d.layouts.GridLayout;
import ssmith.android.lib2d.shapes.BitmapRectangle;
import ssmith.android.lib2d.shapes.Geometry;

import com.scs.multiplayerplatformer.Statics;

public final class StartupModule extends AbstractModule {

	private static Paint paint_large_text = new Paint();
	private static Paint paint_normal_text = new Paint();
	private static Paint paint_button_text = new Paint();
	private static Paint paint_free_text = new Paint();

	static {
		paint_large_text.setARGB(255, 255, 255, 255);
		paint_large_text.setAntiAlias(true);
		paint_large_text.setTextSize(Statics.GetHeightScaled(0.12f)); // Was 28

		paint_button_text.setARGB(255, 0, 0, 0);
		paint_button_text.setAntiAlias(true);
		paint_button_text.setTextSize(GUIFunctions.GetTextSizeToFit("Start GameXXXXX", Statics.SCREEN_WIDTH/3));
		paint_button_text.setStyle(Style.FILL);

		paint_free_text.setARGB(255, 255, 255, 255);
		paint_free_text.setAntiAlias(true);
		paint_free_text.setTextSize(GUIFunctions.GetTextSizeToFit("Start GameXXStart GameXX", Statics.SCREEN_WIDTH/2));

		paint_normal_text.setARGB(255, 255, 255, 255);
		paint_normal_text.setAntiAlias(true);
		paint_normal_text.setTextSize(paint_button_text.getTextSize()/2);

	}


	public StartupModule(AbstractActivity act) {
		super(act, null);

		background = Statics.img_cache.getImage(Statics.BACKGROUND_R, Statics.SCREEN_WIDTH, Statics.SCREEN_HEIGHT);

	}


	@Override
	public void started() {
		AbstractActivity act = Statics.act;

		// Create initial menu
		this.stat_node_front.detachAllChildren();

		BitmapRectangle logo = new BitmapRectangle("Logo", Statics.img_cache.getImage("ninja_logo", Statics.SCREEN_WIDTH * 0.8f, Statics.SCREEN_HEIGHT/5), 0, 0); // logo
		logo.setCentre(Statics.SCREEN_WIDTH/2, Statics.SCREEN_HEIGHT * .28f);
		this.stat_node_front.attachChild(logo);

		BufferedImage bmp_mf_blue = Statics.img_cache.getImage("button_blue", Statics.SCREEN_WIDTH/3, Statics.SCREEN_WIDTH/10);

		GridLayout menu_node = new GridLayout("Menu", Statics.SCREEN_WIDTH/3, Statics.SCREEN_WIDTH/10, 10);

		menu_node.attachChild(new Button(act.getString("start_game"), null, paint_button_text, bmp_mf_blue), 0, 0);
		menu_node.attachChild(new Button(act.getString("settings"), null, paint_button_text, bmp_mf_blue), 0, 1);
		menu_node.attachChild(new Button(act.getString("quit"), null, paint_button_text, bmp_mf_blue), 1, 1);

		menu_node.updateGeometricState();

		menu_node.setCentre(Statics.SCREEN_WIDTH/2, Statics.SCREEN_HEIGHT * 0.7f);
		this.stat_node_front.attachChild(menu_node);

		BitmapRectangle logo2 = new BitmapRectangle("Logo2", Statics.img_cache.getImage("penultimate_logo", Statics.SCREEN_WIDTH * 0.2f, Statics.SCREEN_HEIGHT/10), 0, 0);
		logo2.setCentre(Statics.SCREEN_WIDTH/2, Statics.SCREEN_HEIGHT * .95f);
		this.stat_node_front.attachChild(logo2); // logo

		Label label2 = new Label("Version", act.getString("version") + " " + Statics.VERSION_NAME, null, paint_normal_text);
		label2.setLocation(5, Statics.SCREEN_HEIGHT - (paint_normal_text.getTextSize()*2));
		stat_node_front.attachChild(label2);

		stat_node_front.updateGeometricState();

		this.stat_cam.lookAt(Statics.SCREEN_WIDTH/2, Statics.SCREEN_HEIGHT/2, true);
	}


	@Override
	public void updateGame(long interpol) {
		// Do nothing
	}


	@Override
	public boolean processEvent(MyEvent ev) throws IOException {
		AbstractActivity act = Statics.act;

		if (ev.getAction() == MotionEvent.ACTION_UP) {
			// Adjust for camera location
			float x = ev.getX() + stat_cam.left;
			float y = ev.getY() + this.stat_cam.top;
			ArrayList<Geometry> colls = this.stat_node_front.getCollidersAt(x, y);
			if (colls.size() > 0) {
				for (Geometry g : colls) {
					if (g instanceof AbstractComponent) {
						AbstractComponent b = (AbstractComponent)g;
						String cmd = b.getActionCommand();
						if (cmd.length() > 0) { // In case it's a textbox or something
							if (cmd.equalsIgnoreCase(act.getString("start_game"))) {
								startNewGame();
								return true;
							} else if (cmd.equalsIgnoreCase(act.getString("quit"))) {
								// todo
								return true;
							} else {
								throw new RuntimeException("Unknown command: '" + cmd + "'");
							}
						}
					}
				}
			}
		}	
		return false;		
	}


	private void startNewGame() {
		AbstractActivity act = Statics.act;

		//Statics.los_to_see_monsters = false;
		Statics.player_loses_health = false;
		Statics.has_timer = false;
		SelectLevelModule game = new SelectLevelModule(act, this);
		this.getThread().setNextModule(game);

	}


	@Override
	public boolean onBackPressed() {
		// todo - exit?
		//act.finish();
		return true;
	}


}
