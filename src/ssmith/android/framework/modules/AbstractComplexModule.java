package ssmith.android.framework.modules;

import java.util.ArrayList;

import ssmith.android.compatibility.MotionEvent;
import ssmith.android.framework.AbstractActivity;
import ssmith.android.framework.MyEvent;
import ssmith.android.lib2d.MyPointF;
import ssmith.android.lib2d.shapes.Geometry;
import ssmith.lang.GeometryFuncs;

import com.scs.worldcrafter.Statics;

/**
 * This module automatically handles dragging and clicking of icons.
 * 
 */
public abstract class AbstractComplexModule extends AbstractModule {

	private static final float MIN_DRAG_DIST = 10f;

	private boolean is_down = false;
	private MyPointF act_start_drag = new MyPointF();

	private boolean is_dragging = false;
	protected boolean scroll_lr = true;

	public AbstractComplexModule(AbstractActivity _act, AbstractModule _return_to) {
		super(_act, _return_to);
	}


	@Override
	public boolean processEvent(MyEvent ev) throws Exception {
		//Log.d(Statics.NAME, "Action2: " + ev.getAction());
		//Log.d(Statics.NAME, "Coords2: " + ev.getY());

		if (ev.getAction() == MotionEvent.ACTION_DOWN && is_down == false) {
			is_down = true;
			act_start_drag.x = ev.getX();
			act_start_drag.y = ev.getY();

		} else if (ev.getAction() == MotionEvent.ACTION_MOVE) { // Dragging!
			float offx = act_start_drag.x - ev.getX();
			float offy = act_start_drag.y - ev.getY();
			double dist = GeometryFuncs.distance(0, 0, offx, offy);
			if (dist > MIN_DRAG_DIST || is_dragging) {// && dist < MAX_DRAG_DIST) {//|| is_dragging) {
				if (scroll_lr == false) {
					offx = 0;
				}
				this.root_cam.moveCam(offx, offy);
				if (this.root_node.getHeight() > Statics.SCREEN_HEIGHT) {
					if (this.root_cam.top < this.root_node.getWorldY()) {
						this.root_cam.moveCam(0, this.root_node.getWorldY() - this.root_cam.top);
					} else if (this.root_cam.bottom > this.root_node.getWorldBounds().bottom) {
						this.root_cam.moveCam(0, this.root_node.getWorldBounds().bottom - this.root_cam.bottom);
					}
				}
				is_dragging = true;
				act_start_drag.x = ev.getX();
				act_start_drag.y = ev.getY();
			}
		} else if (ev.getAction() == MotionEvent.ACTION_UP) {
			is_down = false;
			if (is_dragging == false) { // Check for an icon pressed
				// Adjust for camera location
				float x = ev.getX() + stat_cam.left;
				float y = ev.getY() + this.stat_cam.top;

				ArrayList<Geometry> colls = this.stat_node_front.getCollidersAt(x, y);
				if (colls.size() > 0) {
					for (Geometry g : colls) {
						if (this.componentClicked(g)) {
							return true;
						}
					}
				}
				x = ev.getX() + root_cam.left;
				y = ev.getY() + root_cam.top;
				colls = this.root_node.getCollidersAt(x, y);
				if (colls.size() > 0) {
					for (Geometry g : colls) {
						if (this.componentClicked(g)) {
							return true;
						}
					}
				}
			} else {
				is_dragging = false;
			}
		}	
		return false;			
	}


	public abstract boolean componentClicked(Geometry c);

}

