package ssmith.android.lib2d;

import java.util.ArrayList;

import ssmith.android.lib2d.shapes.Geometry;
import ssmith.lang.GeometryFuncs;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;

public abstract class Spatial {

	private static final float MIN_SIZE = 0.01f;

	protected RectF world_bounds = new RectF(); // To decide if it should be drawn
	public PointF parent_world_coords = new PointF();
	public Node parent = null;
	protected String name;	
	public boolean visible = true;
	public boolean collides = true;
	public boolean needs_updating = false;
	private MyPointF centre_tmp = new MyPointF();

	public Spatial(String _name) {
		super();

		name = _name;
	}


	protected void refreshParentWorldCoordsFromParent() { //This is coords, not bounds!
		if (this.parent != null) {
			this.parent_world_coords.x = this.getParent().world_coords.x;
			this.parent_world_coords.y = this.getParent().world_coords.y;
		} else {
			this.parent_world_coords.x = 0;
			this.parent_world_coords.y = 0;
		}

	}


	protected void ensureWorldBoundsNotEmpty() {
		// Ensure not empty
		if (this.world_bounds.right <= this.world_bounds.left) {
			this.world_bounds.right = this.world_bounds.left + MIN_SIZE;
		}
		if (this.world_bounds.bottom <= this.world_bounds.top) {
			this.world_bounds.bottom = this.world_bounds.top + MIN_SIZE;
		}

	}


	public abstract boolean intersects(Spatial s);


	public abstract boolean contains(float x, float y);


	public String toString() {
		return name + "[WB: " + world_bounds.left + ", " + world_bounds.top + ", " + world_bounds.right + ", " + world_bounds.bottom + "]";
	}


	public RectF getWorldBounds() {
		return world_bounds;
	}


	public void setParent(Node n) {
		this.parent = n;
	}



	/**
	 * Gets all the geometries that have a point at x,y
	 * @param x
	 * @param y
	 * @return
	 */
	public ArrayList<Geometry> getCollidersAt(float x, float y) {
		return getCollidersAt((Node)this, x, y, new ArrayList<Geometry>());
	}


	private static ArrayList<Geometry> getCollidersAt(Node root_node, float x, float y, ArrayList<Geometry> list_of_colliders) {
		if (root_node.collides) {
			if (root_node.getNumChildren() > 0 && (root_node.getWidth() == 0 || root_node.getHeight() == 0)) {
				throw new RuntimeException("Node " + root_node.name + " needs updating!");
			}
			if (root_node.contains(x, y)) {
				for (Spatial child : root_node.getChildren()) {
					if (child.collides) {
						if (child.contains(x, y)) {
							if (child instanceof Node) {
								getCollidersAt((Node)child, x, y, list_of_colliders);
							} else {
								list_of_colliders.add((Geometry)child);
							}
						}
					}

				}
			}
		}
		return list_of_colliders;
	}


	/**
	 * Returns all the Geometry's that collide with us.
	 * @param node
	 * @return
	 */
	public ArrayList<Geometry> getColliders(Node node) {
		return getColliders(node, new ArrayList<Geometry>());
	}


	private ArrayList<Geometry> getColliders(Node root_node, ArrayList<Geometry> list_of_colliders) {
		if (root_node.getNumChildren() > 0 && (root_node.getWidth() == 0 || root_node.getHeight() == 0)) {
			throw new RuntimeException("Node needs updating!");
		}
		if (this != root_node) { // Otherwise we could collide with ourselves
			if (root_node.collides) {
				if (this.intersects(root_node)) {
					for (Spatial child : root_node.getChildren()) {
						if (this != child) {
							if (child.collides) {
								if (this.intersects(child)) {
									if (child instanceof Node) {
										getColliders((Node)child, list_of_colliders);
									} else {
										list_of_colliders.add((Geometry)child);
									}
								}
							}
						}
					}
				}
			}
		}
		return list_of_colliders;
	}


	public Node getParent() {
		return this.parent;
	}


	public void removeFromParent() {
		if (this.parent != null) {
			this.parent.getChildren().remove(this);
			this.parent = null;
		}
	}


	public abstract float getHeight();

	public abstract float getWidth();

	public abstract void doDraw(Canvas g, Camera cam, long interpol);

	public abstract void updateGeometricState();

	public abstract void setLocation(float x, float y);

	public void setByLTRB(float l, float t, float r, float b) {
		setLocation(Math.min(l, r), Math.min(t, b));
		setSize(Math.max(l, r) - Math.min(l, r), Math.max(t, b) - Math.min(t, b));
	}

	public void setByLTWH(float l, float t, float w, float h) {
		setLocation(l, t);
		setSize(w, h);
	}

	public abstract void setSize(float w, float h);

	public float getWorldCentreX() {
		return (this.world_bounds.left + this.world_bounds.right) / 2;
	}


	public float getWorldCentreY() {
		return (this.world_bounds.top + this.world_bounds.bottom) / 2;
	}


	public float getWorldX() {
		return (this.world_bounds.left);
	}


	public float getWorldY() {
		return (this.world_bounds.top);
	}


	public float getScreenX(Camera cam) {
		return this.world_bounds.left - cam.left;

	}

	public float getScreenY(Camera cam) {
		return this.world_bounds.top - cam.top;
	}
	
	
	public MyPointF getWorldCentre_CreatesNew() {
		centre_tmp.x = this.getWorldCentreX();
		centre_tmp.y = this.getWorldCentreY();
		return centre_tmp;
	}
	
	
	public float getDistanceTo(Spatial s) {
		return (float)GeometryFuncs.distance(this.getWorldCentreX(), this.getWorldCentreY(), s.getWorldCentreX(), s.getWorldCentreY());
	}

}
