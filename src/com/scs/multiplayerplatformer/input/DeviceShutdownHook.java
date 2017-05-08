package com.scs.multiplayerplatformer.input;

import org.gamepad4j.Controllers;

public class DeviceShutdownHook extends Thread {

	public DeviceShutdownHook() {
		super(DeviceShutdownHook.class.getSimpleName());
		
	}
	
	
	public void run() {
		if (DeviceThread.USE_CONTROLLERS) {
			Controllers.shutdown();
		}
	}

}
