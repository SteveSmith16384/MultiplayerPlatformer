package com.scs.ninja.main.full;


import ssmith.android.compatibility.Bundle;
import ssmith.android.framework.AbstractActivity;

import com.scs.worldcrafter.Statics;

/**
 * This is for initially loading the game.
 *
 */
public final class Main_Activity extends AbstractActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Statics.BACKGROUND_R = "ninja_background2";
		Statics.PLAYER_SPEED_DIVISOR = 7f;
	}

	
	public static void main(String args[]) {
		try {
			Main_Activity act = new Main_Activity();
			act.onCreate(null);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}


}
