package ssmith.android.lib2d.gui;

import android.graphics.Paint;


public class GUIFunctions {

	private static Paint dummy_paint = new Paint();

	static {
		dummy_paint.setARGB(255, 0, 0, 0);
		dummy_paint.setAntiAlias(true);
	}
	
	
	private GUIFunctions() {
		
	}
	
	
	/*public static void ApplyRendererToControls(Node n, IButtonRenderer renderer) {
		for (Spatial s : n.getChildren()) {
			if (s instanceof AbstractTextComponent) {
				AbstractTextComponent c = (AbstractTextComponent)s;
				c.renderer = renderer;
			}
		}
		
	}*/
	
	
	public static float GetTextSizeToFit(String text, float max_width) {
		int size = 200;// Max size - 
		max_width = max_width * 0.9f; // Fit inside frame
		while (size > 2) {
			dummy_paint.setTextSize(size);
			if (dummy_paint.measureText(text) < max_width) {
				break;  //return size;
			}
			size--;
		}
		return size;
	}
	
	

}
