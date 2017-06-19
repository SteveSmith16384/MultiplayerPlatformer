package ssmith.android.framework.modules;

import ssmith.android.compatibility.Paint;
import ssmith.android.framework.IProgressDisplay;
import ssmith.android.lib2d.gui.Label;
import ssmith.android.lib2d.gui.MultiLineLabel;
import ssmith.util.IDisplayText;

import com.scs.multiplayerplatformer.Statics;

public abstract class AbstractPleaseWaitModule extends SimpleAbstractModule implements IDisplayText, IProgressDisplay {

	private Label label3;
	private MultiLineLabel logLabel;
	
	private static Paint paint_large_text = new Paint();
	private static Paint paint_normal_text = new Paint();

	static {
		paint_large_text.setARGB(255, 255, 255, 255);
		paint_large_text.setAntiAlias(true);
		//paint_large_text.setStyle(Style.STROKE);
		//paint_large_text.setTextSize(Statics.GetHeightScaled(0.09f));

		paint_normal_text.setARGB(255, 255, 255, 255);
		paint_normal_text.setAntiAlias(true);
		//paint_normal_text.setTextSize(Statics.GetHeightScaled(0.05f));
	}
	
	
	public AbstractPleaseWaitModule() {
		super();

		Label l = new Label("Title", Statics.act.getString("please_wait"), 0, 0, null, paint_large_text, true);
		l.setCentre(Statics.SCREEN_WIDTH/2, paint_large_text.getTextSize());
		this.statNodeFront.attachChild(l);
		
		logLabel = new MultiLineLabel("display", "", null, paint_normal_text, true, Statics.SCREEN_WIDTH * 0.8f);
		logLabel.setLocation(Statics.SCREEN_WIDTH*0.1f, Statics.SCREEN_HEIGHT * 0.35f);
		statNodeFront.attachChild(logLabel);
		
		label3 = new Label("progress", "", null, paint_normal_text);
		label3.setCentre(Statics.SCREEN_WIDTH/2, Statics.SCREEN_HEIGHT * 0.9f);
		statNodeFront.attachChild(label3);
		
		this.statNodeFront.updateGeometricState();

		this.statCam.lookAt(Statics.SCREEN_WIDTH/2, Statics.SCREEN_HEIGHT/2, true);

	}


	@Override
	public void displayText(String s) {
		logLabel.appendText(s + "\n");
	}


	@Override
	public void displayProgress(String s) {
		label3.setText(s);
		
	}

}
