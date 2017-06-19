package ssmith.android.lib2d.shapes;

import ssmith.android.compatibility.Canvas;
import ssmith.android.compatibility.Paint;
import ssmith.android.compatibility.PointF;
import ssmith.android.lib2d.Camera;
import ssmith.android.lib2d.MyPointF;
import ssmith.android.lib2d.Node;
import ssmith.android.lib2d.Spatial;
import ssmith.lang.GeometryFuncs;
import ssmith.lang.GeometryFunctions2;
import ssmith.lang.Maths;


public class Line extends Geometry {
	
	public MyPointF localStart, localEnd;


	public Line(String name, Paint paint) {
		this(name, 0, 0, 0, 0, paint);
	}
	
	
	public Line(String name, Spatial start, Spatial end, Paint paint) {
		this(name, start.getWorldCentreX(), start.getWorldCentreY(), end.getWorldCentreX(), end.getWorldCentreY(), paint);
		
		//this.update(start, end);
	}
	
	
	public Line(String name, MyPointF start, MyPointF end, Paint paint) {
		this(name, start.x, start.y, end.x, end.y, paint);
		
		//this.update(start, end);
	}
	
	
	public Line(String name, float sx, float sy, float ex, float ey, Paint paint) {
		super(name, paint);
		this.setXYXY(sx, sy, ex, ey);
		//this.updateGeometricState();  Pointless as we haven't got a parent yet!
		
	}
	
	
	public void scale(float f) {
		localStart.x = localStart.x * f;
		localStart.y = localStart.y * f;
		localEnd.x = localEnd.x * f;
		localEnd.y = localEnd.y * f;
	}
	
	
	public MyPointF getVector() {
		return this.localEnd.subtract(this.localStart);
	}
	
	
	@Override
	public void doDraw(Canvas g, Camera cam, long interpol) {
		if (this.visible) {
			g.drawLine(parentWorldCoords.x + this.localStart.x - cam.left, parentWorldCoords.y + this.localStart.y - cam.top, parentWorldCoords.x + this.localEnd.x - cam.left, parentWorldCoords.y + this.localEnd.y - cam.top, paint);
		}
	}


	public float getLength() {
		return GeometryFuncs.distance(this.localStart.x, this.localStart.y, this.localEnd.x, this.localEnd.y);
	}
	
	
	@Override
	public boolean intersects(Spatial s) {
		if (s instanceof Node || s instanceof AbstractRectangle) {
			return GeometryFunctions2.isLineIntersectingRectangle(parentWorldCoords.x + this.localStart.x, parentWorldCoords.y + this.localStart.y, parentWorldCoords.x + this.localEnd.x, parentWorldCoords.y + this.localEnd.y, 
					s.getWorldBounds().left, s.getWorldBounds().top, s.getWorldBounds().right, s.getWorldBounds().bottom); 
		} else if (s instanceof Line) {
			Line l2 = (Line)s;
			return GeometryFuncs.GetLineIntersection(parentWorldCoords.x + this.localStart.x, parentWorldCoords.y + this.localStart.y, parentWorldCoords.x + this.localEnd.x, parentWorldCoords.y + this.localEnd.y, l2.parentWorldCoords.x + l2.localStart.x, l2.parentWorldCoords.y + l2.localStart.y, l2.parentWorldCoords.x + l2.localEnd.x, l2.parentWorldCoords.y + l2.localEnd.y) != null;
		} else {
			throw new RuntimeException("intersects() not imlemented in Line for " + s);
		}
	}


	@Override
	public float getHeight() {
		return Maths.mod(this.localStart.y - this.localEnd.y);
	}


	@Override
	public float getWidth() {
		return Maths.mod(this.localStart.x - this.localEnd.x);
	}


	@Override
	public void setLocation(float x, float y) {
		float off_x = x - this.localStart.x;
		float off_y = y - this.localStart.y;
		/*this.local_start.x = x;
		this.local_start.y = y;
		this.local_end.x = this.local_end.x + off_x;
		this.local_end.y = this.local_end.y + off_y;*/
		this.moveBy(off_x, off_y);
	}


	public void moveBy(float x, float y) {
		this.localStart.x = this.localStart.x + x;
		this.localStart.y = this.localStart.y + y;
		this.localEnd.x = this.localEnd.x + x;
		this.localEnd.y = this.localEnd.y + y;
	}


	public void setXYXY(float x1, float y1, float x2, float y2) {
		this.localStart.x = x1;
		this.localStart.y = y1;
		this.localEnd.x = x2;
		this.localEnd.y = y2;
	}


	public void setX2Y2(float x, float y) {
		this.localEnd.x = x;
		this.localEnd.y = y;
	}


	public void setSize(float w, float h) {
	//	this.local_end.x = this.local_start.x + w;
	//	this.local_end.y = this.local_start.y + h;
	}

	
	@Override
	public void updateGeometricState() {
		super.refreshParentWorldCoordsFromParent();

		worldBounds.top = Math.min(this.localStart.y, this.localEnd.y) + parentWorldCoords.y;
		worldBounds.bottom = Math.max(this.localStart.y, this.localEnd.y) + parentWorldCoords.y;

		worldBounds.left = Math.min(this.localStart.x, this.localEnd.x) + parentWorldCoords.x;
		worldBounds.right = Math.max(this.localStart.x, this.localEnd.x) + parentWorldCoords.x;

		super.ensureWorldBoundsNotEmpty();

		needsUpdating = false;
	}


	public PointF getStart() {
		return new PointF(parentWorldCoords.x + this.localStart.x, parentWorldCoords.y + this.localStart.y);
	}

	
	public MyPointF getEndpoint() {
		return new MyPointF(parentWorldCoords.x + this.localEnd.x, parentWorldCoords.y + this.localEnd.y);
	}


	@Override
	public boolean contains(float x, float y) {
		return false;
	}

	
/*	public MyPointF getNormal_CreatesNew() {
		return new MyPointF(this.end.x - this.start.x, this.end.y - this.start.y).normalizeLocal(); 
	}

	
	public MyPointF getEndpoint() {
		return this.end;
	}*/
}
