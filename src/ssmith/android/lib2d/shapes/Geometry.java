package ssmith.android.lib2d.shapes;

import ssmith.android.lib2d.Spatial;
import android.graphics.Paint;

public abstract class Geometry extends Spatial {
	
	protected Paint paint;
	public boolean pressed = false;
	
	public Geometry(String name, Paint _paint) {
		super(name);
		
		paint = _paint;
	}
	
	
	public void setPaint(Paint p) {
		paint = p;
	}
	

}
