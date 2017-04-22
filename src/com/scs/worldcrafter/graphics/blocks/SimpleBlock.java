package com.scs.worldcrafter.graphics.blocks;

public class SimpleBlock { // This is used for storing map data

	public byte type;
	public boolean on_fire = false;
	//public String collectable_type = null;
	
	public SimpleBlock() {
		super();
	}
	
	
	public SimpleBlock(byte t) {
		this();
		
		type = t;
	}

}
