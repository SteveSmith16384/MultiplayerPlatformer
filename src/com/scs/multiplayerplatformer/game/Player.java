package com.scs.multiplayerplatformer.game;

import com.scs.multiplayerplatformer.input.IInputDevice;

public final class Player {

	public int num, score;
	public IInputDevice input;
	
	public Player(IInputDevice _input, int _num) {
		super();
		
		input = _input;
		num = _num;
	}

}
