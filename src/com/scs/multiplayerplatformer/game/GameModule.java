package com.scs.multiplayerplatformer.game;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ssmith.android.compatibility.Canvas;
import ssmith.android.compatibility.Paint;
import ssmith.android.compatibility.Style;
import ssmith.android.framework.AbstractActivity;
import ssmith.android.framework.MyEvent;
import ssmith.android.framework.modules.AbstractModule;
import ssmith.android.lib2d.gui.GUIFunctions;
import ssmith.android.lib2d.shapes.AbstractRectangle;
import ssmith.android.lib2d.shapes.Geometry;
import ssmith.android.lib2d.shapes.Rectangle;
import ssmith.lang.GeometryFuncs;
import ssmith.lang.NumberFunctions;
import ssmith.util.IDisplayText;
import ssmith.util.Interval;
import ssmith.util.TSArrayList;

import com.scs.multiplayerplatformer.Statics;
import com.scs.multiplayerplatformer.graphics.Cloud;
import com.scs.multiplayerplatformer.graphics.Explosion;
import com.scs.multiplayerplatformer.graphics.blocks.Block;
import com.scs.multiplayerplatformer.graphics.mobs.AbstractMob;
import com.scs.multiplayerplatformer.graphics.mobs.PlayersAvatar;
import com.scs.multiplayerplatformer.input.IInputDevice;
import com.scs.multiplayerplatformer.input.NewControllerListener;
import com.scs.multiplayerplatformer.mapgen.AbstractLevelData;
import com.scs.multiplayerplatformer.mapgen.MapLoader;
import com.scs.multiplayerplatformer.mapgen.SimpleMobData;
import com.scs.multiplayerplatformer.start.StartupModule;


public final class GameModule extends AbstractModule implements IDisplayText, NewControllerListener {

	public static final byte HAND = 1;

	private static Paint paint_health_bar = new Paint();
	private static Paint paint_health_bar_outline = new Paint();
	private static Paint paint_icon_ink = new Paint(); // For text on icons
	private static Paint paint_icon_background = new Paint(); // For text on icons
	private static Paint paint_inv_ink = new Paint(); // For inv qty
	private static Paint paint_text_ink = new Paint(); // For timer, dist

	private TSArrayList<IProcessable> entities;
	public AbstractLevelData original_level_data;
	public MyEfficientGridLayout new_grid; // todo - rename
	private TimedString msg;
	private Rectangle dummy_rect = new Rectangle(); // for checking the area is clear
	private long levelEndTime;
	public int level;
	private String str_time_remaining;
	private Interval check_for_new_mobs = new Interval(500, true);
	public List<PlayersAvatar> avatars = new ArrayList<>();
	public List<Player> players = new ArrayList<>();
	private List<IInputDevice> newControllers = new ArrayList<>();

	public float current_scale = Statics.MAX_ZOOM;// .25f;//1;//.5f;
	public float new_scale = current_scale;

	static {
		paint_health_bar.setARGB(150, 200, 0, 0); // This is set elsewhere
		paint_health_bar.setAntiAlias(true);
		paint_health_bar.setStyle(Style.FILL);

		paint_health_bar_outline.setARGB(150, 255, 255, 255);
		paint_health_bar_outline.setAntiAlias(true);
		paint_health_bar_outline.setStyle(Style.STROKE);

		paint_icon_ink.setARGB(255, 255, 255, 255);
		paint_icon_ink.setAntiAlias(true);
		paint_icon_ink.setTextSize(GUIFunctions.GetTextSizeToFit("MENU", Statics.ICON_SIZE * 0.9f));

		paint_icon_background.setARGB(155, 255, 255, 255);

		paint_text_ink.setARGB(255, 255, 255, 255);
		paint_text_ink.setAntiAlias(true);
		paint_text_ink.setTextSize(GUIFunctions.GetTextSizeToFit("99XX", Statics.SCREEN_WIDTH/10));

		paint_inv_ink.setARGB(255, 255, 255, 255);
		paint_inv_ink.setAntiAlias(true);
		paint_inv_ink.setTextSize(GUIFunctions.GetTextSizeToFit("99", Statics.SCREEN_WIDTH/10));

	}


	public GameModule(AbstractActivity act) { 
		super(act, null);

		this.mod_return_to = new StartupModule(act);

		this.stat_cam.lookAtTopLeft(true);

		str_time_remaining = act.getString("time_remaining");

		this.setBackground(Statics.BACKGROUND_R);

		// Load a player for each controller
		startNewLevel();

		act.thread.deviceThread.addListener(this);

		msg = new TimedString(this, 2000);

		if (level <= 1) {
			showToast("PRESS FIRE TO START!");
		} else {
			showToast("LEVEL " + level);
		}
	}


	private void startNewLevel() {
		Statics.act.sound_manager.levelStart();

		new File(Statics.MAP_DIR).mkdirs();

		String maps[] = new File(Statics.MAP_DIR).list();

		if (maps == null || maps.length <= 0) {
			throw new RuntimeException("No maps found!  Put them in " + Statics.MAP_DIR);
		}

		entities = new TSArrayList<IProcessable>();

		this.root_node.detachAllChildren();
		this.stat_node_back.detachAllChildren();
		this.stat_node_front.detachAllChildren();

		new EnemyEventTimer(this);
		levelEndTime = System.currentTimeMillis() + (20 * 1000);

		loadMap(Statics.MAP_DIR + maps[NumberFunctions.rnd(0, maps.length-1)]);

		Iterator<IInputDevice> it = Statics.act.thread.deviceThread.getDevices().iterator();
		while (it.hasNext()) {
			IInputDevice input = it.next();
			this.loadPlayer(input);
		}

		for (int i=0 ; i<3 ; i++) {
			new Cloud(this);
		}

		this.showToast("Level " + this.level + "!");
	}


	private void loadMap(String filename2) {
		String filename = null;
		if (Statics.TEST_LEVEL != null) {
			filename = "./maps/" + Statics.TEST_LEVEL;
		} else {
			filename = filename2;
		}
		original_level_data = new MapLoader(filename, false);
		original_level_data.getMap();

		new_grid = new MyEfficientGridLayout(this, original_level_data.getGridWidth(), original_level_data.getGridHeight(), Statics.SQ_SIZE);
		this.root_node.attachChild(new_grid);
		this.entities.add(new_grid);

		this.stat_node_back.updateGeometricState();
		this.stat_node_front.updateGeometricState();

		for (int map_y=0 ; map_y<original_level_data.getGridHeight() ; map_y++) {
			for (int map_x=0 ; map_x<this.original_level_data.getGridWidth() ; map_x++) {
				byte data = original_level_data.getGridDataAt(map_x, map_y);
				if (data > 0) {
					this.addBlock(data, map_x, map_y, false);
				}
			}
		}
		this.root_node.updateGeometricState();

	}


	/**
	 * Return true to clear all other events 
	 */
	@Override
	public boolean processEvent(MyEvent ev) throws Exception {
		try {
			// Do nothing
		} catch (RuntimeException ex) {
			AbstractActivity.HandleError(null, ex);
		}
		return false;
	}


	public void updateGame(long interpol) {
		synchronized (newControllers) {
			while (this.newControllers.isEmpty() == false) {
				this.loadPlayer(this.newControllers.remove(0));
			}
		}

		// Remove any objects marked for removal
		this.entities.refresh();

		// Adjust scale
		if (new_scale > Statics.MAX_ZOOM) {
			new_scale = Statics.MAX_ZOOM;
		} else if (new_scale < Statics.MIN_ZOOM) {
			new_scale = Statics.MIN_ZOOM;
		}
		this.current_scale = new_scale;

		// Process the rest
		if (entities != null) {
			for (IProcessable o : entities) {
				o.process(interpol);
			}
		}

		if (avatars.size() > 0) {
			if (check_for_new_mobs.hitInterval()) {
				this.checkIfMobsNeedCreating();
			}

			float x = 0, y = 0;
			for (PlayersAvatar player : avatars) {
				x += player.getWorldX();
				y += player.getWorldY();
			}
			x = x / this.avatars.size();
			y = y / this.avatars.size();
			this.root_cam.lookAt(x * this.current_scale, y * this.current_scale, true);


			// Do we need to zoom out
			if (avatars.size() > 1) {
				float OUTER = 0.2f;
				float INNER = 0.3f;
				boolean zoomOut = false;
				boolean zoomIn = true;
				for (PlayersAvatar player : avatars) {
					float sx = player.getWindowX(this.root_cam, this.current_scale);
					float sy = player.getWindowY(this.root_cam, this.current_scale);
					zoomOut = sx < Statics.SCREEN_WIDTH * OUTER || sx > Statics.SCREEN_WIDTH * (1f-OUTER) || sy < Statics.SCREEN_HEIGHT * OUTER || sy > Statics.SCREEN_HEIGHT * (1f-OUTER);
					if (zoomOut) {
						break;
					}
					zoomIn = zoomIn && (sx > Statics.SCREEN_WIDTH * INNER && sx < Statics.SCREEN_WIDTH * (1f-INNER) && sy > Statics.SCREEN_HEIGHT * INNER && sy < Statics.SCREEN_HEIGHT * (1f-INNER));
				}
				if (zoomOut) { // zoom out
					new_scale /= Statics.ZOOM_SPEED;
				} else if (zoomIn) { // zoom in
					new_scale *= Statics.ZOOM_SPEED;
				}				


				/*if (Statics.DEBUG) {
					Statics.p("Zoom: " + this.current_scale + " -> " + this.new_scale);
				}*/
			} else {
				// Only one player - set zoom to 1
				this.new_scale = Statics.MAX_ZOOM;
			}

		} else {
			// No players yet!
			float x = original_level_data.getStartPos().x * Statics.SQ_SIZE;
			//x += this.new_grid.getWorldX();
			float y = (original_level_data.getStartPos().y) * Statics.SQ_SIZE;
			//y += this.new_grid.getWorldY();
			this.root_cam.lookAt(x, y, true);
			new_scale = Statics.MAX_ZOOM;
		}

	}


	private void checkIfMobsNeedCreating() {
		// Load mobs
		if (original_level_data.mobs != null && this.avatars.size() > 0) {
			for (int i=0 ; i<original_level_data.mobs.size() ; i++) {
				SimpleMobData sm = original_level_data.mobs.get(i);
				float dist = getDistanceToClosestPlayer(sm.pixel_x); // NumberFunctions.mod(this.player.getWorldX() - sm.pixel_x); 
				if (dist < Statics.ACTIVATE_DIST) { // Needs to be screen width in case we've walked too fast into their "creation zone"
					AbstractMob.CreateMob(this, sm);
					original_level_data.mobs.remove(i);
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
		synchronized (entities) {
			for (IProcessable o : this.entities) {
				if (o instanceof IDrawable) {
					IDrawable id = (IDrawable)o;
					id.doDraw(g, this.root_cam, interpol, current_scale);
				}
			}
		}

		for (Player player : players) {
			g.drawText("Player " + (player.num+1) + " Score: " + player.score, 10, 50+(player.num * paint_text_ink.getTextSize()), paint_text_ink);

		}

		long timeRemaining = levelEndTime - System.currentTimeMillis();
		if (timeRemaining > 0) {
			g.drawText(this.str_time_remaining + ": " + (timeRemaining/1000), 10, Statics.ICON_SIZE + (paint_text_ink.getTextSize()*3), paint_text_ink);
		} else {
			g.drawText("TIME OUT", 10, Statics.ICON_SIZE + (paint_text_ink.getTextSize()*3), paint_text_ink);
		}
		g.drawText(this.original_level_data.levelName, 10, Statics.ICON_SIZE + (paint_text_ink.getTextSize()*5), paint_text_ink);

		if (Statics.SHOW_STATS) {
			g.drawText("Inst Objects: " + this.entities.size(), 10, paint_text_ink.getTextSize()*7, paint_text_ink);
			//g.drawText("Slow Objects: " + this.others_slow.size(), 10, paint_text_ink.getTextSize()*5, paint_text_ink);
			//g.drawText("V. Slow Objects: " + this.others_very_slow.size(), 10, paint_text_ink.getTextSize()*6, paint_text_ink);
			//g.drawText("Darkness: " + this.dark_adj_cont.size(), 10, paint_text_ink.getTextSize()*7, paint_text_ink);
			//g.drawText("Map Squares: " + EfficientGridLayout.objects_being_drawn, 10, paint_text_ink.getTextSize()*4, paint_text_ink);
			//long mem = ((long)r.freeMemory()*100) / (long)r.totalMemory();//) * 100;
			//g.drawText("Free mem: " + mem + "%", 10, paint_text_ink.getTextSize()*5, paint_text_ink);
			/*g.drawText("draw_time: " + this.draw_time, 10, paint_text_ink.getTextSize()*7, paint_text_ink);
			g.drawText("instant_time: " + this.instant_time, 10, paint_text_ink.getTextSize()*8, paint_text_ink);
			g.drawText("total_time: " + this.total_time, 10, paint_text_ink.getTextSize()*9, paint_text_ink);
			 */
		}

		if (msg != null) {
			if (msg.toString().length() > 0) {
				g.drawText(msg.toString(), 10, Statics.SCREEN_HEIGHT - (paint_icon_ink.getTextSize()*.5f), paint_icon_ink);
			}
		}

	}


	public boolean isAreaClear(float x, float y, float w, float h, boolean check_blocks) {
		dummy_rect.setByLTWH(x, y, w, h);
		return isAreaClear(dummy_rect, check_blocks);
	}


	public boolean isAreaClear(Rectangle r, boolean check_blocks) {
		this.root_node.attachChild(r);
		r.parent.updateGeometricState();
		//boolean result = dummy_rect.getColliders(this.root_node).size() <= 0;
		// Check for mobs (i.e. we don't bother about ThrownObjects
		ArrayList<Geometry> colls2 = dummy_rect.getColliders(this.root_node);
		boolean result = true;
		for (Geometry g : colls2) {
			if (g instanceof AbstractMob) {
				result = false;
				break;
			}
		}
		r.removeFromParent();

		if (result && check_blocks) {
			ArrayList<AbstractRectangle> colls = new_grid.getColliders(r.getWorldBounds());
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
		if (map_x >= 0 && map_y >= 0 && map_x < this.original_level_data.getGridWidth() && map_y < this.original_level_data.getGridHeight()) {
			if (type != Block.NOTHING_DAYLIGHT) {
				block = new Block(this, type, map_x, map_y);
			}
			new_grid.setRectAtMap(block, map_x, map_y);

			if (block != null) {
				if (Block.RequireProcessing(block.getType())) {
					//boolean slow = block.getType() == Block.WATER || block.getType() == Block.LAVA || block.getType() == Block.FIRE; // Always process water slow!
					this.addToProcess_Instant(block);//, slow);
				}
			}
		}
		return block;
	}


	public void addToProcess_Instant(IProcessable o) {
		this.entities.add(o);
	}


	public void removeFromProcess(IProcessable o) {
		this.entities.remove(o);
	}


	@Override
	public boolean onBackPressed() {
		this.returnTo();
		return true;
	}


	public void explosionWithDamage(int block_rad, int dam, int pieces, float pxl_x, float pxl_y) {
		Explosion.CreateExplosion(this, pieces, pxl_x, pxl_y, "thrown_rock");
		int map_x = (int)(pxl_x / Statics.SQ_SIZE);
		int map_y = (int)(pxl_y / Statics.SQ_SIZE);

		// Remove blocks
		for (int y=map_y-block_rad ; y<=map_y+block_rad ; y++) {
			for (int x=map_x-block_rad ; x<=map_x+block_rad ; x++) {
				float dist = (float)GeometryFuncs.distance(map_x, map_y, x, y);
				if (dist <= block_rad) {
					Block b = (Block)this.new_grid.getBlockAtMap_MaybeNull(x, y);
					if (b != null) {
						b.damage(2, false, null);
					}
				}
			}

		}

		// Kill mobs
		Rectangle dummy_rect = new Rectangle("Temp", pxl_x - (block_rad * Statics.SQ_SIZE), pxl_y - (block_rad * Statics.SQ_SIZE), block_rad*2 * Statics.SQ_SIZE, block_rad*2 * Statics.SQ_SIZE, null, null);
		this.root_node.attachChild(dummy_rect);
		dummy_rect.parent.updateGeometricState();
		ArrayList<Geometry> colls = dummy_rect.getColliders(this.root_node);
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


	private synchronized void loadPlayer(IInputDevice input) {
		if (input == null) {
			throw new NullPointerException("Input is null");
		}

		// Restart all other players
		for(PlayersAvatar avatar : this.avatars) {
			this.restartPlayer(avatar);
		}

		int num = avatars.size();

		Player player = null;
		if (num >= players.size()) {
			player = new Player(num);
			this.players.add(new Player(num));
		} else {
			player = players.get(num);
		}

		float x = original_level_data.getStartPos().x * Statics.SQ_SIZE;
		float y = (original_level_data.getStartPos().y) * Statics.SQ_SIZE;
		while (!this.isAreaClear(x, y, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT, true)) {
			y = y - Statics.SQ_SIZE;
		}
		PlayersAvatar avatar = new PlayersAvatar(this, player, x, y, input);
		this.avatars.add(avatar);
		avatar.parent.updateGeometricState();
	}


	public void restartPlayer(PlayersAvatar avatar) {
		float x = original_level_data.getStartPos().x * Statics.SQ_SIZE;
		float y = (original_level_data.getStartPos().y-2) * Statics.SQ_SIZE; // -2 so we start above the bed
		avatar.setLocation(x, y);
		addToProcess_Instant(avatar);
		avatar.updateGeometricState();

	}


	public TSArrayList<IProcessable> getOthersInstant() {
		return this.entities;
	}


	@Override
	public void displayText(String s) {
		this.msg.setText(s);
	}


	@Override
	public void newController(IInputDevice input) {
		/*Statics.p("newController(): " + input);

		// Check player doesn't already exist
		for (PlayersAvatar avatar : this.avatars) {
			if (avatar.i)
		}
		this.loadPlayer(input); // This gets called in sep thread!?*/
		synchronized (newControllers) {
			this.newControllers.add(input);
		}
	}


	public void playerCompletedLevel(PlayersAvatar avatar) {
		Statics.act.sound_manager.playerReachedEnd();
		long score_inc = (levelEndTime - System.currentTimeMillis()) / 100;
		if (score_inc > 0) { // Might be negative
			avatar.player.score += score_inc;
			displayText("Player " + avatar.playernumZB + " finished!  Have " + score_inc + " points!");
		}
		avatar.remove();
		this.avatars.remove(avatar);
		// check if no players left
		if (this.avatars.isEmpty()) {
			this.startNewLevel();
		}
	}


}

