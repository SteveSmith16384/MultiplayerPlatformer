package com.scs.multiplayerplatformer.sfx;

import ssmith.audio.SoundCacheThread;

public class StdSfxPlayer extends SoundCacheThread implements ISfxPlayer {

	public StdSfxPlayer(String _root) {
		super(_root);
	}

	@Override
	public void playerJumped() {
		playSound("8-Bit Sound Library/Jump_00");		
	}

	@Override
	public void playerReachedEnd() {
		playSound("8-Bit Sound Library/Jingle_Win_00");		
		
	}

	@Override
	public void playerDied() {
		playSound("8-Bit Sound Library/Jingle_Lose_00");		
		
	}

	@Override
	public void enemyDied() {
		playSound("8-Bit Sound Library/Hit_00");		
	}

	@Override
	public void blockCrumbled() {
		playSound("crumbling");		
	}
	

	@Override
	public void slime() {
		playSound("slime");		
	}

	@Override
	public void levelStart() {
		playSound("8-Bit Sound Library/Jingle_Win_01");
		
	}

}
