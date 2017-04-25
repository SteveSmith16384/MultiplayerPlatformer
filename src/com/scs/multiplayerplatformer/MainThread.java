package com.scs.multiplayerplatformer;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import ssmith.android.compatibility.Canvas;
import ssmith.android.compatibility.Paint;
import ssmith.android.framework.AbstractActivity;
import ssmith.android.framework.MyEvent;
import ssmith.android.framework.modules.AbstractModule;
import ssmith.awt.ImageCache;
import ssmith.lang.Functions;

import com.scs.multiplayerplatformer.start.ErrorModule;
import com.scs.multiplayerplatformer.start.StartupModule;

public final class MainThread extends Thread {

	private static final long ONBACK_GAP = 200;

	private static Paint paint_black_fill = new Paint();

	private ArrayList<MyEvent> events = new ArrayList<MyEvent>();

	protected boolean mRun = true;

	public AbstractModule module;
	public AbstractModule next_module;
	private long last_onback_pressed;
	public Canvas c;
	private long fps;
	public MainWindow window;

	static {
		paint_black_fill.setARGB(255, 0, 0, 0);
		paint_black_fill.setAntiAlias(true);
	}


	public MainThread() {
		super("MainThread");

		this.setDaemon(true);

		window = new MainWindow(this);
		Statics.img_cache = new ImageCache(window);

	}


	@Override
	public void run() {
		try {
			while (mRun) {
				long start = System.currentTimeMillis();

				updateGame();
				doDrawing();

				long diff = System.currentTimeMillis() - start;
				if (diff != 0) {
				fps = 1000/diff;
				}
				Functions.delay(Statics.LOOP_DELAY - diff);
			}
		} catch (Exception ex) {
			AbstractActivity.HandleError(ex);
			JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage() + ".  Please restart", "Error", JOptionPane.ERROR_MESSAGE);
		}
		//AbstractActivity.Log("MainThread ended.");
	}


	public void doDrawing() {
		c = new Canvas(window.bs.getDrawGraphics());
		if (Statics.FULL_SCREEN == false) {
			c.translate(0, Statics.WINDOW_TOP_OFFSET); // Take into account window title
		}
		c.getGraphics().setFont(Statics.stdfnt); // Default

		c.drawRect(0, 0, Statics.SCREEN_WIDTH, Statics.SCREEN_HEIGHT, paint_black_fill);

		if (module != null) {
			module.doDraw(c, Statics.LOOP_DELAY);
		}
		
		if (Statics.DEBUG) {
			c.getGraphics().drawString("FPS: "+fps, 20, 30);
		}
		
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
			//this.setNextModule(new IntroModule()); // re-add when have logo
			this.setNextModule(new StartupModule(Statics.act));
		}
	}


}

