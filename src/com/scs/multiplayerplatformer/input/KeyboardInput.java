package com.scs.multiplayerplatformer.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class KeyboardInput implements IInputDevice, KeyListener {

	private volatile boolean left, right, jump, down, fire;

	public KeyboardInput(JFrame frame) {
		frame.addKeyListener(this);
	}

	
	@Override
	public boolean isLeftPressed() {
		return left;
	}
	

	@Override
	public boolean isRightPressed() {
		return right;
	}

	
	@Override
	public boolean isJumpPressed() {
		return jump;
	}
	

	@Override
	public boolean isDownPressed() {
		return down;
	}


	@Override
	public int getAngle() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	@Override
	public boolean isThrowPressed() {
		return fire;
	}
	

	@Override
	public int getThrowDuration() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void keyPressed(KeyEvent ke) {
		switch (ke.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			left = true;
			break;

		case KeyEvent.VK_RIGHT:
			right = true;
			break;

		case KeyEvent.VK_UP:
			jump = true;
			break;

		case KeyEvent.VK_DOWN:
			down = true;
			break;

		case KeyEvent.VK_SPACE:
			fire = true;
			break;

		}

	}

	
	@Override
	public void keyReleased(KeyEvent ke) {
		switch (ke.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			left = false;
			break;

		case KeyEvent.VK_RIGHT:
			right = false;
			break;

		case KeyEvent.VK_UP:
			jump = false;
			break;

		case KeyEvent.VK_DOWN:
			down = false;
			break;

		case KeyEvent.VK_SPACE:
			fire = false;
			break;

		}

	}
	

	@Override
	public void keyTyped(KeyEvent ke) {

	}

}
