package com.scs.worldcrafter.game;

import ssmith.lang.Functions;
import android.graphics.Point;

import com.scs.worldcrafter.graphics.blocks.Block;

/**
 * If block added, just add dark underneath them all.
 * If block remove, find out next highest light/dark and apply
 *
 */
public class DarknessAdjuster implements IProcessable {

	private static final int INTERATIONS = 15;

	private GameModule game;
	public Point current_coords;
	private byte add_type = -1; // Add light or dark?
	private DarknessAdjusterController controller;

	public DarknessAdjuster(GameModule _game, DarknessAdjusterController _controller, int map_x) {
		super();

		game = _game;
		controller = _controller;
		current_coords = new Point(map_x, 0);

		this.add_type = Block.NOTHING_DAYLIGHT; // Starts as daylight
	}


	@Override
	public void process(long interpol) {
		for (int i=0 ; i<INTERATIONS ; i++) {
			if (this.doBlock() == false) { // Reached bottom?
				break;
			}
		}
	}


	private boolean doBlock() {
		if (current_coords.y >= game.original_level_data.getGridHeight()) {
			this.remove();
			return false;
		} else {
			Block b = (Block)game.new_grid.getBlockAtMap_MaybeNull(this.current_coords.x, this.current_coords.y);
			if (b == null) {
				if (this.add_type == Block.DARKNESS || this.add_type == Block.DARKNESS2) {
					game.addBlock(this.add_type, this.current_coords.x, this.current_coords.y, false);
				}
			} else if (Block.BlocksLight(b.getType()) == false) {
				if (b.getType() == Block.DARKNESS || b.getType() == Block.DARKNESS2) {
					if (this.add_type != Block.DARKNESS && this.add_type != Block.DARKNESS2) { // Don't overwrite it
						game.addBlock(this.add_type, this.current_coords.x, this.current_coords.y, false);
					}
				}
			} else { // Blocks light
				if (Functions.rnd(1, 2) == 1) {
					this.add_type = Block.DARKNESS;
				} else {
					this.add_type = Block.DARKNESS2;
				}
			}
		}
		current_coords.y += 1;
		return true;
	}


	public void remove() {
		this.game.removeFromProcess(this);
		this.controller.signalFinished(this);
	}

}

