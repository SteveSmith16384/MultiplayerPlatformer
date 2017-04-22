package com.scs.worldcrafter.game;

import java.util.HashMap;
import java.util.Iterator;

import ssmith.android.framework.AbstractActivity;

import com.scs.worldcrafter.Statics;
import com.scs.worldcrafter.graphics.blocks.Block;

public final class BlockInventory extends HashMap<Byte, Integer> {

	private static final long serialVersionUID = 1L;

	private GameModule game;

	public BlockInventory(GameModule _game) {
		super();

		game = _game;

		if (Statics.has_infinite_blocks) {
			try {
				for (byte i=2 ; i<=Block.MAX_BLOCK_ID ; i++) {
					Block b = new Block(_game, i, -1, -1); // Check it exists
					if (Block.AddInConstructionMode(b.getType())) {
						this.addBlock(i, 99);
					}
					i++;
				}
			} catch (RuntimeException ex) {
				//BugSenseHandler.log(Statics.NAME, ex);
				AbstractActivity.HandleError(null, ex);
			}
		}	
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
			if (Statics.GAME_MODE != Statics.GM_WORLDCRAFTER) {
				if (this.game.player != null && this.game.player.hasBlockSelected() == false) {
					if (super.containsKey(id)) {
						this.game.player.setItemType(id);
						game.setCurrentItemIcon();
					}
				}
			}
		} else { // Removing item
			if (Statics.has_infinite_blocks == false) { // Don't remove in Construction mode
				if (hasBlock(game.player.getCurrentItemType()) == false) { // Is it the last item of this type
					game.player.setItemType(Block.NOTHING_DAYLIGHT); // Default
					Iterator<Byte> it = this.keySet().iterator();
					if (it.hasNext()) {
						byte other_item = it.next();
						game.player.setItemType(other_item);
					}
					game.setCurrentItemIcon();
				}
			}
		}
		game.updateInvIconAmt();

	}


}

