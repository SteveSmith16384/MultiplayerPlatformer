package ssmith.android.lib2d.gui;

import java.awt.image.BufferedImage;

import ssmith.android.compatibility.Paint;
import ssmith.android.lib2d.shapes.Rectangle;

public class AbstractComponent extends Rectangle {
	
	protected String action_command;
	//public Object tag;

	public AbstractComponent(String name, String cmd, float x, float y, float w, float h, Paint paint, BufferedImage bmp) {
		super(name, x, y, w, h, paint, bmp);
		
		action_command = cmd;

	}

	
	public String getActionCommand() {
		return this.action_command;
	}

}
