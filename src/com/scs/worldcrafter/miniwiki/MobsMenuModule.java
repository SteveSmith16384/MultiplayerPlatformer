package com.scs.worldcrafter.miniwiki;

import java.util.ArrayList;

import ssmith.android.framework.AbstractActivity;
import ssmith.android.framework.modules.AbstractModule;
import ssmith.android.framework.modules.AbstractOptionsModule;
import ssmith.android.lib2d.gui.GUIFunctions;
import ssmith.android.lib2d.gui.Label;
import ssmith.android.util.ImageCache;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import com.scs.ninja.main.lite.R;
import com.scs.worldcrafter.Statics;

public class MobsMenuModule extends AbstractOptionsModule {

	private static final float ICON_WIDTH = Statics.SCREEN_WIDTH/4;

	private static Paint paint_large_text = new Paint();
	private static Paint paint_normal_text = new Paint();

	static {
		paint_large_text.setARGB(255, 0, 0, 0);
		paint_large_text.setAntiAlias(true);
		//paint_large_text.setStyle(Style.STROKE);
		paint_large_text.setTextSize(Statics.GetHeightScaled(0.09f));

		paint_large_text.setARGB(255, 0, 0, 0);
		paint_normal_text.setAntiAlias(true);
	}

	public static ImageCache img_cache;

	public MobsMenuModule(AbstractActivity act, AbstractModule _return_to) {
		super(act, _return_to, 3, paint_normal_text, Statics.img_cache.getImage(R.drawable.button_blue, ICON_WIDTH, Statics.SCREEN_WIDTH/10), false);

		background = Statics.img_cache.getImage(R.drawable.parchment, Statics.SCREEN_WIDTH, Statics.SCREEN_HEIGHT);

		img_cache = new ImageCache(act.getResources());

		Label l = new Label("Credits_title", "Other Mobiles", null, paint_large_text);
		l.setCentre(Statics.SCREEN_WIDTH/2, paint_large_text.getTextSize());
		this.stat_node_front.attachChild(l);

		paint_normal_text.setTextSize(Statics.GetHeightScaled(0.05f));

		this.stat_node_front.updateGeometricState();

		paint_normal_text.setTextSize(GUIFunctions.GetTextSizeToFit("Killer Wasps", ICON_WIDTH));

	}


	@Override
	public void stopped() {
		//img_cache.clear();
	}


	@Override
	public ArrayList<String> getOptions() {
		AbstractActivity act = Statics.act;
		
		ArrayList<String> s = new ArrayList<String>();//String s[] = new String[6];
		s.add(act.getString(R.string.zombie));
		s.add(act.getString(R.string.wasp));
		s.add(act.getString(R.string.skeleton));
		s.add(act.getString(R.string.sheep));
		s.add(act.getString(R.string.pig));
		s.add(act.getString(R.string.chicken));
		s.add(act.getString(R.string.cow));
		s.add(act.getString(R.string.return_text));
		return s;
	}


	@Override
	public void optionSelected(String cmd) {
		AbstractActivity act = Statics.act;
		
		String text = act.getString(R.string.text_not_found);
		Bitmap bmp = null;
		if (cmd.equalsIgnoreCase(act.getString(R.string.zombie))) {
			bmp = img_cache.getImageByKey_WidthOnly(R.drawable.zombie_l0, Statics.SCREEN_WIDTH * .2f);
			text = act.getString(R.string.desc_zombies);
		} else if (cmd.equalsIgnoreCase(act.getString(R.string.skeleton))) {
			bmp = img_cache.getImageByKey_WidthOnly(R.drawable.skeleton_l0, Statics.SCREEN_WIDTH * .14f);
			text = act.getString(R.string.desc_skeletons);
		} else if (cmd.equalsIgnoreCase(act.getString(R.string.wasp))) {
			bmp = img_cache.getImageByKey_WidthOnly(R.drawable.wasp_l1, Statics.SCREEN_WIDTH * .2f);
			text = act.getString(R.string.desc_wasps);
		} else if (cmd.equalsIgnoreCase(act.getString(R.string.sheep))) {
			bmp = img_cache.getImageByKey_WidthOnly(R.drawable.sheep_l, Statics.SCREEN_WIDTH * .2f);
			text = act.getString(R.string.desc_sheep);
		} else if (cmd.equalsIgnoreCase(act.getString(R.string.pig))) {
			bmp = img_cache.getImageByKey_WidthOnly(R.drawable.pig_l, Statics.SCREEN_WIDTH * .2f);
			text = act.getString(R.string.desc_pigs);
		} else if (cmd.equalsIgnoreCase(act.getString(R.string.chicken))) {
			bmp = img_cache.getImageByKey_WidthOnly(R.drawable.chicken_l, Statics.SCREEN_WIDTH * .2f);
			text = act.getString(R.string.desc_chicken);
		} else if (cmd.equalsIgnoreCase(act.getString(R.string.cow))) {
			bmp = img_cache.getImageByKey_WidthOnly(R.drawable.cow_l, Statics.SCREEN_WIDTH * .2f);
			text = act.getString(R.string.desc_cow);
		} else {
			super.returnTo();// super.returnToPrevModule();
		}

		this.getThread().setNextModule(new DetailsModule(act, this, bmp, cmd, text));
	}

}
