package com.scs.multiplayerplatformer.start;

import ssmith.android.compatibility.Paint;
import ssmith.android.framework.AbstractActivity;
import ssmith.android.framework.modules.AbstractModule;
import ssmith.android.framework.modules.AbstractSingleScreenModule;
import ssmith.android.lib2d.gui.Label;
import ssmith.android.lib2d.gui.MultiLineLabel;

import com.scs.multiplayerplatformer.Statics;

public final class ErrorModule extends AbstractSingleScreenModule {
	
	private static Paint paint_large_text = new Paint();
	private static Paint paint_normal_text = new Paint();

	static {
		paint_large_text.setARGB(255, 255, 255, 255);
		paint_large_text.setAntiAlias(true);
		//paint_large_text.setStyle(Style.STROKE);
		//paint_large_text.setTextSize(28);
		//paint_large_text.setTextSize(Statics.GetHeightScaled(0.09f)); // Was 28

		paint_normal_text.setARGB(255, 255, 255, 255);
		paint_normal_text.setAntiAlias(true);
		//paint_large_text.setStyle(Style.STROKE);
	}
	
	
	public ErrorModule(Throwable t) {
		this("Error", t.toString());
	}

	
	public ErrorModule(String title, String text) {
		super();
		
		showError(title, text);

	}
	
	
	private void showError(String title, String text) {
		Label l = new Label("Title", title, 0, 0, null, paint_large_text, true);
		l.setCentre(Statics.SCREEN_WIDTH/2, paint_large_text.getTextSize());
		this.stat_node_front.attachChild(l);

		//paint_normal_text.setTextSize(Statics.GetHeightScaled(0.05f));

		MultiLineLabel label2 = new MultiLineLabel("credits", text, null, paint_normal_text, true, Statics.SCREEN_WIDTH * 0.9f);
		label2.setLocation(10, Statics.SCREEN_HEIGHT/2);
		stat_node_front.attachChild(label2);
		
		this.stat_node_front.updateGeometricState();
	}

}

