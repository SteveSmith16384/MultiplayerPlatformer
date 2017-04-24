package ssmith.android.lib2d.gui;

import java.util.ArrayList;

import ssmith.android.compatibility.Paint;
import ssmith.android.lib2d.Node;

import com.scs.worldcrafter.Statics;

public class MultiLineLabel extends Node {

	private Paint paint, ink;
	private boolean word_wrap; // Automatically word wrap
	private float width;
	protected StringBuffer str;

	public MultiLineLabel(String name, String text, Paint _paint, Paint _ink, boolean _word_wrap, float _width) {
		super(name);

		paint = _paint;
		ink = _ink;
		word_wrap = _word_wrap;
		width = _width;


		str = new StringBuffer(text);
		this.showText();
	}


	protected void showText() {
		this.detachAllChildren();
		String text = str.toString();
		if (word_wrap) {
			ArrayList<String> al_lines = new ArrayList<String>();
			while (true) {
				//ink.setSubpixelText(true);
				int chars = ink.breakText(text, width);  //ink.measureText("first");   For some reason it misses off the last char?
				int cr_pos = text.indexOf("\n"); // Is there a CR in the text we've got?
				if (cr_pos >= 0 && cr_pos < chars) {
					chars = cr_pos;
				}
				if (chars >= text.length()) { // Are we trying to show more text that is remaining?
					al_lines.add(text.substring(0, chars));
					break;
				} else {
					// Did we end on a CR?
					if (text.substring(chars, chars+1).equalsIgnoreCase("\n") == false) {
						// Go back to the prev space
						int space_pos = text.substring(0, chars).lastIndexOf(" ");
						if (space_pos >= 0) {
							chars = space_pos;
						}
					}

					al_lines.add(text.substring(0, chars));
					text = text.substring(chars+1);
				}
			}
			showLines(al_lines);
		} else {
			String lines[] = text.split("\\n");
			ArrayList<String> al_lines = new ArrayList<String>();
			for (int i=0 ; i<lines.length ; i++) {
				al_lines.add(lines[i]);
			}
			showLines(al_lines);
		}
	}


	private void showLines(ArrayList<String> lines) {
		this.removeAllChildren();
		int y = 0;
		for (String s : lines) {
			Label l = new Label(this.name + "_sublabel", s, 0, y, this.paint, ink, false);
			this.attachChild(l);
			l.updateGeometricState(); // ?
			y += (ink.getTextSize() * Statics.LABEL_SPACING);
		}
		this.updateGeometricState();

	}


	public void appendText(String s) {
		str.append(s);
		this.showText();
	}


	public void setText(String s) {
		this.str = new StringBuffer(s);
		this.showText();
	}
	
	
	public String getText() {
		return str.toString();
	}
	
	
}


/*public class MultiLineLabel extends Node {
	
/*	private Paint paint, ink;
	private boolean word_wrap; // Automatically word wrap
	private float width;
	private StringBuffer str;
	
	public MultiLineLabel(String name, String text, Paint _paint, Paint _ink, boolean _word_wrap, float _width) {
		super(name);

		paint = _paint;
		ink = _ink;
		word_wrap = _word_wrap;
		width = _width;


		str = new StringBuffer(text);
		this.showText();
	}


	private void showText() {
		this.detachAllChildren();
		String text = str.toString();
		if (word_wrap) {
			ArrayList<String> al_lines = new ArrayList<String>();
			while (true) {
				int chars = ink.breakText(text, true, width, null);
				int cr_pos = text.indexOf("\n"); // Is there a CR in the text we've got?
				if (cr_pos >= 0 && cr_pos < chars) {
					chars = cr_pos;
				}
				if (chars >= text.length()) { // Are we trying to show more text that is remaining?
					al_lines.add(text.substring(0, chars));
					break;
				} else {
					// Did we end on a CR?
					if (text.substring(chars, chars+1).equalsIgnoreCase("\n") == false) {
						// Go back to the prev space
						int space_pos = text.substring(0, chars).lastIndexOf(" ");
						if (space_pos >= 0) {
							chars = space_pos;
						}
					}

					al_lines.add(text.substring(0, chars));
					text = text.substring(chars+1);
				}
			}
			showLines(al_lines);
		} else {
			String lines[] = text.split("\\n");
			ArrayList<String> al_lines = new ArrayList<String>();
			for (int i=0 ; i<lines.length ; i++) {
				al_lines.add(lines[i]);
			}
			showLines(al_lines);
		}
	}


	protected void showLines(ArrayList<String> lines) {
		this.detachAllChildren();
		int y = 0;
		for (String s : lines) {
			Label l = new Label(this.name + "_sublabel", s, 0, y, this.paint, ink, false);
			this.attachChild(l);
			l.updateGeometricState();
			y += (ink.getTextSize() * 1.2);
		}
		this.updateGeometricState();

	}


	public void appendText(String s) {
		str.append(s);
		this.showText();
	}


	public void setText(String s) {
		this.str = new StringBuffer(s);
		this.showText();
	}
	
}
*/