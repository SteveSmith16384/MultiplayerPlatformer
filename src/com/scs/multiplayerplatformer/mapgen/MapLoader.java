package com.scs.multiplayerplatformer.mapgen;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import ssmith.lang.NumberFunctions;

import com.scs.multiplayerplatformer.Statics;
import com.scs.multiplayerplatformer.graphics.blocks.Block;

public final class MapLoader extends AbstractLevelData {

	private static final String MAP_DIR = "./maps/";
	private static final int MAX_PREV_MAPS = 3;

	// Tags
	public static final String VERSION_TAG = "File Version";
	public static final String MAP_DATA_TAG = "Map_data";
	public static final String MOB_DATA_TAG = "Mob_data";
	public static final String NAME_TAG = "Name:";

	public static final int VERSION = 2;

	private String stage = "", fullpath = "";
	private boolean isResource;
	private int versionFound = 1;
	public String mapName = "";
	private static ArrayList<String> prevMaps = new ArrayList<>();

	public MapLoader(String _filename, boolean _is_resource) {
		super();

		fullpath = MAP_DIR + Statics.GAME_MODE.toString() + "/" + _filename;
		isResource = _is_resource;
		levelName = this.fullpath;
	}


	public static String GetRandomMap() {
		List<String> csv = GetMaps();
		String filename = csv.get(NumberFunctions.rnd(0, csv.size()-1));
		while (prevMaps.contains(filename)) {
			filename = csv.get(NumberFunctions.rnd(0, csv.size()-1));
		}
		prevMaps.add(filename);
		while (prevMaps.size() > MAX_PREV_MAPS) {
			prevMaps.remove(0);
		}
		return filename;
	}


	public static List<String> GetMaps() {
		String[] maps = new File(MAP_DIR + Statics.GAME_MODE.toString()).list();
		List<String> csv = new ArrayList<>();

		for(String s : maps) {
			if (s.toLowerCase().endsWith(".csv")) {
				csv.add(s);
			}
		}

		return csv;
	}


	@Override
	public void getMap() {
		try {
			List<String> text = new ArrayList<>();
			if (isResource) {
				BufferedReader txtReader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/assets/maps/" + fullpath)));
				while (true) {
					String s = txtReader.readLine();
					if (s != null) {
						text.add(s);
					} else {
						break;
					}
				}
				txtReader.close();
			} else {
				text = Files.readAllLines(FileSystems.getDefault().getPath(fullpath, ""));
			}


			String lines[] = new String[text.size()];
			lines = text.toArray(lines);

			int map_row = 0;
			List<byte[]> rows = new ArrayList<byte[]>();
			int max_row_length = -1;
			for (int row=0 ; row<lines.length ; row++) {
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
					} else if (cell.equalsIgnoreCase(MOB_DATA_TAG)) {
						stage = MOB_DATA_TAG;
						continue;
					} else if (cell.equalsIgnoreCase(NAME_TAG)) {
						mapName = cell.substring(NAME_TAG.length());
						continue;
					} 

					if (stage.equalsIgnoreCase(VERSION_TAG)) {
						//if (version_found == 0) {
							this.versionFound = Integer.parseInt(cell);
						//}
					} else if (stage.equalsIgnoreCase(MAP_DATA_TAG)) {
						String blocks[] = lines[row].split(",");
						if (max_row_length < blocks.length) {
							max_row_length = blocks.length;
						}
						byte this_data[] = new byte[blocks.length];
						for (int x=0 ; x<blocks.length ; x++) {
							if (versionFound == 1) {
								this_data[x] = Byte.parseByte(blocks[x]);
							} else if (versionFound == 2) {
								String pieces[] = blocks[x].split("\\|");
								if (NumberFunctions.IsNumeric(pieces[0])) { // Std block
									byte b = Byte.parseByte(pieces[0]);
									this_data[x] = b;
								} else { // Special code
									if (pieces[0].length() > 0) {
										if (pieces[0].toLowerCase().startsWith("m:")) {
											mobs.add(new SimpleMobData(NumberFunctions.ParseByte(pieces[0].substring(2)), x * Statics.SQ_SIZE, (row-6) * Statics.SQ_SIZE)); // -6 so we start above it
										} else {
											/*if (Statics.RELEASE_MODE == false) {
												throw new RuntimeException("Unknown code: " + pieces[0]);
											}*/
										}
									}
								}
							} else {
								throw new RuntimeException("Unknown file version: " + this.versionFound);
							}
							if (this_data[x] == Block.START_POSITION) {
								super.setStartPos(x, map_row);
								this_data[x] = Block.NOTHING_DAYLIGHT;
							}
						}
						rows.add(this_data);
						map_row++;
					} else if (stage.equalsIgnoreCase(MOB_DATA_TAG)) {
						// Load mobs
						String line[] = lines[row].split(",");
						mobs.add(new SimpleMobData(NumberFunctions.ParseByte(line[0].trim()), NumberFunctions.ParseInt(line[1]), NumberFunctions.ParseInt(line[2])));
					} else {
						// Do nothing
					}
				}
			}

			// Copy the data into the main array
			try {
				data = new byte[max_row_length][rows.size()];
				int row_num = 0;
				for (byte[] row : rows) {
					for (int x=0 ; x<row.length ; x++) {
						data[x][row_num] = row[x];
					}
					row_num++;
				}
			} catch (NegativeArraySizeException ex) {
				throw new RuntimeException("Error loading map '" + fullpath + "': No map data tag found", ex);
			}
		} catch (Exception ex) {
			throw new RuntimeException("Error loading map '" + fullpath + "':", ex);
		}
	}


}

