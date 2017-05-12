package com.scs.multiplayerplatformer.input;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import org.gamepad4j.ButtonID;
import org.gamepad4j.Controllers;
import org.gamepad4j.IController;

import ssmith.lang.Functions;

public class DeviceThread extends Thread {

	public static boolean USE_CONTROLLERS = true;

	private Map<Integer, IInputDevice> createdDevices = new HashMap<>();
	private IInputDevice keyboard;
	private List<NewControllerListener> listeners = new ArrayList<>();

	public DeviceThread(JFrame window) {
		super(DeviceThread.class.getSimpleName());

		this.setDaemon(true);

		try {
			Controllers.initialize();
			Runtime.getRuntime().addShutdownHook(new DeviceShutdownHook());
		} catch (Throwable ex) {
			ex.printStackTrace();
			USE_CONTROLLERS = false;
		}

		keyboard = new KeyboardInput(window);
		start();

	}

	
	public Collection<IInputDevice> getDevices() {
		return this.createdDevices.values();
	}

	public void run() {
		try {
			while (true) {
				IController[] gamepads = null;
				if (USE_CONTROLLERS) {
					Controllers.checkControllers();
					gamepads = Controllers.getControllers();

					for (IController gamepad : gamepads) {
						if (gamepad.isButtonPressed(ButtonID.FACE_DOWN)) {
							synchronized (createdDevices) {
								if (createdDevices.get(gamepad.getDeviceID()) == null) {
									this.createController(gamepad.getDeviceID(), new PS4Controller(gamepad));
								}
							}
						}
					}

				}
				if (keyboard.isThrowPressed()) {
					synchronized (createdDevices) {
						//todo - re-add if (createdDevices.get(-1) == null) {
							this.createController(-1, keyboard);
						//}
					}
				}
				Functions.delay(100);
			} 
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


	private void createController(int id, IInputDevice input) {
		synchronized (createdDevices) {
			createdDevices.put(id, input);
		}

		synchronized (listeners) {
			for (NewControllerListener l : this.listeners) {
				l.newController(input);
			}
		}
	}


	public void addListener(NewControllerListener l) {
		synchronized (listeners) {
			this.listeners.add(l);
		}		
	}
}
