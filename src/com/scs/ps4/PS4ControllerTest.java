package com.scs.ps4;

import org.gamepad4j.ButtonID;
import org.gamepad4j.Controllers;
import org.gamepad4j.DpadDirection;
import org.gamepad4j.IButton;
import org.gamepad4j.IController;
import org.gamepad4j.IStick;
import org.gamepad4j.StickID;
import org.gamepad4j.StickPosition;

public final class PS4ControllerTest {

	public static void main(String[] args) {
		new PS4ControllerTest();

	}

	public PS4ControllerTest() {	
		// Initialize the API
		Controllers.initialize();

		while (true) {
			// Poll the state of the controllers
			Controllers.checkControllers();
			IController[] gamepads = Controllers.getControllers();
			if (gamepads.length > 0) {
				// Use the lower gamepad face button ("X" on DualShock, or "A" on Xbox pad)
				IButton jumpButton = gamepads[0].getButton(ButtonID.FACE_DOWN);
				if(jumpButton.isPressed()) {
					p("Down");
				}
				jumpButton = gamepads[0].getButton(ButtonID.FACE_LEFT);
				if(jumpButton.isPressed()) {
					p("Left");
				}
				jumpButton = gamepads[0].getButton(ButtonID.FACE_RIGHT);
				if(jumpButton.isPressed()) {
					p("Right");
				}
				jumpButton = gamepads[0].getButton(ButtonID.FACE_UP);
				if(jumpButton.isPressed()) {
					p("Up");
				}

				// Use the d-pad
				if(gamepads[0].getDpadDirection() != DpadDirection.NONE) {
					p("Down");
				}
				// Use an analog stick
				IStick leftStick = gamepads[0].getStick(StickID.LEFT);
				StickPosition pos = leftStick.getPosition();
				// Now check stick position
				if(!pos.isStickCentered()) {// && pos.getDirection() == 45f) {
					p("Left Here: " + pos.getDirection() + " Deg:" + pos.getDegree());
				}
				IStick rightStick = gamepads[0].getStick(StickID.RIGHT);
				StickPosition posr = rightStick.getPosition();
				// Now check stick position
				if(!posr.isStickCentered() && posr.getDirection() != DpadDirection.NONE) {
					p("Right Here: " + posr.getDirection() + " Deg:" + posr.getDegree());
				}

			} else {
				p("No controllers");
			}
		}
		// Game ended / shut down the API
		//Controllers.shutdown();
	}


	public static void p(String s) {
		System.out.println(s);
	}

}
