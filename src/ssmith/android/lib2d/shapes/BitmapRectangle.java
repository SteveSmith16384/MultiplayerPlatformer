package ssmith.android.lib2d.shapes;

import java.awt.image.BufferedImage;

import ssmith.android.compatibility.Canvas;
import ssmith.android.lib2d.Camera;

public class BitmapRectangle extends AbstractRectangle {

	public BufferedImage bmp;

	public BitmapRectangle(String name, BufferedImage _bmp, float x, float y) {
		super(name, null, x, y, _bmp.getWidth(), _bmp.getHeight());

		bmp = _bmp;
	}

	
	@Override
	public void doDraw(Canvas g, Camera cam, long interpol) {
		if (bmp != null) {
			if (this.visible) {
				g.drawBitmap(bmp, this.world_bounds.left - cam.left, this.world_bounds.top - cam.top, paint);
			}
		}
	}

}
