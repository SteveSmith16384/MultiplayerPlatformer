package com.scs.multiplayerplatformer;


import ssmith.android.framework.AbstractActivity;


/**
 * This is for initially loading the game.
 *
 */
public final class Main_Activity extends AbstractActivity {

	public void onCreate() {
		super.onCreate();

		//Statics.BACKGROUND_IMAGE = "ninja_background2.jpg";
		//Statics.PLAYER_SPEED_DIVISOR = 7f;
	}

	
	public static void main(String args[]) {
		try {
			Main_Activity act = new Main_Activity();
			act.onCreate();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}


}
