package com.scs.multiplayerplatformer.input;

public interface NewControllerListener {

	void newController(IInputDevice input);

	void controllerRemoved(IInputDevice input);

}
