package ssmith.android.lib2d.gui;

import ssmith.android.compatibility.Paint;

import com.scs.worldcrafter.Statics;

public class Label extends AbstractTextComponent {

	public Label(String name, String text, Paint paint, Paint ink) {
		this(name, text, 0, 0, paint, ink, true);
	}

	
	public Label(String name, String text, Paint paint, Paint ink, boolean centre) {
		this(name, text, 0, 0, paint, ink, centre);
	}

	
	public Label(String name, String text, float x, float y, Paint paint, Paint ink, boolean centre) {
		super(name, "", text, x, y, ink.measureText(text)*1.15f, ink.getTextSize() * Statics.LABEL_SPACING, paint, ink, null, centre);
		
		this.collides = false;
	}

}

/*public class Label extends AbstractTextComponent {

	public Label(String name, String text, Paint paint, Paint ink) {
		this(name, text, 0, 0, paint, ink, true);
	}

	
	public Label(String name, String text, Paint paint, Paint ink, boolean centre) {
		this(name, text, 0, 0, paint, ink, centre);
	}

	
	public Label(String name, String text, float x, float y, Paint paint, Paint ink, boolean centre) {
		super(name, "", text, x, y, ink.measureText(text), ink.getTextSize(), paint, ink, null, centre);
		
		this.collides = false;
	}

}
*/