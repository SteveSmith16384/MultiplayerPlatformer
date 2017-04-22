package ssmith.android.lib2d.layouts;

import ssmith.android.lib2d.Node;
import ssmith.android.lib2d.Spatial;

public class ListLayout extends Node {

	private float spacing, comp_width, comp_height;
	private float curr_y_pos = 0;

	public ListLayout(String name, float _spacing, float _comp_width, float _comp_height) {
		super(name);
		
		spacing = _spacing;
		comp_width = _comp_width;
		comp_height = _comp_height;

		curr_y_pos = spacing;
	}


	public void attachChild(Spatial child) {
		super.attachChild(child);

		this.updateGeometricState();  // In case not done, so we can get width/height if it's a node

		float left = spacing;
		float top = curr_y_pos;
		float right = comp_width;
		float bottom = curr_y_pos + comp_height;
		child.setByLTRB(left, top, right, bottom);
		
		curr_y_pos += child.getHeight() + spacing;

	}


}
