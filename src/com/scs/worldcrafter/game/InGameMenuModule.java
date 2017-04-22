package com.scs.worldcrafter.game;

import java.io.File;
import java.util.ArrayList;

import ssmith.android.framework.AbstractActivity;
import ssmith.android.framework.modules.AbstractOptionsModule;
import ssmith.android.framework.modules.KeyboardModule;
import ssmith.android.io.IOFunctions;
import ssmith.android.lib2d.gui.GUIFunctions;
import ssmith.android.lib2d.shapes.BitmapRectangle;
import android.graphics.Paint;

import com.scs.ninja.main.lite.R;
import com.scs.worldcrafter.Statics;
import com.scs.worldcrafter.crafting.MiniCraftingGuide;
import com.scs.worldcrafter.miniwiki.WikiModule;

public class InGameMenuModule extends AbstractOptionsModule {

	private static String SAVE_MAP, SAVE_MAP_AS;// = "Save Game";
	private static final String TOGGLE_DEBUGGING = "Debug";
	private static String GUIDE;// = "Guide";
	private static String CRAFTING;// = "Crafting";
	private static String RETURN;// = "Return";
	private static String ABANDON;// = "Return";

	private static final float ICON_WIDTH = Statics.SCREEN_WIDTH * 0.4f;

	public static Paint paint_text = new Paint();

	static {
		paint_text.setARGB(255, 255, 255, 255);
		paint_text.setAntiAlias(true);
		paint_text.setTextSize(GUIFunctions.GetTextSizeToFit("Save Game As", ICON_WIDTH));

	}


	public InGameMenuModule(AbstractActivity act, GameModule _return_to) {
		super(act, _return_to, 2, paint_text, Statics.img_cache.getImage(R.drawable.button_blue, ICON_WIDTH, Statics.SCREEN_WIDTH/10), false);

		SAVE_MAP = act.getString(R.string.save_game);
		SAVE_MAP_AS = act.getString(R.string.save_game_as);
		GUIDE = act.getString(R.string.guide);
		CRAFTING = act.getString(R.string.crafting);
		RETURN = act.getString(R.string.return_text);
		ABANDON = act.getString(R.string.abandon);

		if (Statics.GAME_MODE == Statics.GM_NINJA) {
			this.setBackground(R.drawable.ninja_background2);

			BitmapRectangle logo = new BitmapRectangle("Logo", Statics.img_cache.getImageByKey_WidthOnly(R.drawable.ninja_logo, Statics.SCREEN_WIDTH * 0.8f), 0, 0); // logo
			logo.setCentre(Statics.SCREEN_WIDTH/2, Statics.SCREEN_HEIGHT * .28f);
			this.stat_node_front.attachChild(logo);
		} else if (Statics.GAME_MODE == Statics.GM_POLICECOP) {
			this.setBackground(R.drawable.policecop_background);
		} else {
			this.setBackground(R.drawable.menu_background);
		}
	}


	@Override
	public void started() {
		// Has the keyboard come back to us with some text
		if (Statics.data.containsKey("code") && Statics.data.containsKey("text")) {
			String f = Statics.data.get("code").toString();
			if (f.equalsIgnoreCase("filename")) {
				String filename = Statics.data.get("text").toString();
				Statics.data.clear();
				GameModule game = (GameModule)super.mod_return_to;
				if (filename.length() > 0) {
					try {
						// Check the file can be created
						String act_filename = Statics.GetExtStorage() + "/" + filename + "_test";
						IOFunctions.SaveText(act_filename, "Test file", false);
						new File(act_filename).delete();
						game.filename = filename;
						game.saveMap();
					} catch (Exception ex) {
						//BugSenseHandler.log(Statics.NAME, ex);
						//AbstractActivity.HandleError(game.act, ex);
						//game.filename = "";
						showToast("Sorry, that is an invalid filename!");
					}

				}
				this.returnTo();//.returnToPrevModule();
			}
		}
	}


	@Override
	public ArrayList<String> getOptions() {
		ArrayList<String> s = new ArrayList<String>();//String s[] = new String[6];
		if (Statics.GAME_MODE == Statics.GM_WORLDCRAFTER) {
			s.add(SAVE_MAP);
			s.add(SAVE_MAP_AS);
			s.add(GUIDE);
			s.add(CRAFTING);
		}
		s.add(RETURN);
		s.add(ABANDON);
		return s;
	}


	@Override
	public void optionSelected(String cmd) {
		AbstractActivity act = Statics.act;
		
		if (cmd.equalsIgnoreCase(SAVE_MAP)) {
			GameModule game = (GameModule)super.mod_return_to;
			if (game.filename.length() > 0) {
				game.saveMap();
				this.returnTo();//.returnToPrevModule();
			} else {
				saveAs();
			}
		} else if (cmd.equalsIgnoreCase(SAVE_MAP_AS)) {
			saveAs();
		} else if (cmd.equalsIgnoreCase(TOGGLE_DEBUGGING)) {
			Statics.SHOW_STATS = !Statics.SHOW_STATS;
			this.returnTo();//.returnToPrevModule();
		} else if (cmd.equalsIgnoreCase(GUIDE)) {
			this.getThread().setNextModule(new WikiModule(act, this));
		} else if (cmd.equalsIgnoreCase(CRAFTING)) {
			this.getThread().setNextModule(new MiniCraftingGuide(act, this));
		} else if (cmd.equalsIgnoreCase(ABANDON)) {
			this.getThread().setNextModule(Statics.GetStartupModule(act));
		} else {
			this.returnTo();//.returnToPrevModule();
		}
	}


	private void saveAs() {
		AbstractActivity act = Statics.act;
		
		Statics.data.put("code", "filename");
		GameModule game = (GameModule)super.mod_return_to;
		KeyboardModule keyb = new KeyboardModule(act, this, R.drawable.button_blue, false, game.filename, false);
		this.getThread().setNextModule(keyb);
	}


}
