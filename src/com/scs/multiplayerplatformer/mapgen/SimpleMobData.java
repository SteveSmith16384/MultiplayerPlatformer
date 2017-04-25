package com.scs.multiplayerplatformer.mapgen;

import com.scs.multiplayerplatformer.graphics.mobs.AbstractMob;


public class SimpleMobData {

	private final byte type;
	public float pixel_x, pixel_y;
	public AbstractMob mob; // Store the actual mob!
	
	
	public SimpleMobData(byte t, float px, float py) {
		super();
		
		type = t;
		pixel_x = px;
		pixel_y = py;
		
		/*if (type == AbstractMob.ENEMY_NINJA_EASY) {
			System.out.print("");
		}*/
	}
	
	
	public byte getType() {
		return this.type;
	}
	
	public String toString() {
		return "Type: " + type + " (" + pixel_x + "," + pixel_y +")"; 
	}
	
}
