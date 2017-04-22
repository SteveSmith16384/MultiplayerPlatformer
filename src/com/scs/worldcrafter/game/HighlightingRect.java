package com.scs.worldcrafter.game;

import android.graphics.RectF;

public class HighlightingRect extends RectF {
	
	private boolean highlighted = false;
	
	public HighlightingRect(float l, float t, float r, float b) {
		super(l, t, r, b);
		
	}
	
	
	public void setHighlighted(float x, float y) {
		this.highlighted = this.contains(x, y);
	}
	
	
	public void setNotHighlighted(float x, float y) {
		if (this.contains(x, y)) {
			this.highlighted = false;
		}
	}
	
	
	public boolean isHighighted() {
		return this.highlighted;
	}

	
}
