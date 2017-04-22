package ssmith.android.framework.modules;

import ssmith.android.framework.AbstractActivity;
import ssmith.android.framework.MyEvent;
import ssmith.android.lib2d.gui.AbstractComponent;
import android.view.MotionEvent;


/**
 * This is for modules that have no dragging and use the 2D GUI.
 *
 */
public abstract class SimpleAbstractModule extends AbstractModule {
	
	public SimpleAbstractModule(AbstractActivity _act, AbstractModule _return_to) {
		super(_act, _return_to);
	}
	
	
	@Override
	public void updateGame(long interpol) {
		// Do nothing
	}


	public boolean processEvent(MyEvent evt) throws Exception {
		if (evt.getAction() == MotionEvent.ACTION_UP) {
			// Adjust for camera location
			float x = evt.getX() + stat_cam.left;
			float y = evt.getY() + this.stat_cam.top;
			AbstractComponent c = super.GetComponentAt(this.stat_node_front, x, y);
			if (c != null) {
				handleClick(c);
				return true;
			}
		}
		return false;
	}
	
	
	public abstract void handleClick(AbstractComponent c) throws Exception;


}
