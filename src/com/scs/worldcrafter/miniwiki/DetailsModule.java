package com.scs.worldcrafter.miniwiki;

import ssmith.android.framework.AbstractActivity;
import ssmith.android.framework.modules.AbstractModule;
import ssmith.android.framework.modules.AbstractSingleScreenModule;
import ssmith.android.lib2d.gui.Button;
import ssmith.android.lib2d.gui.Label;
import ssmith.android.lib2d.gui.MultiLineLabel;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import com.scs.ninja.main.lite.R;
import com.scs.worldcrafter.Statics;

public class DetailsModule extends AbstractSingleScreenModule {

	private static Paint paint_large_text = new Paint();
	private static Paint paint_normal_text = new Paint();

	static {
		paint_large_text.setARGB(255, 0, 0, 0);
		paint_large_text.setAntiAlias(true);
		//paint_large_text.setStyle(Style.STROKE);
		paint_large_text.setTextSize(Statics.GetHeightScaled(0.09f)); // Was 28

		paint_normal_text.setARGB(255, 0, 0, 0);
		paint_normal_text.setAntiAlias(true);
	}


	public DetailsModule(AbstractActivity act, AbstractModule _return_to, Bitmap bmp, String name, String text) {
		super(act, _return_to);

		background = Statics.img_cache.getImage(R.drawable.parchment, Statics.SCREEN_WIDTH, Statics.SCREEN_HEIGHT);

		Label l = new Label("Title", name, null, paint_large_text);
		l.setCentre(Statics.SCREEN_WIDTH/2, paint_large_text.getTextSize());
		this.stat_node_front.attachChild(l);

		paint_normal_text.setTextSize(Statics.GetHeightScaled(0.05f));

		float w = 0.4f;
		if (bmp == null) {
			w = 0.8f;
		}
		MultiLineLabel label2 = new MultiLineLabel("desc", text, null, paint_normal_text, true, Statics.SCREEN_WIDTH * w);
		label2.setLocation(Statics.SCREEN_WIDTH * 0.1f, paint_large_text.getTextSize()*2);
		stat_node_front.attachChild(label2);

		//Bitmap bmp = Block.GetBitmap(_img_cache, block_id, Statics.SCREEN_WIDTH * .3f, Statics.SCREEN_WIDTH * .3f);
		if (bmp != null) {
			Button bt = new Button("", null, paint_normal_text, bmp);
			bt.setLocation(Statics.SCREEN_WIDTH * .6f, paint_large_text.getTextSize()*2);
			stat_node_front.attachChild(bt);
		}
		this.stat_node_front.updateGeometricState();

	}

}
