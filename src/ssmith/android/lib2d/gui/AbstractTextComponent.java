package ssmith.android.lib2d.gui;

import java.awt.image.BufferedImage;

import ssmith.android.compatibility.Canvas;
import ssmith.android.compatibility.Paint;
import ssmith.android.lib2d.Camera;

import com.scs.multiplayerplatformer.Statics;

public abstract class AbstractTextComponent extends AbstractComponent {

	private int max_length;
	protected StringBuffer str;
	protected Paint ink;
	protected float x_offset[], y_offset;
	private boolean centre;
	protected String lines[];

	public AbstractTextComponent(String name, String cmd, String _text, float x, float y, float w, float h, Paint paint, Paint _ink, BufferedImage bmp, boolean _centre) {
		this(name, cmd, _text, x, y, w, h, paint, _ink, -1, bmp, _centre);
	}


	public AbstractTextComponent(String name, String cmd, String _text, float x, float y, float w, float h, Paint paint, Paint _ink, int maxlen, BufferedImage bmp, boolean _centre) {
		super(name, cmd, x, y, w, h, paint, bmp);

		ink = _ink;
		str = new StringBuffer(_text);
		max_length = maxlen;
		centre = _centre;

		if (ink == null && _text.length() > 0) {
			throw new RuntimeException("Null ink for Button " + name);
		}

		this.checkText();
	}


	public void setInkRGB(int r, int g, int b) {
		ink.setARGB(ink.getAlpha(), r, g, b);
	}


	public void calcTextOffset() {
		if (lines != null) {
			String longest = "";
			for (int i=0 ; i<lines.length ; i++) {
				if (lines[i].length() > longest.length()) {
					longest = lines[i];
				}
				float len = ink.measureText(lines[i]);
				if (centre && lines[i].length() > 0) {
					x_offset[i] = (this.getWidth() - len) / 2;
				} else {
					//float len = ink.measureText(lines[i]);
					if (len >= Statics.SCREEN_WIDTH) {
						x_offset[i] = Statics.SCREEN_WIDTH-len;//10;
					} else {
						x_offset[i] = 10;
					}
				}
			}
			y_offset = (this.getHeight() / (lines.length+1)) + (ink.getTextSize()*.35f); // 68 / 2 + 11.75 = 45
			//y_offset = (ink.getTextSize()*.35f);
		}
	}


	public void setSize(float w, float h) {
		super.setSize(w, h);
		this.calcTextOffset();
	}


	@Override
	public void doDraw(Canvas g, Camera cam, long interpol) {
		super.doDraw(g, cam, interpol);
		this.drawText(g, cam);
	}


	protected void drawText(Canvas g, Camera cam) {
		if (this.visible) {
			if (str.length() > 0) {
				float tmp_offset = y_offset;
				for (int i=0 ; i<lines.length ; i++) {
					g.drawText(lines[i], super.getScreenX(cam) + x_offset[i], super.getScreenY(cam) + tmp_offset, ink);
					tmp_offset += y_offset;
				}
			}
		}

	}


	public void appendText(String s) {
		str.append(s);
		checkText();
	}


	public void append(String s) {
		appendText(s);
	}


	public void setText(String s) {
		str = new StringBuffer(s);
		checkText();
	}


	public void setActionCommand(String s) {
		this.actionCommand = s;
	}


	protected void checkText() {
		if (str.length() > 0) {
			if (this.max_length >= 0) {
				if (str.length() > this.max_length) {
					str.delete(this.max_length, this.str.length());
				}
			}
			lines = str.toString().split("\n");
			x_offset = new float[lines.length];
			this.calcTextOffset();
		}
	}


	public String getText() {
		return str.toString();
	}


	public void updateWidth() { // Don't do this automatically
		// Are we now wider?
		this.local_rect.right = this.local_rect.left + (ink.measureText(this.str.toString() + " "));
		this.updateGeometricState();
	}

}

