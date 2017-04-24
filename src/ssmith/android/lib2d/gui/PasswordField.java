package ssmith.android.lib2d.gui;

import java.awt.image.BufferedImage;

import ssmith.android.compatibility.Canvas;
import ssmith.android.compatibility.Paint;
import ssmith.android.lib2d.Camera;

public class PasswordField extends AbstractTextComponent {

	private String pwd;

	/**
	 * Use this when using a layout.
	 */
	public PasswordField(String name, String text, Paint paint, Paint ink, int max_len, BufferedImage bmp) {
		this(name, text, 0, 0, bmp.getWidth(), bmp.getHeight(), paint, ink, max_len, bmp);
	}


	public PasswordField(String name, String text, float x, float y, float w, float h, Paint paint, Paint ink, BufferedImage bmp) {
		this(name, text, x, y, w, h, paint, ink, -1, bmp);
	}


	public PasswordField(String name, String text, float x, float y, float w, float h, Paint paint, Paint ink, int maxlen, BufferedImage bmp) {
		super(name, "", text, x, y, w, h, paint, ink, maxlen, bmp, false);
	}


	@Override
	protected void drawText(Canvas g, Camera cam) {
		if (this.visible) {
			if (str.length() > 0) {
				float tmp_offset = y_offset;
				//for (int i=0 ; i<lines.length ; i++) {
				g.drawText(pwd, super.getScreenX(cam)+ x_offset[0], super.getScreenY(cam) + tmp_offset, ink);
				tmp_offset += y_offset;
				//}
			}
		}

	}


	@Override
	protected void checkText() {
		super.checkText();

		String act_pwd = lines[0];
		pwd = "";
		if (act_pwd.length() >= 2) {
			for (int i=0 ; i<act_pwd.length()-1 ; i++) {
				pwd = pwd + "*";
			}
		}
		if (act_pwd.length() > 0) {
			pwd = pwd + act_pwd.substring(act_pwd.length()-1);
		}
	}


}
