package ssmith.android.framework.modules;

import ssmith.android.lib2d.Node;
import ssmith.android.lib2d.gui.GUIFunctions;
import ssmith.android.lib2d.gui.MultiLineLabel;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import com.scs.worldcrafter.Statics;

public class PleaseWaitDialog extends Node {

	private static final float ICON_WIDTH = Statics.SCREEN_WIDTH/2;

	public static Paint paint_menu_text = new Paint();
	private static Paint paint_small_text = new Paint();
	private static Paint paint_paper = new Paint();

	static {
		paint_menu_text.setARGB(255, 255, 255, 255);
		paint_menu_text.setAntiAlias(true);
		//paint_menu_text.setStyle(Style.STROKE);
		paint_menu_text.setTextSize(GUIFunctions.GetTextSizeToFit("Play GameXX GameXX", ICON_WIDTH));

		paint_small_text.setARGB(255, 255, 255, 255);
		paint_small_text.setAntiAlias(true);
		//paint_small_text.setStyle(Style.STROKE);
		paint_small_text.setTextSize(Statics.SCREEN_HEIGHT * 0.05f);

		paint_paper.setARGB(135, 0, 0, 0);
		paint_paper.setAntiAlias(true);
		paint_paper.setStyle(Style.FILL);
	}


	public PleaseWaitDialog(String msg) {
		super("PleaseWaitDialog");

		/*float new_size = GUIFunctions.GetTextSizeToFit(msg, Statics.SCREEN_WIDTH * 0.75f);
		if (new_size < paint_menu_text.getTextSize()) {
			paint_menu_text.setTextSize(new_size);
		}*/
		MultiLineLabel l = new MultiLineLabel("pw", msg, paint_paper, paint_menu_text, true, Statics.SCREEN_WIDTH * 0.75f);
		l.setCentre(Statics.SCREEN_WIDTH/2, Statics.SCREEN_HEIGHT * .75f);
		this.attachChild(l);

		this.updateGeometricState();

	}

}
