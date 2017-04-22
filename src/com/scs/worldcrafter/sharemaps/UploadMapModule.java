package com.scs.worldcrafter.sharemaps;

import ssmith.android.framework.AbstractActivity;
import ssmith.android.framework.modules.AbstractModule;
import ssmith.android.framework.modules.AbstractPleaseWaitModule;
import ssmith.android.io.IOFunctions;
import ssmith.android.io.WGet_Android_2;
import ssmith.android.lib2d.gui.AbstractComponent;

import com.scs.worldcrafter.Statics;

public class UploadMapModule extends AbstractPleaseWaitModule {

	private String filename;

	public UploadMapModule(AbstractActivity _act, AbstractModule _return_to, String _filename) {
		super(_act, _return_to);

		filename = _filename;

		start();
	}


	@Override
	public void run() {
		try {
			String s = IOFunctions.LoadText(filename);
			//String postdata = "name="
			WGet_Android_2 wg;
			wg = new WGet_Android_2(Statics.URL_FOR_CLIENT + "/worldcrafter/rcvmap.cls", s);
			if (wg.getResponseCode() == 200) {

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void handleClick(AbstractComponent c) throws Exception {

	}

}
