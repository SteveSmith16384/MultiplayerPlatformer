package com.scs.worldcrafter.game;

import java.util.Date;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import ssmith.android.framework.AbstractActivity;
import ssmith.android.io.IOFunctions;
import ssmith.lang.DateFunctions;

import com.scs.worldcrafter.Statics;
import com.scs.worldcrafter.graphics.blocks.Block;
import com.scs.worldcrafter.graphics.mobs.AbstractMob;
import com.scs.worldcrafter.mapgen.LoadSavedMap;

public class SaveMapThread extends Thread {

	private GameModule game;

	public SaveMapThread(GameModule _game) {
		super("SaveMapThread");

		game = _game;
	}


	public void run() {
		try {
			if (game != null) {
				if (game.new_grid != null) {
					StringBuffer str = new StringBuffer();
					str.append("# " + Statics.NAME + " saved map.\n");
					str.append("# Saved on " + DateFunctions.FormatDate(new Date(), "dd-MM-yyyy HH:mm") + ".\n");

					str.append(LoadSavedMap.HEADER_TAG + "\n");
					str.append(LoadSavedMap.VERSION_TAG + "\n");
					str.append(LoadSavedMap.VERSION + "\n");
					str.append(LoadSavedMap.MAP_DATA_TAG + "\n");

					for (int y=0 ; y<game.new_grid.getBlocksHeight() ; y++) {
						for (int x=0 ; x<game.new_grid.getBlocksWidth() ; x++) {
							byte type = 0;
							boolean on_fire = false;
							if (x <= game.map_loaded_up_to_col) {
								Block b = (Block)game.new_grid.getBlockAtMap_MaybeNull(x, y);
								if (b != null) {
									type = b.getType();
									on_fire = b.on_fire;
								}
							} else {
								// It might not have been added to the grid yet!
								type = game.original_level_data.getGridDataAt(x, y).type;
							}
							str.append(type + "|");
							if (on_fire) {
								str.append(LoadSavedMap.ON_FIRE + "|");
							}
							str.append(",");
						}
						str.append("\n");
					}

					if (Statics.has_infinite_blocks == false) {
						// Save inventory
						str.append(LoadSavedMap.INV_DATA_TAG + "\n");
						BlockInventory bi = game.inv;
						Set<Entry<Byte, Integer>> s = bi.entrySet();
						Iterator<Entry<Byte, Integer>> it = s.iterator();
						while (it.hasNext()) {
							Entry<Byte, Integer> e = it.next();
							str.append(e.getKey() + "," + e.getValue() + "\n");
						}
					}

					// Save mobs
					str.append(LoadSavedMap.MOB_DATA_TAG + "\n");
					for (IProcessable ip : game.getOthersInstant()) {
						if (ip instanceof AbstractMob && ip != game.player) {
							AbstractMob mob = (AbstractMob)ip;
							str.append(mob.getType() + ", " + (int)mob.getWorldX() + "," + (int)mob.getWorldY() + "\n");
						}
					}

					str.append("\n");


					IOFunctions.SaveText(Statics.GetExtStorage() + "/" + game.filename, str.toString(), false);
					//new SendEventThread(Statics.EV_SAVED);
				}
			}
		} catch (Exception ex) {
			//BugSenseHandler.log(Statics.NAME, ex);
			AbstractActivity.HandleError(Statics.act, ex);
		}
	}

}
