package com.scs.multiplayerplatformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import ssmith.android.compatibility.Canvas;
import ssmith.android.compatibility.Paint;
import ssmith.android.framework.AbstractActivity;
import ssmith.android.framework.MyEvent;
import ssmith.android.framework.modules.AbstractModule;
import ssmith.awt.ImageCache;
import ssmith.lang.Functions;

import com.scs.multiplayerplatformer.game.GameModule;
import com.scs.multiplayerplatformer.game.Player;
import com.scs.multiplayerplatformer.input.DeviceThread;
import com.scs.multiplayerplatformer.input.IInputDevice;
import com.scs.multiplayerplatformer.input.NewControllerListener;
import com.scs.multiplayerplatformer.start.ErrorModule;
import com.scs.multiplayerplatformer.start.StartupModule;

public final class MainThread extends Thread implements NewControllerListener {

	private static final long ONBACK_GAP = 200;

	private static Paint paint_black_fill = new Paint();

	private ArrayList<MyEvent> events = new ArrayList<MyEvent>();

	protected volatile boolean mRun = true;

	public AbstractModule module;
	public AbstractModule next_module;
	private long last_onback_pressed;
	public Canvas c;
	public MainWindow window;

	public Map<Integer, Player> players = new HashMap<>();
	private List<IInputDevice> newControllers = new ArrayList<>();

	static {
		paint_black_fill.setARGB(255, 0, 0, 0);
		paint_black_fill.setAntiAlias(true);
	}


	public MainThread() {
		super("MainThread");

		this.setDaemon(true);

		window = new MainWindow(this);
		Statics.img_cache = ImageCache.GetInstance();
		Statics.img_cache.c = window;

		DeviceThread deviceThread = new DeviceThread(window);
		deviceThread.addListener(this);
		deviceThread.start();

	}


	@Override
	public void run() {
		try {
			while (mRun) {
				long start = System.currentTimeMillis();

				updateGame();
				doDrawing();

				long diff = System.currentTimeMillis() - start;
				Functions.delay(Statics.LOOP_DELAY - diff);
			}
		} catch (Exception ex) {
			AbstractActivity.HandleError(ex);
			JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage() + ".  Please restart", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}


	public void doDrawing() {
		if (!mRun) {
			return;
		}

		c = new Canvas(window.bs.getDrawGraphics());
		if (Statics.FULL_SCREEN == false) {
			c.translate(0, Statics.WINDOW_TOP_OFFSET); // Take into account window title
		}
		c.getGraphics().setFont(Statics.stdfnt); // Default

		c.drawRect(0, 0, Statics.SCREEN_WIDTH, Statics.SCREEN_HEIGHT, paint_black_fill);

		if (module != null) {
			module.doDraw(c, Statics.LOOP_DELAY);
		}

		/*if (Statics.DEBUG) {
			c.getGraphics().drawString("FPS: "+fps, 20, 30);
		}*/

		window.bs.show();
	}


	public boolean onBackPressed() {
		synchronized (events) {
			events.clear();
		}

		if (this.module != null) {
			if (System.currentTimeMillis() > this.last_onback_pressed + ONBACK_GAP) {
				last_onback_pressed = System.currentTimeMillis();
				return this.module.onBackPressed();
			} else {
				return true;
			}
		} else {
			return false;
		}
	}


	public void addEvent(MyEvent ev) {
		synchronized (events) {
			this.events.add(ev);
		}
	}


	public void setRunning(boolean b) {
		mRun = b;
	}


	public void setNextModule(AbstractModule m) {
		if (this.next_module == null || m instanceof ErrorModule) {  // So if we've got one lined up, it doesn't get overridden
			this.next_module = m;
		}
	}


	protected void updateGame() {
		synchronized (newControllers) {
			if (this.newControllers.isEmpty() == false) {
				while (this.newControllers.isEmpty() == false) {
					this.createPlayer(this.newControllers.remove(0));
				}
				//this.startNewLevel(filename, true); // Restart the level
			}
		}

		if (next_module != null) {
			if (this.module != null) {
				this.module.stopped();
			}
			this.module = next_module;
			next_module = null;
			this.module.started();
			synchronized (events) {
				this.events.clear();
			}
		}

		if (module != null) {
			// Process events
			if (this.events.size() > 0) {
				while (this.events.size() > 0) {
					MyEvent ev = null;
					synchronized (events) {
						ev = this.events.remove(0);
					}
					try {
						if (ev != null) {
							if (module.processEvent(ev)) {
								synchronized (events) {
									this.events.clear();
								}
							}
						}
					} catch (Exception ex) {
						AbstractActivity.HandleError(ex);
					}
				}
			}
			module.updateGame(Statics.LOOP_DELAY);
		} else {
			// Load default module
			if (Statics.DEBUG) {
				this.setNextModule(new GameModule(Statics.act, null));
			} else {
				this.setNextModule(new StartupModule(Statics.act));
			}
		}
	}


	@Override
	public synchronized void newController(IInputDevice input) {
		synchronized (newControllers) {
			this.newControllers.add(input);
		}
	}


	private void createPlayer(IInputDevice input) {
		int num = players.size();
		synchronized (players) {
			if (this.players.containsKey(input.getID()) == false) {
				Player player = new Player(input, num);
				this.players.put(input.getID(), player);
				this.module.newPlayer(player);
			}
		}
		//this.msg.setText("Player " + (num+1) + " joined!");
	}




}

