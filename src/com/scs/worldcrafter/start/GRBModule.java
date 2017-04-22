package com.scs.worldcrafter.start;

import ssmith.android.framework.AbstractActivity;
import ssmith.android.framework.MyEvent;
import ssmith.android.framework.modules.AbstractModule;
import ssmith.android.lib2d.shapes.BitmapRectangle;

import com.scs.ninja.main.lite.R;
import com.scs.worldcrafter.Statics;

public class GRBModule extends AbstractModule {
	
	private BitmapRectangle logo;
	private long end_time;
	
	public GRBModule(AbstractActivity act, AbstractModule _return_to) {
		super(act, _return_to);
		
		logo = new BitmapRectangle("Logo", Statics.img_cache.getImage(R.drawable.grb, Statics.SCREEN_WIDTH, Statics.SCREEN_HEIGHT), 0, 0);
		this.root_node.attachChild(logo);
		this.root_node.updateGeometricState();
		
		this.root_cam.lookAt(Statics.SCREEN_WIDTH/2, Statics.SCREEN_HEIGHT/2, true);
		
		end_time = System.currentTimeMillis() + (1000 * 4);
		
	}

	
	@Override
	public boolean processEvent(MyEvent evt) throws Exception {
		// Do nothing!
		return true;
	}

	
	@Override
	public void updateGame(long interpol) {
		if (System.currentTimeMillis() > end_time) {
			returnTo();
		}
	}
	
	
}

