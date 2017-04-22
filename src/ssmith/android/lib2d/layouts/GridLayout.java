package ssmith.android.lib2d.layouts;

import ssmith.android.lib2d.Node;
import ssmith.android.lib2d.Spatial;

public class GridLayout extends Node {

	private float spacing, comp_width, comp_height;

	public GridLayout(String name, float comp_w, float comp_h, float space) {
		super(name);
		spacing = space;
		comp_width = comp_w;
		comp_height = comp_h;
		
	}


	public void attachChild(Spatial s) {
		throw new RuntimeException("Use attachChild(Spatial s, int grid_x, int grid_y) instead");
	}


	public void attachChild(Spatial s, int grid_x, int grid_y) {
		super.attachChild(s);

		s.updateGeometricState(); // In case not done, so we get WorldBounds

		float left = (spacing * grid_x) + (comp_width * (grid_x));
		float top = (spacing * grid_y) + (comp_height * (grid_y));
		float right = (spacing * grid_x) + (comp_width * (grid_x+1));
		float bottom = (spacing * grid_y) + (comp_height * (grid_y+1));

		s.setByLTRB(left, top, right, bottom);

	}

	
	public float getCompWidth() {
		return this.comp_width;
	}

	
	public float getCompHeight() {
		return this.comp_height;
	}

}

/*public class GridLayout extends Node {

	private float spacing, comp_width, comp_height;

	public GridLayout(String name, float comp_w, float comp_h, float space) {
		super(name);
		spacing = space;
		comp_width = comp_w;
		comp_height = comp_h;
		
	}


	public void attachChild(Spatial s) {
		throw new RuntimeException("Use attachChild(Spatial s, int grid_x, int grid_y) instead");
	}


	public void attachChild(Spatial s, int grid_x, int grid_y) {
		super.attachChild(s);

		s.updateGeometricState(); // In case not done, so we get WorldBounds

		float left = (spacing * grid_x) + (comp_width * (grid_x));
		float top = (spacing * grid_y) + (comp_height * (grid_y));
		float right = (spacing * grid_x) + (comp_width * (grid_x+1));
		float bottom = (spacing * grid_y) + (comp_height * (grid_y+1));

		s.setByLTRB(left, top, right, bottom);

	}

	
	public float getCompWidth() {
		return this.comp_width;
	}

	
	public float getCompHeight() {
		return this.comp_height;
	}

}
*/