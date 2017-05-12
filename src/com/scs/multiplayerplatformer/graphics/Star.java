package com.scs.multiplayerplatformer.graphics;

import ssmith.android.lib2d.MyPointF;

import com.scs.multiplayerplatformer.game.GameModule;
import com.scs.multiplayerplatformer.graphics.mobs.PlayersAvatar;

public class Star extends Collectable {

	public Star(GameModule _game, MyPointF _start, float size) {
		super(_game, _start, size);
	}

	@Override
	protected void collected(PlayersAvatar avatar) {
		avatar.player.score += 100;
		
	}

}
