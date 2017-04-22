package com.scs.ninja.main.full;


import ssmith.android.framework.AbstractActivity;
import android.os.Bundle;

import com.scs.ninja.main.lite.R;
import com.scs.worldcrafter.Statics;

/**
 * This is for initially loading the game.
 *
 */
public final class Main_Activity extends AbstractActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Statics.GAME_MODE = Statics.GM_NINJA;
		//Statics.MARKET_URL = "market://details?id=com.scs.ninja.main.full";
		Statics.PACKAGE = "com.scs.ninja.main.full";
		Statics.BACKGROUND_R = R.drawable.ninja_background2;
		Statics.PLAYER_SPEED_DIVISOR = 7f;
		Statics.FULL_VERSION = true;
	}

}
