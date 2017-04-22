package ssmith.android.lib2d.gui;

import ssmith.android.lib2d.shapes.Rectangle;
import android.graphics.Bitmap;
import android.graphics.Paint;

public class AbstractComponent extends Rectangle {
	
	protected String action_command;
	//public Object tag;

	public AbstractComponent(String name, String cmd, float x, float y, float w, float h, Paint paint, Bitmap bmp) {
		super(name, x, y, w, h, paint, bmp);
		
		action_command = cmd;

	}

	
	public String getActionCommand() {
		return this.action_command;
	}

}
