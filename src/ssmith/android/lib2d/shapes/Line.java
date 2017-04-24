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
	
	public MyPointF local_start, local_end;


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
		local_start.x = local_start.x * f;
		local_start.y = local_start.y * f;
		local_end.x = local_end.x * f;
		local_end.y = local_end.y * f;
	}
	
	
	public MyPointF getVector() {
		return this.local_end.subtract(this.local_start);
	}
	
	
	@Override
	public void doDraw(Canvas g, Camera cam, long interpol) {
		if (this.visible) {
			g.drawLine(parent_world_coords.x + this.local_start.x - cam.left, parent_world_coords.y + this.local_start.y - cam.top, parent_world_coords.x + this.local_end.x - cam.left, parent_world_coords.y + this.local_end.y - cam.top, paint);
		}
	}


	public float getLength() {
		return GeometryFuncs.distance(this.local_start.x, this.local_start.y, this.local_end.x, this.local_end.y);
	}
	
	
	@Override
	public boolean intersects(Spatial s) {
		if (s instanceof Node || s instanceof AbstractRectangle) {
			return GeometryFunctions2.isLineIntersectingRectangle(parent_world_coords.x + this.local_start.x, parent_world_coords.y + this.local_start.y, parent_world_coords.x + this.local_end.x, parent_world_coords.y + this.local_end.y, 
					s.getWorldBounds().left, s.getWorldBounds().top, s.getWorldBounds().right, s.getWorldBounds().bottom); 
		} else if (s instanceof Line) {
			Line l2 = (Line)s;
			return GeometryFuncs.GetLineIntersection(parent_world_coords.x + this.local_start.x, parent_world_coords.y + this.local_start.y, parent_world_coords.x + this.local_end.x, parent_world_coords.y + this.local_end.y, l2.parent_world_coords.x + l2.local_start.x, l2.parent_world_coords.y + l2.local_start.y, l2.parent_world_coords.x + l2.local_end.x, l2.parent_world_coords.y + l2.local_end.y) != null;
		} else {
			throw new RuntimeException("intersects() not imlemented in Line for " + s);
		}
	}


	@Override
	public float getHeight() {
		return Maths.mod(this.local_start.y - this.local_end.y);
	}


	@Override
	public float getWidth() {
		return Maths.mod(this.local_start.x - this.local_end.x);
	}


	@Override
	public void setLocation(float x, float y) {
		float off_x = x - this.local_start.x;
		float off_y = y - this.local_start.y;
		/*this.local_start.x = x;
		this.local_start.y = y;
		this.local_end.x = this.local_end.x + off_x;
		this.local_end.y = this.local_end.y + off_y;*/
		this.moveBy(off_x, off_y);
	}


	public void moveBy(float x, float y) {
		this.local_start.x = this.local_start.x + x;
		this.local_start.y = this.local_start.y + y;
		this.local_end.x = this.local_end.x + x;
		this.local_end.y = this.local_end.y + y;
	}


	public void setXYXY(float x1, float y1, float x2, float y2) {
		this.local_start.x = x1;
		this.local_start.y = y1;
		this.local_end.x = x2;
		this.local_end.y = y2;
	}


	public void setX2Y2(float x, float y) {
		this.local_end.x = x;
		this.local_end.y = y;
	}


	public void setSize(float w, float h) {
	//	this.local_end.x = this.local_start.x + w;
	//	this.local_end.y = this.local_start.y + h;
	}

	
	@Override
	public void updateGeometricState() {
		super.refreshParentWorldCoordsFromParent();

		world_bounds.top = Math.min(this.local_start.y, this.local_end.y) + parent_world_coords.y;
		world_bounds.bottom = Math.max(this.local_start.y, this.local_end.y) + parent_world_coords.y;

		world_bounds.left = Math.min(this.local_start.x, this.local_end.x) + parent_world_coords.x;
		world_bounds.right = Math.max(this.local_start.x, this.local_end.x) + parent_world_coords.x;

		super.ensureWorldBoundsNotEmpty();

		needs_updating = false;
	}


	public PointF getStart() {
		return new PointF(parent_world_coords.x + this.local_start.x, parent_world_coords.y + this.local_start.y);
	}

	
	public MyPointF getEndpoint() {
		return new MyPointF(parent_world_coords.x + this.local_end.x, parent_world_coords.y + this.local_end.y);
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
