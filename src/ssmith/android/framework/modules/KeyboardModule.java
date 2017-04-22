package ssmith.android.framework.modules;

import ssmith.android.framework.AbstractActivity;
import ssmith.android.io.IOFunctions;
import ssmith.android.lib2d.gui.AbstractComponent;
import ssmith.android.lib2d.gui.AbstractTextComponent;
import ssmith.android.lib2d.gui.Button;
import ssmith.android.lib2d.gui.GUIFunctions;
import ssmith.android.lib2d.gui.Keyboard;
import ssmith.android.lib2d.gui.Label;
import ssmith.android.lib2d.gui.PasswordField;
import ssmith.android.lib2d.layouts.FlowGridLayout;
import ssmith.android.lib2d.layouts.VerticalFlowLayout;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import com.scs.ninja.main.lite.R;
import com.scs.worldcrafter.Statics;

public class KeyboardModule extends SimpleAbstractModule {
	
	private Keyboard keyboard;
	private AbstractTextComponent lbl_text;
	private Button finished, del;
	
	private static Paint paint_normal_text = new Paint();
	static {
		paint_normal_text.setARGB(255, 255, 255, 255);
		paint_normal_text.setAntiAlias(true);
		//paint_normal_text.setStyle(Style.STROKE);
		paint_normal_text.setTextSize(GUIFunctions.GetTextSizeToFit("QWERTYUIOPQWERTYUIOP", Statics.SCREEN_WIDTH));
	}

	public KeyboardModule(AbstractActivity act, AbstractModule _return_to, int r, boolean mask, String text, boolean show_symbols) {
		super(act, _return_to);
		
		//return_to = _return_to;
		
		if (mask) {
			lbl_text = new PasswordField("Text", text, 0, 0, Statics.SCREEN_HEIGHT/2, Statics.SCREEN_HEIGHT/10, null, paint_normal_text, 999, null);	
		} else {
			lbl_text = new Label("Text", text, Statics.SCREEN_HEIGHT/2, Statics.SCREEN_HEIGHT/10, null, paint_normal_text, false);	
		}
		
		keyboard = new Keyboard(Statics.img_cache, r, paint_normal_text, Statics.SCREEN_WIDTH, Statics.SCREEN_HEIGHT/2, show_symbols); //keyboard.local_coords
		
		Bitmap bmp = Statics.img_cache.getImage(r, Statics.SCREEN_WIDTH/4, Statics.SCREEN_HEIGHT/10);
		FlowGridLayout extra = new FlowGridLayout("Extras", bmp.getWidth(), bmp.getHeight(), 5, 10);
		finished = new Button(act.getString(R.string.kbd_enter), null, paint_normal_text, bmp);
		del = new Button(act.getString(R.string.kbd_del), null, paint_normal_text, bmp);
		extra.attachChild(finished);
		extra.attachChild(del);

		
		VerticalFlowLayout layout = new VerticalFlowLayout("VFL", 5);
		layout.attachChild(lbl_text); // lbl_text.getHeight()
		layout.attachChild(keyboard); // keyboard.getHeight() 
		layout.attachChild(extra); // extra.getHeight()
		this.stat_node_front.attachChild(layout);
		
		this.stat_node_front.updateGeometricState();
		
		this.stat_cam.lookAt(layout, true);
	}


	@Override
	public void handleClick(AbstractComponent c) throws Exception {
		AbstractActivity act = Statics.act;
		
		IOFunctions.Vibrate(act.getBaseContext(), Statics.VIBRATE_LEN);
		
		Object cmd = c.getActionCommand();
		if (cmd.toString().length() == 1) {
			this.lbl_text.appendText(cmd.toString());
		} else if (c == del) {
			String curr = this.lbl_text.getText();
			if (curr.length() > 0) {
				this.lbl_text.setText(curr.substring(0, curr.length()-1));
			}
		} else if (c == finished) {
			String code = Statics.data.get("code").toString();
			Statics.data.clear();
			Statics.data.put("code", code);
			Statics.data.put("text", this.lbl_text.getText());
			//this.getThread().setNextModule(return_to);
			this.returnTo();
		}
		
	}

	
}
