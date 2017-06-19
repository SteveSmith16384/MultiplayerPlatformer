package ssmith.android.lib2d;

import ssmith.android.compatibility.PointF;
import ssmith.android.compatibility.RectF;
import ssmith.lang.GeometryFuncs;

import com.scs.multiplayerplatformer.Statics;

public class Camera extends RectF {

	//public static final long CAM_UPDATE = 0;
	//private static final float MOVE_SPEED = .1f;
	private static final float LOCKON_DIST = 20; // Was 10

	private PointF targetPoint = new PointF(); // Where we're aiming at
	private boolean lockToTarget; // if false, camera slides to the target
	private boolean moving = false; // If not locked, are we actually moving?
	private PointF actualPoint = new PointF(); // What we're currently looking at

	public Camera() {
		super();
	}

	
	public PointF getActualCentre() {
		return this.actualPoint;
	}
	

	public void moveCam(float offx, float offy) {
		this.actualPoint.x += offx;
		this.actualPoint.y += offy;

		lockToTarget = true;

		this.updateWindow();
	}


	public void lookAtTopLeft(boolean lock) {
		this.lookAt(Statics.SCREEN_WIDTH/2, Statics.SCREEN_HEIGHT/2, lock);
	}
	
	
	public void lookAt(float x, float y, boolean lock) {
		lockToTarget = lock;
		if (lock) {
			actualPoint.x = x;
			actualPoint.y = y;
			moving = false;
			this.updateWindow();
		} else {
			targetPoint.x = x;
			targetPoint.y = y;
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
		if (lockToTarget == false) {
			if (moving) {
				float dist = (float)GeometryFuncs.distance(actualPoint.x, actualPoint.y, targetPoint.x, targetPoint.y);
				if (dist <= LOCKON_DIST) {
					lookAt(targetPoint.x, targetPoint.y, true);
				} else {
					float off_x = 0;//Math.wrong(target_point.x - actual_point.x) * MOVE_SPEED * interpol;//  * dist;
					float off_y = 0;//Math.wrong(target_point.y - actual_point.y) * MOVE_SPEED * interpol;//   * dist;
					actualPoint.x += off_x;//(target_point.x + actual_point.x)/2;
					actualPoint.y += off_y;//= (target_point.y + actual_point.y)/2;
					this.updateWindow();
				}
			}
		}
	}
	
	
	private void updateWindow() {
		this.left = actualPoint.x - ((Statics.SCREEN_WIDTH/2));
		this.top = actualPoint.y - ((Statics.SCREEN_HEIGHT/2));
		this.right = actualPoint.x + ((Statics.SCREEN_WIDTH/2));
		this.bottom = actualPoint.y + ((Statics.SCREEN_HEIGHT/2));
	}
	
}
