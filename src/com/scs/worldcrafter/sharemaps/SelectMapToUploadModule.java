package com.scs.worldcrafter.sharemaps;

import java.io.File;
import java.util.ArrayList;

import ssmith.android.framework.AbstractActivity;
import ssmith.android.framework.modules.AbstractModule;
import ssmith.android.framework.modules.AbstractOptionsModule;
import ssmith.android.lib2d.gui.GUIFunctions;
import android.graphics.Paint;

import com.scs.ninja.main.lite.R;
import com.scs.worldcrafter.Statics;

public class SelectMapToUploadModule extends AbstractOptionsModule {

	public static Paint paint_text = new Paint();

	static {
		paint_text.setARGB(255, 255, 255, 255);
		paint_text.setAntiAlias(true);
		paint_text.setTextSize(GUIFunctions.GetTextSizeToFit("Cannot access SD card", Statics.SCREEN_WIDTH/2));

	}


	public SelectMapToUploadModule(AbstractActivity act, AbstractModule _return_to) {
		super(act, _return_to, 1, paint_text, Statics.img_cache.getImage(R.drawable.button_blue, Statics.SCREEN_WIDTH/2, Statics.SCREEN_WIDTH/10), false);

		background = Statics.img_cache.getImage(R.drawable.menu_background, Statics.SCREEN_WIDTH, Statics.SCREEN_HEIGHT);

	}


	@Override
	public ArrayList<String> getOptions() {
		AbstractActivity act = Statics.act;
		
		File dir = Statics.GetExtStorage();
		File files[] = dir.listFiles();

		if (files != null) {
			ArrayList<String> s = new ArrayList<String>();
			//s.add(act.getString(R.string.new_map));
			for (int i=0 ; i<files.length ; i++) {
				if (files[i].isDirectory() == false) {
					s.add(files[i].getName());
				}
			}
			if (s.size() == 0) {
				s.add("You have no saved maps.");
			}
			return s;
		} else {
			ArrayList<String> s = new ArrayList<String>();
			//s.add(act.getString(R.string.new_map));
			s.add(act.getString(R.string.cannot_access_sd_card));
			return s;
		}
	}


	@Override
	public void optionSelected(String cmd) {
		AbstractActivity act = Statics.act;
		
		String filename = cmd;
		// todo - check the file exists

		UploadMapModule mod = new UploadMapModule(act, this, filename);
		this.getThread().setNextModule(mod);

	}



}
