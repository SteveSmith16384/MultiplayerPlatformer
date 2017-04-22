package ssmith.android.lib2d.layouts;

import ssmith.android.lib2d.Spatial;

public class NewListLayout extends GridLayout {
	
	public NewListLayout(String name, float comp_w, float comp_h, float space) {
		super(name, comp_w, comp_h, space);
	}

	public void attachChild(Spatial s) {
		int size = this.getNumChildren();
		super.attachChild(s, 0, size);
	}


	public void attachChild(Spatial s, int grid_x, int grid_y) {
		throw new RuntimeException("Use attachChild(Spatial s) instead");
	}
	
}
