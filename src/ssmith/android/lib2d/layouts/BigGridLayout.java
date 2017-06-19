package ssmith.android.lib2d.layouts;

import java.util.Hashtable;

import ssmith.android.lib2d.Node;
import ssmith.android.lib2d.Spatial;

public class BigGridLayout extends Node {

	private float spacing, comp_width, comp_height;
	private int subnodeSize;
	private Hashtable<String,Node> subnodes = new Hashtable<String,Node>();

	public BigGridLayout(String name, float comp_w, float comp_h, float space, int _subnode_size) {
		super(name);
		
		spacing = space;
		comp_width = comp_w;
		comp_height = comp_h;
		subnodeSize = _subnode_size;
		
	}


	public void attachChild(Spatial s, int grid_x, int grid_y) {
		GridLayout n = this.getSubNode(grid_x, grid_y);
		n.attachChild(s, grid_x, grid_y);
	}

	
	protected GridLayout getSubNode(int x, int y) {
		x = x/subnodeSize;
		y = y/subnodeSize;
		String s = "|" + x + "," + y + "|";
		while (subnodes.containsKey(s) == false) {
			GridLayout n = new GridLayout("MapNode_" + x + "_" + y, this.comp_width, this.comp_height, this.spacing);
			this.attachChild(n);
			this.subnodes.put(s, n);

		}
		GridLayout n2 = (GridLayout)this.subnodes.get(s);
		return n2;
	}


	public float getCompWidth() {
		return this.comp_width;
	}

	
	public float getCompHeight() {
		return this.comp_height;
	}

}
