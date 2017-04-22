package ssmith.android.lib2d;

import com.scs.worldcrafter.Statics;

import ssmith.lang.GeometryFuncs;
import android.graphics.PointF;
import android.graphics.RectF;

public class Camera extends RectF {

	private static final float LOCKON_DIST = 10;

	public int zoom = 1;
	private PointF target_point = new PointF(); // Where we're aiming at
	private boolean lock_to_target; // if false, camera slides to the target
	private boolean moving = false; // If not locked, are we actuall moving?
	private PointF actual_point = new PointF(); // What we're currently looking at

	public Camera() {
		super();
	}

	
	public PointF getActualCentre() {
		return this.actual_point;
	}
	

	public void moveCam(float offx, float offy) {
		this.actual_point.x += offx;
		this.actual_point.y += offy;

		lock_to_target = true;

		this.updateWindow();
	}


	public void lookAtTopLeft(boolean lock) {
		this.lookAt(Statics.SCREEN_WIDTH/2, Statics.SCREEN_HEIGHT/2, lock);
	}
	
	
	public void lookAt(float x, float y, boolean lock) {
		lock_to_target = lock;
		if (lock) {
			actual_point.x = x;
			actual_point.y = y;
			moving = false;
			this.updateWindow();
		} else {
			target_point.x = x;
			target_point.y = y;
			moving = true;
		}
	}

	
	public void lookAt(Spatial s, boolean lock) {
		this.lookAt(s.getWorldCentreX(), s.getWorldCentreY(), lock);
	}
	
	
	public void update(float interpol) {
		if (this.right - this.left <= 0) {
			//throw new Exception("Camera not looking at anything!");
		}
		if (lock_to_target == false) {
			if (moving) {
				float dist = (float)GeometryFuncs.distance(actual_point.x, actual_point.y, target_point.x, target_point.y);
				if (dist <= LOCKON_DIST) {
					lookAt(target_point.x, target_point.y, true);
				} else {
					actual_point.x = (target_point.x + actual_point.x)/2;
					actual_point.y = (target_point.y + actual_point.y)/2;
					this.updateWindow();
				}
			}
		}
	}
	
	
	private void updateWindow() {
		this.left = actual_point.x - ((Statics.SCREEN_WIDTH/2) / this.zoom);
		this.top = actual_point.y - ((Statics.SCREEN_HEIGHT/2) / this.zoom);
		this.right = actual_point.x + ((Statics.SCREEN_WIDTH/2) / this.zoom);
		this.bottom = actual_point.y + ((Statics.SCREEN_HEIGHT/2) / this.zoom);
	}
	
}
