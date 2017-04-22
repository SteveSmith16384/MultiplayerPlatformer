package ssmith.android.lib2d;

import android.graphics.Paint;
import android.graphics.Paint.Style;

public class Lib2DStatics {
	
	public static final boolean DEBUG_GFX = false;

	public static Paint paint_red_line = new Paint();

	static {
		paint_red_line.setARGB(255, 255, 0, 0);
		paint_red_line.setAntiAlias(true);
		paint_red_line.setStyle(Style.STROKE);
	}
}
