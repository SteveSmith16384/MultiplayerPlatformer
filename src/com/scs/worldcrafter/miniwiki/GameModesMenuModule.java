package com.scs.worldcrafter.miniwiki;

import java.util.ArrayList;

import ssmith.android.framework.AbstractActivity;
import ssmith.android.framework.modules.AbstractModule;
import ssmith.android.framework.modules.AbstractOptionsModule;
import ssmith.android.lib2d.gui.GUIFunctions;
import ssmith.android.lib2d.gui.Label;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import com.scs.ninja.main.lite.R;
import com.scs.worldcrafter.Statics;

public class GameModesMenuModule extends AbstractOptionsModule {

	//private String options[] = {"Construction", "Survival", "Farming", "Quest", "Race", "Return"};

	private static final float ICON_WIDTH = Statics.SCREEN_WIDTH/3;

	private static Paint paint_large_text = new Paint();
	private static Paint paint_normal_text = new Paint();

	static {
		paint_large_text.setARGB(255, 0, 0, 0);
		paint_large_text.setAntiAlias(true);
		paint_large_text.setStyle(Style.STROKE);
		//paint_large_text.setTextSize(28);
		paint_large_text.setTextSize(Statics.GetHeightScaled(0.09f)); // Was 28

		paint_normal_text.setARGB(255, 0, 0, 0);
		paint_normal_text.setAntiAlias(true);
		//paint_large_text.setStyle(Style.STROKE);
	}


	public GameModesMenuModule(AbstractActivity act, AbstractModule _return_to) {
		super(act, _return_to, 2, paint_normal_text, Statics.img_cache.getImage(R.drawable.button_blue, ICON_WIDTH, Statics.SCREEN_WIDTH/10), false);

		background = Statics.img_cache.getImage(R.drawable.parchment, Statics.SCREEN_WIDTH, Statics.SCREEN_HEIGHT);

		Label l = new Label("Credits_title", act.getString(R.string.game_modes), null, paint_large_text);
		l.setCentre(Statics.SCREEN_WIDTH/2, paint_large_text.getTextSize());
		this.stat_node_front.attachChild(l);

		paint_normal_text.setTextSize(Statics.GetHeightScaled(0.05f));

		this.stat_node_front.updateGeometricState();

		paint_normal_text.setTextSize(GUIFunctions.GetTextSizeToFit("Construction", ICON_WIDTH));

	}


	/*@Override
	public void doDraw(Canvas c, long interpol) {
		c.drawBitmap(this.background, 0, 0, paint_normal_text);
		super.doDraw(c, interpol);
	}*/


	@Override
	public ArrayList<String> getOptions() {
		AbstractActivity act = Statics.act;
		
		ArrayList<String> s = new ArrayList<String>();//String s[] = new String[6];
		s.add(act.getString(R.string.construction));
		s.add(act.getString(R.string.survival));
		s.add(act.getString(R.string.farming));
		s.add(act.getString(R.string.quest));
		s.add(act.getString(R.string.race));
		s.add(act.getString(R.string.return_text));
		return s;
	}


	@Override
	public void optionSelected(String cmd) {
		AbstractActivity act = Statics.act;
		
			String text = act.getString(R.string.text_not_found);// "Text not found";
			Bitmap bmp = null;
			if (cmd.equalsIgnoreCase(act.getString(R.string.construction))) {
				//bmp = this.img_cache.getImage(R.drawable.zombie_l0, Statics.SCREEN_WIDTH * .3f, Statics.SCREEN_WIDTH * .3f);
				text = act.getString(R.string.desc_construction);
			} else if (cmd.equalsIgnoreCase(act.getString(R.string.survival))) {
				//bmp = this.img_cache.getImage(R.drawable.wasp_l1, Statics.SCREEN_WIDTH * .3f, Statics.SCREEN_WIDTH * .3f);
				text = act.getString(R.string.desc_survival);
			} else if (cmd.equalsIgnoreCase(act.getString(R.string.farming))) {
				//bmp = this.img_cache.getImage(R.drawable.wasp_l1, Statics.SCREEN_WIDTH * .3f, Statics.SCREEN_WIDTH * .3f);
				text = act.getString(R.string.desc_farming);
			} else if (cmd.equalsIgnoreCase(act.getString(R.string.quest))) {
				bmp = Statics.img_cache.getImage(R.drawable.amulet, Statics.SCREEN_WIDTH * .3f, Statics.SCREEN_WIDTH * .3f);
				text = act.getString(R.string.desc_quest);
			} else if (cmd.equalsIgnoreCase(act.getString(R.string.race))) {
				bmp = Statics.img_cache.getImage(R.drawable.amulet, Statics.SCREEN_WIDTH * .3f, Statics.SCREEN_WIDTH * .3f);
				text = act.getString(R.string.desc_race);
			} else {
				super.returnTo();// super.returnToPrevModule();
			}

			this.getThread().setNextModule(new DetailsModule(act, this, bmp, cmd, text));
	}

}
