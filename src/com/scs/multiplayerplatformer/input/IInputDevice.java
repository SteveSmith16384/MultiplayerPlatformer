package com.scs.multiplayerplatformer.input;

public interface IInputDevice {

	int getID();
	
	boolean isLeftPressed();

	boolean isRightPressed();
	
	boolean isJumpPressed();

	boolean isUpPressed();
	
	boolean isDownPressed();
	
	float getStickDistance();
	
	boolean isThrowPressed();
	
	int getAngle();

	//float getThrowDuration();

}
