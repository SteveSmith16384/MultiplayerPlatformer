package com.scs.multiplayerplatformer.sfx;

public interface ISfxPlayer {

	void playerJumped();
	
	void playerReachedEnd();

	void levelStart();
	
	void playerDied();

	void enemyDied();
	
	void blockCrumbled();
	
	void slime();

	void playSound(String f);
}
