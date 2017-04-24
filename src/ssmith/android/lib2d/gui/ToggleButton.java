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

	private RectF temp_rect2 = new RectF();
	private boolean selected = false;
	private Paint deselected_background_paint_and_ink;
	private BufferedImage deselected_bmp;

	public ToggleButton(String _text, Paint paint, Paint _deselected_background_paint, Paint ink, BufferedImage bmp, BufferedImage _deselected_bmp) {
		super(_text, paint, ink, bmp);

		deselected_background_paint_and_ink = _deselected_background_paint;
		deselected_bmp = _deselected_bmp;
	}


	public ToggleButton(String cmd, String _text, float x, float y, Paint paint, Paint _deselected_background_paint, Paint ink, BufferedImage bmp, BufferedImage _deselected_bmp) {
		super(cmd + "_Btn", cmd, _text, x, y, paint, ink, bmp);

		deselected_background_paint_and_ink = _deselected_background_paint;
		deselected_bmp = _deselected_bmp;
	}


	public static void SetAll(ArrayList<Spatial> children, boolean b) {
		for (Spatial s : children) {
			if (s instanceof ToggleButton) {
				ToggleButton t = (ToggleButton)s;
				t.setSelected(b);
			} else if (s instanceof Node) {
				Node n = (Node)s;
				SetAll(n.getChildren(), b);
			}
		}
	}


	@Override
	public void doDraw(Canvas g, Camera cam, long interpol) {
		if (selected) {
			super.doDraw(g, cam, interpol);
		} else {
			if (this.visible) {
				if (this.deselected_bmp == null) {
					temp_rect2.set(this.world_bounds.left - cam.left, this.world_bounds.top - cam.top, this.world_bounds.right - cam.left, this.world_bounds.bottom - cam.top);
					g.drawRect(temp_rect2, deselected_background_paint_and_ink);
				} else {
					g.drawBitmap(this.deselected_bmp, this.world_bounds.left - cam.left, this.world_bounds.top - cam.top, deselected_background_paint_and_ink);
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
