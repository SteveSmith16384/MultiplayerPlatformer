package com.scs.worldcrafter.main.lite;

import ssmith.android.framework.AbstractActivity;
import android.os.Bundle;

import com.scs.worldcrafter.Statics;

/**
 * This is for initially loading the game.
 *
 */
public final class Main_Activity extends AbstractActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Statics.GAME_MODE = Statics.GM_WORLDCRAFTER;
		//Statics.MARKET_URL = "market://details?id=com.scs.worldcrafter.main.lite";
		Statics.PACKAGE = "com.scs.worldcrafter.main.lite";
	}

}
