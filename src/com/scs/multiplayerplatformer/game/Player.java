package com.scs.multiplayerplatformer.game;

import com.scs.multiplayerplatformer.input.IInputDevice;

public final class Player {

	public int numZB;
	public int score;
	public IInputDevice input;
	
	private static int nextId = 0;
	
	public Player(IInputDevice _input) {
		super();
		
		input = _input;
		numZB = nextId++;
	}
	
	
	@Override
	public String toString() {
		return "Player" + numZB;
	}

}
