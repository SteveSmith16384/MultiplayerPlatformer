package ssmith.android.lib2d.layouts;

import ssmith.android.lib2d.Node;
import ssmith.android.lib2d.Spatial;

public class VerticalFlowLayout extends Node {

	private int spacing;
	private int curr_y_pos = 0;

	public VerticalFlowLayout(String name, int space) {
		super(name);
		spacing = space;

		curr_y_pos = space;
	}


	public void attachChild(Spatial child) {
		super.attachChild(child);

		this.updateGeometricState();  // In case not done, so we can get width/height if it's a node

		float left = spacing;
		float top = curr_y_pos;
		float right = child.getWidth();
		float bottom = curr_y_pos + child.getHeight();
		child.setByLTRB(left, top, right, bottom);
		
		curr_y_pos += child.getHeight() + spacing;

		child.updateGeometricState();  // Update its position now its been added to parent


	}


}
