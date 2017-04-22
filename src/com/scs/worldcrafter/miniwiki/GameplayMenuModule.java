package com.scs.worldcrafter.miniwiki;

import java.util.ArrayList;

import ssmith.android.framework.AbstractActivity;
import ssmith.android.framework.modules.AbstractModule;
import ssmith.android.framework.modules.AbstractOptionsModule;
import ssmith.android.lib2d.gui.GUIFunctions;
import ssmith.android.lib2d.gui.Label;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import com.scs.ninja.main.lite.R;
import com.scs.worldcrafter.Statics;

public class GameplayMenuModule extends AbstractOptionsModule {

	private static final float ICON_WIDTH = Statics.SCREEN_WIDTH/4;

	private static Paint paint_large_text = new Paint();
	private static Paint paint_normal_text = new Paint();

	static {
		paint_large_text.setARGB(255, 0, 0, 0);
		paint_large_text.setAntiAlias(true);
		//paint_large_text.setStyle(Style.STROKE);
		//paint_large_text.setTextSize(28);
		paint_large_text.setTextSize(Statics.GetHeightScaled(0.09f)); // Was 28

		paint_normal_text.setARGB(255, 0, 0, 0);
		paint_normal_text.setAntiAlias(true);
		//paint_large_text.setStyle(Style.STROKE);
	}


	public GameplayMenuModule(AbstractActivity act, AbstractModule _return_to) {
		super(act, _return_to, 3, paint_normal_text, Statics.img_cache.getImage(R.drawable.button_blue, ICON_WIDTH, Statics.SCREEN_WIDTH/10), false);

		background = Statics.img_cache.getImage(R.drawable.parchment, Statics.SCREEN_WIDTH, Statics.SCREEN_HEIGHT);

		Label l = new Label("Credits_title", act.getString(R.string.gameplay), null, paint_large_text);
		l.setCentre(Statics.SCREEN_WIDTH/2, paint_large_text.getTextSize());
		this.stat_node_front.attachChild(l);

		paint_normal_text.setTextSize(Statics.GetHeightScaled(0.05f));

		this.stat_node_front.updateGeometricState();

		paint_normal_text.setTextSize(GUIFunctions.GetTextSizeToFit("TANGLEWEED", ICON_WIDTH));

	}


	@Override
	public ArrayList<String> getOptions() {
		AbstractActivity act = Statics.act;

		ArrayList<String> s = new ArrayList<String>();//String s[] = new String[6];
		s.add(act.getString(R.string.game_modes));
		s.add(act.getString(R.string.throwing));
		s.add(act.getString(R.string.crafting));
		s.add(act.getString(R.string.health_bar));
		s.add(act.getString(R.string.saving));
		s.add(act.getString(R.string.return_text));
		return s;
	}


	@Override
	public void optionSelected(String cmd) {
		AbstractActivity act = Statics.act;

		String text = act.getString(R.string.text_not_found);
		if (cmd.equalsIgnoreCase(act.getString(R.string.game_modes))) {
			this.getThread().setNextModule(new GameModesMenuModule(act, this));
			return;
		} else if (cmd.equalsIgnoreCase(act.getString(R.string.throwing))) {
			text = act.getString(R.string.desc_throwing);
		} else if (cmd.equalsIgnoreCase(act.getString(R.string.health_bar))) {
			text = act.getString(R.string.desc_health_bar);
		} else if (cmd.equalsIgnoreCase(act.getString(R.string.saving))) {
			text = act.getString(R.string.desc_saving, Statics.NAME);
		} else if (cmd.equalsIgnoreCase(act.getString(R.string.crafting))) {
			text = act.getString(R.string.desc_crafting);
		} else {
			super.returnTo();// super.returnToPrevModule();
		}

		this.getThread().setNextModule(new DetailsModule(act, this, null, cmd, text));
	}

}
