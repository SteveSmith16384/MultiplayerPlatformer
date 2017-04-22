package ssmith.android.lib2d.shapes;

import com.scs.worldcrafter.Statics;

import ssmith.android.lib2d.Camera;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class BitmapRectangle extends AbstractRectangle {

	public Bitmap bmp;

	public BitmapRectangle(String name, Bitmap _bmp, float x, float y) {
		super(name, null, x, y, _bmp.getWidth(), _bmp.getHeight());

		bmp = _bmp;
	}

	
	@Override
	public void doDraw(Canvas g, Camera cam, long interpol) {
		if (bmp != null) {
			if (this.visible) {
				if (Statics.RELEASE_MODE == false) {
					if (bmp.isRecycled()) {
						throw new RuntimeException("Bitmap " + this.name + " has been recycled!");
					}
				}
				g.drawBitmap(bmp, this.world_bounds.left - cam.left, this.world_bounds.top - cam.top, paint);
			}
		}
	}

}
