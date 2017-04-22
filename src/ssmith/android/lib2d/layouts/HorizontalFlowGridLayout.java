package ssmith.android.lib2d.layouts;

import ssmith.android.lib2d.Node;
import ssmith.android.lib2d.Spatial;

public class HorizontalFlowGridLayout extends Node {

	private float spacing;
	private float curr_x_pos = 0;

	public HorizontalFlowGridLayout(String name, float space) {
		super(name);
		spacing = space;

		curr_x_pos = space;
	}


	public void attachChild(Spatial child) {
		super.attachChild(child);

		child.updateGeometricState();  // In case not done, so we can get width/height if it's a node

		float left = curr_x_pos;
		float top = spacing;
		float right = curr_x_pos + child.getWidth();
		float bottom = child.getHeight();
		child.setByLTRB(left, top, right, bottom);
		
		curr_x_pos += child.getWidth() + spacing;

	}


}
