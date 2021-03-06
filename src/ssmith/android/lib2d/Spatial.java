package ssmith.android.lib2d;

import java.util.ArrayList;

import ssmith.android.compatibility.Canvas;
import ssmith.android.compatibility.PointF;
import ssmith.android.compatibility.RectF;
import ssmith.android.lib2d.shapes.Geometry;
import ssmith.lang.GeometryFuncs;

public abstract class Spatial {

	private static final float MIN_SIZE = 0.01f;

	protected RectF worldBounds = new RectF(); // To decide if it should be drawn
	public PointF parentWorldCoords = new PointF();
	public Node parent = null;
	protected String name;	
	public boolean visible = true;
	public boolean collides = true;
	public boolean needsUpdating = false;
	private MyPointF tmpCentre = new MyPointF();

	public Spatial(String _name) {
		super();

		name = _name;
	}


	protected void refreshParentWorldCoordsFromParent() { //This is coords, not bounds!
		if (this.parent != null) {
			this.parentWorldCoords.x = this.getParent().worldCoords.x;
			this.parentWorldCoords.y = this.getParent().worldCoords.y;
		} else {
			this.parentWorldCoords.x = 0;
			this.parentWorldCoords.y = 0;
		}

	}


	protected void ensureWorldBoundsNotEmpty() {
		// Ensure not empty
		if (this.worldBounds.right <= this.worldBounds.left) {
			this.worldBounds.right = this.worldBounds.left + MIN_SIZE;
		}
		if (this.worldBounds.bottom <= this.worldBounds.top) {
			this.worldBounds.bottom = this.worldBounds.top + MIN_SIZE;
		}

	}


	public abstract boolean intersects(Spatial s);


	public abstract boolean contains(float x, float y);


	public String toString() {
		return name + "[WB: " + worldBounds.left + ", " + worldBounds.top + ", " + worldBounds.right + ", " + worldBounds.bottom + "]";
	}


	public RectF getWorldBounds() {
		return worldBounds;
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
		return (this.worldBounds.left + this.worldBounds.right) / 2;
	}


	public float getWorldCentreY() {
		return (this.worldBounds.top + this.worldBounds.bottom) / 2;
	}


	public float getWorldX() {
		return (this.worldBounds.left);
	}


	public float getWorldY() {
		return (this.worldBounds.top);
	}


	public float getScreenX(Camera cam) {
		return this.worldBounds.left - cam.left;

	}

	public float getScreenY(Camera cam) {
		return this.worldBounds.top - cam.top;
	}
	
	
	public MyPointF getWorldCentre_CreatesNew() {
		tmpCentre.x = this.getWorldCentreX();
		tmpCentre.y = this.getWorldCentreY();
		return tmpCentre;
	}
	
	
	public float getDistanceTo(Spatial s) {
		return (float)GeometryFuncs.distance(this.getWorldCentreX(), this.getWorldCentreY(), s.getWorldCentreX(), s.getWorldCentreY());
	}

}
