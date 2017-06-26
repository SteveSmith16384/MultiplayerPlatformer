package com.scs.multiplayerplatformer.game;

import com.scs.multiplayerplatformer.input.IInputDevice;

public final class Player { // todo - tostring

	public int num; // todo - zb
	public int score;
	public IInputDevice input;
	
	private static int nextId = 0;
	
	public Player(IInputDevice _input) {
		super();
		
		input = _input;
		num = nextId++;
	}

}
