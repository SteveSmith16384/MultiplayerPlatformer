package com.scs.worldcrafter.mapgen;

import java.util.ArrayList;

import ssmith.lang.DateFunctions;
import ssmith.lang.Functions;

import com.scs.worldcrafter.Statics;
import com.scs.worldcrafter.graphics.blocks.Block;
import com.scs.worldcrafter.graphics.blocks.SimpleBlock;
import com.scs.worldcrafter.graphics.mobs.AbstractMob;

public class MinecraftLevelData extends AbstractLevelData {

	private static final int MAP_WIDTH = 400;
	private static final int MAP_HEIGHT = 150;

	public MinecraftLevelData() {
		super();
	}


	public void run() {
		data = new SimpleBlock[MAP_WIDTH][MAP_HEIGHT];
		mobs = new ArrayList<SimpleMobData>();

		int x, y;
		int WATER_HEIGHT = MAP_HEIGHT / 4;
		// Create water level
		for(y=0 ; y<MAP_HEIGHT ; y++) {
			for(x=0 ; x<MAP_WIDTH ; x++) {
				data[x][y] = new SimpleBlock();
				if (y>WATER_HEIGHT) {
					data[x][y].type = Block.WATER;
				} else {
					data[x][y].type = Block.NOTHING_DAYLIGHT;
				}
			}
		}

		// Create land
		y = (WATER_HEIGHT) - 2;
		for(x=0 ; x<MAP_WIDTH ; x++) {
			if (y > MAP_HEIGHT * .2f) {
				data[x][y].type = Block.GRASS;
			} else {
				data[x][y].type = Block.SNOW;
			}
			int rock_level = Functions.rnd(2, 4);
			for (int i=1 ; i<= rock_level ; i++) {
				data[x][y+i].type = Block.SOIL;
			}
			for (int y2=y+rock_level+1 ; y2<MAP_HEIGHT ; y2++) {
				if (Functions.rnd(1, 2500) == 1) {
					data[x][y2].type = Block.GOLD;
				} else {
					int i = Functions.rnd(1, 25);
					switch (i) {
					case 1:
						data[x][y2].type = Block.TANGLEWEED;
						break;
					case 2:
						data[x][y2].type = Block.DARKNESS; // Need this to overwrite the water
						break;
					case 3:
						data[x][y2].type = Block.SOIL;
						break;
					case 4:
						data[x][y2].type = Block.IRON_ORE;
						break;
					case 5:
						data[x][y2].type = Block.CLAY;
						break;
					case 6:
						data[x][y2].type = Block.FLINT;
						break;
					default:
						data[x][y2].type = Block.ROCK;
					}
				}
			}

			// Adjust height
			if (x > 10) {
				if (x < MAP_WIDTH/4) {
					y = y + Functions.rnd(-1, 1);
				} else if (x < MAP_WIDTH/2) {
					y = y + Functions.rnd(-2, 2);
				} else if (x < MAP_WIDTH * .75) {
					y = y + Functions.rnd(-4, 4);
				} else {
					y = y + Functions.rnd(-10, 10);
				}
			}
			// Check we're not too high/low
			if (y < 5) {
				y = 10;
			} else if (y > MAP_HEIGHT - 5) {
				y = (MAP_HEIGHT-10);
			}
			
		}


		// Add sand
		byte[] types_sand = {Block.SOIL, Block.ROCK, Block.DARKNESS, Block.DARKNESS2, Block.GRASS};
		for(y=1 ; y<MAP_HEIGHT-1 ; y++) {
			for(x=1 ; x<MAP_WIDTH-1 ; x++) {
				if (data[x][y].type == Block.WATER) {
					checkAndChangeAdjacentSquares(x, y, types_sand, Block.SAND);
				}
			}
		}


		// Add trees
		long now = System.currentTimeMillis();
		for (int i=0 ; i<MAP_WIDTH/20 ; i++) {
			if (System.currentTimeMillis() > now + DateFunctions.MINUTE/10) {
				break;
			}
			x = Functions.rnd(10, MAP_WIDTH-10);
			for (y=5 ; y<MAP_HEIGHT ; y++) {
				if (data[x][y].type != Block.NOTHING_DAYLIGHT) {
					if (Block.IsSolidGround(data[x][y].type) == false) {
						i--;
						break;
					}
					data[x][y-1].type = Block.TREE_BARK;
					data[x][y-2].type = Block.TREE_BARK;
					data[x][y-3].type = Block.TREE_BARK;
					data[x][y-4].type = Block.TREE_BARK;

					data[x-1][y-2].type = Block.TREE_BRANCH_LEFT;
					if (Functions.rnd(1, 3) > 1) {
						data[x-1][y-1].type = Block.ACORN;
					}
					data[x+1][y-2].type = Block.TREE_BRANCH_RIGHT;
					if (Functions.rnd(1, 3) > 1) {
						data[x+1][y-1].type = Block.ACORN;
					}
					data[x-1][y-4].type = Block.TREE_BRANCH_LEFT;
					if (Functions.rnd(1, 3) > 1) {
						data[x-1][y-3].type = Block.ACORN;
					}
					data[x+1][y-4].type = Block.TREE_BRANCH_RIGHT;
					if (Functions.rnd(1, 3) > 1) {
						data[x+1][y-3].type = Block.ACORN;
					}
					break;
				}
			}
		}			


		// Dungeons
		now = System.currentTimeMillis();
		for (int i=0 ; i<MAP_WIDTH/3 ; i++) {
			if (System.currentTimeMillis() > now + DateFunctions.MINUTE/10) {
				break;
			}
			int w = Functions.rnd(2, 15);
			int h = Functions.rnd(3, 15);
			int s_x = Functions.rnd(1, MAP_WIDTH-(w*2));
			int s_y = Functions.rnd(WATER_HEIGHT+10, MAP_HEIGHT-(h*2));

			boolean clear = true;
			for (y=s_y ; y<s_y+h ; y++) {
				for (x=s_x ; x<s_x+w ; x++) {
					if (data[x][y].type == Block.WATER) {
						clear = false;
						break;
					}
				}
			}

			if (clear) {
				// Build the dungeon
				boolean lava_floor = Functions.rnd(1, 10) == 1;
				boolean laid_chest = false;
				for (y=s_y ; y<s_y + h ; y++) {
					for (x=s_x ; x<s_x + w ; x++) {
						if ((y > s_y && y < s_y+h-1) && (x > s_x && x < s_x+w-1)) {
							if (Functions.rnd(1, 2) == 1 && (y == s_y+1 && x == s_x+1)) {
								data[x][y].type = Block.COBWEB;
							} else {
								if (Functions.rnd(1, 2) == 1) {
									data[x][y].type = Block.DARKNESS;
								} else {
									data[x][y].type = Block.DARKNESS2;
								}
							}
							if (lava_floor) {
								if (y == s_y+h-2) { // Is it the ground row
									data[x][y].type = Block.LAVA;
									//data[x][y].process_slow = true;
								}
							} else {
								if (laid_chest == false) {
									// put something in the dungeon
									if (y == s_y+h-2) { // Is it the ground row
										if (Functions.rnd(1, 6) == 1) {
											laid_chest = true;
											int t = Functions.rnd(1, 5);
											if (t == 1) {
												data[x][y].type = Block.DYNAMITE;
											} else if (t == 2) {
												data[x][y].type = Block.SLIME;
											} else {
												data[x][y].type = Block.CHEST;
											}
											if (Statics.monsters) {
												data[x-1][y].type = Block.MONSTER_GENERATOR;
											}
										}										
									}
								}
							}
						} else {
							data[x][y].type = Block.STONE;
						}
					}
				}
			}
		}


		// Sprinkle power-ups
		now = System.currentTimeMillis();
		for (int i=0 ; i<MAP_WIDTH/20 ; i++) {
			if (System.currentTimeMillis() > now + DateFunctions.MINUTE/10) { // Check we're not stuck in a loop
				break;
			}
			x = Functions.rnd(10, MAP_WIDTH-10);
			for (y=1 ; y<MAP_HEIGHT ; y++) {
				if (data[x][y].type != Block.NOTHING_DAYLIGHT) {
					if (Block.IsSolidGround(data[x][y].type) == false) {// != Block.SOIL && data[x][y].type != Block.GRASS && data[x][y].type != Block.SNOW) {
						i--;
						break;
					}
					int j = Functions.rnd(1, 7);
					switch (j) {
					case 1:
						data[x][y-1].type = Block.ACORN;
						break;
					case 2:
						data[x][y-1].type = Block.WHEAT_SEED;
						break;
					case 3:
						this.mobs.add(new SimpleMobData(AbstractMob.SHEEP, x * Statics.SQ_SIZE, (y-1) * Statics.SQ_SIZE));
						break;
					case 4:
						data[x][y-1].type = Block.WEEDS;
						break;
					case 5:
						this.mobs.add(new SimpleMobData(AbstractMob.PIG, x * Statics.SQ_SIZE, (y-1) * Statics.SQ_SIZE));
						break;
					case 6:
						this.mobs.add(new SimpleMobData(AbstractMob.CHICKEN, x * Statics.SQ_SIZE, (y-1) * Statics.SQ_SIZE));
						break;
					case 7:
						this.mobs.add(new SimpleMobData(AbstractMob.COW, x * Statics.SQ_SIZE, (y-2) * Statics.SQ_SIZE));
						break;
					default:
						// Do nothing
					}
					break;
				}
			}
		}			


		y = WATER_HEIGHT;
		while (true) {
			y += 20;
			if (y >= MAP_HEIGHT-10) {
				break;
			}
			createRiver(y, Block.DARKNESS2, false);
		}
		createRiver(MAP_HEIGHT-5, Block.LAVA, true);

		// Create walls along both sides to block everything in.
		for(y=0 ; y<MAP_HEIGHT ; y++) {
			data[0][y].type = Block.ADAMANTIUM;
			data[MAP_WIDTH-1][y].type = Block.ADAMANTIUM;
		}
		for(x=0 ; x<MAP_WIDTH ; x++) {
			data[x][MAP_HEIGHT-1].type = Block.ADAMANTIUM;
		}

		/*if (Statics.DEBUGGING) {
			start_pos = new Point(3, WATER_HEIGHT-5);
			this.mobs.add(new SimpleMobData(AbstractMob.SHEEP, 5 * Statics.SQ_SIZE, (WATER_HEIGHT-4)* Statics.SQ_SIZE));
			this.mobs.add(new SimpleMobData(AbstractMob.PIG, 7 * Statics.SQ_SIZE, (WATER_HEIGHT-4)* Statics.SQ_SIZE));
			data[5][WATER_HEIGHT-2].type = Block.LIT_FURNACE;
		}*/


	}


	private void createRiver(int y, byte type, boolean spotty) {
		// Create lava river
		for(int x=0 ; x<MAP_WIDTH ; x++) {
			for(int i=0 ; i <= 3 ; i++) {
				if (spotty == false || Functions.rnd(1, 5) > 1) {
					if (data[x][y-i].type != Block.MONSTER_GENERATOR) {
						data[x][y-i].type = type;
					}
				}
			}
			y = y + Functions.rnd(-1, 1);
			if (y >= MAP_HEIGHT-3) {
				y = MAP_HEIGHT-5;
			} else if (y <= 3) {
				y = 5;
			}
		}
	}


}
