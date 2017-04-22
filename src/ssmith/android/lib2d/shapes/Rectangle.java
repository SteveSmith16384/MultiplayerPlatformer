package ssmith.android.lib2d.shapes;

import ssmith.android.lib2d.Camera;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.scs.worldcrafter.Statics;

public class Rectangle extends AbstractRectangle {

	private RectF temp_rect = new RectF();
	protected Bitmap bmp_background;


	public Rectangle() {
		this("Temp_Rect", null, null);
	}


	public Rectangle(String name, Paint paint, Bitmap _bmp) {
		this(name, 0, 0, 0, 0, paint, _bmp);
	}


	public Rectangle(String name, float x, float y, float w, float h, Paint paint, Bitmap _bmp) {
		super(name, paint);

		local_rect.left = x;
		local_rect.top = y;
		local_rect.right = x+w;
		local_rect.bottom = y+h;

		bmp_background = _bmp;

		this.updateGeometricState();

	}


	@Override
	public void doDraw(Canvas g, Camera cam, long interpol) {
		if (Statics.RELEASE_MODE == false) {
			if (this.needs_updating) {
				throw new RuntimeException(this.name + " needs updating!");
			}
			if (bmp_background != null) {
				if (bmp_background.isRecycled()) {
					throw new RuntimeException(this.name + " has a recycled bitmap!");
				}				
			}
		}
		if (Statics.RELEASE_MODE) {
			if (bmp_background != null) {
				if (bmp_background.isRecycled()) {
					return; // Don't error
				}
			}
		}

		if (this.visible) {
			if (bmp_background == null) {
				if (paint != null) {
					temp_rect.set(this.world_bounds.left - cam.left, this.world_bounds.top - cam.top, this.world_bounds.right - cam.left, this.world_bounds.bottom - cam.top);
					g.drawRect(temp_rect, paint);
				}
			} else {
				g.drawBitmap(bmp_background, this.world_bounds.left - cam.left, this.world_bounds.top - cam.top, paint);
			}
		}
	}


}

