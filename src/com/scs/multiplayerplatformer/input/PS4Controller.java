package com.scs.multiplayerplatformer.input;

import org.gamepad4j.ButtonID;
import org.gamepad4j.DpadDirection;
import org.gamepad4j.IController;
import org.gamepad4j.IStick;
import org.gamepad4j.StickID;
import org.gamepad4j.StickPosition;

public final class PS4Controller implements IInputDevice {

	private IController gamepad;

	public PS4Controller(IController _gamepad) {
		gamepad = _gamepad;
	}


	@Override
	public boolean isLeftPressed() {
		StickPosition pos = gamepad.getStick(StickID.LEFT).getPosition();
		//return pos.getDirection() == DpadDirection.LEFT;
		return pos.getDegree() > 252 && pos.getDegree() < 360;

	}


	@Override
	public boolean isRightPressed() {
		StickPosition pos = gamepad.getStick(StickID.LEFT).getPosition();
		//return pos.getDirection() == DpadDirection.RIGHT;
		return pos.getDegree() > 17 && pos.getDegree() < 152;
	}


	@Override
	public boolean isJumpPressed() {
		return gamepad.isButtonPressed(ButtonID.FACE_DOWN);
	}


	@Override
	public boolean isUpPressed() {
		StickPosition pos = gamepad.getStick(StickID.LEFT).getPosition();
		return pos.getDirection() == DpadDirection.UP;
	}


	@Override
	public boolean isDownPressed() {
		StickPosition pos = gamepad.getStick(StickID.LEFT).getPosition();
		return pos.getDirection() == DpadDirection.DOWN;
	}


	@Override
	public float getStickDistance() {
		StickPosition pos = gamepad.getStick(StickID.LEFT).getPosition();
		/*if (Statics.DEBUG) {
			Statics.p("Dist=" + pos.getDistanceToCenter());
		}*/
		return pos.getDistanceToCenter();
	}

	
	@Override
	public int getAngle() {
		IStick leftStick = gamepad.getStick(StickID.LEFT);
		StickPosition pos = leftStick.getPosition();
		return (int)pos.getDegree() -90; // 0=up, 90=right
	}


	@Override
	public boolean isThrowPressed() {
		return gamepad.isButtonPressed(ButtonID.FACE_RIGHT);
	}


	/*@Override
	public float getThrowDuration() {
		return .5f;
	}*/

}
