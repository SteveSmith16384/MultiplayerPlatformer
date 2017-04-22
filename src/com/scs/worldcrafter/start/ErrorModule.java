package com.scs.worldcrafter.start;

import ssmith.android.framework.AbstractActivity;
import ssmith.android.framework.modules.AbstractModule;
import ssmith.android.framework.modules.AbstractSingleScreenModule;
import ssmith.android.lib2d.gui.Label;
import ssmith.android.lib2d.gui.MultiLineLabel;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import com.scs.ninja.main.lite.R;
import com.scs.worldcrafter.Statics;

public final class ErrorModule extends AbstractSingleScreenModule {
	
	private static Paint paint_large_text = new Paint();
	private static Paint paint_normal_text = new Paint();

	static {
		paint_large_text.setARGB(255, 255, 255, 255);
		paint_large_text.setAntiAlias(true);
		//paint_large_text.setStyle(Style.STROKE);
		//paint_large_text.setTextSize(28);
		paint_large_text.setTextSize(Statics.GetHeightScaled(0.09f)); // Was 28

		paint_normal_text.setARGB(255, 255, 255, 255);
		paint_normal_text.setAntiAlias(true);
		//paint_large_text.setStyle(Style.STROKE);
	}
	
	
	public ErrorModule(AbstractActivity act, AbstractModule _return_to, Throwable t) {
		this(act, _return_to, "Error", t.toString());
	}

	
	public ErrorModule(AbstractActivity act, AbstractModule _return_to, String title, String text) {
		super(act, _return_to);
		
		if (Statics.GAME_MODE == Statics.GM_WORLDCRAFTER) {
			background = Statics.img_cache.getImage(R.drawable.menu_background, Statics.SCREEN_WIDTH, Statics.SCREEN_HEIGHT);
		}

		showError(title, text);

	}
	
	
	private void showError(String title, String text) {
		Label l = new Label("Title", title, 0, 0, null, paint_large_text, true);
		l.setCentre(Statics.SCREEN_WIDTH/2, paint_large_text.getTextSize());
		this.stat_node_front.attachChild(l);

		paint_normal_text.setTextSize(Statics.GetHeightScaled(0.05f));

		MultiLineLabel label2 = new MultiLineLabel("credits", text, null, paint_normal_text, true, Statics.SCREEN_WIDTH * 0.9f);
		label2.setLocation(10, Statics.SCREEN_HEIGHT/2);
		stat_node_front.attachChild(label2);
		
		this.stat_node_front.updateGeometricState();
	}

}

