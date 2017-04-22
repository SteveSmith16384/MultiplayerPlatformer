package ssmith.android.lib2d.gui;

import android.graphics.Bitmap;
import android.graphics.Paint;

public class TextBox extends AbstractTextComponent {
	
	/**
	 * Use this when using a layout.
	 */
	public TextBox(String name, String text, Paint paint, Paint ink, int max_len, Bitmap bmp) {
		this(name, text, 0, 0, 1, 1, paint, ink, max_len, bmp);
	}
	
	
	public TextBox(String name, String text, float x, float y, float w, float h, Paint paint, Paint ink, Bitmap bmp) {
		this(name, text, x, y, w, h, paint, ink, -1, bmp);
	}
	
	
	public TextBox(String name, String text, float x, float y, float w, float h, Paint paint, Paint ink, int maxlen, Bitmap bmp) {
		super(name, "", text, x, y, w, h, paint, ink, maxlen, bmp, false);
	}

}
