package ssmith.android.lib2d.gui;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import ssmith.android.compatibility.Canvas;
import ssmith.android.compatibility.Paint;
import ssmith.android.compatibility.RectF;
import ssmith.android.lib2d.Camera;
import ssmith.android.lib2d.Node;
import ssmith.android.lib2d.Spatial;

public class ToggleButton extends Button {

	private RectF tempRect = new RectF();
	private boolean selected = false;
	private Paint deselectedBackgroundPaint;
	private BufferedImage deselectedBMP;

	public ToggleButton(String _text, Paint paint, Paint _deselected_background_paint, Paint ink, BufferedImage bmp, BufferedImage _deselected_bmp) {
		super(_text, paint, ink, bmp);

		deselectedBackgroundPaint = _deselected_background_paint;
		deselectedBMP = _deselected_bmp;
	}


	public ToggleButton(String cmd, String _text, float x, float y, Paint paint, Paint _deselected_background_paint, Paint ink, BufferedImage bmp, BufferedImage _deselected_bmp) {
		super(cmd + "_Btn", cmd, _text, x, y, paint, ink, bmp);

		deselectedBackgroundPaint = _deselected_background_paint;
		deselectedBMP = _deselected_bmp;
	}


	public static void setAll(ArrayList<Spatial> children, boolean b) {
		for (Spatial s : children) {
			if (s instanceof ToggleButton) {
				ToggleButton t = (ToggleButton)s;
				t.setSelected(b);
			} else if (s instanceof Node) {
				Node n = (Node)s;
				setAll(n.getChildren(), b);
			}
		}
	}


	@Override
	public void doDraw(Canvas g, Camera cam, long interpol) {
		if (selected) {
			super.doDraw(g, cam, interpol);
		} else {
			if (this.visible) {
				if (this.deselectedBMP == null) {
					tempRect.set(this.worldBounds.left - cam.left, this.worldBounds.top - cam.top, this.worldBounds.right - cam.left, this.worldBounds.bottom - cam.top);
					g.drawRect(tempRect, deselectedBackgroundPaint);
				} else {
					g.drawBitmap(this.deselectedBMP, this.worldBounds.left - cam.left, this.worldBounds.top - cam.top, deselectedBackgroundPaint);
				}
				super.drawText(g, cam);
				/*if (str.length() > 0) {
					//g.drawText(str.toString(), super.getScreenX(cam)+5, super.getScreenY(cam)+20, deslected_background_paint_and_ink);
					g.drawText(str.toString(), super.getScreenX(cam)+ x_offset, super.getScreenY(cam) + y_offset, deselected_background_paint_and_ink);

				}*/


			}
		}
	}


	public void toggeSelected() {
		this.selected = !this.selected;
	}


	public boolean isSelected() {
		return this.selected;
	}


	public void setSelected(boolean b) {
		this.selected = b;
	}

}
