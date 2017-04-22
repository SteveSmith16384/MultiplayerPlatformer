package ssmith.android.lib2d.layouts;

import ssmith.android.lib2d.Spatial;

public class FlowGridLayout extends GridLayout {
	
	private int cols;
	private int curr_col, curr_row;
	
	public FlowGridLayout(String name, float comp_w, float comp_h, float space, int _cols) {
		super(name, comp_w, comp_h, space);
		
		cols = _cols;
	}
	
	
	public void attachChild(Spatial s) {
		super.attachChild(s, curr_col, curr_row);
		curr_col++;
		if (curr_col >= cols) {
			curr_col = 0;
			curr_row++;
		}
	}


	public void attachChild(Spatial s, int grid_x, int grid_y) {
		throw new RuntimeException("Use attachChild(Spatial s) instead");
	}
	

	@Override
	public void detachAllChildren() {
		super.detachAllChildren();
		
		curr_col = 0;
		curr_row = 0;
		
		
	}
	
}
