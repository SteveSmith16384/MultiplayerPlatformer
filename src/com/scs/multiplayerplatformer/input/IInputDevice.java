package com.scs.multiplayerplatformer.input;

public interface IInputDevice {

	boolean isLeftPressed();

	boolean isRightPressed();
	
	boolean isJumpPressed();

	boolean isDownPressed();
	
	boolean isThrowPressed();
	
	int getAngle();

	int getThrowDuration();

}
