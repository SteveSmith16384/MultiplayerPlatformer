package ssmith.android.compatibility;

import java.awt.Color;

import com.scs.worldcrafter.Statics;

public class Paint {

	public Color color;
	public Typeface typeface = null;//Statics.stdfnt; // Default
	private float strokeWidth = 1f;
	public boolean antialiasing = true;

	public Paint() {
		typeface = Statics.stdfnt; // Default
	}


	public void setStyle(int i) {
	}


	public void setARGB(int a, int r, int g, int b) {
		color = new Color(r, g, b, a);
	}


	public void setAntiAlias(boolean a) {
		antialiasing = a;
	}


	public float getTextSize() {
		//if (this.typeface != null) { 
			return this.typeface.getSize();
		/*}
		return 10;*/
	}


	public void setTextSize(float s) {
	}


	public void setStrokeWidth(float w) {
		strokeWidth = w;
	}


	public float getStrokeWidth() {
		return strokeWidth;
	}


	public void setTypeface(Typeface t) {
		typeface = t;
	}


	public Typeface getTypeface() {
		return typeface; 
	}


	public float measureText(String s) {
		Canvas c = Statics.act.thread.c;
		if (c != null) {
			return c.getGraphics().getFontMetrics(this.getTypeface()).stringWidth(s);
		} else {
			return 100;
		}
	}


	public int breakText(String s, float maxWidth) {
		int pos = s.length();
		while (true)  {
			float len = this.measureText(s.substring(0, pos));
			if (len < maxWidth) {
				return pos;
			}
			pos = s.lastIndexOf(" ", pos-1);
		}
	}


	public int getAlpha() {
		return this.color.getAlpha();
	}


	public void setColor(Color c, int alpha255) {
		this.color = new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha255);
	}


	public void setColor(Color c) {
		this.color = c;
	}

}
