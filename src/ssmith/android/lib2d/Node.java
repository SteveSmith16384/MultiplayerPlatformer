package ssmith.android.lib2d;

import java.util.ArrayList;

import ssmith.android.compatibility.Canvas;
import ssmith.android.compatibility.PointF;
import ssmith.android.compatibility.RectF;
import ssmith.android.lib2d.shapes.Geometry;
import ssmith.android.lib2d.shapes.Line;
import ssmith.android.lib2d.shapes.Rectangle;
import ssmith.lang.GeometryFunctions2;

public class Node extends Spatial {

	private PointF localCoords = new MyPointF(); // The adjustment to local co-ords.  WE CANNOT USE WORLD BOUNDS!
	protected PointF worldCoords = new MyPointF(); // Our world coords, based on our parents world coords and our local coords
	protected RectF localBounds = new RectF(); // For storing the local bounds of all contained shapes.  Do not directly edit, let updateGeometricState() do it!
	private ArrayList<Spatial> children = new ArrayList<Spatial>();

	public Node(String name) {
		this(name, 0, 0);
	}


	public Node(String name, float x, float y) {
		super(name);

		this.localCoords.x = x;
		this.localCoords.y = y;
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

			s.needsUpdating = true;
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
		this.worldCoords.x = this.parentWorldCoords.x + this.localCoords.x;
		this.worldCoords.y = this.parentWorldCoords.y + this.localCoords.y;

		if (children.size() > 0) {
			// Set our bounds to infinite opposite
			localBounds.left = Float.MAX_VALUE;
			localBounds.top = Float.MAX_VALUE;
			localBounds.right = (Float.MAX_VALUE * -1);
			localBounds.bottom = (Float.MAX_VALUE * -1);

			synchronized (children) {
				for (Spatial child : children) {
					child.updateGeometricState(); // Need this in case it's a node to update it's bounds!

					localBounds.left = Math.min(localBounds.left, child.worldBounds.left);
					localBounds.top = Math.min(localBounds.top, child.worldBounds.top);
					localBounds.right = Math.max(localBounds.right, child.worldBounds.right);
					localBounds.bottom = Math.max(localBounds.bottom, child.worldBounds.bottom);
				}
			}
		} else {
			// No children, so we're zero-sized
			localBounds.left = 0;
			localBounds.top = 0;
			localBounds.right = 0;
			localBounds.bottom = 0;
		}

		// Now update our world bounds
		this.worldBounds.left = localBounds.left;// + this.parent_world_coords.x;// + this.local_coords.x;
		this.worldBounds.top = localBounds.top;// + this.parent_world_coords.y;// + this.local_coords.y;
		this.worldBounds.right = localBounds.right;// + this.parent_world_coords.x;// + this.local_coords.x;
		this.worldBounds.bottom = localBounds.bottom;// + this.parent_world_coords.y;// + this.local_coords.y;

		/*local_bounds.left -= this.world_bounds.left;
		local_bounds.top  -= this.world_bounds.top;
		local_bounds.right -= this.world_bounds.right;
		local_bounds.bottom -= this.world_bounds.bottom;*/

		this.needsUpdating = false;
	}


	@Override
	public void doDraw(Canvas g, Camera cam, long interpol) {
		if (this.visible) {
			if (Lib2DStatics.DEBUG_GFX) {
				//if (this.name.equalsIgnoreCase("root_node")) {
				//RectF temp_rect = new RectF(this.world_coords.x - cam.left, this.world_coords.y - cam.top, this.world_coords.x - cam.left+2, this.world_coords.y - cam.top+2);
				RectF temp_rect = new RectF(this.worldBounds.left - cam.left-2, this.worldBounds.top - cam.top-2, this.worldBounds.right - cam.left+2, this.worldBounds.bottom - cam.top+2);
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
			return RectF.intersects(this.worldBounds, s.getWorldBounds());
		} else if (s instanceof Line) {
			Line l2 = (Line)s;
			return GeometryFunctions2.isLineIntersectingRectangle(l2.worldBounds.left, l2.worldBounds.top, l2.worldBounds.right, l2.worldBounds.bottom, 
					this.worldBounds.left, this.worldBounds.top, this.worldBounds.right, this.worldBounds.bottom);
		} else {
			throw new RuntimeException("intersects() not implemented in Node for " + s);
		}
	}


	@Override
	public boolean contains(float x, float y) {
		return this.worldBounds.contains(x, y);
	}


	public void removeAllChildren() {
		detachAllChildren();
	}


	@Override
	public void setLocation(float x, float y) {
		localCoords.x = x;
		localCoords.y = y;

	}


	@Override
	public void setSize(float w, float h) {
		// Do nothing
	}


	public PointF getLocation() {
		return this.localCoords;
	}


	@Override
	public float getHeight() {
		return localBounds.bottom - localBounds.top;
	}


	@Override
	public float getWidth() {
		return localBounds.right - localBounds.left;
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
