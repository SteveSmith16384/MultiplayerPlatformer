package com.scs.multiplayerplatformer.sfx;

import ssmith.audio.SoundCacheThread;

public final class StdSfxPlayer extends SoundCacheThread implements ISfxPlayer {

	public StdSfxPlayer(String _root) {
		super(_root);
		
	}

	@Override
	public void playerJumped() {
		playSound("8-Bit Sound Library/Jump_00.wav");		
	}

	@Override
	public void playerReachedEnd() {
		playSound("8-Bit Sound Library/Jingle_Win_00.wav");		
		
	}

	@Override
	public void playerDied() {
		playSound("8-Bit Sound Library/Jingle_Lose_00.wav");		
		
	}

	@Override
	public void enemyDied() {
		playSound("8-Bit Sound Library/Hit_00.wav");		
	}

	@Override
	public void blockCrumbled() {
		playSound("crumbling.wav");		
	}
	

	@Override
	public void slime() {
		playSound("slime.wav");		
	}

	@Override
	public void levelStart() {
		playSound("8-Bit Sound Library/Jingle_Win_01.wav");
		
	}

}
