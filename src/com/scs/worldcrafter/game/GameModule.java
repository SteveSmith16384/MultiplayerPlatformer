package com.scs.worldcrafter.game;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import ssmith.android.compatibility.Canvas;
import ssmith.android.compatibility.MotionEvent;
import ssmith.android.compatibility.Paint;
import ssmith.android.compatibility.RectF;
import ssmith.android.compatibility.Style;
import ssmith.android.framework.AbstractActivity;
import ssmith.android.framework.MyEvent;
import ssmith.android.framework.modules.AbstractModule;
import ssmith.android.lib2d.MyPointF;
import ssmith.android.lib2d.gui.Button;
import ssmith.android.lib2d.gui.GUIFunctions;
import ssmith.android.lib2d.gui.ToggleButton;
import ssmith.android.lib2d.shapes.AbstractRectangle;
import ssmith.android.lib2d.shapes.BitmapRectangle;
import ssmith.android.lib2d.shapes.Geometry;
import ssmith.android.lib2d.shapes.Rectangle;
import ssmith.android.util.Timer;
import ssmith.lang.Functions;
import ssmith.lang.GeometryFuncs;
import ssmith.lang.NumberFunctions;
import ssmith.util.IDisplayText;
import ssmith.util.Interval;
import ssmith.util.TSArrayList;

import com.scs.worldcrafter.Statics;
import com.scs.worldcrafter.graphics.Explosion;
import com.scs.worldcrafter.graphics.ThrownItem;
import com.scs.worldcrafter.graphics.blocks.Block;
import com.scs.worldcrafter.graphics.blocks.SimpleBlock;
import com.scs.worldcrafter.graphics.mobs.AbstractMob;
import com.scs.worldcrafter.graphics.mobs.PlayersAvatar;
import com.scs.worldcrafter.mapgen.AbstractLevelData;
import com.scs.worldcrafter.mapgen.SimpleMobData;

/**
 * Lists:-
 * Instant: Mobs - Mobs are always instant, as they are removed if too far away (unless they are not, like sheep).
 * Slow: Blocks and clouds and other misc items
 * V. SLow: BLock
 *
 */
public final class GameModule extends AbstractModule implements IDisplayText {

	public static final byte HAND = 1;
	
	// Icons
	private static String CURRENT_ITEM;
	private static String MAP;// = R.string.icon_map;
	private static String TEST = "TEST";
	private static String MENU;// = R.string.icon_menu;
	private static String ID;// = R.string.icon_id;

	private static final int LOAD_MAP_BATCH_SIZE = 5;
	private static final int ICON_INSETS = 10;
	private static final float THROW_DRAG_DIST = Statics.SQ_SIZE;

	private static Paint paint_health_bar = new Paint();
	private static Paint paint_health_bar_outline = new Paint();
	private static Paint paint_icon_ink = new Paint(); // For text on icons
	private static Paint paint_icon_background = new Paint(); // For text on icons
	private static Paint paint_inv_ink = new Paint(); // For inv qty
	private static Paint paint_text_ink = new Paint(); // For timer, dist
	private static Paint paint_day = new Paint();
	private static Paint paint_night = new Paint();
	private static Paint paint_movement_highlight = new Paint();

	//private boolean is_down = false;
	private MyPointF act_start_drag = new MyPointF();
	private TSArrayList<IProcessable> others_instant;
	private TSArrayList<IProcessable> others_slow;
	private TSArrayList<Block> others_very_slow;
	public PlayersAvatar player;
	private int last_slow_object_processed = 0;
	private int last_very_slow_object_processed = 0;
	public AbstractLevelData original_level_data;
	private boolean got_map = false;
	public MyEfficientGridLayout new_grid;
	public int map_loaded_up_to_col = -1;
	public BlockInventory inv;
	private Button curr_item_icon, current_item_image, cmd_menu;
	public boolean is_day = true;
	public TimedString msg;
	private RectF health_bar = new RectF();
	private RectF health_bar_outline = new RectF();
	private Rectangle dummy_rect = new Rectangle(); // for checking the area is clear
	private ProximityScanner prox_scanner;
	private boolean game_over = false;
	private String game_over_reason;
	private Timer time_remaining;
	public String filename;
	public int level;
	private String str_time_remaining;
	private Interval check_for_new_mobs = new Interval(500, true);

	//private BitmapRectangle arrow_left, arrow_right, arrow_up, arrow_down;

	private long draw_time = 0; // Avg. 20
	private long instant_time = 0; // Avg. 4-5
	private long total_time = 0; // Avg. 4-5


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

		paint_day.setARGB(255, 200, 200, 255);
		paint_day.setAntiAlias(true);
		paint_day.setStyle(Style.FILL);

		paint_night.setARGB(255, 0, 0, 0);
		paint_night.setAntiAlias(true);
		paint_night.setStyle(Style.FILL);

		paint_movement_highlight.setARGB(55, 255, 255, 255);
		paint_movement_highlight.setAntiAlias(true);
		paint_movement_highlight.setStyle(Style.FILL);

	}


	public GameModule(AbstractActivity act, AbstractLevelData _original_level_data, int _level, String save_filename) {
		super(act, null);

		original_level_data = _original_level_data;
		level = _level;
		filename = save_filename;

		CURRENT_ITEM = "";// No, we show the qty instead, on the overlaying icon act.getString(R.string.icon_inv);
		MENU = act.getString("icon_menu");
		TEST = "TEST";
		ID = act.getString("icon_id");

		//this.got_offsets = true;

		others_instant = new TSArrayList<IProcessable>();
		others_slow = new TSArrayList<IProcessable>();
		others_very_slow = new TSArrayList<Block>();

		msg = new TimedString(this, 2000);

		this.root_node.detachAllChildren();
		this.stat_node_back.detachAllChildren();
		this.stat_node_front.detachAllChildren();

		original_level_data.start();

		/*if (Statics.GAME_MODE != Statics.GM_POLICECOP) {
			for (int i=0 ; i<3 ; i++) {
				new Cloud(this);
			}
		} else {
			new BankingSpaceship(this, 1);
			new BankingSpaceship(this, -1);
		}*/

		this.stat_node_back.updateGeometricState();
		this.stat_cam.lookAtTopLeft(true);

		str_time_remaining = act.getString("time_remaining");

			this.setBackground("ninja_background2");
			this.showToast("Level " + this.level + "!");
	}


	/**
	 * Return true to clear all other events 
	 */
	@Override
	public boolean processEvent(MyEvent ev) throws Exception {
		AbstractActivity act = Statics.act;
		
		try {
			if (player != null) {
				/*if ((ev.getAction() == MotionEvent.ACTION_DOWN || ev.getAction() == MotionEvent.ACTION_POINTER_DOWN)) {// && is_down == false) {
					if (Statics.cfg.using_buttons == false) {
						checkForHighlights(ev, true);
					} else {
						if (this.arrow_left.contains(ev.getX(), ev.getY())) {
							this.arrow_left.pressed = true;
						}
						if (this.arrow_right.contains(ev.getX(), ev.getY())) {
							this.arrow_right.pressed = true;
						}
						if (this.arrow_up.contains(ev.getX(), ev.getY())) {
							this.arrow_up.pressed = true;
						}
						if (this.arrow_down.contains(ev.getX(), ev.getY())) {
							this.arrow_down.pressed = true;
						}
					}
					//is_down = true;
					act_start_drag.x = ev.getX();
					act_start_drag.y = ev.getY();

				} else if ((ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_POINTER_UP)) {// && is_down) {
					if (Statics.cfg.using_buttons == false) {
					} else {
						if (this.arrow_left != null) { // here
							if (this.arrow_left.contains(ev.getX(), ev.getY())) {
								this.arrow_left.pressed = false;
							}
							if (this.arrow_right.contains(ev.getX(), ev.getY())) {
								this.arrow_right.pressed = false;
							}
							if (this.arrow_up.contains(ev.getX(), ev.getY())) {
								this.arrow_up.pressed = false;
							}
							if (this.arrow_down.contains(ev.getX(), ev.getY())) {
								this.arrow_down.pressed = false;
							}
						}
					}
					//is_down = false;

						if (player.is_on_ice == false) {
							player.move_x_offset = 0;
						}
						this.player.moving_down = false;
						player.moving_with_keys = false;

						float drag_dist = act_start_drag.subtract(ev.getX(), ev.getY()).length();
						float drag_start = GeometryFuncs.distance(act_start_drag.x, act_start_drag.y, Statics.SCREEN_WIDTH/2, Statics.SCREEN_HEIGHT/2);
						if (drag_dist < THROW_DRAG_DIST || drag_start > Statics.SQ_SIZE*2) {
							// Might be an icon
							// Need this on OnUp so it doesn't register a keypress with the inventory as well
							/*AbstractComponent c = this.GetComponentAt(stat_node_front, ev.getX(), ev.getY());
							if (c != null) {
								if (c instanceof Button) {
									Button b = (Button)c;
									if (b.getActionCommand() == ID) {
										ToggleButton t = (ToggleButton)c;
										t.toggeSelected();
										if (t.isSelected()) {
											this.showToast(act.getString(R.string.click_on_a_block));
										}
									} else if (b.getActionCommand() == TEST) {
									}
									return true;
								}
							}
							// Adjust for camera location
							float rel_x = ev.getX() + root_cam.left;
							float rel_y = ev.getY() + root_cam.top;

							if (this.btn_id.isSelected()) {
								Block block_found = (Block)this.new_grid.getBlockAtPixel_MaybeNull(rel_x, rel_y);
								if (block_found != null) {
									new BlockHighlighter(this, block_found);
									//this.msg.setText(act.getString(R.string.that_is, block_found.getDesc()));
									this.showToast(act.getString(R.string.that_is, block_found.getDesc()));
									this.btn_id.setSelected(false);
									return true;
								}
							} else {
								digOrBuild(rel_x, rel_y);
							}
						} else {
							throwItem(ev);
							updateInvIconAmt();
						}
					act_start_drag.x = ev.getX();
					act_start_drag.y = ev.getY();
				} else if (ev.getAction() == MotionEvent.ACTION_MOVE) {// && is_down) {  //MotionEvent.
					checkForHighlights(ev, true);
				}*/
			}
		} catch (RuntimeException ex) {
			AbstractActivity.HandleError(null, ex);
		}
		return false;
	}


	public void updateGame(long interpol) {
		AbstractActivity act = Statics.act;
		
		long total_start = System.currentTimeMillis();

		// Remove any objects marked for removal
		this.others_instant.refresh();
		this.others_slow.refresh();
		this.others_very_slow.refresh();

		if (got_map == false) {
			if (this.original_level_data.isAlive()) {
				/*if (this.filename.length() == 0) {
					msg.setText(act.getString(R.string.generating_map));
				} else {*/
				if (original_level_data.max_rows != 0) {
					msg.setText(act.getString("loading_map") + " (" + original_level_data.row + "/" + original_level_data.max_rows + ")");
				} else {
					msg.setText(act.getString("loading_map"));
				}
				//}
			} else {
				this.mapGeneratedOrLoaded();
			}
		}

		if (player != null) {
			/*if (Statics.cfg.using_buttons) {
				if (this.arrow_left.pressed) {
					player.move_x_offset = -1;
				} else if (this.arrow_right.pressed) {
					player.move_x_offset = 1;
				}
				if (this.arrow_up.pressed) {
					this.player.startJumping();
				} else if (this.arrow_down.pressed) {
					this.player.moving_down = true;
				}
			} else {
				if (rect_ctrl_move_left.isHighighted()) {
					player.move_x_offset = -1;
				}
				if (rect_ctrl_move_right.isHighighted()) {
					player.move_x_offset = 1;
				}
				if (rect_ctrl_jump_left.isHighighted()) {
					player.move_x_offset = -1;
					player.startJumping();
				}
				if (rect_ctrl_jump_right.isHighighted()) {
					player.move_x_offset = 1;
					player.startJumping();
				}
				if (rect_ctrl_up.isHighighted()) {
					player.startJumping();
				}
				if (rect_ctrl_down.isHighighted()) {
					player.moving_down = true;
				}
				/*if (ev.getX() < Statics.SCREEN_WIDTH*.25f || ev.getX() > Statics.SCREEN_WIDTH*.75f) {
					if (ev.getY() < Statics.SCREEN_HEIGHT * .33f) {
						this.player.startJumping();
					}
					player.moving_with_keys = true;
					if (ev.getX() < Statics.SCREEN_WIDTH/2) {
						player.move_x_offset = -1;
					} else {
						player.move_x_offset = 1;
					}
				} else { // Middle of screen
					if (ev.getY() < Statics.SCREEN_HEIGHT * 0.33f) { // Don't have it in middle as we don't want to jump when throwing shuriken
						this.player.startJumping();
					} else if (ev.getY() > Statics.SCREEN_HEIGHT * .66f) {
						this.player.moving_down = true;
					}
				}*/
			//}*/
		}

		// Process the rest
		long start_instant = System.currentTimeMillis();
		if (others_instant != null) {
			for (IProcessable o : others_instant) {
				o.process(interpol);
			}
		}
		instant_time = System.currentTimeMillis() - start_instant;

		// Check for any very_slow blocks that are close
		if (others_very_slow != null && this.player != null) {
			for (int i=0 ; i<20 ; i++) {
				if (last_very_slow_object_processed < others_very_slow.size()) {
					Block ip = others_very_slow.get(last_very_slow_object_processed); 
					if (GeometryFuncs.distance(this.player.getWorldCentreX(), this.player.getWorldCentreY(), ip.getWorldCentreX(), ip.getWorldCentreY()) < Statics.SCREEN_WIDTH) {
						this.others_very_slow.remove(last_very_slow_object_processed);
						this.others_slow.add(ip);
					}
					last_very_slow_object_processed++;
				} else {
					last_very_slow_object_processed = 0;
				}
			}
		}


		// Move any slow objects?
		if (others_slow != null) {
			for (int i=0 ; i<2 ; i++) {
				if (last_slow_object_processed < others_slow.size()) {
					IProcessable o = others_slow.get(last_slow_object_processed);
					if (o != null) {
						o.process(interpol);
					}
					// See if it needs moving as it's far away
					if (o instanceof Block) {
						Block b = (Block)o;
						if (b.alwaysProcess() == false) {
							if (GeometryFuncs.distance(this.player.getWorldCentreX(), this.player.getWorldCentreY(), b.getWorldCentreX(), b.getWorldCentreY()) > Statics.SCREEN_WIDTH) {
								this.others_slow.remove(last_slow_object_processed);
								this.others_very_slow.add(o);
							}
						}
					}
					last_slow_object_processed++;
				} else {
					last_slow_object_processed = 0;
				}
			}
		}

		if (check_for_new_mobs.hitInterval()) {
			this.checkIfMobsNeedCreating();
		}


		if (this.player != null) {
			this.root_cam.lookAt(this.player, true);
		}

		if (this.time_remaining != null) {
			if (time_remaining.hasHit(interpol)) {
				this.gameOver(act.getString("out_of_time"));
			}
		}

		checkGameOver();

		total_time = System.currentTimeMillis() - total_start;

	}


	private void checkIfMobsNeedCreating() {
		// Load mobs
		if (original_level_data.mobs != null && this.player != null) {
			for (int i=0 ; i<original_level_data.mobs.size() ; i++) {
				SimpleMobData sm = original_level_data.mobs.get(i);
				float dist = NumberFunctions.mod(this.player.getWorldX() - sm.pixel_x); 
				if (dist < Statics.ACTIVATE_DIST) { // Needs to be screen width in case we've walked too fast into their "creation zone"
					AbstractMob.CreateMob(this, sm);
					original_level_data.mobs.remove(i);
					i--;
				}
			}
		}

	}

	public void doDraw(Canvas g, long interpol) {
		long start = System.currentTimeMillis();
		super.doDraw(g, interpol);

		if (this.player != null) {
			// Health bar
			float height = (Statics.HEALTH_BAR_HEIGHT / 100) * this.player.getHealth();
			health_bar.set(0, Statics.SCREEN_HEIGHT-height, Statics.HEALTH_BAR_WIDTH, Statics.SCREEN_HEIGHT);
			paint_health_bar.setARGB(150, 200-(player.getHealth()*2), player.getHealth()*2, 0);
			g.drawRect(health_bar, paint_health_bar);
			if (health_bar_outline != null) {
				g.drawRect(health_bar_outline, paint_health_bar_outline);
			}

			//g.drawText("Health: " + this.player.getHealth(), 10, paint_text_ink.getTextSize(), paint_text_ink);
			//g.drawText("Rocks: " + this.player.rocks, 10, (paint_text_ink.getTextSize()*2), paint_text_ink);
			if (this.time_remaining != null) {
				g.drawText(this.str_time_remaining + ": " + (this.time_remaining.getTimeRemaining()/1000), 10, Statics.ICON_SIZE + (paint_text_ink.getTextSize()*3), paint_text_ink);
			}
		}
		if (Statics.SHOW_STATS) {
			g.drawText("Inst Objects: " + this.others_instant.size(), 10, paint_text_ink.getTextSize()*4, paint_text_ink);
			g.drawText("Slow Objects: " + this.others_slow.size(), 10, paint_text_ink.getTextSize()*5, paint_text_ink);
			g.drawText("V. Slow Objects: " + this.others_very_slow.size(), 10, paint_text_ink.getTextSize()*6, paint_text_ink);
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


		draw_time = System.currentTimeMillis() - start;

	}


	private void mapGeneratedOrLoaded() {
		// Add icons
		curr_item_icon = new Button(CURRENT_ITEM, CURRENT_ITEM, paint_icon_background, paint_icon_ink, Statics.img_cache.getImage("button_red", Statics.ICON_SIZE, Statics.ICON_SIZE));
		//curr_item_icon.setByLTWH(Statics.SCREEN_WIDTH-(Statics.ICON_SIZE*3), 0, Statics.ICON_SIZE, Statics.ICON_SIZE);
		curr_item_icon.setLocation(0, 0);
		this.stat_node_front.attachChild(curr_item_icon);

		this.cmd_menu = new Button(MENU, MENU, paint_icon_background, paint_icon_ink, Statics.img_cache.getImage("button_red", Statics.ICON_SIZE, Statics.ICON_SIZE));
		//this.cmd_menu.setByLTWH(Statics.SCREEN_WIDTH-(Statics.ICON_SIZE*2), 0, Statics.ICON_SIZE, Statics.ICON_SIZE);
		this.cmd_menu.setLocation(Statics.ICON_SIZE, 0);
		this.stat_node_front.attachChild(this.cmd_menu);

		msg.setText("");

		new_grid = new MyEfficientGridLayout(this, original_level_data.getGridWidth(), original_level_data.getGridHeight(), Statics.SQ_SIZE);

		this.root_node.attachChild(new_grid);	

		this.root_node.updateGeometricState();

		this.stat_node_back.updateGeometricState();
		this.stat_node_front.updateGeometricState();

		this.root_cam.lookAt(root_node, false);

		inv = new BlockInventory(this);
		// Load inventory
		if (original_level_data.block_inv != null) {
			inv.putAll(original_level_data.block_inv);
		}

		// Player - must be added last so they are at the front
		loadPlayer();

		got_map = true;

		checkIfMapNeedsLoading();

		new EnemyEventTimer(this);

		if (Statics.amulet) {
			prox_scanner = new ProximityScanner(this);
			this.addToProcess_Instant(prox_scanner);
			if (original_level_data.getAmuletPos() != null) { // Have we loaded the amulet's location?
				prox_scanner.setMapTarget(original_level_data.getAmuletPos());
				createTimer();
			} else {
				newAmulet();
			}
		}

		health_bar_outline = new RectF(0, Statics.SCREEN_HEIGHT-Statics.HEALTH_BAR_HEIGHT, Statics.HEALTH_BAR_WIDTH, Statics.SCREEN_HEIGHT);

	}


	public void newAmulet() {
		int ax = Functions.rnd(10, original_level_data.getGridWidth()-1);
		int ay = Functions.rnd(original_level_data.getGridHeight()/2, original_level_data.getGridHeight()-1);
		this.addBlock(Block.AMULET, ax, ay, true);
		original_level_data.data[ax][ay].type = Block.AMULET;
		if (prox_scanner != null) {
			prox_scanner.setMapTarget(new Point(ax, ay));
		}
		createTimer(); // Re-start timer
	}


	private void createTimer() {
		if (Statics.has_timer) {
			// Create timer
			long time = (long)(prox_scanner.getDistance(false) / Statics.SQ_SIZE) * 1200;
			if (this.player.getWorldY() > prox_scanner.target.y) {
				time = (long)(time * 1.5); // Give more time if going upwards.
			}
			this.time_remaining = new Timer(time);
		}
	}


	private void loadMap(int end_col) {
		if (end_col - this.map_loaded_up_to_col < LOAD_MAP_BATCH_SIZE) {
			end_col = this.map_loaded_up_to_col + LOAD_MAP_BATCH_SIZE;
		}

		end_col = Math.min(end_col, this.original_level_data.getGridWidth()-1);
		for (int map_y=0 ; map_y<original_level_data.getGridHeight() ; map_y++) {
			for (int map_x=map_loaded_up_to_col+1 ; map_x<=end_col ; map_x++) {
				SimpleBlock sb = original_level_data.getGridDataAt(map_x, map_y);
				if (sb != null) {
					byte data = sb.type;
					if (data > 0) {
						Block block = this.addBlock(data, map_x, map_y, false);
						if (original_level_data.getGridDataAt(map_x, map_y).on_fire) {
							block.on_fire = true;
						}
					}
				}
			}
		}
		this.new_grid.parent.updateGeometricState();

		// Load mobs
		/*if (original_level_data.mobs != null) {
			for (int i=0 ; i<original_level_data.mobs.size() ; i++) {
				SimpleMobData sm = original_level_data.mobs.get(i);
				float dist = NumberFunctions.mod(this.player.getWorldX() - sm.pixel_x); 
				if (dist < Statics.ACTIVATE_DIST) {
					AbstractMob.CreateMob(this, sm);
					original_level_data.mobs.remove(i);
					i--;
				}
			}
		}*/

		map_loaded_up_to_col = end_col;

	}


	private void loadMoreMap(int col) {
		this.loadMap(col);
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
					boolean slow = block.getType() == Block.WATER || block.getType() == Block.LAVA || block.getType() == Block.FIRE; // Always process water slow!
					this.addToProcess_Slow(block, slow);
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


	private void throwItem(MyEvent ev) {
		AbstractActivity act = Statics.act;
		
		byte type = this.player.getCurrentItemType();
		byte fallback_type = -1;
		//if (type <= 0) { // No item selected
				fallback_type = Block.SHURIKEN; // Default
			//fallback_type = type;
		//}

		if (Block.CanBeThrown(type) == false || this.inv.hasBlock(type) == false) {
			type = fallback_type;
		}

		boolean thrown = false;
		if (this.inv.hasBlock(type)) {
			thrown = true;
		} else {
			this.showToast("You have nothing to throw!  Try a shuriken");
			return;
		}

		if (thrown) {
			act.sound_manager.playSound("throwitem");
			if (type == Block.SHURIKEN) {
				ThrownItem.ThrowShuriken(this, player, new MyPointF(ev.getX(), ev.getY()).subtractLocal(act_start_drag).normalizeLocal());
			} else {
				ThrownItem.ThrowRock(this, type, player, new MyPointF(ev.getX(), ev.getY()).subtractLocal(act_start_drag).normalizeLocal());

			}
			/*float speed = Statics.ROCK_SPEED;
			float grav = Statics.ROCK_GRAVITY;
			if (type == Block.SHURIKEN) {
				speed = Statics.ROCK_SPEED*2;
				grav = Statics.ROCK_GRAVITY/2;
			}
			new ThrownItem(this, type, player, new MyPointF(ev.getX(), ev.getY()).subtractLocal(act_start_drag).normalizeLocal(), 1, speed, grav, Statics.ROCK_SIZE);*/
			this.inv.addBlock(type, -1);
		}

	}


	public void addToProcess_Instant(IProcessable o) {//, boolean instant, boolean slow) {
		this.others_instant.add(o);
	}


	public void addToProcess_Slow(IProcessable o, boolean slow) {
		if (slow) {
			this.others_slow.add(o);
		} else {
			this.others_very_slow.add(o);
		}
	}


	public void removeFromProcess(IProcessable o) {
		this.others_instant.remove(o);
		this.others_slow.remove(o);
		this.others_very_slow.remove(o);
	}


	@Override
	public boolean onBackPressed() {
		AbstractActivity act = Statics.act;
		
		//this.getThread().setNextModule(new InGameMenuModule(act, this));
		return true;
	}


	public void gameOver(String reason) {
		game_over_reason = reason;
		this.game_over = true;
	}


	private void checkGameOver() {
		AbstractActivity act = Statics.act;
		
		if (this.game_over) {
			this.getThread().setNextModule(new GameOverModule(act, this, game_over_reason));
		}
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
						b.damage(2, false);
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
				m.damage(dam);
			}
		}
		dummy_rect.removeFromParent();
	}


	public int getNumProcessInstant() {
		return this.others_instant.size();
	}


	public void setCurrentItemIcon() {
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
			current_item_image = new Button("Current Item", ""+inv.get(player.getCurrentItemType()), this.curr_item_icon.getWorldX()+ICON_INSETS, this.curr_item_icon.getWorldY()+ICON_INSETS, null, paint_inv_ink, bmp);
			this.stat_node_front.attachChild(current_item_image);
			current_item_image.updateGeometricState();
		}
		this.updateInvIconAmt();
	}


	public void updateInvIconAmt() {
		if (this.current_item_image != null) {
			this.current_item_image.setText("0");
		}
		try {
			if (this.player != null && current_item_image != null && this.inv != null) {
				if (player.hasBlockSelected()) {
					if (this.inv.containsKey(this.player.getCurrentItemType())) {
						int amt = this.inv.get(this.player.getCurrentItemType());
						this.current_item_image.setText(""+amt);
					}
				}
			}
		} catch (RuntimeException ex) {
			AbstractActivity.HandleError(null, ex);
		}
	}


	public void loadPlayer() {
		this.game_over = false;
		if (player != null) {
			player.removeFromParent();
			this.removeFromProcess(player);
			this.others_instant.refresh();
		}
		player = new PlayersAvatar(this, original_level_data.getStartPos().x * Statics.SQ_SIZE, (original_level_data.getStartPos().y-2) * Statics.SQ_SIZE); // -2 so we start above the bed
		player.parent.updateGeometricState();
		setCurrentItemIcon();
		checkIfMapNeedsLoading();
	}


	public void checkIfMapNeedsLoading() {
		int pl_sq = (int)((this.player.getWorldX() + Statics.SCREEN_WIDTH) / Statics.SQ_SIZE);
		if (pl_sq > this.map_loaded_up_to_col) {
			loadMoreMap(pl_sq);
		}
	}


	public TSArrayList<IProcessable> getOthersInstant() {
		return this.others_instant;
	}


	@Override
	public void displayText(String s) {
		this.msg.setText(s);
	}


}

