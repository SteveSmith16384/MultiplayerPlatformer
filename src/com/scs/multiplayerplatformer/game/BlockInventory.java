package com.scs.multiplayerplatformer.game;

import java.util.HashMap;
import java.util.Iterator;

import com.scs.multiplayerplatformer.graphics.blocks.Block;
import com.scs.multiplayerplatformer.graphics.mobs.PlayersAvatar;

public final class BlockInventory extends HashMap<Byte, Integer> {

	private static final long serialVersionUID = 1L;

	private PlayersAvatar player;

	public BlockInventory(PlayersAvatar _player) {
		super();

		player = _player;
	}


	public boolean hasBlock(byte id) {
		if (super.containsKey(id)) {
			int amt = super.get(id);
			return amt > 0;
		}
		return false;
	}


	public void addBlock(byte id, int amt) {
		int old_amt = 0;
		if (super.containsKey(id)) {
			old_amt = super.get(id);
		}
		old_amt += amt;
		if (old_amt > 0) {
			super.put(id, old_amt);
		} else {
			super.remove(id);
		}

		// Give it to the player?
		if (amt > 0) { // Adding an item
			if (player != null && player.hasBlockSelected() == false) {
				if (super.containsKey(id)) {
					player.setItemType(id);
					//game.setCurrentItemIcon(player);
				}
			}
		} else { // Removing item
			if (hasBlock(player.getCurrentItemType()) == false) { // Is it the last item of this type
				player.setItemType(Block.NOTHING_DAYLIGHT); // Default
				Iterator<Byte> it = this.keySet().iterator();
				if (it.hasNext()) {
					byte other_item = it.next();
					player.setItemType(other_item);
				}
				//game.setCurrentItemIcon(player);
			}
		}
		//game.updateInvIconAmt(player);

	}


}

