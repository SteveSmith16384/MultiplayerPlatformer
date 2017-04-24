package ssmith.android.lib2d.gui;

import java.awt.image.BufferedImage;

import ssmith.android.compatibility.Paint;

public class Button extends AbstractTextComponent {
	
	/**
	 * Use this one when using a layout
	 * @param cmd
	 * @param _text
	 * @param paint
	 */
	public Button(String _text, Paint paint, Paint ink, BufferedImage bmp) {
		this(_text + "_Btn", _text, _text, 0, 0, paint, ink, bmp);
	}
	
	
	/**
	 * Use this one when using a layout
	 * @param cmd
	 * @param _text
	 * @param paint
	 */
	public Button(String cmd, String _text, Paint paint, Paint ink, BufferedImage bmp) {
		this(cmd + "_Btn", cmd, _text, 0, 0, paint, ink, bmp);
	}

	
	public Button(String cmd, String _text, float x, float y, Paint paint, Paint ink, BufferedImage bmp) {
		this(cmd + "_Btn", cmd, _text, x, y, paint, ink, bmp);
	}
	
	
	public Button(String name, String cmd, String _text, float x, float y, Paint paint, Paint ink, BufferedImage bmp) {
		super(name, cmd, _text, x, y, bmp.getWidth(), bmp.getHeight(), paint, ink, bmp, true);
	}
	
	
}
