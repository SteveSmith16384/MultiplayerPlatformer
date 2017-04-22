package ssmith.android.framework;

public final class MyEvent {
	
	public int action;
	public float x, y;
	
	public MyEvent(int _action, float _x, float _y) {
		action = _action;
		x = _x;
		y = _y;
	}
	
	
	public String toString() {
		return "A:" + action + " X:" + x + " Y:" + y;
	}
	public int getAction() {
		return action;
	}
	
	
	public float getX() {
		return x;
	}


	public float getY() {
		return y;
	}


}
