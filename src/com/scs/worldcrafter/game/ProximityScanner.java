package com.scs.worldcrafter.game;

import ssmith.android.util.Timer;
import ssmith.lang.GeometryFuncs;
import android.graphics.Point;

import com.scs.worldcrafter.Statics;

public class ProximityScanner implements IProcessable {
	
	private static final int INTERVAL = 2000;
	
	public Point target;
	private GameModule game;
	private Timer t;
	private int last_dist;
	
	public ProximityScanner(GameModule _game) {
		super();
		
		game = _game;
		
		t = new Timer(INTERVAL);
	}
	
	
	public void setMapTarget(Point p) {
		target = new Point((int)(p.x * Statics.SQ_SIZE), (int)(p.y * Statics.SQ_SIZE));
		
	}
	
	
	public int getDistance(boolean cached) {
		if (cached) {
		return last_dist / 10;
		} else {
			return (int)GeometryFuncs.distance(game.player.getWorldCentreX(), game.player.getWorldCentreY(), target.x, target.y);
		}
	}


	@Override
	public void process(long interpol) {
		if (t.hasHit(interpol)) {
			last_dist = (int)GeometryFuncs.distance(game.player.getWorldCentreX(), game.player.getWorldCentreY(), target.x, target.y); 
		}
	}

}
