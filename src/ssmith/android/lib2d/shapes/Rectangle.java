package ssmith.android.lib2d.shapes;

import java.awt.image.BufferedImage;

import ssmith.android.compatibility.Canvas;
import ssmith.android.compatibility.Paint;
import ssmith.android.compatibility.RectF;
import ssmith.android.lib2d.Camera;

import com.scs.multiplayerplatformer.Statics;

public class Rectangle extends AbstractRectangle {

	private RectF temp_rect = new RectF();
	protected BufferedImage bmp_background;


	public Rectangle() {
		this("Temp_Rect", null, null);
	}


	public Rectangle(String name, Paint paint, BufferedImage _bmp) {
		this(name, 0, 0, 0, 0, paint, _bmp);
	}


	public Rectangle(String name, float x, float y, float w, float h, Paint paint, BufferedImage _bmp) {
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
			if (this.needsUpdating) {
				throw new RuntimeException(this.name + " needs updating!");
			}
		}
		if (this.visible) {
			if (bmp_background == null) {
				if (paint != null) {
					temp_rect.set(this.worldBounds.left - cam.left, this.worldBounds.top - cam.top, this.worldBounds.right - cam.left, this.worldBounds.bottom - cam.top);
					g.drawRect(temp_rect, paint);
				}
			} else {
				g.drawBitmap(bmp_background, this.worldBounds.left - cam.left, this.worldBounds.top - cam.top, paint);
			}
		}
	}


}

