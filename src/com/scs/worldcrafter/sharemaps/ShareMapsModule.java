package com.scs.worldcrafter.sharemaps;

import java.util.ArrayList;

import ssmith.android.framework.AbstractActivity;
import ssmith.android.framework.modules.AbstractModule;
import ssmith.android.framework.modules.AbstractOptionsModule;
import ssmith.android.lib2d.gui.GUIFunctions;
import android.graphics.Paint;

import com.scs.ninja.main.lite.R;
import com.scs.worldcrafter.Statics;

public class ShareMapsModule extends AbstractOptionsModule {
	
	private static final float ICON_WIDTH = Statics.SCREEN_WIDTH * 0.4f;

	public static Paint paint_text = new Paint();

	static {
		paint_text.setARGB(255, 255, 255, 255);
		paint_text.setAntiAlias(true);
		paint_text.setTextSize(GUIFunctions.GetTextSizeToFit("CONSTRUCTION", ICON_WIDTH));

	}

	
	public ShareMapsModule(AbstractActivity _act, AbstractModule _return_to) {
		super(_act, _return_to, 1, paint_text, Statics.img_cache.getImage(R.drawable.button_blue, ICON_WIDTH, Statics.SCREEN_WIDTH/10), false);

		background = Statics.img_cache.getImage(R.drawable.menu_background, Statics.SCREEN_WIDTH, Statics.SCREEN_HEIGHT);
	}


	@Override
	public ArrayList<String> getOptions() {
		AbstractActivity act = Statics.act;
		
		ArrayList<String> s = new ArrayList<String>();
		s.add(act.getString(R.string.download_maps));
		s.add(act.getString(R.string.upload_maps));
		return s;
	}


	@Override
	public void optionSelected(String cmd) {
		AbstractActivity act = Statics.act;
		
		if (cmd.equalsIgnoreCase(act.getString(R.string.download_maps))) {
			//todo - this.getThread().setNextModule(new DownloadMapsModule(this.act, this));
		} else {
			this.getThread().setNextModule(new SelectMapToUploadModule(act, this));
		}
	}

}
