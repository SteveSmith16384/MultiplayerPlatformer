package com.scs.multiplayerplatformer.mapgen;

import com.scs.multiplayerplatformer.graphics.mobs.AbstractMob;

public final class SimpleMobData {

	private final byte type;
	public float pixelX, pixelY;
	public AbstractMob mob; // Store the actual mob!
	
	
	public SimpleMobData(byte t, float px, float py) {
		super();
		
		type = t;
		pixelX = px;
		pixelY = py;
		
	}
	
	
	public byte getType() {
		return this.type;
	}
	
	
	public String toString() {
		return "Type: " + type + " (" + pixelX + "," + pixelY +")"; 
	}
	
}
