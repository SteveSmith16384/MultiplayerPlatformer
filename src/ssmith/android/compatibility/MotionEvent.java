package ssmith.android.compatibility;

import java.awt.event.MouseEvent;

public class MotionEvent {

	public static final int ACTION_DOWN = MouseEvent.MOUSE_PRESSED;
	public static final int ACTION_MOVE = MouseEvent.MOUSE_DRAGGED;
	public static final int ACTION_UP = MouseEvent.MOUSE_RELEASED;
	
	public static final int ACTION_POINTER_DOWN = -1;
	public static final int ACTION_POINTER_UP = -1;
	
	public MotionEvent() {
	}

}
