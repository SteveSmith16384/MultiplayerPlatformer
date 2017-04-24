package com.scs.worldcrafter.start;

import ssmith.android.compatibility.Paint;
import ssmith.android.framework.AbstractActivity;
import ssmith.android.framework.modules.AbstractModule;
import ssmith.android.framework.modules.AbstractSingleScreenModule;
import ssmith.android.lib2d.gui.Label;
import ssmith.android.lib2d.gui.MultiLineLabel;

import com.scs.worldcrafter.Statics;

public final class AboutModule extends AbstractSingleScreenModule {
	
	private static Paint paint_large_text = new Paint();
	private static Paint paint_normal_text = new Paint();

	static {
		paint_large_text.setARGB(255, 255, 255, 255);
		paint_large_text.setAntiAlias(true);
		//paint_large_text.setStyle(Style.STROKE);
		paint_large_text.setTextSize(Statics.GetHeightScaled(0.09f));

		paint_normal_text.setARGB(255, 255, 255, 255);
		paint_normal_text.setAntiAlias(true);
		paint_normal_text.setTextSize(Statics.GetHeightScaled(0.05f));
		
	}
	
	
	public AboutModule(AbstractActivity act, AbstractModule _return_to) {
		super(act, _return_to);
		
		background = Statics.img_cache.getImage("menu_background", Statics.SCREEN_WIDTH, Statics.SCREEN_HEIGHT);

		/*todo - this Label l = new Label("Credits_title", act.getString(R.string.about) + " " + act.getString(R.string.app_name), null, paint_large_text);
		l.setCentre(Statics.SCREEN_WIDTH/2, paint_large_text.getTextSize());
		this.stat_node_front.attachChild(l);
*/
		StringBuffer str = new StringBuffer();
	//	str.append(act.getString(R.string.app_name) + " " + act.getString(R.string.about_text) + " " + Statics.EMAIL);

		MultiLineLabel label2 = new MultiLineLabel("credits", str.toString(), null, paint_normal_text, true, Statics.SCREEN_WIDTH * 0.9f);
		label2.setCentre(Statics.SCREEN_WIDTH/2, Statics.SCREEN_HEIGHT/2);
		stat_node_front.attachChild(label2);
		
		this.stat_node_front.updateGeometricState();
		

	}


}
