package ssmith.android.lib2d.layouts;

import java.util.ArrayList;

import com.scs.worldcrafter.Statics;

import ssmith.android.lib2d.Node;
import ssmith.android.lib2d.shapes.BitmapRectangle;
import ssmith.android.lib2d.shapes.Geometry;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.Log;

public class BigBitmapGridLayout extends Node {
	
	private static Paint default_paint = new Paint();

	private float tile_size;
	public static int num_bmps;
	
	
	static {
		default_paint.setARGB(255, 255, 255, 255);
		default_paint.setAntiAlias(true);
		default_paint.setStyle(Style.STROKE);
	}


	public BigBitmapGridLayout(String name, float _tile_size) {
		super(name);
		
		tile_size = _tile_size;
	}

	
	public void attachChild(Bitmap bmp, float pxl_x, float pxl_y) {
		ArrayList<Geometry> colls = this.getCollidersAt(pxl_x, pxl_y);
		BitmapRectangle rect;
		if (colls.size() > 0) {
			rect = (BitmapRectangle)colls.get(0);
		} else {
			int sx = (int)(((int)(pxl_x / tile_size)) * tile_size);
			int sy = (int)(((int)(pxl_y / tile_size)) * tile_size);
			Bitmap tile_bmp = Bitmap.createBitmap((int)tile_size, (int)tile_size, Bitmap.Config.RGB_565);
			rect = new BitmapRectangle("Bmp_tile", tile_bmp, sx, sy);//, tile_size, tile_size);
			this.attachChild(rect);
			rect.parent.updateGeometricState();
			num_bmps++;
			Log.d(Statics.NAME, num_bmps + " bitmaps created.");
		}
		Canvas c = new Canvas(rect.bmp);
		float left = pxl_x % rect.getWorldX(); // rect.getWorldX() % pxl_x
		float top = pxl_y % rect.getWorldY();
		c.drawBitmap(bmp, left, top, default_paint);
	}
	
	
}
