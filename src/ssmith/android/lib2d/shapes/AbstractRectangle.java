package ssmith.android.lib2d.shapes;

import ssmith.android.compatibility.Paint;
import ssmith.android.compatibility.RectF;
import ssmith.android.lib2d.MyPointF;
import ssmith.android.lib2d.Node;
import ssmith.android.lib2d.Spatial;
import ssmith.lang.GeometryFunctions2;

public abstract class AbstractRectangle extends Geometry {

	protected RectF local_rect = new RectF();
	
	public AbstractRectangle(String name, Paint _paint) {
		super(name, _paint);

	}
	
	public AbstractRectangle(String name, Paint _paint, float x, float y, float w, float h) {
		this(name, _paint);
		
		this.updateCoordsXYWH(x, y, w, h);
	}


	public void updateCoordsXYWH(float x, float y, float w, float h) {
		local_rect.left = x;
		local_rect.top = y;
		local_rect.right = x+w;
		local_rect.bottom = y+h;

		this.needsUpdating = true;
	}


	public void setByXYXY(float sx, float sy, float ex, float ey) {
		this.local_rect.left = sx;
		this.local_rect.top = sy;
		this.local_rect.right = ex;
		this.local_rect.bottom = ey;

		this.needsUpdating = true;
	}
	
	
	public void updateGeometricState() {
		super.refreshParentWorldCoordsFromParent();

		worldBounds.top = Math.min(local_rect.top, local_rect.bottom) + parentWorldCoords.y;
		worldBounds.bottom = Math.max(local_rect.top,local_rect.bottom) + parentWorldCoords.y;
			
		worldBounds.left = Math.min(local_rect.left, local_rect.right) + parentWorldCoords.x;
		worldBounds.right = Math.max(local_rect.left, local_rect.right) + parentWorldCoords.x;

		super.ensureWorldBoundsNotEmpty();
		
		this.needsUpdating = false;
	}
	

	@Override
	public boolean intersects(Spatial s) {
		if (s instanceof Node || s instanceof AbstractRectangle) {
			return RectF.intersects(this.worldBounds, s.getWorldBounds());// GeometryFunctions2.is//GeometryFuncs.DoLineAndRectCross(this.getWorldBounds().left, this.getWorldBounds().top, this.getWorldBounds().right, this.getWorldBounds().bottom, s.getWorldBounds().left, s.getWorldBounds().top, s.getWorldBounds().right, s.getWorldBounds().bottom);
		} else if (s instanceof Line) {
			Line l2 = (Line)s;
			return GeometryFunctions2.isLineIntersectingRectangle(l2.getWorldBounds().left, l2.getWorldBounds().top, l2.getWorldBounds().right, l2.getWorldBounds().bottom, 
					this.worldBounds.left, this.worldBounds.top, this.worldBounds.right, this.worldBounds.bottom);
		} else {
			throw new RuntimeException("intersects() not imlemented in AbstractRectangle for '" + s + "' (or its subclasses)");
		}
	}

	
	@Override
	public boolean contains(float x, float y) {
		return this.worldBounds.contains(x, y);
	}


	public void adjustLocation(MyPointF d) {
		this.adjustLocation(d.x, d.y);
	}
	
	
	public void adjustLocation(float x, float y) {
		this.setLocation(this.local_rect.left + x, this.local_rect.top + y);
	}
	
	
	@Override
	public void setLocation(float x, float y) {
		float old_left = this.local_rect.left;
		float old_top = this.local_rect.top;
		
		local_rect.left = x;
		local_rect.top = y;
		
		local_rect.right += (x - old_left);
		local_rect.bottom += (y - old_top);		
		
		this.needsUpdating = true;
	}


	public void setCentre(float x, float y) {
		float x2 = x - (this.getWidth() / 2);
		float y2 = y - (this.getHeight() / 2);
		this.setLocation(x2, y2);
	}


	public void setXPos(float x) {
		float old_left = this.local_rect.left;
		local_rect.left = x;
		local_rect.right += (x - old_left);
		this.needsUpdating = true;
	}


	public float getLocalXPos() {
		return this.local_rect.left;
	}
	
	
	public float getLocalYPos() {
		return this.local_rect.top;
	}
	
	
	public void setYPos(float y) {
		float old_top = this.local_rect.top;
		local_rect.top = y;
		local_rect.bottom += (y - old_top);
		this.needsUpdating = true;
	}


	@Override
	public void setSize(float w, float h) {
		local_rect.right = local_rect.left + w;
		local_rect.bottom = local_rect.top + h;

		this.needsUpdating = true;
	}


	@Override
	public float getHeight() {
		return local_rect.height();
	}


	@Override
	public float getWidth() {
		return local_rect.width();
	}
	
	
	public float getLocalCentreX() {
		return this.local_rect.centerX();
	}


	public float getLocalCentreY() {
		return this.local_rect.centerY();
	}


	public float getWorldCentreX() {
		return this.worldBounds.centerX();
	}


	public float getWorldCentreY() {
		return this.worldBounds.centerY();
	}


}
