package com.scs.multiplayerplatformer.game;

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
import ssmith.android.util.Timer;
import ssmith.lang.Functions;
import ssmith.lang.GeometryFuncs;
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
import com.scs.multiplayerplatformer.mapgen.LoadMap;
import com.scs.multiplayerplatformer.mapgen.SimpleMobData;


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
	public MyEfficientGridLayout new_grid;
	private TimedString msg;
	private Rectangle dummy_rect = new Rectangle(); // for checking the area is clear
	private Timer time_remaining;
	public int level;
	private String str_time_remaining;
	private Interval check_for_new_mobs = new Interval(500, true);
	public List<PlayersAvatar> players = new ArrayList<>();

	public float current_scale = 1;//.5f;
	public float target_scale = 1;//.5f;

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

		paint_text_ink.setARGB(255, 125, 125, 125);
		paint_text_ink.setAntiAlias(true);
		paint_text_ink.setTextSize(GUIFunctions.GetTextSizeToFit("99XX", Statics.SCREEN_WIDTH/10));

		paint_inv_ink.setARGB(255, 255, 255, 255);
		paint_inv_ink.setAntiAlias(true);
		paint_inv_ink.setTextSize(GUIFunctions.GetTextSizeToFit("99", Statics.SCREEN_WIDTH/10));

	}


	public GameModule(AbstractActivity act, int _level) { // AbstractLevelData _original_level_data, 
		super(act, null);

		//this.stat_node_back.updateGeometricState();
		this.stat_cam.lookAtTopLeft(true);

		str_time_remaining = act.getString("time_remaining");

		this.setBackground("ninja_background2");

		// Load a player for each controller
		startNewLevel(_level);

		Iterator<IInputDevice> it = act.thread.deviceThread.createdDevices.values().iterator();
		while (it.hasNext()) {
			IInputDevice input = it.next();
			this.loadPlayer(input);
		}
		act.thread.deviceThread.addListener(this);

		msg = new TimedString(this, 2000);

		if (level <= 1) {
			showToast("PRESS FIRE TO START!");
		} else {
			showToast("LEVEL " + level);
		}
	}


	private void startNewLevel(int _level) {
		level = _level;

		this.root_node.detachAllChildren();
		this.stat_node_back.detachAllChildren();
		this.stat_node_front.detachAllChildren();

		loadMap();

		new EnemyEventTimer(this);

		for (int i=0 ; i<3 ; i++) {
			new Cloud(this);
		}

		// mark all players as not completed level
		for(PlayersAvatar player : players) {
			player.completedLevel = false;
			restartPlayer(player);
		}

		this.showToast("Level " + this.level + "!");
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
		// Remove any objects marked for removal
		this.entities.refresh();

		// Process the rest
		if (entities != null) {
			for (IProcessable o : entities) {
				o.process(interpol);
			}
		}

		if (check_for_new_mobs.hitInterval()) {
			this.checkIfMobsNeedCreating();
		}


		if (players.size() > 0) {
			float x = 0, y = 0;
			boolean allCompleted = true;
			for (PlayersAvatar player : players) {
				x += player.getWorldX();
				y += player.getWorldY();
				allCompleted = allCompleted && player.completedLevel; 
			}
			x = x / this.players.size();
			y = y / this.players.size();
			this.root_cam.lookAt(x, y, true);

			if (allCompleted) {
				this.startNewLevel(this.level + 1);
			}

		} else {
			this.root_cam.lookAt(Statics.SCREEN_WIDTH/2, Statics.SCREEN_HEIGHT, false);
			this.current_scale = 1f;
		}
		this.root_cam.update(interpol);

		if (this.time_remaining != null) {
			if (time_remaining.hasHit(interpol)) {
				this.startNewLevel(level++);
			}
		}
	}


	private void checkIfMobsNeedCreating() {
		// Load mobs
		if (original_level_data.mobs != null && this.players.size() > 0) {
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
		for(PlayersAvatar player : players) {
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

		for (PlayersAvatar player : players) {
			g.drawText("Player " + (player.playernum+1) + " Score: " + player.score, 10, 50+(player.playernum * paint_text_ink.getTextSize()), paint_text_ink);

		}

		if (this.time_remaining != null) {
			g.drawText(this.str_time_remaining + ": " + (this.time_remaining.getTimeRemaining()/1000), 10, Statics.ICON_SIZE + (paint_text_ink.getTextSize()*3), paint_text_ink);
		}

		if (Statics.SHOW_STATS) {
			g.drawText("Inst Objects: " + this.entities.size(), 10, paint_text_ink.getTextSize()*4, paint_text_ink);
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


	private void loadMap() {
		entities = new TSArrayList<IProcessable>();
		original_level_data = new LoadMap(Statics.GetMapFilename(level));
		original_level_data.getMap();

		new_grid = new MyEfficientGridLayout(this, original_level_data.getGridWidth(), original_level_data.getGridHeight(), Statics.SQ_SIZE);
		this.root_node.attachChild(new_grid);
		this.entities.add(new_grid);

		this.stat_node_back.updateGeometricState();
		this.stat_node_front.updateGeometricState();
		//this.root_cam.lookAt(root_node, false);

		for (int map_y=0 ; map_y<original_level_data.getGridHeight() ; map_y++) {
			for (int map_x=0 ; map_x<this.original_level_data.getGridWidth()-1 ; map_x++) {
				byte data = original_level_data.getGridDataAt(map_x, map_y);
				if (data > 0) {
					this.addBlock(data, map_x, map_y, false);
				}
			}
		}
		this.root_node.updateGeometricState();

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
		AbstractActivity act = Statics.act;

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

			if (not_loaded_from_file) { // If it's not loaded from file, we always check darkness
				if (type == Block.FIRE) {
					act.sound_manager.playSound("start_fire");
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
		return true;
	}


	/*public void gameOver(String reason) {
		game_over_reason = reason;
		this.game_over = true;
	}*/


	/*private void checkGameOver() {
		AbstractActivity act = Statics.act;

		if (this.game_over) {
			this.getThread().setNextModule(new GameOverModule(act, this, game_over_reason));
		}
	}*/


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


	/*public void setCurrentItemIcon(PlayersAvatar player) {
		if (current_item_image != null) {
			current_item_image.removeFromParent();
		}
		BufferedImage bmp = null;
		float size = Statics.ICON_SIZE -(ICON_INSETS*2);
		if (player.hasBlockSelected()) {
			bmp = Block.GetBitmap(Statics.img_cache, player.getCurrentItemType(), size, size);
		} else if (player.getCurrentItemType() == HAND) {
			bmp = Statics.img_cache.getImage("hand", size, size);
		}
		if (bmp != null) {
			current_item_image = new Button("Current Item", ""+player.inv.get(player.getCurrentItemType()), this.curr_item_icon.getWorldX()+ICON_INSETS, this.curr_item_icon.getWorldY()+ICON_INSETS, null, paint_inv_ink, bmp);
			this.stat_node_front.attachChild(current_item_image);
			current_item_image.updateGeometricState();
		}
		this.updateInvIconAmt(player);
	}


	public void updateInvIconAmt(PlayersAvatar player) {
		if (this.current_item_image != null) {
			this.current_item_image.setText("0");
		}
		try {
			if (player != null && current_item_image != null && player.inv != null) {
				if (player.hasBlockSelected()) {
					if (player.inv.containsKey(player.getCurrentItemType())) {
						int amt = player.inv.get(player.getCurrentItemType());
						this.current_item_image.setText(""+amt);
					}
				}
			}
		} catch (RuntimeException ex) {
			AbstractActivity.HandleError(null, ex);
		}
	}*/


	private synchronized void loadPlayer(IInputDevice input) {
		float x = original_level_data.getStartPos().x * Statics.SQ_SIZE;
		float y = (original_level_data.getStartPos().y-2) * Statics.SQ_SIZE; // -2 so we start above the bed
		PlayersAvatar player = new PlayersAvatar(this, players.size(), x, y, input);//, controllerID);
		//player.inv = new BlockInventory(this, player);
		this.players.add(player);
		player.parent.updateGeometricState();
		//this.restartPlayer(player);
	}


	public void restartPlayer(PlayersAvatar player) {
		float x = original_level_data.getStartPos().x * Statics.SQ_SIZE;
		float y = (original_level_data.getStartPos().y-2) * Statics.SQ_SIZE; // -2 so we start above the bed
		player.setLocation(x, y);
		addToProcess_Instant(player);
		root_node.attachChild(player);
		player.updateGeometricState();

	}

	/*public void checkIfMapNeedsLoading() {
		if (players.size() > 0) {
			for (PlayersAvatar player : this.players) {
				int pl_sq = (int)((player.getWorldX() + Statics.SCREEN_WIDTH) / Statics.SQ_SIZE);
				if (pl_sq > this.map_loaded_up_to_col) {
					loadMoreMap(pl_sq);
				}
			}
		} else {
			loadMoreMap((int)(Statics.SCREEN_WIDTH/Statics.SQ_SIZE_INT));
		}
	}*/


	public TSArrayList<IProcessable> getOthersInstant() {
		return this.entities;
	}


	@Override
	public void displayText(String s) {
		this.msg.setText(s);
	}


	@Override
	public void newController(IInputDevice input) {
		this.loadPlayer(input);

	}


}

