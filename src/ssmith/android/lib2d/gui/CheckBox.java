package ssmith.android.lib2d.gui;

import ssmith.android.lib2d.Camera;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.scs.ninja.main.lite.R;
import com.scs.worldcrafter.Statics;

public class CheckBox extends Button {
	
	private boolean ticked;
	private Bitmap bmp;

	public CheckBox(String text, Paint paint, Paint ink, Bitmap bmp, boolean _ticked) {
		super(text, paint, ink, bmp);
		
		this.setChecked(_ticked);
		
	}
	
	
	public void toggle() {
		this.setChecked(!ticked);
	}
	
	
	public void setChecked(boolean b) {
		ticked = b;
		if (ticked) {
			bmp = Statics.img_cache.getImage(R.drawable.tick, this.bmp_background.getHeight(), this.bmp_background.getHeight());
		} else {
			bmp = Statics.img_cache.getImage(R.drawable.cross, this.bmp_background.getHeight(), this.bmp_background.getHeight());
		}

	}

	
	@Override
	public void doDraw(Canvas g, Camera cam, long interpol) {
		super.doDraw(g, cam, interpol);
		this.drawTick(g, cam);
	}


	protected void drawTick(Canvas g, Camera cam) {
		if (this.visible) {
			g.drawBitmap(bmp, super.getScreenX(cam) + this.local_rect.width() -  this.ink.getTextSize(), super.getScreenY(cam), ink);
		}

	}
	
	
	public boolean isChecked() {
		return this.ticked;
	}

}
