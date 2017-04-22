package ssmith.android.lib2d;

import java.util.ArrayList;

import ssmith.android.lib2d.shapes.Geometry;
import ssmith.android.lib2d.shapes.Line;
import ssmith.android.lib2d.shapes.Rectangle;
import ssmith.lang.GeometryFunctions2;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;

public class Node extends Spatial {

	private PointF local_coords = new MyPointF(); // The adjustment to local co-ords.  WE CANNOT USE WORLD BOUNDS!
	protected PointF world_coords = new MyPointF(); // Our world coords, based on our parents world coords and our local coords
	protected RectF local_bounds = new RectF(); // For storing the local bounds of all contained shapes.  Do not directly edit, let updateGeometricState() do it!
	private ArrayList<Spatial> children = new ArrayList<Spatial>();

	public Node(String name) {
		this(name, 0, 0);
	}


	public Node(String name, float x, float y) {
		super(name);

		this.local_coords.x = x;
		this.local_coords.y = y;
	}


	public int getNumChildren() {
		return this.children.size();
	}


	public ArrayList<Spatial> getChildren() {
		synchronized (children) {
			return this.children;
		}
	}


	public void attachChild(Spatial s) {
		attachChild(this.getNumChildren(), s);
	}


	public void attachChild(int pos, Spatial s) {
		synchronized (children) {
			if (s.getParent() != null) {
				throw new RuntimeException(s + " already has a parent!");
			}
			if (s == this) {
				throw new RuntimeException("Cannot attach node to itself!");
			}
			this.children.add(pos, s);
			s.setParent(this);

			s.needs_updating = true;
		}
	}


	public void detachChild(Spatial s) {
		synchronized (children) {
			s.removeFromParent();
		}
	}


	public void detachAllChildren() {
		synchronized (children) {
			while (this.children.size() > 0) {
				Spatial s = this.children.get(0);
				s.removeFromParent();
			}
		}
	}


	public void updateGeometricState() {
		/*if (parent) {
			if (this.parent != null) {
				this.parent.updateGeometricState(false);
			}
		}*/
		super.refreshParentWorldCoordsFromParent();

		// Update our world coords
		this.world_coords.x = this.parent_world_coords.x + this.local_coords.x;
		this.world_coords.y = this.parent_world_coords.y + this.local_coords.y;

		if (children.size() > 0) {
			// Set our bounds to infinite opposite
			local_bounds.left = Float.MAX_VALUE;
			local_bounds.top = Float.MAX_VALUE;
			local_bounds.right = (Float.MAX_VALUE * -1);
			local_bounds.bottom = (Float.MAX_VALUE * -1);

			synchronized (children) {
				for (Spatial child : children) {
					child.updateGeometricState(); // Need this in case it's a node to update it's bounds!

					local_bounds.left = Math.min(local_bounds.left, child.world_bounds.left);
					local_bounds.top = Math.min(local_bounds.top, child.world_bounds.top);
					local_bounds.right = Math.max(local_bounds.right, child.world_bounds.right);
					local_bounds.bottom = Math.max(local_bounds.bottom, child.world_bounds.bottom);
				}
			}
		} else {
			// No children, so we're zero-sized
			local_bounds.left = 0;
			local_bounds.top = 0;
			local_bounds.right = 0;
			local_bounds.bottom = 0;
		}

		// Now update our world bounds
		this.world_bounds.left = local_bounds.left;// + this.parent_world_coords.x;// + this.local_coords.x;
		this.world_bounds.top = local_bounds.top;// + this.parent_world_coords.y;// + this.local_coords.y;
		this.world_bounds.right = local_bounds.right;// + this.parent_world_coords.x;// + this.local_coords.x;
		this.world_bounds.bottom = local_bounds.bottom;// + this.parent_world_coords.y;// + this.local_coords.y;

		/*local_bounds.left -= this.world_bounds.left;
		local_bounds.top  -= this.world_bounds.top;
		local_bounds.right -= this.world_bounds.right;
		local_bounds.bottom -= this.world_bounds.bottom;*/

		this.needs_updating = false;
	}


	@Override
	public void doDraw(Canvas g, Camera cam, long interpol) {
		if (this.visible) {
			if (Lib2DStatics.DEBUG_GFX) {
				//if (this.name.equalsIgnoreCase("root_node")) {
				//RectF temp_rect = new RectF(this.world_coords.x - cam.left, this.world_coords.y - cam.top, this.world_coords.x - cam.left+2, this.world_coords.y - cam.top+2);
				RectF temp_rect = new RectF(this.world_bounds.left - cam.left-2, this.world_bounds.top - cam.top-2, this.world_bounds.right - cam.left+2, this.world_bounds.bottom - cam.top+2);
				g.drawRect(temp_rect, Lib2DStatics.paint_red_line);
				//}
			}
			synchronized (children) {
				try {
					for (Spatial child : children) {
						if (child.visible) {
							if (RectF.intersects(child.getWorldBounds(), cam)) { // Only draw children in the view
								child.doDraw(g, cam, interpol); 
							}
						}
					}
				} catch (java.util.ConcurrentModificationException ex) {
					// Do nothing' we've probably removed something inside its doDraw() method.
				}
			}
		}
	}


	@Override
	public boolean intersects(Spatial s) {
		if (s instanceof Node || s instanceof Rectangle) {
			return RectF.intersects(this.world_bounds, s.getWorldBounds());
		} else if (s instanceof Line) {
			Line l2 = (Line)s;
			return GeometryFunctions2.isLineIntersectingRectangle(l2.world_bounds.left, l2.world_bounds.top, l2.world_bounds.right, l2.world_bounds.bottom, 
					this.world_bounds.left, this.world_bounds.top, this.world_bounds.right, this.world_bounds.bottom);
		} else {
			throw new RuntimeException("intersects() not implemented in Node for " + s);
		}
	}


	@Override
	public boolean contains(float x, float y) {
		return this.world_bounds.contains(x, y);
	}


	public void removeAllChildren() {
		detachAllChildren();
	}


	@Override
	public void setLocation(float x, float y) {
		local_coords.x = x;
		local_coords.y = y;

	}


	@Override
	public void setSize(float w, float h) {
		// Do nothing
	}


	public PointF getLocation() {
		return this.local_coords;
	}


	@Override
	public float getHeight() {
		return local_bounds.bottom - local_bounds.top;
	}


	@Override
	public float getWidth() {
		return local_bounds.right - local_bounds.left;
	}


	public void setCentre(float x, float y) {
		if (this.getWidth() == 0 || this.getHeight() == 0) {
			throw new RuntimeException(this.name + " has no dimensions yet.  Try calling UpdateGeometricState().");
		}
		float x2 = x - (this.getWidth() / 2);
		float y2 = y - (this.getHeight() / 2);
		this.setLocation(x2, y2);
	}


	/**
	 * Gets all the geometries that have a point at x,y
	 * @param x
	 * @param y
	 * @return
	 */
	public ArrayList<Geometry> getCollidersAt(float x, float y) {
		ArrayList<Geometry> list = new ArrayList<Geometry>();
		getCollidersAt(x, y, list);
		return list;
	}


	protected void getCollidersAt(float x, float y, ArrayList<Geometry> list_of_colliders) {
		if (collides) { //this
			if (contains(x, y)) {
				synchronized (children) {
					for (Spatial child : children) {
						if (child.collides) {
							if (child.contains(x, y)) {
								if (child instanceof Node) {
									Node ch = (Node)child;
									ch.getCollidersAt(x, y, list_of_colliders);
								} else {
									list_of_colliders.add((Geometry)child);
								}
							}
						}
					}
				}
			}
		}
		//return list_of_colliders;
	}



}
