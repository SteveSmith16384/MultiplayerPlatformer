package ssmith.android.compatibility;

public class PointF {

	public float x, y;

	public PointF() {
		this(0, 0);
	}

	
	public PointF(float x_, float y_) {
		x = x_;
		y = y_;
	}
	
	
	public float length() {
		return (float)Math.sqrt((x*x) + (y*y));
	}

}
