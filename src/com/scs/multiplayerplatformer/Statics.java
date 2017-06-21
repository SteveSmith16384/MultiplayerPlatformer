package com.scs.multiplayerplatformer;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import ssmith.android.compatibility.Typeface;
import ssmith.android.framework.AbstractActivity;
import ssmith.awt.ImageCache;

public final class Statics {
	
	public enum GameMode {Normal, RaceToTheDeath, Testing }

	public static final boolean FULL_SCREEN = false;
	public static final boolean HIDE_GFX = false;
	public static final boolean DEBUG = false;
	public static final boolean RELEASE_MODE = true; // Stricter if false
	public static final String VERSION_NAME = "1.1";

	// Sides
	public static final byte SD_PLAYERS_SIDE = 0;
	public static final byte SD_ENEMY_SIDE = 1;

	public static final int LOOP_DELAY = 30;
	public static final int MAX_INSTANTS = 999;
	public static final int DYNAMITE_DAMAGE = 10;
	public static final String EMAIL = "help@penultimateapps.com"; 
	public static final float LABEL_SPACING = 1.2f;
	public static float ACTIVATE_DIST;
	public static final int WINDOW_TOP_OFFSET = 25;
	public static String BACKGROUND_IMAGE = "harrys_background.png"; //"ninja_background2.jpg";
	public static final long FREEZE_DUR = 1000;
	public static final int MAX_BMP_WIDTH = 100;
	public static final int MAX_PLAYER_SPRITES = 3;
	public static final int LEVEL_TIME_SECS = 120;
	public static final float ZOOM_IN_SPEED = 1.01f;
	public static final float ZOOM_OUT_SPEED = .98f; // Need to zoom out fast
	public static float MAX_ZOOM_OUT;
	public static float MAX_ZOOM_IN;
	public static GameMode GAME_MODE = GameMode.Normal;

	public static AbstractActivity act;
	public static Typeface stdfnt, iconfnt, bigfnt;
	public static ImageCache img_cache;


	// Bitmap scales
	public static String NAME;
	public static float SCREEN_WIDTH, SCREEN_HEIGHT;
	public static float SQ_SIZE;
	public static int SQ_SIZE_INT;
	private static final float ICON_SIZE_PCENT = 11f;
	public static float ICON_SIZE;
	public static float PLAYER_WIDTH, PLAYER_HEIGHT;
	public static final float PLAYER_SPEED_DIVISOR = 7f;
	public static float PLAYER_SPEED, ENEMY_NINJA_SPEED;
	public static float PLAYER_FALL_SPEED, MAX_FALL_SPEED;
	private static final float ROCK_SIZE_PCENT = 60f; //3f;
	private static final float SLIME_SIZE_PCENT = 30f; //3f;
	public static float ROCK_SIZE, SLIME_SIZE;
	private static final float ROCK_SPEED_PCENT = 22f;//1.5f;
	public static float ROCK_SPEED;
	public static final float ROCK_GRAVITY = 0.06f;// 0.0598f; // Must be constant!
	private static final float BULLET_SPEED_PCENT = 2f;
	public static float BULLET_SPEED;
	private static final float BUBBLE_SPEED_PCENT = .5f;
	public static float BUBBLE_SPEED;
	private static final float PLATFORM_SPEED_PCENT = .4f;
	public static float PLATFORM_SPEED;
	public static final float CLOUD_WIDTH_PCENT = 21f;
	public static float CLOUD_WIDTH;
	public static final float CLOUD_HEIGHT_PCENT = 14f;
	public static float CLOUD_HEIGHT;
	public static final float CLOUD_SPEED_PCENT = .4f;
	public static float CLOUD_SPEED;
	public static float JUMP_Y;// = -1.2f;
	public static final int SHURIKENS_FROM_BLOCK = 5;
	public static float WASP_WIDTH, WASP_HEIGHT, WASP_SPEED;

	public static void init(AbstractActivity _act) {
		if (FULL_SCREEN) {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			SCREEN_WIDTH = (int)screenSize.getWidth();
			SCREEN_HEIGHT = (int)screenSize.getHeight();
		} else {
			SCREEN_WIDTH = 800;
			SCREEN_HEIGHT = 600;
		}

		SQ_SIZE = 50; //SCREEN_WIDTH * (SQ_SIZE_PCENT/100);
		SQ_SIZE_INT = (int)Math.ceil(SQ_SIZE);
		MAX_ZOOM_OUT = SCREEN_WIDTH / 6000;
		MAX_ZOOM_IN = SCREEN_WIDTH / 1000;//800;
		ICON_SIZE = SCREEN_WIDTH * (ICON_SIZE_PCENT/100);
		PLAYER_WIDTH = SQ_SIZE * 0.8f;
		PLAYER_HEIGHT = (SQ_SIZE*2) * 0.9f;
		PLAYER_SPEED = SQ_SIZE / PLAYER_SPEED_DIVISOR;//10f;
		JUMP_Y = -SQ_SIZE/40;//30;
		PLAYER_FALL_SPEED = PLAYER_SPEED/3f;
		MAX_FALL_SPEED = Statics.SQ_SIZE/2; 
		ENEMY_NINJA_SPEED = PLAYER_SPEED * 0.25f;
		ROCK_SIZE = SQ_SIZE * (ROCK_SIZE_PCENT/100);
		SLIME_SIZE = SQ_SIZE * (SLIME_SIZE_PCENT/100);
		ROCK_SPEED = SQ_SIZE * (ROCK_SPEED_PCENT/100);
		BULLET_SPEED = SQ_SIZE * (BULLET_SPEED_PCENT/100);
		BUBBLE_SPEED = SQ_SIZE * (BUBBLE_SPEED_PCENT/100);
		PLATFORM_SPEED = SQ_SIZE * (PLATFORM_SPEED_PCENT/100);
		CLOUD_WIDTH = SCREEN_WIDTH * (CLOUD_WIDTH_PCENT/100);
		CLOUD_HEIGHT = SCREEN_WIDTH * (CLOUD_HEIGHT_PCENT/100);
		CLOUD_SPEED = SQ_SIZE * (CLOUD_SPEED_PCENT/100);
		WASP_WIDTH = PLAYER_WIDTH;
		WASP_HEIGHT = PLAYER_WIDTH * 1.5f;
		WASP_SPEED = PLAYER_SPEED /2;

		ACTIVATE_DIST = Statics.SQ_SIZE * 10;// Statics.SCREEN_WIDTH * .65f; // Dist when something should be processed 

		// Load font
		/*try {
				InputStream fntStr = Statics.class.getClassLoader().getResourceAsStream("fonts/SF Distant Galaxy.ttf");
				GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
				ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, fntStr));
				fntStr.close();
				iconfnt = new Typeface("SF Distant Galaxy", Font.PLAIN, 10);
				stdfnt = new Typeface("SF Distant Galaxy", Font.BOLD, 14);
				bigfnt = new Typeface("SF Distant Galaxy", Font.BOLD, 28);
			} catch (Exception e) {
				e.printStackTrace();*/
		// just use helvetica
		iconfnt = new Typeface("Helvetica", Font.PLAIN, 10);
		stdfnt = new Typeface("Helvetica", Font.BOLD, 14);
		bigfnt = new Typeface("Helvetica", Font.BOLD, 28);

	}


	// ----------------------------------------------------------------------

	public static void p(String s) {
		System.out.println(s);
	}


	public static void pe(String s) {
		System.err.println(s);
	}

}
