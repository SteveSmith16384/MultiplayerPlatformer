package ssmith.android.lib2d.shapes;

import ssmith.android.lib2d.Camera;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class AnimatedBitmapRectangle extends AbstractRectangle {

	private long time_frame, curr_timer = 0;
	private int curr_image;
	private Bitmap[] images;

	public AnimatedBitmapRectangle(String name, Bitmap[] _images, long _time_frame, float x, float y, float w, float h) {
		super(name, null, x, y, w, h);

		time_frame = _time_frame;
		images = _images;
		curr_image = 0;
	}


	public Bitmap[] getImages() {
		return this.images;
		
	}
	
	
	@Override
	public void doDraw(Canvas g, Camera cam, long interpol) {
		if (images.length > 1 && this.time_frame > 0) {
			curr_timer -= interpol;
			if (curr_timer <= 0) {
				curr_timer = time_frame;
				this.curr_image++;
				if (curr_image >= images.length) {
					curr_image = 0;
				}
			}
		}
		if (this.visible) {
			Bitmap bmp = null;
			bmp = this.images[this.curr_image];
			g.drawBitmap(bmp, this.world_bounds.left - cam.left, this.world_bounds.top - cam.top, paint);
		}
	}


}
