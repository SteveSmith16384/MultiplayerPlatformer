package com.scs.worldcrafter.start;

import ssmith.android.framework.AbstractActivity;
import ssmith.android.framework.MyEvent;
import ssmith.android.framework.modules.AbstractModule;
import ssmith.android.lib2d.shapes.BitmapRectangle;

import com.scs.ninja.main.lite.R;
import com.scs.worldcrafter.Statics;

public final class IntroModule extends AbstractModule {
	
	private BitmapRectangle logo;
	
	public IntroModule(AbstractActivity act, AbstractModule _return_to) {
		super(act, _return_to);
		
		//background = Statics.img_cache.getImage(R.drawable.menu_background, Statics.SCREEN_WIDTH, Statics.SCREEN_HEIGHT);
		//logo = new BitmapRectangle("Logo", Statics.img_cache.getImageByKey_HeightOnly(R.drawable.worldcrafter_logo, Statics.SCREEN_HEIGHT), Statics.SCREEN_WIDTH, Statics.SCREEN_HEIGHT*.2f);
		logo = new BitmapRectangle("Logo", Statics.img_cache.getImageByKey_HeightOnly(R.drawable.worldcrafter_logo, Statics.SCREEN_HEIGHT), Statics.SCREEN_WIDTH, 0);
		this.root_node.attachChild(logo);
		this.root_node.updateGeometricState();
		
		this.root_cam.lookAt(Statics.SCREEN_WIDTH/2, Statics.SCREEN_HEIGHT/2, true);
		
		//Statics.img_cache.clear(); // Save memory
		//System.gc();
	}

	
	@Override
	public void started() {
		this.returnTo(); // Don't show intro at the moment.
	}
	
	
	@Override
	public void stopped() {
		Statics.img_cache.remove(R.drawable.worldcrafter_logo);
	}
	
	
	@Override
	public boolean processEvent(MyEvent evt) throws Exception {
		this.returnTo();
		return true;
	}

	
	@Override
	public void updateGame(long interpol) {
		logo.adjustLocation(-Statics.SCREEN_WIDTH/100f, 0);
		logo.parent.updateGeometricState();
		
		if (logo.getWorldBounds().right < 0) {
			returnTo();
		}
	}
	
	
}
