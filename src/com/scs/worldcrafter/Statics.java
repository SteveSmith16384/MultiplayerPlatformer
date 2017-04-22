package com.scs.worldcrafter;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import ssmith.android.framework.ErrorReporter;

import ssmith.android.framework.AbstractActivity;
import ssmith.android.framework.modules.AbstractModule;
import ssmith.android.util.ImageCache;
import android.content.Context;
import android.os.Environment;

import com.scs.ninja.main.lite.R;
import com.scs.worldcrafter.start.StartupModule;
import com.scs.worldcrafter.start.ninja.StartupModule_Ninja;
import com.scs.worldcrafter.start.policecop.StartupModule_PoliceCop;

public final class Statics {
	
	public static final boolean DEBUGGING = false;
	public static final boolean ANDROID_MARKET = true;
	public static final boolean KOREAN = false;
	public static final boolean RELEASE_MODE = true; // Stricter if false
	
	public static final int SHURIKENS_FROM_BLOCK = 5;
	
	// Game Modes
	public static final int GM_WORLDCRAFTER = 0;
	public static final int GM_NINJA = 1;
	public static final int GM_POLICECOP = 2;
	
	// Settings changed depending on game mode
	public static int GAME_MODE = -1;
	public static String PACKAGE = "";
	public static int BACKGROUND_R;
	public static boolean FULL_VERSION = true;
	
	public static final String URL_FOR_CLIENT = "http://www.rafakrotiri.info"; // Do NOT add a slash to the end
	//public static final String URL_FOR_CLIENT = "http://10.0.0.18:8083"; // Do NOT add a slash to the end
	//public static final String URL_FOR_CLIENT = "http://10.0.2.2:8083"; // THIS IS FOR WHEN RUNNING IN AN EMULATOR.  Do NOT add a slash to the end.  

	// Sides
	public static final byte SD_PLAYERS_SIDE = 0;
	public static final byte SD_ENEMY_SIDE = 1;
	
	public static AbstractActivity act;
	public static String VERSION_NAME;
	public static String NAME;
	public static float SCREEN_WIDTH, SCREEN_HEIGHT;
	public static boolean SHOW_STATS = false;
	public static final int LOOP_DELAY = 30;
	public static final int TILT_THRESH = 7;
	public static final int MAX_INSTANTS = 999;
	public static final int DYNAMITE_DAMAGE = 10;
	public static final String EMAIL = "help@penultimateapps.com"; 
	public static final int VIBRATE_LEN = 30;
	public static final float LABEL_SPACING = 1.2f;
	public static final float JUMP_Y = -1.2f;
	public static float ACTIVATE_DIST, DEACTIVATE_DIST;
	public static final int MUSIC_R = R.raw.chippytoon;

	public static ImageCache img_cache;
	public static WCConfigFile cfg;

	// Fields for passing data
	public static Hashtable<String, Object> data = new Hashtable<String, Object>(); // For passing data

	// Game settings
	public static boolean show_tutorial = true;
	public static boolean los_to_see_monsters = false;
	public static boolean monsters = true;
	public static boolean player_loses_health = false;
	public static boolean amulet = false;
	public static boolean has_infinite_blocks = false;
	public static boolean has_timer = false;
	
	// Bitmap scales
	private static float SQ_SIZE_PCENT = 6f;
	public static float SQ_SIZE;
	public static int SQ_SIZE_INT;
	private static final float ICON_SIZE_PCENT = 11f;
	public static float ICON_SIZE;
	public static float PLAYER_WIDTH, PLAYER_HEIGHT;
	public static float PIG_WIDTH, PIG_HEIGHT;
	public static float COW_WIDTH, COW_HEIGHT;
	public static float PLAYER_SPEED_DIVISOR = 10f;
	public static float PLAYER_SPEED, SHEEP_SPEED, PIG_SPEED, ALIEN_SPEED, ZOMBIE_SPEED, ENEMY_NINJA_SPEED, SKELETON_SPEED, CHICKEN_SPEED, COW_SPEED;
	public static float PLAYER_FALL_SPEED, MAX_FALL_SPEED;
	private static final float ROCK_SIZE_PCENT = 3f;
	private static final float SLIME_SIZE_PCENT = 3f;
	public static float ROCK_SIZE, SLIME_SIZE;
	private static final float ROCK_SPEED_PCENT = 1.5f;
	public static float ROCK_SPEED;
	public static float ROCK_GRAVITY;
	private static final float BULLET_SPEED_PCENT = 2f;
	public static float BULLET_SPEED;
	private static final float BUBBLE_SPEED_PCENT = .5f;
	public static float BUBBLE_SPEED;
	private static final float PLATFORM_SPEED_PCENT = .4f;
	public static float PLATFORM_SPEED;
	public static float INVADER_WIDTH, INVADER_HEIGHT;
	public static float INVADER_SPEED;
	public static final float SUN_SIZE_PCENT = 14f;
	public static float SUN_SIZE;
	public static final float SUN_SPEED_PCENT = .01f;
	public static float SUN_SPEED;
	public static final float CLOUD_WIDTH_PCENT = 21f;
	public static float CLOUD_WIDTH;
	public static final float CLOUD_HEIGHT_PCENT = 14f;
	public static float CLOUD_HEIGHT;
	public static final float CLOUD_SPEED_PCENT = .4f;
	public static float CLOUD_SPEED;
	public static float WASP_WIDTH, WASP_HEIGHT;
	public static float WASP_SPEED;
	public static float HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT;
	public static float SPIDER_WIDTH, SPIDER_HEIGHT;
	public static float SPIDER_SPEED;
	
	public static final float SPACESHIP_BANKING_WIDTH_PCENT = 14f;
	public static float SPACESHIP_BANKING_WIDTH;
	public static final float SPACESHIP_BANKING_HEIGHT_PCENT = 21f;
	public static float SPACESHIP_BANKING_HEIGHT;

	public static boolean initd = false;

	
	public static void init(float screen_width, float screen_height, Context context) {
		if (initd == false) {
			if (screen_width < screen_height) {
				// Swap them
				float tmp = screen_width;
				screen_width = screen_height;
				screen_height = tmp;
			}
			
			if (GAME_MODE == GM_NINJA) {
				Statics.SQ_SIZE_PCENT = 8f;
			}
			
			img_cache = new ImageCache(context.getResources());
			//try {
				cfg = new WCConfigFile();
			/*} catch (IOException e) {
				//showToast("Unable to load config!");
			}*/

			SCREEN_WIDTH = screen_width;
			SCREEN_HEIGHT = screen_height;

			SQ_SIZE = screen_width * (SQ_SIZE_PCENT/100);
			SQ_SIZE_INT = (int)Math.ceil(SQ_SIZE);
			ICON_SIZE = screen_width * (ICON_SIZE_PCENT/100);
			PLAYER_WIDTH = SQ_SIZE * 0.8f;
			PLAYER_HEIGHT = (SQ_SIZE*2) * 0.9f;
			PIG_WIDTH = (SQ_SIZE * 1.5f)-2;
			PIG_HEIGHT = (SQ_SIZE)-1;
			COW_WIDTH = (SQ_SIZE * 2f)-2;
			COW_HEIGHT = (SQ_SIZE*1.5f)-1;
			PLAYER_SPEED = SQ_SIZE / PLAYER_SPEED_DIVISOR;//10f;
			SHEEP_SPEED = PLAYER_SPEED / 3f;
			PIG_SPEED = PLAYER_SPEED / 4f;
			CHICKEN_SPEED = PLAYER_SPEED / 3f;
			COW_SPEED = PLAYER_SPEED / 5f;
			PLAYER_FALL_SPEED = PLAYER_SPEED/3f;
			MAX_FALL_SPEED = Statics.SQ_SIZE/2; 
			/*PLAYER_JUMP_START_SPEED = screen_width * (PLAYER_JUMP_START_SPEED_PCENT/100);
			SHEEP_JUMP_START_SPEED = screen_width * (SHEEP_JUMP_START_SPEED_PCENT/100);
			PIG_JUMP_START_SPEED = screen_width * (PIG_JUMP_START_SPEED_PCENT/100);
			CHICKEN_JUMP_START_SPEED = screen_width * (CHICKEN_JUMP_START_SPEED_PCENT/100);
			COW_JUMP_START_SPEED = screen_width * (COW_JUMP_START_SPEED_PCENT/100);
			PLAYER_JUMP_REDUCTION = screen_width * (PLAYER_JUMP_REDUCTION_PCENT/100);
			SHEEP_JUMP_REDUCTION = screen_width * (SHEEP_JUMP_REDUCTION_PCENT/100);
			PIG_JUMP_REDUCTION = screen_width * (PIG_JUMP_REDUCTION_PCENT/100);
			CHICKEN_JUMP_REDUCTION = screen_width * (CHICKEN_JUMP_REDUCTION_PCENT/100);
			COW_JUMP_REDUCTION = screen_width * (COW_JUMP_REDUCTION_PCENT/100);*/
			ALIEN_SPEED = PLAYER_SPEED * 0.5f;
			ZOMBIE_SPEED = PLAYER_SPEED * 0.15f;
			ENEMY_NINJA_SPEED = PLAYER_SPEED * 0.25f;
			SKELETON_SPEED = PLAYER_SPEED * 0.25f;
			ROCK_SIZE = screen_width * (ROCK_SIZE_PCENT/100);
			SLIME_SIZE = screen_width * (SLIME_SIZE_PCENT/100);
			ROCK_SPEED = screen_width * (ROCK_SPEED_PCENT/100);
			ROCK_GRAVITY = 0.0598f;//Must be constant!  Was: screen_width * (ROCK_GRAVITY_PCENT/100);
			BULLET_SPEED = screen_width * (BULLET_SPEED_PCENT/100);
			BUBBLE_SPEED = screen_width * (BUBBLE_SPEED_PCENT/100);
			PLATFORM_SPEED = screen_width * (PLATFORM_SPEED_PCENT/100);
			INVADER_WIDTH = SQ_SIZE*1.5f;
			INVADER_HEIGHT = SQ_SIZE*1.5f;
			INVADER_SPEED = PLAYER_SPEED * 1.1f;
			SUN_SIZE = screen_width * (SUN_SIZE_PCENT/100);
			SUN_SPEED = screen_width * (SUN_SPEED_PCENT/100);
			CLOUD_WIDTH = screen_width * (CLOUD_WIDTH_PCENT/100);
			CLOUD_HEIGHT = screen_width * (CLOUD_HEIGHT_PCENT/100);
			CLOUD_SPEED = screen_width * (CLOUD_SPEED_PCENT/100);
			SPACESHIP_BANKING_WIDTH = screen_width * (SPACESHIP_BANKING_WIDTH_PCENT/100);
			SPACESHIP_BANKING_HEIGHT = screen_width * (SPACESHIP_BANKING_HEIGHT_PCENT/100);
			WASP_WIDTH = SQ_SIZE;
			WASP_HEIGHT = SQ_SIZE;
			WASP_SPEED = PLAYER_SPEED * 1.1f;
			
			HEALTH_BAR_HEIGHT =  Statics.SCREEN_HEIGHT/3;
			HEALTH_BAR_WIDTH =  Statics.SCREEN_WIDTH * 0.05f;
			
			ACTIVATE_DIST = Statics.SCREEN_WIDTH * .65f; // Dist when something should be processed 
			DEACTIVATE_DIST = Statics.SCREEN_WIDTH * .75f; // Dist when something should be removed/
			
			initd = true;
		}
	}

	
	public static float GetHeightScaled(float frac) {
		return SCREEN_HEIGHT * frac;
	}

	
	public static float GetWidthScaled(float frac) {
		return SCREEN_WIDTH * frac;
	}

	
	public static File GetExtStorage() {
		File f = new File(Environment.getExternalStorageDirectory() + "/" + NAME.replaceAll("\\!", ""));
		if (f.exists() == false) {
			f.mkdir();
		}
		return f;

	}

	
	public static File GetConfigDir() {
		File f = new File(GetExtStorage() + "/config");
		if (f.exists() == false) {
			f.mkdir();
		}
		return f;

	}
	
	
	public static AbstractModule GetStartupModule(AbstractActivity act) {
		if (Statics.GAME_MODE == Statics.GM_WORLDCRAFTER) {
			return new StartupModule(act);
		} else if (Statics.GAME_MODE == Statics.GM_NINJA) {
			return new StartupModule_Ninja(act);
		} else if (Statics.GAME_MODE == Statics.GM_POLICECOP) {
			return new StartupModule_PoliceCop(act);
		} else {
			throw new RuntimeException("Unknown game type: " + Statics.GAME_MODE);
		}
	}
	
	
	public static int GetNinjaFilename(int level) {
		if (Statics.FULL_VERSION == false) {
			if (level > 3) {
				return -1; // Complete!
			}
		}
		
		switch (level) {
		case 1:
			return R.raw.ninja_level1;
		case 2:
			return R.raw.ninja_level2;
		case 3:
			return R.raw.ninja_level3;
		case 4:
			return R.raw.ninja_level4;
		case 5:
			return R.raw.ninja_level5;
		case 6:
			return R.raw.ninja_level6;
		case 7:
			// Inform me
			try {
				throw new RuntimeException("Player on level 6!");
			} catch (Exception ex) {
				ErrorReporter.getInstance().handleSilentException(ex);
			}
		default:
			return -1;//throw new RuntimeException("Level " + level + " not defined!");
		}
	}
	
	
}
