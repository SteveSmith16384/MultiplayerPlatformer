package com.scs.multiplayerplatformer.input;

import org.gamepad4j.ButtonID;
import org.gamepad4j.DpadDirection;
import org.gamepad4j.IController;
import org.gamepad4j.StickID;
import org.gamepad4j.StickPosition;

public class PS4Controller implements IInputDevice {

	private IController gamepad;
	
	public PS4Controller(IController _gamepad) {
		gamepad = _gamepad;
	}
	

	@Override
	public boolean isLeftPressed() {
		StickPosition pos = gamepad.getStick(StickID.LEFT).getPosition();
		return pos.getDirection() == DpadDirection.LEFT;
	}

	
	@Override
	public boolean isRightPressed() {
		StickPosition pos = gamepad.getStick(StickID.LEFT).getPosition();
		return pos.getDirection() == DpadDirection.RIGHT;
	}

	
	@Override
	public boolean isJumpPressed() {
		return gamepad.isButtonPressed(ButtonID.FACE_DOWN);
	}

	
	@Override
	public boolean isDownPressed() {
		StickPosition pos = gamepad.getStick(StickID.LEFT).getPosition();
		return pos.getDirection() == DpadDirection.DOWN;
	}

	
	@Override
	public int getAngle() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public boolean isThrowPressed() {
		return gamepad.isButtonPressed(ButtonID.FACE_RIGHT);
	}


	@Override
	public int getThrowDuration() {
		return 1;
	}

}
