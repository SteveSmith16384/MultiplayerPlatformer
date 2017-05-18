package ssmith.android.framework.modules;

import ssmith.android.compatibility.Paint;
import ssmith.android.framework.AbstractActivity;
import ssmith.android.framework.IProgressDisplay;
import ssmith.android.lib2d.gui.Label;
import ssmith.android.lib2d.gui.MultiLineLabel;
import ssmith.util.IDisplayText;

import com.scs.multiplayerplatformer.Statics;

public abstract class AbstractPleaseWaitModule extends SimpleAbstractModule implements IDisplayText, IProgressDisplay {

	private Label label3;
	private MultiLineLabel log_label;
	
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
	
	
	public AbstractPleaseWaitModule(AbstractActivity act, AbstractModule _return_to) {
		super(act, _return_to);

		Label l = new Label("Title", act.getString("please_wait"), 0, 0, null, paint_large_text, true);
		l.setCentre(Statics.SCREEN_WIDTH/2, paint_large_text.getTextSize());
		this.stat_node_front.attachChild(l);
		
		log_label = new MultiLineLabel("display", "", null, paint_normal_text, true, Statics.SCREEN_WIDTH * 0.8f);
		log_label.setLocation(Statics.SCREEN_WIDTH*0.1f, Statics.SCREEN_HEIGHT * 0.35f);
		stat_node_front.attachChild(log_label);
		
		label3 = new Label("progress", "", null, paint_normal_text);
		label3.setCentre(Statics.SCREEN_WIDTH/2, Statics.SCREEN_HEIGHT * 0.9f);
		stat_node_front.attachChild(label3);
		
		this.stat_node_front.updateGeometricState();

		this.stat_cam.lookAt(Statics.SCREEN_WIDTH/2, Statics.SCREEN_HEIGHT/2, true);

	}


	@Override
	public void displayText(String s) {
		log_label.appendText(s + "\n");
	}


	@Override
	public void displayProgress(String s) {
		label3.setText(s);
		
	}

}
