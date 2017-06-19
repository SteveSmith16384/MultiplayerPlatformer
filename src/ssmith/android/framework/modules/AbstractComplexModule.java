package ssmith.android.framework.modules;

import java.util.ArrayList;

import ssmith.android.compatibility.MotionEvent;
import ssmith.android.framework.MyEvent;
import ssmith.android.lib2d.MyPointF;
import ssmith.android.lib2d.shapes.Geometry;
import ssmith.lang.GeometryFuncs;

import com.scs.multiplayerplatformer.Statics;

/**
 * This module automatically handles dragging and clicking of icons.
 * 
 */
public abstract class AbstractComplexModule extends AbstractModule {

	private static final float MIN_DRAG_DIST = 10f;

	private boolean isDown = false;
	private MyPointF act_start_drag = new MyPointF();

	private boolean isDragging = false;
	protected boolean scrollLR = true;

	public AbstractComplexModule() {
		super();
	}


	@Override
	public boolean processEvent(MyEvent ev) throws Exception {
		//Log.d(Statics.NAME, "Action2: " + ev.getAction());
		//Log.d(Statics.NAME, "Coords2: " + ev.getY());

		if (ev.getAction() == MotionEvent.ACTION_DOWN && isDown == false) {
			isDown = true;
			act_start_drag.x = ev.getX();
			act_start_drag.y = ev.getY();

		} else if (ev.getAction() == MotionEvent.ACTION_MOVE) { // Dragging!
			float offx = act_start_drag.x - ev.getX();
			float offy = act_start_drag.y - ev.getY();
			double dist = GeometryFuncs.distance(0, 0, offx, offy);
			if (dist > MIN_DRAG_DIST || isDragging) {// && dist < MAX_DRAG_DIST) {//|| is_dragging) {
				if (scrollLR == false) {
					offx = 0;
				}
				this.rootCam.moveCam(offx, offy);
				if (this.rootNode.getHeight() > Statics.SCREEN_HEIGHT) {
					if (this.rootCam.top < this.rootNode.getWorldY()) {
						this.rootCam.moveCam(0, this.rootNode.getWorldY() - this.rootCam.top);
					} else if (this.rootCam.bottom > this.rootNode.getWorldBounds().bottom) {
						this.rootCam.moveCam(0, this.rootNode.getWorldBounds().bottom - this.rootCam.bottom);
					}
				}
				isDragging = true;
				act_start_drag.x = ev.getX();
				act_start_drag.y = ev.getY();
			}
		} else if (ev.getAction() == MotionEvent.ACTION_UP) {
			isDown = false;
			if (isDragging == false) { // Check for an icon pressed
				// Adjust for camera location
				float x = ev.getX() + statCam.left;
				float y = ev.getY() + this.statCam.top;

				ArrayList<Geometry> colls = this.statNodeFront.getCollidersAt(x, y);
				if (colls.size() > 0) {
					for (Geometry g : colls) {
						if (this.componentClicked(g)) {
							return true;
						}
					}
				}
				x = ev.getX() + rootCam.left;
				y = ev.getY() + rootCam.top;
				colls = this.rootNode.getCollidersAt(x, y);
				if (colls.size() > 0) {
					for (Geometry g : colls) {
						if (this.componentClicked(g)) {
							return true;
						}
					}
				}
			} else {
				isDragging = false;
			}
		}	
		return false;			
	}


	public abstract boolean componentClicked(Geometry c);

}

