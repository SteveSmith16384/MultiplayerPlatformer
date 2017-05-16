package com.scs.multiplayerplatformer.mapgen;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class AbstractLevelData {

	public byte[][] data;
	protected Point start_pos;
	public HashMap<Byte, Integer> block_inv;
	public ArrayList<SimpleMobData> mobs = new ArrayList<SimpleMobData>();
	public volatile int row, max_rows; // To track progress

	public AbstractLevelData() {
		//super("MapGen");

	}


	public abstract void getMap();


	public byte getGridDataAt(int x, int y) {
		return data[x][y];
	}


	public Point getStartPos() {
		if (start_pos == null) {
			throw new NullPointerException("No start position defined");
		}
		return this.start_pos;
	}
	
	
	public void setStartPos(int map_x, int map_y) {
		start_pos = new Point(map_x, map_y);
	}


	public int getGridHeight() {
		return data[0].length;
	}


	public int getGridWidth() {
		return data.length;
	}


	protected void checkAndChangeAdjacentSquares(int x, int y, byte[] from, byte to) {
		for (int y2=y-1 ; y2<=y+1 ; y2++) {
			for (int x2=x-1 ; x2<=x+1 ; x2++) {
				if (x2 != x || y2 != y) {
					checkAndChangeSquare(x2, y2, from, to);
				}
			}
		}
	}


	protected void checkAndChangeSquare(int x, int y, byte[] from, byte to) {
		try {
			for(int i=0 ;i<from.length ; i++) {
				byte type = data[x][y];
				if (type == from[i]) {
					data[x][y] = to;
				}
			}
		} catch (java.lang.ArrayIndexOutOfBoundsException ex) {
			ex.printStackTrace();
		}
	}

}
