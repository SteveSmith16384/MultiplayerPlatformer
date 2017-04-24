package com.scs.worldcrafter.start.ninja;

import java.awt.Point;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.scs.worldcrafter.Statics;
import com.scs.worldcrafter.graphics.blocks.Block;
import com.scs.worldcrafter.graphics.blocks.SimpleBlock;
import com.scs.worldcrafter.mapgen.AbstractLevelData;
import com.scs.worldcrafter.mapgen.SimpleMobData;

import ssmith.android.framework.AbstractActivity;
import ssmith.lang.NumberFunctions;

public class LoadSavedMap extends AbstractLevelData {

	// Tags
	public static final String HEADER_TAG = "Worldcrafter Save File";
	public static final String VERSION_TAG = "File Version";
	public static final String MAP_DATA_TAG = "Map_data";
	public static final String INV_DATA_TAG = "Inv_data";
	public static final String MOB_DATA_TAG = "Mob_data";

	// Block Codes
	public static final String ON_FIRE = "of";

	public static final int VERSION = 2;

	private String stage = "", filename = "";
	private int version_found = 1;

	public LoadSavedMap(String _filename) {
		super();
		
		filename = _filename;
	}


	@Override
	public void run() {
		try {
			List<String> text = Files.readAllLines(FileSystems.getDefault().getPath(".", filename));//IOFunctions.ReadRawText(Statics.act, r_level);
			String lines[] = new String[text.size()];
			lines = text.toArray(lines);
			int map_row = 0;
			ArrayList<SimpleBlock[]> rows = new ArrayList<SimpleBlock[]>();
			max_rows = lines.length;
			int max_row_length = -1;
			for (row=0 ; row<lines.length ; row++) {
				lines[row] = lines[row].replaceAll("\"", "");
				if (lines[row].startsWith("#") == false && lines[row].length() > 0) {
					String cell = lines[row];
					if (cell.indexOf(",") >= 0) {
						cell = lines[row].split(",")[0];
					}
					if (cell.equalsIgnoreCase(MAP_DATA_TAG)) {
						stage = MAP_DATA_TAG;
						continue;
					} else if (cell.equalsIgnoreCase(VERSION_TAG)) {
						stage = VERSION_TAG;
						continue;
					} else if (cell.equalsIgnoreCase(INV_DATA_TAG)) {
						stage = INV_DATA_TAG;
						continue;
					} else if (cell.equalsIgnoreCase(MOB_DATA_TAG)) {
						stage = MOB_DATA_TAG;
						continue;
					} 

					if (stage.equalsIgnoreCase(VERSION_TAG)) {
						this.version_found = Integer.parseInt(cell);
					} else if (stage.equalsIgnoreCase(MAP_DATA_TAG)) {
						String blocks[] = lines[row].split(",");
						/*if (last_row_length < 0) {
							last_row_length = blocks.length;
						} else if (blocks.length != last_row_length) {
							throw new RuntimeException("Discrepancy in number of block in row " + row);
						}*/
						if (max_row_length < blocks.length) {
							max_row_length = blocks.length;
						}
						SimpleBlock this_data[] = new SimpleBlock[blocks.length];
						for (int x=0 ; x<blocks.length ; x++) {
							this_data[x] = new SimpleBlock();
							if (version_found == 1) {
								this_data[x].type = Byte.parseByte(blocks[x]);
							} else if (version_found == 2) {
								String pieces[] = blocks[x].split("\\|");
								if (NumberFunctions.IsNumeric(pieces[0])) { // Std block
									byte b = Byte.parseByte(pieces[0]);
									this_data[x].type = b;//Byte.parseByte(b);
								} else { // Special code
									//checkSpecial(this_data[x], pieces[0], x, map_row);
									if (pieces[0].length() > 0) {
										/*if (pieces[0].toLowerCase().startsWith("c:")) {
											this_data[x].collectable_type = pieces[0].substring(2);
										} else*/
										if (pieces[0].toLowerCase().startsWith("m:")) {
											mobs.add(new SimpleMobData(NumberFunctions.ParseByte(pieces[0].substring(2)), x * Statics.SQ_SIZE, (row-6) * Statics.SQ_SIZE)); // -6 so we start above it
										} else {
											if (Statics.RELEASE_MODE == false) {
												throw new RuntimeException("Unknown code: " + pieces[0]);
											}
										}
									}
								}
								for (int i=1 ; i<pieces.length ; i++) {
									if (pieces[i].equalsIgnoreCase(ON_FIRE)) {
										this_data[x].on_fire = true;
									}
								}
							} else {
								throw new RuntimeException("Unknown file version: " + this.version_found);
							}
							if (this_data[x].type == Block.AMULET) {
								amulet_pos = new Point(x, map_row);
							}
						}
						rows.add(this_data);
						map_row++;
					} else if (stage.equalsIgnoreCase(INV_DATA_TAG)) {
						// Load inventory
							if (block_inv == null) {
								super.block_inv = new HashMap<Byte, Integer>();
							}
							String line[] = lines[row].split(",");
							super.block_inv.put(NumberFunctions.ParseByte(line[0]), NumberFunctions.ParseInt(line[1]));
					} else if (stage.equalsIgnoreCase(MOB_DATA_TAG)) {
						// Load mobs
						/*if (mobs == null) {
							mobs = new ArrayList<SimpleMobData>();
						}*/
						String line[] = lines[row].split(",");
						mobs.add(new SimpleMobData(NumberFunctions.ParseByte(line[0].trim()), NumberFunctions.ParseInt(line[1]), NumberFunctions.ParseInt(line[2])));
					} else {
						// Do nothing
					}
				}
			}

			// Copy the data into the main array
			data = new SimpleBlock[max_row_length][rows.size()];
			int row_num = 0;
			for (SimpleBlock[] row : rows) {
				for (int x=0 ; x<row.length ; x++) {
					if (row[x] != null) {
						data[x][row_num] = row[x];
					}
				}
				row_num++;
			}

		} catch (Exception e) {
			AbstractActivity.HandleError(e);
		}

	}


	/*private void checkSpecial(SimpleBlock b, String code, int map_x, int map_y) {
		if (code.equalsIgnoreCase(AbstractCollectable.MEDIKIT)) {
			//mobs.add(new SimpleMobData(NumberFunctions.ParseByte(line[0].trim()), NumberFunctions.ParseInt(line[1]), NumberFunctions.ParseInt(line[2])));
			b.collectable_type = code;
		} else {
			throw new RuntimeException("Unknown collectable type: " + code)
		}
	}*/
}
