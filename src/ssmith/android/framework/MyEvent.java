package ssmith.android.framework;

import java.awt.event.MouseEvent;

import com.scs.multiplayerplatformer.Statics;

public class MyEvent {
	
	private MouseEvent mouseEvent;
	
	public MyEvent(MouseEvent me) {
		this.mouseEvent = me;
	}
	
	
	public String toString() {
		return "A:" + getAction() + " X:" + getX() + " Y:" + getY();
	}
	
	
	public int getAction() {
		return this.mouseEvent.getID();
	}
	
	
	public float getX() {
		return mouseEvent.getX();
	}


	public float getY() {
		if (!Statics.FULL_SCREEN) {
			return mouseEvent.getY()-Statics.WINDOW_TOP_OFFSET;
		}
		return mouseEvent.getY();
	}


}
