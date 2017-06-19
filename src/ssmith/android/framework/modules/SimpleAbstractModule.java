package ssmith.android.framework.modules;

import ssmith.android.compatibility.MotionEvent;
import ssmith.android.framework.MyEvent;
import ssmith.android.lib2d.gui.AbstractComponent;


/**
 * This is for modules that have no dragging and use the 2D GUI.
 *
 */
public abstract class SimpleAbstractModule extends AbstractModule {
	
	public SimpleAbstractModule() {
		super();
	}
	
	
	@Override
	public void updateGame(long interpol) {
		// Do nothing
	}


	public boolean processEvent(MyEvent evt) throws Exception {
		if (evt.getAction() == MotionEvent.ACTION_UP) {
			// Adjust for camera location
			float x = evt.getX() + statCam.left;
			float y = evt.getY() + this.statCam.top;
			AbstractComponent c = super.GetComponentAt(this.statNodeFront, x, y);
			if (c != null) {
				handleClick(c);
				return true;
			}
		}
		return false;
	}
	
	
	public abstract void handleClick(AbstractComponent c) throws Exception;


}
