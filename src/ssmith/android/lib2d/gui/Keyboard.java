package ssmith.android.lib2d.gui;

import ssmith.android.lib2d.Node;
import ssmith.android.lib2d.layouts.GridLayout;
import ssmith.android.util.ImageCache;
import android.graphics.Bitmap;
import android.graphics.Paint;

public class Keyboard extends Node {
	
	//private float key_size;
	private Bitmap bmp;
	
	public Keyboard(ImageCache img_cache, int r, Paint ink, float w, float h, boolean show_symbols) {
		super("Keyboard");
		
		float key_width = w / 12;
		float key_height = h / 4;
		
		bmp = img_cache.getImage(r, key_width, key_height);
		GridLayout grid = new GridLayout("Keyboard_Grid", key_width, key_height, 2f);
		/*if (show_symbols) {
			addRowOfLetters(grid, 0, "@-&+=:;<>", bmp, ink);
		}*/
		addRowOfLetters(grid, 0, "1234567890", bmp, ink);
		addRowOfLetters(grid, 1, "qwertyuiop", bmp, ink);
		addRowOfLetters(grid, 2, "asdfghjkl_", bmp, ink);
		addRowOfLetters(grid, 3, "zxcvbnm,. ", bmp, ink);
		this.attachChild(grid);
		//this.updateGeometricState();
	}
	
	
	private void addRowOfLetters(GridLayout grid, int row, String s, Bitmap bmp, Paint ink) {
		for (int i=0 ; i<s.length() ; i++) {
			addLetter(grid, s.substring(i, i+1), i, row, bmp, ink);
		}
	}
	

	private void addLetter(GridLayout grid, String s, int col, int row, Bitmap bmp, Paint ink) {
		Button b = new Button(s, null, ink, bmp);
		grid.attachChild(b, col, row);
	}

}
