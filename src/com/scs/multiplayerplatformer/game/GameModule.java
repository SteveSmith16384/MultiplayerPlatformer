package com.scs.multiplayerplatformer.game;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import org.gamepad4j.Controllers;

import ssmith.android.compatibility.Canvas;
import ssmith.android.compatibility.Paint;
import ssmith.android.compatibility.Style;
import ssmith.android.framework.AbstractActivity;
import ssmith.android.framework.MyEvent;
import ssmith.android.framework.modules.AbstractModule;
import ssmith.android.lib2d.Camera;
import ssmith.android.lib2d.MyPointF;
import ssmith.android.lib2d.gui.GUIFunctions;
import ssmith.android.lib2d.shapes.AbstractRectangle;
import ssmith.android.lib2d.shapes.Geometry;
import ssmith.android.lib2d.shapes.Rectangle;
import ssmith.lang.GeometryFuncs;
import ssmith.util.IDisplayText;
import ssmith.util.RealtimeInterval;
import ssmith.util.ReturnObject;
import ssmith.util.TSArrayList;

import com.scs.multiplayerplatformer.Statics;
import com.scs.multiplayerplatformer.Statics.GameMode;
import com.scs.multiplayerplatformer.graphics.Cloud;
import com.scs.multiplayerplatformer.graphics.Explosion;
import com.scs.multiplayerplatformer.graphics.GameObject;
import com.scs.multiplayerplatformer.graphics.blocks.Block;
import com.scs.multiplayerplatformer.graphics.mobs.AbstractMob;
import com.scs.multiplayerplatformer.graphics.mobs.PlayersAvatar;
import com.scs.multiplayerplatformer.input.DeviceThread;
import com.scs.multiplayerplatformer.mapgen.AbstractLevelData;
import com.scs.multiplayerplatformer.mapgen.MapLoader;
import com.scs.multiplayerplatformer.mapgen.SimpleMobData;
import com.scs.multiplayerplatformer.start.SelectLevelModule;

public final class GameModule extends AbstractModule implements IDisplayText {

	public static final byte HAND = 1;

	private static Paint paint_icon_ink = new Paint(); // For text on icons
	private static Paint paint_text_ink = new Paint(); // For timer, dist

	private TSArrayList<IProcessable> entities = new TSArrayList<IProcessable>();;
	public AbstractLevelData levelData;
	public MyEfficientGridLayout blockGrid;
	private TimedString msg;
	private Rectangle dummyRect = new Rectangle(); // for checking the area is clear
	private long levelEndTime;
	private String strTimeRemaining;
	private RealtimeInterval checkForNewMobs = new RealtimeInterval(500, true);
	private TSArrayList<PlayersAvatar> avatars = new TSArrayList<>();
	private String filename;
	private boolean restartGame = false;
	private long restartGameAt;

	public float currentScale;
	private float newScale;// = current_scale;

	static {
		paint_icon_ink.setARGB(255, 255, 255, 255);
		paint_icon_ink.setAntiAlias(true);
		paint_icon_ink.setTextSize(GUIFunctions.getTextSizeToFit("MENU", Statics.ICON_SIZE * 0.9f));

		paint_text_ink.setARGB(255, 255, 255, 255);
		paint_text_ink.setAntiAlias(true);
		paint_text_ink.setTextSize(GUIFunctions.getTextSizeToFit("99XX", Statics.SCREEN_WIDTH/10));
	}


	public GameModule(String _filename) { 
		super();

		this.statCam.lookAtTopLeft(true);
		strTimeRemaining = Statics.act.getString("time_remaining");
		this.setBackground(Statics.BACKGROUND_IMAGE);

		startNewLevel(_filename, true);

		msg = new TimedString(this, 2000);
		msg.setText("PRESS FIRE (X) TO START!");

	}


	// filename = null to load random map
	private void startNewLevel(String _filename, boolean sameMap) {
		Statics.act.soundManager.levelStart();
		restartGame = false;
		if (Statics.GAME_MODE == GameMode.RaceToTheDeath) {
			currentScale = 1;
		} else {
			currentScale = Statics.MAX_ZOOM_IN;
		}
		newScale = currentScale;

		synchronized (entities) {
			entities.clear();
		}
		synchronized (avatars) {
			avatars.clear();
		}

		this.rootNode.detachAllChildren();
		this.statNodeBack.detachAllChildren();
		this.statNodeFront.detachAllChildren();

		for (int i=0 ; i<3 ; i++) {
			new Cloud(this);
		}

		new EnemyEventTimer(this);
		levelEndTime = System.currentTimeMillis() + (Statics.LEVEL_TIME_SECS * 1000);

		if (_filename == null || sameMap == false) {
			filename = MapLoader.GetRandomMap();
		} else {
			this.filename = _filename;
		}
		loadMap(filename);

		// Create an avatar for each player
		synchronized (this.getThread().players) {
			for (Player player : this.getThread().players.values()) {
				this.createAvatar(player);
			}
		}

		showToast(this.levelData.levelName);
	}


	private void createAvatar(Player player) {
		PlayersAvatar avatar = new PlayersAvatar(this, 0, 0, player, player.numZB);
		synchronized (avatars) {
			this.avatars.add(avatar);
			this.avatars.refresh();
		}
		addToProcess(avatar);
		this.restartAvatar(avatar); // Need this to position them
	}


	public void restartAvatar(PlayersAvatar avatar) {
		float x, y;
		if (avatar.checkpoint_map != null) {
			x = avatar.checkpoint_map.x * Statics.SQ_SIZE;
			y = avatar.checkpoint_map.y * Statics.SQ_SIZE;
		} else {
			x = levelData.getStartPos().x * Statics.SQ_SIZE;
			y = (levelData.getStartPos().y) * Statics.SQ_SIZE; // -2 so we start above the bed
		}
		while (!this.isAreaClear(x, y, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT, true)) {
			y = y - Statics.SQ_SIZE;
		}
		avatar.setLocation(x, y);
		avatar.updateGeometricState();

	}


	private void loadMap(String filename) {
		levelData = new MapLoader(filename, false);
		levelData.getMap();

		blockGrid = new MyEfficientGridLayout(this, levelData.getGridWidth(), levelData.getGridHeight(), Statics.SQ_SIZE);
		this.rootNode.attachChild(blockGrid);
		synchronized (entities) {
			this.entities.add(blockGrid);
		}
		this.statNodeBack.updateGeometricState();
		this.statNodeFront.updateGeometricState();

		for (int map_y=0 ; map_y<levelData.getGridHeight() ; map_y++) {
			for (int map_x=0 ; map_x<this.levelData.getGridWidth() ; map_x++) {
				byte data = levelData.getGridDataAt(map_x, map_y);
				if (data > 0) {
					this.addBlock(data, map_x, map_y, false);
				}
			}
		}
		this.rootNode.updateGeometricState();

	}


	/**
	 * Return true to clear all other events 
	 */
	@Override
	public boolean processEvent(MyEvent ev) throws Exception {
		try {
			// Do nothing
		} catch (RuntimeException ex) {
			AbstractActivity.handleError(null, ex);
		}
		return false;
	}


	public void updateGame(long interpol) {
		if (DeviceThread.USE_CONTROLLERS) {
			Controllers.checkControllers();
		}

		// Remove any objects marked for removal
		synchronized (entities) {
			this.entities.refresh();
		}
		synchronized (avatars) {
			this.avatars.refresh();
		}

		if (this.restartGame) {
			if (this.restartGameAt < System.currentTimeMillis()) {
				this.startNewLevel(null, false);
			}
		}

		// Adjust scale
		//if (Statics.GAME_MODE != GameMode.RaceToTheDeath) {
		if (newScale > Statics.MAX_ZOOM_IN) {
			newScale = Statics.MAX_ZOOM_IN;
		} else if (newScale < Statics.MAX_ZOOM_OUT) {
			newScale = Statics.MAX_ZOOM_OUT;
		}
		//}
		this.currentScale = newScale;

		// Process the rest
		if (entities != null) {
			synchronized (entities) {
				for (IProcessable o : entities) {
					o.process(interpol);
				}
			}
		}

		if (avatars.size() > 0) {
			if (checkForNewMobs.hitInterval()) {
				this.checkIfMobsNeedCreating(this.currentScale, this.rootCam);
			}
		}
		
		if (avatars.size() > 1) {
			if (Statics.GAME_MODE != GameMode.RaceToTheDeath) {
				float avgx = 0, avgy = 0;
				for (PlayersAvatar player : avatars) {
					avgx += player.getWorldX();
					avgy += player.getWorldY() + (Statics.PLAYER_HEIGHT/2);
				}
				avgx = avgx / this.avatars.size();
				avgy = avgy / this.avatars.size();
				this.rootCam.lookAt(avgx * this.currentScale, avgy * this.currentScale, true);

				// Do we need to zoom in/out?
				float OUTER = 0.2f;
				float INNER = 0.22f; // 0.3f
				boolean zoomOut = false; // Need to zoom out quickly
				boolean zoomIn = true; // Slowly
				synchronized (avatars) {
					for (PlayersAvatar player : avatars) {
						float sx = player.getWindowX(this.rootCam, this.currentScale);
						float sy = player.getWindowY(this.rootCam, this.currentScale);
						zoomOut = sx < Statics.SCREEN_WIDTH * OUTER || sx > Statics.SCREEN_WIDTH * (1f-OUTER) || sy < Statics.SCREEN_HEIGHT * OUTER || sy > Statics.SCREEN_HEIGHT * (1f-OUTER);
						if (zoomOut) {
							break;
						}
						zoomIn = zoomIn && (sx > Statics.SCREEN_WIDTH * INNER && sx < Statics.SCREEN_WIDTH * (1f-INNER) && sy > Statics.SCREEN_HEIGHT * INNER && sy < Statics.SCREEN_HEIGHT * (1f-INNER));
					}
				}
				if (zoomOut) {
					newScale *= Statics.ZOOM_OUT_SPEED;
				} else if (zoomIn) {
					newScale *= Statics.ZOOM_IN_SPEED;
				}
			} else { // Race to the Death if (Statics.GAME_MODE == GameMode.RaceToTheDeath) {
				float rightMost = 0;
				float avgx = 0, avgy = 0;
				PlayersAvatar rightmostPlayer = null; 
				for (PlayersAvatar player : avatars) {
					avgx += player.getWorldX();
					avgy += player.getWorldY() + (Statics.PLAYER_HEIGHT/2);
					if (player.getWorldX() >= rightMost) {
						rightMost = player.getWorldX(); 
						rightmostPlayer = player;
					}
				}
				avgx = avgx / this.avatars.size();
				avgy = avgy / this.avatars.size();

				//float x = rightmostPlayer.getWorldX();
				//float y = rightmostPlayer.getWorldY() + (Statics.PLAYER_HEIGHT/2);
				this.rootCam.lookAt(rightmostPlayer.getWorldX() * this.currentScale, avgy * this.currentScale, true);

				float OUTER = 0.2f;
				//float INNER = 0.22f; // 0.3f
				boolean zoomOut = false; // Need to zoom out quickly
				//boolean zoomIn = true; // Slowly
				synchronized (avatars) {
					for (PlayersAvatar player : avatars) {
						//float sx = player.getWindowX(this.rootCam, this.currentScale);
						float sy = player.getWindowY(this.rootCam, this.currentScale);
						zoomOut = sy < Statics.SCREEN_HEIGHT * OUTER || sy > Statics.SCREEN_HEIGHT * (1f-OUTER);
						if (zoomOut) {
							break;
						}
						//zoomIn = zoomIn && (sy > Statics.SCREEN_HEIGHT * INNER && sy < Statics.SCREEN_HEIGHT * (1f-INNER));
					}
				}
				if (zoomOut) {
					newScale *= Statics.ZOOM_OUT_SPEED;
				} else { //if (zoomIn) {
					newScale *= Statics.ZOOM_IN_SPEED;
				}

				// Check if a player is off-screen
				for (PlayersAvatar player : avatars) {
					if (!player.isOnScreen(rootCam, this.currentScale)) {
						//player.died();
						playerDied(player); // Don't call player.died() since it will just freeze them in this game mode
						break; // Prevent CME
					}
				}
			}

		} else if (avatars.size() == 1) {
			// Only one player - zoom in
			if (this.newScale < Statics.MAX_ZOOM_IN) {
				newScale *= Statics.ZOOM_IN_SPEED;
			} else {
				this.newScale = Statics.MAX_ZOOM_IN;
			}
			PlayersAvatar rightmostPlayer = avatars.get(0);
			this.rootCam.lookAt(rightmostPlayer.getWorldX() * this.currentScale, rightmostPlayer.getWorldY() * this.currentScale, true);

		} else {
			// No players yet!
			float x = levelData.getStartPos().x * Statics.SQ_SIZE;
			//x += this.new_grid.getWorldX();
			float y = (levelData.getStartPos().y) * Statics.SQ_SIZE;
			//y += this.new_grid.getWorldY();
			this.rootCam.lookAt(x * this.currentScale, y * this.currentScale, true);
			//this.root_cam.lookAt(x, y, true);
			if (Statics.GAME_MODE != GameMode.RaceToTheDeath) {
				newScale = Statics.MAX_ZOOM_OUT;
			}
		}

	}


	private void checkIfMobsNeedCreating(float scale, Camera cam) {
		// Load mobs
		if (levelData.mobs != null && this.avatars.size() > 0) {
			for (int i=0 ; i<levelData.mobs.size() ; i++) {
				SimpleMobData sm = levelData.mobs.get(i);
				//float dist = getDistanceToClosestPlayer(sm.pixel_x); // NumberFunctions.mod(this.player.getWorldX() - sm.pixel_x);
				int x = (int)(sm.pixelX * scale - cam.left);
				int y = (int)(sm.pixelY * scale - cam.top);
				boolean onscreen = this.isOnScreen(x, y, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT);
				if (onscreen) {//dist < Statics.ACTIVATE_DIST) { // Needs to be screen width in case we've walked too fast into their "creation zone"
					AbstractMob.createMob(this, sm);
					levelData.mobs.remove(i);
					i--;
				}
			}
		}
	}


	public float getDistanceToClosestPlayer(float xpos) {
		float closest = 9999;
		for(PlayersAvatar player : avatars) {
			float dist = Math.abs(player.getWorldX() - xpos); 
			if (dist < closest) {
				closest = dist;
			}
		}
		return closest;
	}


	public void doDraw(Canvas g, long interpol) {
		super.doDraw(g, interpol);

		// Manually draw our entities
		try {
			synchronized (entities) {
				for (IProcessable o : this.entities) {
					if (o instanceof IDrawable) {
						IDrawable id = (IDrawable)o;
						id.doDraw(g, this.rootCam, interpol, currentScale);
					}
				}
			}
		} catch (ConcurrentModificationException ex) {
			ex.printStackTrace();
		}

		int y = 80;
		int yInc = (int)paint_text_ink.getTextSize()*2;
		
		// Show scores
		synchronized (this.getThread().players) {
			for (Player player : this.getThread().players.values()) {
				g.drawText("Player " + (player.numZB+1) + " Score: " + player.score, 10, y, paint_text_ink);
				y += yInc;
			}
		}

		if (Statics.GAME_MODE == GameMode.Normal) {
			long timeRemaining = levelEndTime - System.currentTimeMillis();
			if (timeRemaining > 0) {
				g.drawText(this.strTimeRemaining + ": " + (timeRemaining/1000), 10, y, paint_text_ink);
			} else {
				g.drawText("TIME OUT", 10, y, paint_text_ink);
			}
			y += yInc;//paint_text_ink.getTextSize();
		}

		g.drawText(this.levelData.levelName, 10, y, paint_text_ink);
		y += yInc;//paint_text_ink.getTextSize();

		if (msg != null && msg.toString().length() > 0) {
			g.drawText(msg.toString(), 10, y, paint_icon_ink);
			y += yInc;//paint_text_ink.getTextSize();
		}

	}


	public boolean isAreaClear(float x, float y, float w, float h, boolean check_blocks) {
		dummyRect.setByLTWH(x, y, w, h);
		return isAreaClear(dummyRect, check_blocks);
	}


	public boolean isAreaClear(Rectangle r, boolean check_blocks) {
		this.rootNode.attachChild(r);
		r.parent.updateGeometricState();
		//boolean result = dummy_rect.getColliders(this.root_node).size() <= 0;
		// Check for mobs (i.e. we don't bother about ThrownObjects
		ArrayList<Geometry> colls2 = dummyRect.getColliders(this.rootNode);
		boolean result = true;
		for (Geometry g : colls2) {
			if (g instanceof AbstractMob) {
				result = false;
				break;
			}
		}
		r.removeFromParent();

		if (result && check_blocks) {
			ArrayList<AbstractRectangle> colls = blockGrid.getColliders(r.getWorldBounds());
			for (AbstractRectangle ar : colls) {
				if (ar instanceof Block) {
					Block b = (Block)ar;
					if (Block.BlocksAllMovement(b.getType())) {
						return false;
					}
				}
			}
		}

		return result;
	}


	public Block addBlock(byte type, int map_x, int map_y, boolean not_loaded_from_file) {
		Block block = null;
		if (map_x >= 0 && map_y >= 0 && map_x < this.levelData.getGridWidth() && map_y < this.levelData.getGridHeight()) {
			if (type != Block.NOTHING_DAYLIGHT) {
				block = new Block(this, type, map_x, map_y);
			}
			blockGrid.setRectAtMap(block, map_x, map_y);

			if (block != null) {
				if (Block.RequireProcessing(block.getType())) {
					this.addToProcess(block);//, slow);
				}
			}
		}
		return block;
	}


	public void addToProcess(IProcessable o) {
		this.entities.add(o);
	}


	public void removeFromProcess(IProcessable o) {
		this.entities.remove(o);
	}


	@Override
	public boolean onBackPressed() {
		this.getThread().setNextModule(new SelectLevelModule());
		return true;
	}


	public void explosionWithDamage(int block_rad, int dam, int pieces, float pxl_x, float pxl_y) {
		Explosion.createExplosion(this, pieces, pxl_x, pxl_y, "thrown_rock");
		int map_x = (int)(pxl_x / Statics.SQ_SIZE);
		int map_y = (int)(pxl_y / Statics.SQ_SIZE);

		// Remove blocks
		for (int y=map_y-block_rad ; y<=map_y+block_rad ; y++) {
			for (int x=map_x-block_rad ; x<=map_x+block_rad ; x++) {
				float dist = (float)GeometryFuncs.distance(map_x, map_y, x, y);
				if (dist <= block_rad) {
					Block b = (Block)this.blockGrid.getBlockAtMap_MaybeNull(x, y);
					if (b != null) {
						b.damage(2, false, null);
					}
				}
			}

		}

		// Kill mobs
		Rectangle dummy_rect = new Rectangle("Temp", pxl_x - (block_rad * Statics.SQ_SIZE), pxl_y - (block_rad * Statics.SQ_SIZE), block_rad*2 * Statics.SQ_SIZE, block_rad*2 * Statics.SQ_SIZE, null, null);
		this.rootNode.attachChild(dummy_rect);
		dummy_rect.parent.updateGeometricState();
		ArrayList<Geometry> colls = dummy_rect.getColliders(this.rootNode);
		for (Geometry c : colls) {
			if (c instanceof AbstractMob) {
				AbstractMob m = (AbstractMob)c;
				m.died();//.damage(dam);
			}
		}
		dummy_rect.removeFromParent();
	}


	public int getNumProcessInstant() {
		return this.entities.size();
	}


	@Override
	public void displayText(String s) {
		this.msg.setText(s);
	}


	public void playerCompletedLevel(PlayersAvatar avatar) {
		Statics.act.soundManager.playerReachedEnd();
		long score_inc = (levelEndTime - System.currentTimeMillis()) / 100;
		if (this.avatars.size() == this.getThread().players.size()) {
			// First player to get to the end!
			score_inc = score_inc * 2;
		}
		if (score_inc > 0) { // Might be negative
			avatar.player.score += score_inc;
			displayText("Player " + avatar.playernumZB + " finished!  Have " + score_inc + " points!");
		}
		avatar.remove();
		synchronized (avatars) {
			this.avatars.remove(avatar);
			this.avatars.refresh();
		}

		// check if no players left
		if (this.avatars.isEmpty()) {
			this.startNewLevel(null, false); // Load random map after playing first selected map
		} else if (Statics.GAME_MODE == GameMode.RaceToTheDeath) {
			this.startNewLevel(null, false); // Load random map after playing first selected map
		}
	}


	public boolean isOnScreen(float x, float y, float w, float h) {
		return x+w >= 0 && x <= Statics.SCREEN_WIDTH && y+h >= 0 && y<= Statics.SCREEN_HEIGHT;
	}


	public float getDistanceToClosestPlayer(ReturnObject<PlayersAvatar> returnClosest, GameObject o) {
		float closestDistance = 9999;
		synchronized (avatars) {
			for(PlayersAvatar player : avatars) {
				float dist = o.getDistanceTo(player); 
				if (dist < closestDistance) {
					if (returnClosest != null) {
						returnClosest.toReturn = player;
					}
					closestDistance = dist;
				}
			}
		}
		return closestDistance;
	}


	public PlayersAvatar getVisiblePlayer(GameObject o) {
		synchronized (avatars) {
			for (PlayersAvatar player : avatars) { // todo - use Line() from Roguelike
				MyPointF dir = player.getWorldCentre_CreatesNew().subtract(o.getWorldCentre_CreatesNew());//new MyPointF(mob.getWorldCentreX(), mob.getWorldCentreY());
				float len = dir.length();
				int num = (int)(len / Statics.SQ_SIZE) * 3;
				dir.divideLocal(num);
				for (int i=0 ; i<num ; i++) {
					float x = o.getWorldCentreX() + (dir.x * i);
					float y = o.getWorldCentreY() + (dir.y * i);
					int map_x = (int)(x / Statics.SQ_SIZE);
					int map_y = (int)(y / Statics.SQ_SIZE);
					Block b = (Block) blockGrid.getBlockAtMap_MaybeNull(map_x, map_y);
					if (b != null) {
						if (b.getType() != Block.NOTHING_DAYLIGHT) {
							//return false;
							continue;
						}
					}
				}
				return player;
			}
			return null;
		}
	}


	public void playerDied(PlayersAvatar avatar) {
		Statics.act.soundManager.playerDied();
		if (Statics.GAME_MODE == GameMode.RaceToTheDeath) {
			avatar.remove();
			synchronized (avatars) {
				this.avatars.remove(avatar);
				//this.avatars.refresh();  No!  get CME
			}

			// check if no players left
			if (this.avatars.size() == 1) {
				//this.startNewLevel(null, false); // Load random map after playing first selected map
				PlayersAvatar winner = this.avatars.get(0);
				this.showToast("Player " + (winner.playernumZB+1) + " HAS WON!");
				long score_inc = (levelEndTime - System.currentTimeMillis()) / 100;
				avatar.player.score += score_inc;
				restartGame = true;
				restartGameAt = System.currentTimeMillis() + 3000;
			}
		} else {
			restartAvatar(avatar);
		}
	}
	
	
	@Override
	public void newPlayer(Player player) {
		this.startNewLevel(filename, true);
		
		this.msg.setText("Player " + player.numZB+1 + " joined!");
	}


	@Override
	public void playerRemoved(Player player) {
		synchronized (avatars) {
			for (PlayersAvatar avatar : avatars) {
				if (avatar.player == player) {
					avatars.remove(avatar);
					avatar.remove();
					this.msg.setText("Player " + (player.numZB+1) + " has left!");
					break;
				}
			}
		}
	}




}

