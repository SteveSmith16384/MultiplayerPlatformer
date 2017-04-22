package com.scs.worldcrafter;

import java.util.ArrayList;

import ssmith.android.framework.AbstractActivity;
import ssmith.android.framework.MyEvent;
import ssmith.android.framework.modules.AbstractModule;
import ssmith.lang.Functions;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.KeyEvent;
import android.view.SurfaceHolder;

import com.scs.worldcrafter.start.GRBModule;
import com.scs.worldcrafter.start.IntroModule;

/**
 * This class should be usable by ANY android game.
 *
 */
public final class MainThread extends Thread {

	private static Paint paint_black_fill = new Paint();

	private ArrayList<MyEvent> events = new ArrayList<MyEvent>();

	/** Indicate whether the surface has been created & is ready to draw */
	protected boolean mRun = false;

	/** Handle to the surface manager object we interact with */
	private SurfaceHolder mSurfaceHolder;

	private boolean paused = false;

	public AbstractModule current_module;
	public String error = "";
	private AbstractModule next_module;

	static {
		paint_black_fill.setARGB(255, 0, 0, 0);
		paint_black_fill.setAntiAlias(true);
		paint_black_fill.setStyle(Style.FILL);

	}

	public MainThread() {
		super("MainThread");

		synchronized (events) {
			events.clear();
		}

	}


	/*@Override
	public void run() {
		while (mRun) {
			long start = System.currentTimeMillis();
			if (paused == false && mSurfaceHolder != null) {
				Canvas c = null;
				try {
					c = mSurfaceHolder.lockCanvas(null);
					//long interpol = System.currentTimeMillis() - mLastTime;
					if (c != null) { // In case the surface is the "sleep" surface
						synchronized (mSurfaceHolder) {
							//Functions.delay(10);
							updateGame();//interpol);
							doDraw(c);//interpol);
						}
					}
					//this.mLastTime = System.currentTimeMillis();
				} catch (Exception ex) {
					AbstractActivity.HandleError(Statics.act, ex);
				} finally {
					// do this in a finally so that if an exception is thrown
					// during the above, we don't leave the Surface in an
					// inconsistent state
					if (c != null) {
						try {
							mSurfaceHolder.unlockCanvasAndPost(c); // Errors with android.view.Surface$CompatibleCanvas@46174090
						} catch (Exception ex) {
							ex.printStackTrace();
							paused = false; // Otherwise it gets stuck   paused = false;
						}
					}
				}
			}
			long diff = System.currentTimeMillis() - start;
			Functions.delay(Statics.LOOP_DELAY - diff);
		}
		AbstractActivity.Log("Thread ended...");
	}*/


	@Override
	public void run() {
		while (mRun) {
			long start = System.currentTimeMillis();
			if (Statics.initd) { // Otherwise SCREEN_WIDTH not set and everything goes wrong
				if (paused == false && mSurfaceHolder != null) {
					updateGame();
					doDrawing();
				}
			}
			long diff = System.currentTimeMillis() - start;
			Functions.delay(Statics.LOOP_DELAY - diff);
		}
		AbstractActivity.Log("Thread ended...");
	}


	public void doDrawing() {
		Canvas c = null;
		try {
			c = mSurfaceHolder.lockCanvas(null);
			if (c != null) { // In case the surface is the "sleep" surface
				synchronized (mSurfaceHolder) {
					//doDraw(c);//interpol);
					c.drawRect(0, 0, Statics.SCREEN_WIDTH, Statics.SCREEN_HEIGHT, paint_black_fill);

					if (current_module != null) {
						current_module.doDraw(c, Statics.LOOP_DELAY);
					}
				}
			}
		} catch (Exception ex) {
			AbstractActivity.HandleError(Statics.act, ex);
		} finally {
			// do this in a finally so that if an exception is thrown
			// during the above, we don't leave the Surface in an
			// inconsistent state
			if (c != null) {
				try {
					mSurfaceHolder.unlockCanvasAndPost(c); // Errors with android.view.Surface$CompatibleCanvas@46174090
				} catch (Exception ex) {
					ex.printStackTrace();
					paused = false; // Otherwise it gets stuck   paused = false;
				}
			}
		}
	}


	public void setPause(boolean p) {
		this.paused = p;
	}


	public boolean onKeyDown(int keyCode, KeyEvent msg) {
		if (this.current_module != null) {
			return this.current_module.onKeyDown(keyCode, msg);
		}
		return false;
	}


	public boolean onKeyUp(int keyCode, KeyEvent msg) {
		if (this.current_module != null) {
			return this.current_module.onKeyUp(keyCode, msg);
		}
		return false;
	}


	public boolean onBackPressed() {
		if (this.current_module != null) {
			return this.current_module.onBackPressed();
		}
		return false;
	}


	public void addMotionEvent(MyEvent ev) {
		synchronized (events) {
			this.events.add(ev);
		}
	}


	/**
	 * Used to signal the thread whether it should be running or not.
	 * Passing true allows the thread to run; passing false will shut it
	 * down if it's already running. Calling start() after this was most
	 * recently called with false will result in an immediate shutdown.
	 * 
	 * @param b true to run, false to shut down
	 */
	public void setRunning(boolean b) {
		mRun = b;
	}


	public void setNextModule(AbstractModule m) {
		if (this.next_module == null) {  // So if we've got one lined up, it doesn't get overridden
			this.next_module = m;
		}
	}


	public void doDraw(Canvas c) {
		// Clear the background
		c.drawRect(0, 0, Statics.SCREEN_WIDTH, Statics.SCREEN_HEIGHT, paint_black_fill);

		// Draw any error
		/*if (error.length() > 0) {
			c.drawText(error, 5, this.view.canvas_height - 20, Painters.paint_white_line);
		} else {
			//c.drawText("no error", 5, 20, Painters.paint_white_line);
		}*/

		if (current_module != null) {
			current_module.doDraw(c, Statics.LOOP_DELAY);
		}
	}


	protected void updateGame() {
		if (next_module != null) {
			if (Statics.initd) { // Don't do anything until we've got the display system!
				if (this.current_module != null) {
					this.current_module.stopped();
				}
				this.current_module = next_module;
				next_module = null;
				this.current_module.started();
				synchronized (events) {
					this.events.clear();
				}
			}
		}

		if (current_module != null) {
			// Process events
			if (this.events.size() > 0) {
				while (this.events.size() > 0) {
					MyEvent ev = null;
					synchronized (events) {
						ev = this.events.remove(0);
					}
					try {
						if (ev != null) {
							if (current_module.processEvent(ev)) {
								synchronized (events) {
									this.events.clear();
								}
							}
						}
					} catch (Exception ex) {
						//BugSenseHandler.log(Statics.NAME, ex);
						AbstractActivity.HandleError(Statics.act, ex);
					}
				}
			}
			current_module.updateGame(Statics.LOOP_DELAY);
		} else {
			if (Statics.initd) { // Don't do anything until we've got the display system!
				// Load default module
				//this.setNextModule(new IntroModule(Statics.act));
				AbstractModule s = Statics.GetStartupModule(Statics.act);
				if (Statics.KOREAN) {
					this.setNextModule(new GRBModule(Statics.act, s));
				} else if (Statics.GAME_MODE == Statics.GM_NINJA) {
					this.setNextModule(Statics.GetStartupModule(Statics.act));
				} else if (Statics.GAME_MODE == Statics.GM_POLICECOP) {
					this.setNextModule(Statics.GetStartupModule(Statics.act));
				} else {
					this.setNextModule(new IntroModule(Statics.act, s));
				}
			}

		}
	}


	public void setSurfaceHolder(SurfaceHolder holder) {
		this.mSurfaceHolder = holder;
	}

}

