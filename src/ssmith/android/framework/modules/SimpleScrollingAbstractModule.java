package ssmith.android.framework.modules;

import ssmith.android.framework.AbstractActivity;
import ssmith.android.framework.MyEvent;
import ssmith.android.lib2d.gui.AbstractComponent;
import ssmith.lang.GeometryFuncs;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.scs.worldcrafter.Statics;

public abstract class SimpleScrollingAbstractModule extends AbstractModule {

	private static final float MIN_DRAG_DIST = Statics.SCREEN_HEIGHT/25;// Was 30, SCS 2/4/13

	private PointF last_down_screen = new PointF();
	private boolean is_dragging = false;

	public SimpleScrollingAbstractModule(AbstractActivity _act, AbstractModule _return_to) {
		super(_act, _return_to);
	}


	@Override
	public void updateGame(long interpol) {
		// Do nothing
	}


	@Override
	public boolean processEvent(MyEvent ev) throws Exception {
		if (ev.getAction() == MotionEvent.ACTION_UP) { // Note we only catch UP so we don't select two options at the same time!
			last_down_screen.y = ev.getY();

			if (is_dragging == false) { // Check for an icon pressed
				// Adjust for camera location
				float x = ev.getX() + stat_cam.left;
				float y = ev.getY() + this.stat_cam.top;

				AbstractComponent c = super.GetComponentAt(this.stat_node_front, x, y);
				if (c != null) {
					handleClick(c);
					return true;
				}
			} 
			is_dragging = false;
		} else if (ev.getAction() == MotionEvent.ACTION_MOVE) { // Dragging!
				float offy = last_down_screen.y - ev.getY();
				double dist = GeometryFuncs.distance(0, 0, 0, offy);
				if (dist > MIN_DRAG_DIST || is_dragging) {
					this.stat_cam.moveCam(0, offy);
					if (this.root_node.getHeight() > Statics.SCREEN_HEIGHT) {
						if (this.root_cam.top < this.root_node.getWorldY()) {
							this.root_cam.moveCam(0, this.root_node.getWorldY() - this.root_cam.top);
						} else if (this.root_cam.bottom > this.root_node.getWorldBounds().bottom) {
							this.root_cam.moveCam(0, this.root_node.getWorldBounds().bottom - this.root_cam.bottom);
						}
					}
					is_dragging = true;
				}
				last_down_screen.y = ev.getY();
		} else if (ev.getAction() == MotionEvent.ACTION_DOWN) { // Dragging!
			last_down_screen.y = ev.getY();
		}	
		return false;			
	}


	public abstract void handleClick(AbstractComponent c) throws Exception;


}
